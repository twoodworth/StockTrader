package algorithms.customAlgorithms;

import algorithms.Algorithm;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;

public class ShortsB extends Algorithm {


    private final ArrayList<BigDecimal> historicalD1 = new ArrayList<>();
    private final ArrayList<BigDecimal> historicalD2 = new ArrayList<>();
    private final ArrayList<BigDecimal> historicalD3 = new ArrayList<>();
    private final ArrayList<BigDecimal> historicalD4 = new ArrayList<>();
    private final ArrayList<BigDecimal> historicalD5 = new ArrayList<>();
    private final ArrayList<BigDecimal> historicalD6 = new ArrayList<>();
    private final ArrayList<BigDecimal> historicalD7 = new ArrayList<>();
    private final ArrayList<BigDecimal> historicalD8 = new ArrayList<>();
    private final ArrayList<BigDecimal> historicalD9 = new ArrayList<>();
    private final ArrayList<BigDecimal> historicalD10 = new ArrayList<>();
    private final ArrayList<BigDecimal> historicalD11 = new ArrayList<>();
    private final ArrayList<BigDecimal> historicalD12 = new ArrayList<>();
    private final ArrayList<BigDecimal> historicalD13 = new ArrayList<>();
    private final ArrayList<BigDecimal> historicalD14 = new ArrayList<>();
    private final ArrayList<BigDecimal> historicalD15 = new ArrayList<>();
    private final ArrayList<Long> historicalTimestamps = new ArrayList<>();
    private volatile BigDecimal previous = BigDecimal.ZERO;
    private volatile BigDecimal cumulativeD1 = BigDecimal.ZERO;
    private volatile BigDecimal cumulativeD2 = BigDecimal.ZERO;
    private volatile BigDecimal cumulativeD3 = BigDecimal.ZERO;
    private volatile BigDecimal cumulativeD4 = BigDecimal.ZERO;
    private volatile BigDecimal cumulativeD5 = BigDecimal.ZERO;
    private volatile BigDecimal cumulativeD6 = BigDecimal.ZERO;
    private volatile BigDecimal cumulativeD7 = BigDecimal.ZERO;
    private volatile BigDecimal cumulativeD8 = BigDecimal.ZERO;
    private volatile BigDecimal cumulativeD9 = BigDecimal.ZERO;
    private volatile BigDecimal cumulativeD10 = BigDecimal.ZERO;
    private volatile BigDecimal cumulativeD11 = BigDecimal.ZERO;
    private volatile BigDecimal cumulativeD12 = BigDecimal.ZERO;
    private volatile BigDecimal cumulativeD13 = BigDecimal.ZERO;
    private volatile BigDecimal cumulativeD14 = BigDecimal.ZERO;
    private volatile BigDecimal cumulativeD15 = BigDecimal.ZERO;

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
            temp = cumulativeD6;
            temp = temp.subtract(historicalD6.get(0));
            cumulativeD6 = temp;
            historicalD6.remove(0);
            temp = cumulativeD7;
            temp = temp.subtract(historicalD7.get(0));
            cumulativeD7 = temp;
            historicalD7.remove(0);
            temp = cumulativeD8;
            temp = temp.subtract(historicalD8.get(0));
            cumulativeD8 = temp;
            historicalD8.remove(0);
            temp = cumulativeD9;
            temp = temp.subtract(historicalD9.get(0));
            cumulativeD9 = temp;
            historicalD9.remove(0);
            temp = cumulativeD10;
            temp = temp.subtract(historicalD10.get(0));
            cumulativeD10 = temp;
            historicalD10.remove(0);
            temp = cumulativeD11;
            temp = temp.subtract(historicalD11.get(0));
            cumulativeD11 = temp;
            historicalD11.remove(0);
            temp = cumulativeD12;
            temp = temp.subtract(historicalD12.get(0));
            cumulativeD12 = temp;
            historicalD12.remove(0);
            temp = cumulativeD13;
            temp = temp.subtract(historicalD13.get(0));
            cumulativeD13 = temp;
            historicalD13.remove(0);
            temp = cumulativeD14;
            temp = temp.subtract(historicalD14.get(0));
            cumulativeD14 = temp;
            historicalD14.remove(0);
            temp = cumulativeD15;
            temp = temp.subtract(historicalD15.get(0));
            cumulativeD15 = temp;
            historicalD15.remove(0);
        }

        BigDecimal d1 = price.subtract(previous);
        BigDecimal d2;
        BigDecimal d3;
        BigDecimal d4;
        BigDecimal d5;
        BigDecimal d6;
        BigDecimal d7;
        BigDecimal d8;
        BigDecimal d9;
        BigDecimal d10;
        BigDecimal d11;
        BigDecimal d12;
        BigDecimal d13;
        BigDecimal d14;
        BigDecimal d15;

        if (historicalTimestamps.size() > 0) {
            var i = historicalTimestamps.size() - 1;
            var prevD1 = historicalD1.get(i);
            var prevD2 = historicalD2.get(i);
            var prevD3 = historicalD3.get(i);
            var prevD4 = historicalD4.get(i);
            var prevD5 = historicalD5.get(i);
            var prevD6 = historicalD6.get(i);
            var prevD7 = historicalD7.get(i);
            var prevD8 = historicalD8.get(i);
            var prevD9 = historicalD9.get(i);
            var prevD10 = historicalD10.get(i);
            var prevD11 = historicalD11.get(i);
            var prevD12 = historicalD12.get(i);
            var prevD13 = historicalD13.get(i);
            var prevD14 = historicalD14.get(i);


            d2 = d1.subtract(prevD1);
            d3 = d2.subtract(prevD2);
            d4 = d3.subtract(prevD3);
            d5 = d4.subtract(prevD4);
            d6 = d5.subtract(prevD5);
            d7 = d6.subtract(prevD6);
            d8 = d7.subtract(prevD7);
            d9 = d8.subtract(prevD8);
            d10 = d9.subtract(prevD9);
            d11 = d10.subtract(prevD10);
            d12 = d11.subtract(prevD11);
            d13 = d12.subtract(prevD12);
            d14 = d13.subtract(prevD13);
            d15 = d14.subtract(prevD14);
        } else {
            d2 = d1;
            d3 = d2;
            d4 = d3;
            d5 = d4;
            d6 = d5;
            d7 = d6;
            d8 = d7;
            d9 = d8;
            d10 = d9;
            d11 = d10;
            d12 = d11;
            d13 = d12;
            d14 = d13;
            d15 = d14;
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

        temp = cumulativeD6;
        temp = temp.add(d6);
        cumulativeD6 = temp;
        historicalD6.add(d6);

        temp = cumulativeD7;
        temp = temp.add(d7);
        cumulativeD7 = temp;
        historicalD7.add(d7);

        temp = cumulativeD8;
        temp = temp.add(d8);
        cumulativeD8 = temp;
        historicalD8.add(d8);

        temp = cumulativeD9;
        temp = temp.add(d9);
        cumulativeD9 = temp;
        historicalD9.add(d9);

        temp = cumulativeD10;
        temp = temp.add(d10);
        cumulativeD10 = temp;
        historicalD10.add(d10);

        temp = cumulativeD11;
        temp = temp.add(d11);
        cumulativeD11 = temp;
        historicalD11.add(d11);

        temp = cumulativeD12;
        temp = temp.add(d12);
        cumulativeD12 = temp;
        historicalD12.add(d12);

        temp = cumulativeD13;
        temp = temp.add(d13);
        cumulativeD13 = temp;
        historicalD13.add(d13);

        temp = cumulativeD14;
        temp = temp.add(d14);
        cumulativeD14 = temp;
        historicalD14.add(d14);

        temp = cumulativeD15;
        temp = temp.add(d15);
        cumulativeD15 = temp;
        historicalD15.add(d15);

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

    public BigDecimal getAvgD6() {
        if (historicalTimestamps.size() == 0) return BigDecimal.ZERO;
        return cumulativeD6.divide(BigDecimal.valueOf(historicalTimestamps.size()), 2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getAvgD7() {
        if (historicalTimestamps.size() == 0) return BigDecimal.ZERO;
        return cumulativeD7.divide(BigDecimal.valueOf(historicalTimestamps.size()), 2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getAvgD8() {
        if (historicalTimestamps.size() == 0) return BigDecimal.ZERO;
        return cumulativeD8.divide(BigDecimal.valueOf(historicalTimestamps.size()), 2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getAvgD9() {
        if (historicalTimestamps.size() == 0) return BigDecimal.ZERO;
        return cumulativeD9.divide(BigDecimal.valueOf(historicalTimestamps.size()), 2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getAvgD10() {
        if (historicalTimestamps.size() == 0) return BigDecimal.ZERO;
        return cumulativeD10.divide(BigDecimal.valueOf(historicalTimestamps.size()), 2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getAvgD11() {
        if (historicalTimestamps.size() == 0) return BigDecimal.ZERO;
        return cumulativeD11.divide(BigDecimal.valueOf(historicalTimestamps.size()), 2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getAvgD12() {
        if (historicalTimestamps.size() == 0) return BigDecimal.ZERO;
        return cumulativeD12.divide(BigDecimal.valueOf(historicalTimestamps.size()), 2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getAvgD13() {
        if (historicalTimestamps.size() == 0) return BigDecimal.ZERO;
        return cumulativeD13.divide(BigDecimal.valueOf(historicalTimestamps.size()), 2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getAvgD14() {
        if (historicalTimestamps.size() == 0) return BigDecimal.ZERO;
        return cumulativeD14.divide(BigDecimal.valueOf(historicalTimestamps.size()), 2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getAvgD15() {
        if (historicalTimestamps.size() == 0) return BigDecimal.ZERO;
        return cumulativeD15.divide(BigDecimal.valueOf(historicalTimestamps.size()), 2, RoundingMode.HALF_EVEN);
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
            historicalD6.add(BigDecimal.ZERO);
            historicalD7.add(BigDecimal.ZERO);
            historicalD8.add(BigDecimal.ZERO);
            historicalD9.add(BigDecimal.ZERO);
            historicalD10.add(BigDecimal.ZERO);
            historicalD11.add(BigDecimal.ZERO);
            historicalD12.add(BigDecimal.ZERO);
            historicalD13.add(BigDecimal.ZERO);
            historicalD14.add(BigDecimal.ZERO);
            historicalD15.add(BigDecimal.ZERO);
            historicalTimestamps.add(new Date().getTime());
            updateHistory(price);
            return;
        }

        var d15 = getAvgD15();
        var d14 = getAvgD14().add(d15);
        var d13 = getAvgD13().add(d14);
        var d12 = getAvgD4().add(d13);
        var d11 = getAvgD4().add(d12);
        var d10 = getAvgD4().add(d11);
        var d9 = getAvgD4().add(d10);
        var d8 = getAvgD4().add(d9);
        var d7 = getAvgD4().add(d8);
        var d6 = getAvgD4().add(d7);
        var d5 = getAvgD4().add(d6);
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
        return "SB";
    }

    @Override
    public String getDescription() {
        return "Constantly day trades and short sells by predicting the next price.";
    }
}
