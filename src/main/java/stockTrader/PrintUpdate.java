package stockTrader;

import bots.SingleStockBot;

import java.util.ArrayList;
import java.util.TimerTask;

public class PrintUpdate extends TimerTask {

    public PrintUpdate() {
    }

    @Override
    public void run() {
        if (StockTrader.getViewUpdates()) {
            var net = StockTrader.getBalance();
            if (StockTrader.getCsvFormat()) {
                var stamp = StockTrader.getTimestamp();
                StringBuilder printout = new StringBuilder();
                var bots = new ArrayList<>(SingleStockBot.getActiveBots());
                bots.sort(String::compareTo);
                for (var id : bots) {
                    var bot = SingleStockBot.getBot(id);
                    var value = bot.getValue();
                    net = net.add(value);
                    printout.append(",").append("$").append(StockTrader.bigDecimalToString(value, 2));
                }

                printout.insert(0, stamp + ",$" + StockTrader.bigDecimalToString(net, 2));
                if (StockTrader.getViewUpdates() && !StockTrader.getIsScanning()) {
                    System.out.println(printout);
                }
            } else {
                var stamp = StockTrader.getTimestampFormatted();
                StringBuilder printout = new StringBuilder();
                var bots = new ArrayList<>(SingleStockBot.getActiveBots());
                bots.sort(String::compareTo);
                for (var id : bots) {
                    var bot = SingleStockBot.getBot(id);
                    var value = bot.getValue();
                    net = net.add(value);
                    printout.append(" | ").append(id).append(": $").append(StockTrader.bigDecimalToString(value, 2));
                }

                printout.insert(0, stamp + " Net: $" + StockTrader.bigDecimalToString(net, 2));
                if (StockTrader.getViewUpdates() && !StockTrader.getIsScanning()) {
                    System.out.println(printout);
                }
            }
        }
    }
}