package algorithms.customAlgorithms;

import algorithms.Algorithm;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;

public class DayTradeA extends Algorithm {

    private volatile boolean boughtLastTick = false;
    private volatile boolean soldLastTick = false;
    public volatile BigDecimal previous = null;
    private volatile BigDecimal lastBuyPrice = null;
    private final ArrayList<BigDecimal> historicalChanges5Min = new ArrayList<>();
    private final ArrayList<BigDecimal> historicalPrices5Min = new ArrayList<>();
    private final ArrayList<Long> historicalTimestamps5Min = new ArrayList<>();
    private final ArrayList<BigDecimal> historicalPrices20S = new ArrayList<>();
    private final ArrayList<Long> historicalTimestamps20S = new ArrayList<>();
    private volatile BigDecimal cur5MinTotal = BigDecimal.ZERO;
    private volatile BigDecimal cur5MinChangeTotal = BigDecimal.ZERO;

    private BigDecimal get5MinAvg() {
        return cur5MinTotal.divide(BigDecimal.valueOf(historicalTimestamps5Min.size()), 2, RoundingMode.HALF_EVEN);
    }

    private BigDecimal get5MinAvgChange() {
        return cur5MinChangeTotal.divide(BigDecimal.valueOf(historicalTimestamps5Min.size()), 2, RoundingMode.HALF_EVEN);
    }

    private void updateHistory(BigDecimal price, Long currentTimestamp) {
        var old5Min = currentTimestamp - 300000L;
        var old1Min = currentTimestamp - 20000L;
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
        while (historicalTimestamps20S.size() > 0 && historicalTimestamps20S.get(0) < old1Min) {
            historicalTimestamps20S.remove(0);
            historicalPrices20S.remove(0);
        }
        var total = cur5MinTotal;
        total = total.add(price);
        cur5MinTotal = total;
        historicalTimestamps5Min.add(currentTimestamp);
        historicalPrices5Min.add(price);
        historicalChanges5Min.add(price.subtract(previous));
        historicalTimestamps20S.add(currentTimestamp);
        historicalPrices20S.add(price);
    }

    private BigDecimal get20sMininum() {
        var list = new ArrayList<>(historicalPrices20S);
        list.sort(BigDecimal::compareTo);
        return list.get(0).setScale(2, RoundingMode.HALF_EVEN);

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
            lastBuyPrice = price;
            var stamp = new Date().getTime();
            updateHistory(price, stamp);
            return;
        }

        var avgPrice = get5MinAvg();
        var avgChange = get5MinAvgChange();
        var prevPriceComparison = price.compareTo(previous);
        var avgPriceComparison = price.compareTo(avgPrice);
        var lastBuyComparison = price.compareTo(lastBuyPrice);
        var avgChangeDirection = avgChange.compareTo(BigDecimal.ZERO);

        var prevSoldLastTick = soldLastTick;
        if (shares > 0 && prevPriceComparison > 0 && avgPriceComparison > 0 && lastBuyComparison > 0 && (avgChangeDirection >= 0 || boughtLastTick)) {
            bot.sell(shares);
            soldLastTick = true;
        } else {
            soldLastTick = false;
        }
        if (cash.compareTo(price) > 0) {
            var min = get20sMininum();
            var minimumComparison = price.compareTo(min);
            if (minimumComparison <= 0 && avgPriceComparison < 0 && (avgChangeDirection <= 0 || prevSoldLastTick)) {
                var amount = cash.divide(price, 2, RoundingMode.FLOOR).toBigInteger();
                var intAmount = Integer.parseInt(amount.toString());
                bot.buy(intAmount);
                boughtLastTick = true;
                lastBuyPrice = price;
            } else {
                boughtLastTick = false;
            }
        } else {
            boughtLastTick = false;
        }

        previous = price;
        var stamp = new Date().getTime();
        updateHistory(price, stamp);

    }

    @Override
    public String getId() {
        return "A";
    }

    @Override
    public String getDescription() {
        return "Constantly day trades and will never sell at a loss.";
    }
}
