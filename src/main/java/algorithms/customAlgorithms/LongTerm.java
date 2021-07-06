package algorithms.customAlgorithms;

import algorithms.Algorithm;

import java.math.RoundingMode;

public class LongTerm extends Algorithm {

    @Override
    public void analyze() {
        var bot = super.getBot();
        var cash = bot.getCash();
        var quote = bot.getQuote();
        var price = quote.getPrice();
        if (cash.compareTo(price) > 0) {
            var amount = cash.divide(price, 2, RoundingMode.FLOOR).toBigInteger();
            var intAmount = Integer.parseInt(amount.toString());
            bot.buy(intAmount);
        }
    }


    @Override
    public String getId() {
        return "L";
    }

    @Override
    public String getDescription() {
        return "Invests long-term (buys then never sells).";
    }
}
