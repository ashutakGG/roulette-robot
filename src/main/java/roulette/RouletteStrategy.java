package roulette;

import java.util.Optional;

public interface RouletteStrategy {
    Optional<Integer> nextBet(int balance, RouletteStatistics stats);

    int startingAmount();
}
