package roulette;

// TODO Implement real statistics.
public class RouletteStatistics {
    private boolean empty = true;
    private Bet lastBet;
    private boolean isLastWin;

    public void addBet(Bet bet, boolean win) {
        lastBet = bet;
        isLastWin = win;
        empty = false;
    }

    public boolean isLastWin() {
        if (empty)
            throw new IllegalStateException();

        return isLastWin;
    }

    public Bet lastBet() {
        if (empty)
            throw new IllegalStateException();

        return lastBet;
    }

    public boolean isEmpty() {
        return empty;
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RouletteStatistics)) return false;

        RouletteStatistics that = (RouletteStatistics) o;

        if (empty != that.empty) return false;
        if (lastBet != that.lastBet) return false;
        return isLastWin == that.isLastWin;

    }

    @Override
    public int hashCode() {
        int result = (empty ? 1 : 0);
        result = 31 * result + lastBet.hashCode();
        result = 31 * result + (isLastWin ? 1 : 0);
        return result;
    }
}
