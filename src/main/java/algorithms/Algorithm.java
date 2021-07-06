package algorithms;

import bots.SingleStockBot;

public abstract class Algorithm {
    private SingleStockBot bot = null;

    protected Algorithm() {

    }

    public void setBot(SingleStockBot bot) {
        if (this.bot == null) {
            this.bot = bot;
        }
    }

    protected SingleStockBot getBot() {
        return bot;
    }

    abstract void analyze();

    /**
     * Returns the algorithm ID.
     * @return algorithm ID
     */
    public abstract String getId();

    /**
     * Returns the algorithm description.
     * @return algorithm description
     */
    public abstract String getDescription();

}
