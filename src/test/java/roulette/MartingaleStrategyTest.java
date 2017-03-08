package roulette;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static roulette.BetKind.RED;

public class MartingaleStrategyTest {
    private static final Bet MIN_BET = new Bet(5, RED);
    private static final int BALANCE = 100;
    private RouletteStrategy strategy;

    @Before
    public void setUp() throws Exception {
        strategy = new MartingaleStrategy(MIN_BET.amount());
    }

    @Test
    public void betWithEmptyStatisticsEqualsToMinBet() throws Exception {
        Bet bet = strategy.nextBet(100, new RouletteStatistics());

        Assert.assertEquals(MIN_BET.amount(), bet.amount());
    }

    @Test
    public void betAfterFailEqualsToTwoMinBets() throws Exception {
        RouletteStatistics stats = new RouletteStatistics();
        stats.addBet(MIN_BET, false);

        Bet bet = strategy.nextBet(BALANCE, stats);

        Assert.assertEquals(MIN_BET.amount() * 2, bet.amount());
    }

    @Test
    public void betAfter2FailsEqualsToFourMinBets() throws Exception {
        RouletteStatistics stats = new RouletteStatistics();
        stats.addBet(MIN_BET, false);
        stats.addBet(new Bet(MIN_BET.amount() * 2, MIN_BET.kind()), false);

        Bet bet = strategy.nextBet(BALANCE, stats);

        Assert.assertEquals(MIN_BET.amount() * 4, bet.amount());
    }

    @Test
    public void firstBetAfterWinEqualsToMinBet() throws Exception {
        RouletteStatistics stats = new RouletteStatistics();
        stats.addBet(MIN_BET, false);
        stats.addBet(new Bet(MIN_BET.amount() * 2, MIN_BET.kind()), true);

        Bet bet = strategy.nextBet(BALANCE, stats);

        Assert.assertEquals(MIN_BET.amount(), bet.amount());
    }
}