package roulette;

import static roulette.BetKind.RED;

public class MartingaleStrategy implements RouletteStrategy {
    private final int minBet;

    public MartingaleStrategy(int minBet) {
        if (minBet <= 0)
            throw new IllegalArgumentException("MinBet: " + minBet);

        this.minBet = minBet;
    }

    @Override
    public Bet nextBet(int balance, RouletteStatistics stats) {
        if (stats.isEmpty() || stats.isLastWin())
            return new Bet(minBet, RED);

        return new Bet(stats.lastBet().amount() * 2, RED);
    }
}
