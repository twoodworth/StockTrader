package stockTrader;

import bots.SingleStockBot;
import stockTrader.StockTrader;

import java.util.ArrayList;
import java.util.TimerTask;

public class PrintNetWorth extends TimerTask {

    public PrintNetWorth() {
    }

    @Override
    public void run() {
        var net = StockTrader.getBalance();
        StringBuilder printout = new StringBuilder();
        var bots = new ArrayList<>(SingleStockBot.getActiveBots());
        bots.sort(String::compareTo);
        for (var id : bots) {
            var bot = SingleStockBot.getBot(id);
            var value = bot.getValue();
            net = net.add(value);
            printout.append(" | ").append(id).append(": $").append(StockTrader.bigDecimalToString(value, 2));
        }

        printout.insert(0, StockTrader.getTimestamp() + " Net: $" + StockTrader.bigDecimalToString(net, 2));
        if (StockTrader.viewUpdates && !StockTrader.isScanning) {
            System.out.println(printout);
        }
    }
}