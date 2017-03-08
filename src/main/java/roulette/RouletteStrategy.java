package roulette;

public interface RouletteStrategy {
    Bet nextBet(int balance, RouletteStatistics stats);

    default int startingAmount() {
        throw new UnsupportedOperationException();
    }
}
