package bots;

import algorithms.Algorithm;
import algorithms.AlgorithmAnalyzer;
import stockTrader.StockTrader;
import yahoofinance.Stock;
import yahoofinance.quotes.stock.StockQuote;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class SingleStockBot {
    private static final HashMap<String, SingleStockBot> activeBots = new HashMap<>();
    private volatile BigDecimal cash;
    private volatile int shares;
    private final String id;
    private final Stock stock;
    private volatile boolean paused = false;
    private volatile boolean removed = false;

    public static HashSet<String> getActiveBots() {
        return new HashSet<>(activeBots.keySet());
    }

    public static SingleStockBot getBot(String botID) {
        return activeBots.get(botID);
    }

    public static void create(String symbol, Algorithm algorithm, BigDecimal initialInvestment) {
        var id = symbol + "-" + algorithm.getId();
        if (activeBots.containsKey(id)) {
            System.out.println("A bot is already running for that stock and algorithm.");
            return;
        }
        var stock = StockTrader.getStock(symbol);
        var bot = new SingleStockBot(symbol, stock, algorithm, initialInvestment);
        activeBots.put(id, bot);
        StockTrader.setBalance(StockTrader.getBalance().subtract(initialInvestment));
        System.out.println("Bot successfully created.");
    }

    private SingleStockBot(String id, Stock stock, Algorithm algorithm, BigDecimal initialInvestment) {
        this.id = id;
        cash = initialInvestment.setScale(2, RoundingMode.HALF_EVEN);
        shares = 0;
        this.stock = stock;
        assert stock != null;
        algorithm.setBot(this);
        AlgorithmAnalyzer.begin(algorithm);
    }

    public boolean isPaused() {
        return paused;
    }

    public void remove() {
        setPaused(true);
        sell(shares);
        var tempCash = cash;
        withdraw(cash);
        activeBots.remove(id);
        removed = true;
        System.out.println("Bot successfully removed.");
        System.out.println("$" + StockTrader.bigDecimalToString(tempCash, 2) + " has been transferred into your main account.");
    }

    public String getId() {
        return id;
    }

    public BigDecimal getCash() {
        return cash;
    }

    public int getShares() {
        return shares;
    }

    public Stock getStock() {
        return stock;
    }

    public StockQuote getQuote() {
        StockQuote quote = null;
        while (quote == null) {
            try {
                quote = stock.getQuote(true);
            } catch (Exception ignored) {
            }
        }
        return quote;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isRemoved() {
        return removed;
    }

    public BigDecimal getValue() {
        StockQuote quote = null;
        while (quote == null) {
            quote = getQuote();
        }
        var price = quote.getPrice();
        var stockValue = price.multiply(BigDecimal.valueOf(shares)).setScale(2, RoundingMode.HALF_EVEN);
        return stockValue.add(cash);
    }

    public void deposit(BigDecimal amount) {
        var cash = this.cash;
        cash = cash.add(amount);
        this.cash = cash;
        StockTrader.setBalance(StockTrader.getBalance().subtract(amount));
    }

    public void withdraw(BigDecimal amount) {
        var cash = this.cash;
        cash = cash.subtract(amount);
        this.cash = cash;
        StockTrader.setBalance(StockTrader.getBalance().add(amount));
    }


    public void buy(int amount) {
        var price = getQuote().getPrice();
        var cost = price.multiply(BigDecimal.valueOf(amount));
        var tempCash = cash;
        tempCash = tempCash.subtract(cost);
        cash = tempCash;

        var tempShares = shares;
        tempShares += amount;
        shares = tempShares;

        if (StockTrader.viewUpdates && !StockTrader.isScanning) {
            System.out.println(StockTrader.getTimestamp() + "\t\t\t\t\t\tBOUGHT " + amount + " " + id + " shares for $" + StockTrader.bigDecimalToString(cost, 2) + " ($" + StockTrader.bigDecimalToString(price, 2) + " each)");
        }
    }

    public void sell(int amount) {
        var price = getQuote().getPrice();
        var cost = price.multiply(BigDecimal.valueOf(amount));
        var tempCash = cash;
        tempCash = tempCash.add(cost);
        cash = tempCash;

        var tempShares = shares;
        tempShares -= amount;
        shares = tempShares;

        if (StockTrader.viewUpdates && !StockTrader.isScanning) {
            System.out.println(StockTrader.getTimestamp() + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tSOLD " + amount + " " + id + " shares for $" + StockTrader.bigDecimalToString(cost, 2) + " ($" + StockTrader.bigDecimalToString(price, 2) + " each)");
        }
    }
}
