package roulette;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

public class MartingaleStrategyTest {
    private static final int MIN_BET = 5;
    private static final int BALANCE = 100;
    private RouletteStrategy strategy;

    @Before
    public void setUp() throws Exception {
        strategy = new MartingaleStrategy(MIN_BET);
    }

    @Test
    public void betWithEmptyStatisticsEqualsToMinBet() throws Exception {
        Optional<Integer> betAmount = strategy.nextBet(100, new RouletteStatistics());

        Assert.assertEquals(MIN_BET, (int)betAmount.get());
    }

    @Test
    public void betAfterFailEqualsToTwoMinBets() throws Exception {
        RouletteStatistics stats = new RouletteStatistics();
        stats.addBet(MIN_BET, false);

        Optional<Integer> betAmount = strategy.nextBet(BALANCE, stats);

        Assert.assertEquals(MIN_BET * 2, (int)betAmount.get());
    }

    @Test
    public void betAfter2FailsEqualsToFourMinBets() throws Exception {
        RouletteStatistics stats = new RouletteStatistics();
        stats.addBet(MIN_BET, false);
        stats.addBet(MIN_BET * 2, false);

        Optional<Integer> betAmount = strategy.nextBet(BALANCE, stats);

        Assert.assertEquals(MIN_BET * 4, (int)betAmount.get());
    }

    @Test
    public void firstBetAfterWinEqualsToMinBet() throws Exception {
        RouletteStatistics stats = new RouletteStatistics();
        stats.addBet(MIN_BET, false);
        stats.addBet(MIN_BET * 2, true);

        Optional<Integer> betAmount = strategy.nextBet(BALANCE, stats);

        Assert.assertEquals(MIN_BET, (int)betAmount.get());
    }
}