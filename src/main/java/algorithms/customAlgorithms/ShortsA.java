package algorithms.customAlgorithms;

import algorithms.Algorithm;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;

public class ShortsA extends Algorithm {


    private final ArrayList<BigDecimal> historicalD1 = new ArrayList<>();
    private final ArrayList<BigDecimal> historicalD2 = new ArrayList<>();
    private final ArrayList<BigDecimal> historicalD3 = new ArrayList<>();
    private final ArrayList<BigDecimal> historicalD4 = new ArrayList<>();
    private final ArrayList<BigDecimal> historicalD5 = new ArrayList<>();
    private final ArrayList<Long> historicalTimestamps = new ArrayList<>();
    private volatile BigDecimal previous = BigDecimal.ZERO;
    private volatile BigDecimal cumulativeD1 = BigDecimal.ZERO;
    private volatile BigDecimal cumulativeD2 = BigDecimal.ZERO;
    private volatile BigDecimal cumulativeD3 = BigDecimal.ZERO;
    private volatile BigDecimal cumulativeD4 = BigDecimal.ZERO;
    private volatile BigDecimal cumulativeD5 = BigDecimal.ZERO;

    private void updateHistory(BigDecimal price) {
        var currentTimestamp = new Date().getTime();
        var old1Min = currentTimestamp - 60000L;
        while (historicalTimestamps.size() > 0 && historicalTimestamps.get(0) < old1Min) {
            historicalTimestamps.remove(0);
            var temp = cumulativeD1;
            temp = temp.subtract(historicalD1.get(0));
            cumulativeD1 = temp;
            historicalD1.remove(0);
            temp = cumulativeD2;
            temp = temp.subtract(historicalD2.get(0));
            cumulativeD2 = temp;
            historicalD2.remove(0);
            temp = cumulativeD3;
            temp = temp.subtract(historicalD3.get(0));
            cumulativeD3 = temp;
            historicalD3.remove(0);
            temp = cumulativeD4;
            temp = temp.subtract(historicalD4.get(0));
            cumulativeD4 = temp;
            historicalD4.remove(0);
            temp = cumulativeD5;
            temp = temp.subtract(historicalD5.get(0));
            cumulativeD5 = temp;
            historicalD5.remove(0);
        }

        BigDecimal d1 = price.subtract(previous);
        BigDecimal d2;
        BigDecimal d3;
        BigDecimal d4;
        BigDecimal d5;

        if (historicalTimestamps.size() > 0) {
            var i = historicalTimestamps.size() - 1;
            var prevD1 = historicalD1.get(i);
            var prevD2 = historicalD2.get(i);
            var prevD3 = historicalD3.get(i);
            var prevD4 = historicalD4.get(i);


            d2 = d1.subtract(prevD1);
            d3 = d2.subtract(prevD2);
            d4 = d3.subtract(prevD3);
            d5 = d4.subtract(prevD4);
        } else {
            d2 = d1;
            d3 = d2;
            d4 = d3;
            d5 = d4;
        }

            previous = price;

            var temp = cumulativeD1;
            temp = temp.add(d1);
            cumulativeD1 = temp;
            historicalD1.add(d1);

            temp = cumulativeD2;
            temp = temp.add(d2);
            cumulativeD2 = temp;
            historicalD2.add(d2);

            temp = cumulativeD3;
            temp = temp.add(d3);
            cumulativeD3 = temp;
            historicalD3.add(d3);

            temp = cumulativeD4;
            temp = temp.add(d4);
            cumulativeD4 = temp;
            historicalD4.add(d4);

            temp = cumulativeD5;
            temp = temp.add(d5);
            cumulativeD5 = temp;
            historicalD5.add(d5);

        historicalTimestamps.add(currentTimestamp);
    }

    public BigDecimal getAvgD1() {
        if (historicalTimestamps.size() == 0) return BigDecimal.ZERO;
        return cumulativeD1.divide(BigDecimal.valueOf(historicalTimestamps.size()), 2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getAvgD2() {
        if (historicalTimestamps.size() == 0) return BigDecimal.ZERO;
        return cumulativeD2.divide(BigDecimal.valueOf(historicalTimestamps.size()), 2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getAvgD3() {
        if (historicalTimestamps.size() == 0) return BigDecimal.ZERO;
        return cumulativeD3.divide(BigDecimal.valueOf(historicalTimestamps.size()), 2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getAvgD4() {
        if (historicalTimestamps.size() == 0) return BigDecimal.ZERO;
        return cumulativeD4.divide(BigDecimal.valueOf(historicalTimestamps.size()), 2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getAvgD5() {
        if (historicalTimestamps.size() == 0) return BigDecimal.ZERO;
        return cumulativeD5.divide(BigDecimal.valueOf(historicalTimestamps.size()), 2, RoundingMode.HALF_EVEN);
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
            historicalD1.add(BigDecimal.ZERO);
            historicalD2.add(BigDecimal.ZERO);
            historicalD3.add(BigDecimal.ZERO);
            historicalD4.add(BigDecimal.ZERO);
            historicalD5.add(BigDecimal.ZERO);
            historicalTimestamps.add(new Date().getTime());
            updateHistory(price);
            return;
        }

        var d5 = getAvgD5();
        var d4 = getAvgD4().add(d5);
        var d3 = getAvgD3().add(d4);
        var d2 = getAvgD2().add(d3);
        var d1 = getAvgD1().add(d2);
        var prediction = price.add(d1);

        var direction = price.subtract(previous).compareTo(BigDecimal.ZERO);
        var nextDirection =  prediction.subtract(price).compareTo(BigDecimal.ZERO);
        var isMax = nextDirection < direction;
        var isMin = nextDirection > direction;

        var sellAmount = shares + Math.max(0, Integer.parseInt(cash.add(price.multiply(BigDecimal.valueOf(shares))).multiply(BigDecimal.valueOf(0.5)).divide(price, 2, RoundingMode.FLOOR).toBigInteger().toString()));
        if (sellAmount != 0 && isMax) {
            bot.sell(sellAmount);
        }

        var amount = cash.divide(price, 2, RoundingMode.FLOOR).toBigInteger();
        var intAmount = Integer.parseInt(amount.toString());
        if (intAmount > 0 && isMin) {
            bot.buy(intAmount);
        }

        updateHistory(price);
    }

    @Override
    public String getId() {
        return "S3";
    }

    @Override
    public String getDescription() {
        return "Constantly day trades and short sells by predicting the next price.";
    }
}
