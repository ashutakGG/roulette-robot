package roulette;

import java.util.Optional;

public class MartingaleStrategy implements RouletteStrategy {
    private final int minBet;

    public MartingaleStrategy(int minBet) {
        if (minBet <= 0)
            throw new IllegalArgumentException("MinBet: " + minBet);

        this.minBet = minBet;
    }

    @Override
    public Optional<Integer> nextBet(int balance, RouletteStatistics stats) {
        if (stats.isEmpty() || stats.isLastWin())
            return Optional.of(minBet);

        return Optional.of(stats.lastBet() * 2);
    }
}
