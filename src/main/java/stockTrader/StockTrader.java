package stockTrader;

import algorithms.*;
import bots.SingleStockBot;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

public class StockTrader {

    private static volatile BigDecimal balance = BigDecimal.ZERO;
    private static final Scanner scanner = new Scanner(System.in);
    private static final AlgorithmManager algManager = AlgorithmManager.getInstance();
    private static volatile boolean viewUpdates = false;
    private static volatile boolean csvFormat = false;
    private static volatile boolean isScanning = false;
    public static final Timer timer = new Timer();

    public static boolean getViewUpdates() {
        return viewUpdates;
    }

    public static boolean getCsvFormat() {
        return csvFormat;
    }

    public static boolean getIsScanning() {
        return isScanning;
    }

    public static void setBalance(BigDecimal amount) {
        balance = amount;
    }

    public static BigDecimal getBalance() {
        return balance;
    }


    private static void getInitialBalance() {
        System.out.println();
        System.out.println("*********************************************************");
        System.out.println("*   ~#~#~   Welcome to my StockBot simulator.   ~#~#~   *");
        System.out.println("*********************************************************");
        System.out.println();
        System.out.println("First, please enter how much your initial balance will be:");
        balance = getBigDecimalInput();
        while (balance == null) {
            balance = getBigDecimalInput();
        }
        System.out.println();
        System.out.println("Balance has been set to $" + bigDecimalToString(balance, 3));
        System.out.println("You may now create and manage bots.");
    }

    public static void main(String[] args) {
        Logger.getRootLogger().setLevel(Level.OFF);
        timer.schedule(new PrintUpdate(), 0, 5000);
        System.out.println();
        getInitialBalance();
        while (true) {
            printMenu();
            var input = scanner.next();
            switch (input) {
                case "a": {
                    isScanning = true;
                    System.out.println("Enter stock symbol: ");
                    var symbol = scanner.next().toUpperCase();
                    Stock stock;
                    try {
                        stock = YahooFinance.get(symbol);
                        if (stock == null) {
                            System.out.println("Unknown stock.");
                            isScanning = false;
                            break;
                        }
                    } catch (IOException e) {
                        System.out.println("An error occurred, try again.");
                        isScanning = false;
                        break;
                    }
                    var name = stock.getName();
                    if (name == null) {
                        System.out.println("Unknown stock, try again.");
                        isScanning = false;
                        break;
                    }
                    System.out.println("Select a trading algorithm for " + name + ": ");
                    algManager.printAlgorithms();
                    var algorithm = algManager.getAlgorithm(scanner.next());
                    if (algorithm == null) {
                        System.out.println("Invalid algorithm.");
                        isScanning = false;
                        break;
                    }

                    System.out.println("Enter your initial investment into " + name + ": ");
                    var amount = getBigDecimalInput();
                    if (amount == null) {
                        System.out.println("Amount must be a valid number.");
                        isScanning = false;
                        break;
                    }
                    SingleStockBot.create(symbol, algorithm, amount);
                    isScanning = false;
                    break;
                }
                case "b": {
                    var bots = SingleStockBot.getActiveBots();
                    if (bots.size() == 0) {
                        System.out.println("You have no active bots. Create a bot first.");
                    } else {
                        isScanning = true;
                        System.out.println("Select a bot: ");
                        for (var botID : bots) {
                            var bot = SingleStockBot.getBot(botID);
                            var stock = bot.getStock();
                            String name;
                            if (stock == null) {
                                name = botID;
                            } else {
                                name = stock.getName();
                                if (name == null || name.equals("null")) name = botID;
                            }
                            System.out.println("[" + botID + "] " + name);
                        }
                        input = scanner.next();
                        if (!bots.contains(input)) {
                            System.out.println("Bot not found. Returning to main menu.");
                        } else {
                            botOperations(SingleStockBot.getBot(input));
                        }
                    }
                    isScanning = false;
                    break;
                }
                case "c": {
                    System.out.println();
                    System.out.println("*** Breakdown: ***");
                    var total = balance;
                    System.out.println("Balance: $" + bigDecimalToString(balance, 2));
                    for (var symbol : SingleStockBot.getActiveBots()) {
                        var bot = SingleStockBot.getBot(symbol);
                        var value = bot.getValue();
                        System.out.println(symbol + " bot: $" + bigDecimalToString(value, 2));
                        total = total.add(value);
                    }
                    System.out.println();
                    System.out.println("Total: $" + bigDecimalToString(total, 2));
                    break;
                }
                case "d": {
                    if (viewUpdates) {
                        viewUpdates = false;
                        System.out.println("Live updates have been disabled.");
                    } else {
                        viewUpdates = true;
                        System.out.println("Live updates have been enabled.");
                    }
                    break;
                }
                case "e": {
                    if (csvFormat) {
                        csvFormat = false;
                        System.out.println("CSV format disabled.");
                    } else {
                        csvFormat = true;
                        System.out.println("CSV format enabled.");

                        StringBuilder printout = new StringBuilder();
                        printout.append("Timestamp,Net");
                        var bots = new ArrayList<>(SingleStockBot.getActiveBots());
                        bots.sort(String::compareTo);
                        for (var id : bots) {
                            printout.append(",").append(id);
                        }

                        System.out.println(printout);
                    }
                    break;
                }
                case "f": {
                    System.exit(0);
                    return;
                }
                default: {
                    System.out.println("Unknown operation.");
                    break;
                }

            }
        }
    }

