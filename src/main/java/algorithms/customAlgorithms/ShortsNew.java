package algorithms.customAlgorithms;

import algorithms.Algorithm;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;

public class ShortsNew extends Algorithm {

    public volatile BigDecimal previous = null;
    private final ArrayList<BigDecimal> historicalChanges5Min = new ArrayList<>();
    private final ArrayList<BigDecimal> historicalPrices5Min = new ArrayList<>();
    private final ArrayList<Long> historicalTimestamps5Min = new ArrayList<>();
    private final ArrayList<BigDecimal> historicalPrices1Min = new ArrayList<>();
    private final ArrayList<Long> historicalTimestamps1Min = new ArrayList<>();
    private final ArrayList<Long> historicalTimestamps3Min = new ArrayList<>();
    private final ArrayList<BigDecimal> historicalChanges3Min = new ArrayList<>();
    private volatile BigDecimal cur5MinTotal = BigDecimal.ZERO;
    private volatile BigDecimal cur5MinChangeTotal = BigDecimal.ZERO;
    private volatile BigDecimal cur3MinChangeTotal = BigDecimal.ZERO;

    private BigDecimal get5MinAvg() {
        return cur5MinTotal.divide(BigDecimal.valueOf(historicalTimestamps5Min.size()), 2, RoundingMode.HALF_EVEN);
    }

    private BigDecimal get5MinAvgChange() {
        return cur5MinChangeTotal.divide(BigDecimal.valueOf(historicalTimestamps5Min.size()), 2, RoundingMode.HALF_EVEN);
    }

    private void updateHistory(BigDecimal price, Long currentTimestamp) {
        var old5Min = currentTimestamp - 300000L;
        var old3Min = currentTimestamp - 180000L;
        var old1Min = currentTimestamp - 60000L;
        while (historicalTimestamps5Min.size() > 0 && historicalTimestamps5Min.get(0) < old5Min) {
            var historical5Min = historicalPrices5Min.get(0);
            var historical5MinChange = historicalChanges5Min.get(0);
            historicalTimestamps5Min.remove(0);
            historicalPrices5Min.remove(0);
            historicalChanges5Min.remove(0);
            var total = cur5MinTotal;
            var totalChange = cur5MinChangeTotal;
            total = total.subtract(historical5Min);
            totalChange = totalChange.subtract(historical5MinChange);
            cur5MinTotal = total;
            cur5MinChangeTotal = totalChange;
        }
        while (historicalTimestamps3Min.size() > 0 && historicalTimestamps3Min.get(0) < old3Min) {
            var historical3MinChange = historicalChanges3Min.get(0);
            historicalTimestamps3Min.remove(0);
            historicalChanges3Min.remove(0);
            var total = cur3MinChangeTotal;
            total = total.subtract(historical3MinChange);
            cur3MinChangeTotal = total;
        }
        while (historicalTimestamps1Min.size() > 0 && historicalTimestamps1Min.get(0) < old1Min) {
            historicalTimestamps1Min.remove(0);
            historicalPrices1Min.remove(0);
        }
        var total = cur5MinTotal;
        total = total.add(price);
        cur5MinTotal = total;
        historicalTimestamps5Min.add(currentTimestamp);
        historicalPrices5Min.add(price);
        var change = price.subtract(previous);
        historicalChanges5Min.add(change);
        historicalTimestamps3Min.add(currentTimestamp);
        historicalChanges3Min.add(change);
        historicalTimestamps1Min.add(currentTimestamp);
        historicalPrices1Min.add(price);
    }

    private BigDecimal get1MinuteMin() {
        var list = new ArrayList<>(historicalPrices1Min);
        list.sort(BigDecimal::compareTo);
        return list.get(0).setScale(2, RoundingMode.HALF_EVEN);

    }

    private BigDecimal get3MinuteAvgChange() {
        return cur3MinChangeTotal.divide(BigDecimal.valueOf(historicalTimestamps3Min.size()), 2, RoundingMode.HALF_EVEN);

    }

    @Override
    protected void analyze() {
        var bot = super.getBot();
        var cash = bot.getCash();
        var shares = bot.getShares();
        var quote = bot.getQuote();
        var price = quote.getPrice();

        if (previous == null) {
            previous = price;
            var stamp = new Date().getTime();
            updateHistory(price, stamp);
            return;
        }

        var avgPrice = get5MinAvg();
        var avgChange5m = get5MinAvgChange();
        var avgChange3m = get3MinuteAvgChange();
        var prevPriceComparison = price.compareTo(previous);
        var avgPriceComparison = price.compareTo(avgPrice);
        var avgChangeDirection5m = avgChange5m.compareTo(BigDecimal.ZERO);
        var avgChangeDirection3m = avgChange3m.compareTo(BigDecimal.ZERO);

        var sellAmount = shares + Math.max(0, Integer.parseInt(cash.add(price.multiply(BigDecimal.valueOf(shares))).multiply(BigDecimal.valueOf(0.5)).divide(price, 2, RoundingMode.FLOOR).toBigInteger().toString()));
        if (sellAmount != 0 && prevPriceComparison > 0 && avgPriceComparison > 0 && avgChangeDirection5m >= 0 && (avgChangeDirection3m <= 0)) {
            bot.sell(sellAmount);
        }
        var min = get1MinuteMin();
        var minimumComparison = price.compareTo(min);
        if (minimumComparison <= 0 && avgPriceComparison < 0 && (avgChangeDirection5m <= 0)) {
            var amount = cash.divide(price, 2, RoundingMode.FLOOR).toBigInteger();
            var intAmount = Integer.parseInt(amount.toString());
            if (intAmount > 0) {
                bot.buy(intAmount);
            }
        }


        var stamp = new Date().getTime();
        previous = price;
        updateHistory(price, stamp);
    }

    @Override
    public String getId() {
        return "SN";
    }

    @Override
    public String getDescription() {
        return "Constantly day trades and short sells (using updated algorithm).";
    }
}
