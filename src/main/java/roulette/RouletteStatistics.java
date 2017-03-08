package roulette;

// TODO Implement real statistics.
public class RouletteStatistics {
    private boolean empty = true;
    private int lastBet;
    private boolean isLastWin;

    public void addBet(int betAmount, boolean win) {
        lastBet = betAmount;
        isLastWin = win;
        empty = false;
    }

    public boolean isLastWin() {
        if (empty)
            throw new IllegalStateException();

        return isLastWin;
    }

    public int lastBet() {
        if (empty)
            throw new IllegalStateException();

        return lastBet;
    }

    public boolean isEmpty() {
        return empty;
    }
}