    public static String getTimestampFormatted() {
        return "[" + getTimestamp() + "]";
    }

    public static String getTimestamp() {
        var date = new Date();
        var timestamp = new Timestamp(date.getTime()).toString();

        if (timestamp.length() == 21) timestamp += "00";
        else if (timestamp.length() == 22) timestamp += "0";
        return timestamp;
    }

    private static void botOperations(SingleStockBot bot) {
        while (true) {
            isScanning = true;
            System.out.println();
            System.out.println("*** " + bot.getId() + " Menu ***");
            System.out.println("[a] Get bot value");
            System.out.println("[b] Deposit into bot");
            System.out.println("[c] Withdraw from bot");
            if (bot.isPaused()) {
                System.out.println("[d] Unpause bot");
            } else {
                System.out.println("[d] Pause bot (stocks will remain invested)");
            }
            System.out.println("[e] Remove bot");
            System.out.println("[f] Return to main menu");
            System.out.println();
            isScanning = false;

            var input = scanner.next();
            switch (input) {
                case "a": {
                    System.out.println("" + bot.getId() + " bot is currently worth $" + bigDecimalToString(bot.getValue(), 2));
                    break;
                }
                case "b": {
                    isScanning = true;
                    System.out.println("Enter amount to deposit: ");
                    var amount = getBigDecimalInput();
                    if (amount == null) {
                        System.out.println("Must be a number.");
                        isScanning = false;
                        break;
                    }

                    bot.deposit(amount);
                    System.out.println("$" + bigDecimalToString(amount, 2) + " successfully deposited into " + bot.getId() + " bot.");
                    isScanning = false;
                    break;
                }
                case "c": {
                    isScanning = true;
                    System.out.println("Enter amount to withdraw: ");
                    var amount = getBigDecimalInput();
                    if (amount == null) {
                        System.out.println("Amount is not valid.");
                        isScanning = false;
                        break;
                    }
                    bot.withdraw(amount);
                    System.out.println("$" + bigDecimalToString(amount, 2) + " successfully withdrawn from " + bot.getId() + " bot.");

                    isScanning = false;
                    break;
                }
                case "d": {
                    var isPaused = bot.isPaused();
                    bot.setPaused(!isPaused);
                    if (isPaused) System.out.println(bot.getId() + " bot successfully unpaused.");
                    else
                        System.out.println(bot.getId() + " bot successfully paused. Call this function again to pause it.");
                    break;
                }
                case "e": {
                    var id = bot.getId();
                    bot.remove();
                    System.out.println(id + " successfully removed. Balance has been transferred back into your main account.");
                    System.out.println("Returning to main menu.");
                    return;
                }
                case "f": {
                    System.out.println("Returning to main menu.");
                    return;
                }
                default: {
                    System.out.println("Unkown operation.");
                    break;
                }
            }
        }
    }

    private static void printMenu() {
        isScanning = true;
        System.out.println();
        System.out.println("*** Menu ***");
        System.out.println("[a] Create a bot");
        System.out.println("[b] Manage a bot");
        System.out.println("[c] View account breakdown");
        System.out.println("[d] Toggle live updates");
        System.out.println("[e] Toggle CSV format of live updates");
        System.out.println("[f] Exit (will not save)");
        System.out.println();
        isScanning = false;
    }

    public static Stock getStock(String symbol) {
        try {
            var stock = YahooFinance.get(symbol);
            if (stock == null) {
                System.out.println("Unknown stock.");
                return null;
            } else {
                return stock;
            }
        } catch (Exception e) {
            System.out.println("Failed to retrieve stock from symbol.");
            e.printStackTrace();
            return null;
        }
    }

    private static BigDecimal getBigDecimalInput() {
        try {
            isScanning = true;
            var number = scanner.nextBigDecimal();
            if (number.compareTo(BigDecimal.ZERO) < 0) {
                System.out.println("Cannot be negative. Try again:");
                isScanning = false;
                return null;
            }
            isScanning = false;
            return number;
        } catch (InputMismatchException e) {
            System.out.println("Must be a valid number. Try again:");
            scanner.next();
            isScanning = false;
            return null;
        } catch (StackOverflowError e) {
            System.out.println("You failed to many times. Value set to '0'.");
            isScanning = false;
            return BigDecimal.ZERO;
        } catch (Exception e) {
            isScanning = false;
            return null;
        }
    }

    public static String bigDecimalToString(BigDecimal decimal, int places) {
        var string = decimal.toString();
        var db = Double.parseDouble(string);
        return String.format("%." + places + "f", db);
    }
}
