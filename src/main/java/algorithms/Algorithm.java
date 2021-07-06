package algorithms;

import bots.SingleStockBot;

/**
 * An abstract class used for creating trading algorithms.
 */
public abstract class Algorithm {
    private SingleStockBot bot = null;

    /**
     * Constructs an Algorithm.
     */
    protected Algorithm() {

    }

    /**
     * Assigns a bot to this algorithm if not already set.
     *
     * @param bot: The bot to assign this#bot to
     */
    public void setBot(SingleStockBot bot) {
        if (this.bot == null) {
            this.bot = bot;
        }
    }

    /**
     * Returns the bot corresponding to this algorithm.
     *
     * @return The bot corresponding to this algorithm.
     */
    protected SingleStockBot getBot() {
        return bot;
    }

    /**
     * This method is used by the bot in order
     * to buy and sell stocks. It analyzes market-related
     * data in order to determine how many shares it should
     * buy, hold, or sell.
     * <p>
     * In order for a bot to buy stocks, the
     * SingleStockBot#buy() method must be called inside
     * of this method. Similarly, in order to
     * sell, the SingleStockBot#sell() method must
     * be called inside of this method.
     */
    protected abstract void analyze();

    /**
     * Returns the algorithm ID.
     *
     * @return algorithm ID
     */
    public abstract String getId();

    /**
     * Returns the algorithm description.
     *
     * @return algorithm description
     */
    public abstract String getDescription();

}
