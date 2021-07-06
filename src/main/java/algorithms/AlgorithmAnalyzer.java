package algorithms;

import bots.SingleStockBot;

public class AlgorithmAnalyzer implements Runnable {

    private final Algorithm algorithm;
    private final SingleStockBot bot;
    private Thread thread;

    public static void begin(Algorithm algorithm) {
        var analyzer = new AlgorithmAnalyzer(algorithm);
        analyzer.thread = new Thread(analyzer);
        analyzer.thread.start();
    }

    private AlgorithmAnalyzer(Algorithm algorithm) {
        this.algorithm = algorithm;
        this.bot = algorithm.getBot();
    }

    @Override
    public void run() {
        while (!bot.isRemoved()) {
            if (!bot.isPaused()) {
                algorithm.analyze();
            }
        }
        thread.interrupt();
    }
}
