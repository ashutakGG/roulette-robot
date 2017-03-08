package roulette;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static roulette.BetKind.BLACK;

public class RouletteStatisticsTest {
    private RouletteStatistics stats = new RouletteStatistics();

    @Test(expected = IllegalStateException.class)
    public void isLastWinThrowExceptionForNewStats() throws Exception {
        stats.isLastWin();
    }

    @Test(expected = IllegalStateException.class)
    public void lastBetThrowExceptionForNewStats() throws Exception {
        stats.lastBet();
    }

    @Test
    public void newStatistics() throws Exception {
        assertTrue(stats.isEmpty());
    }

    @Test
    public void addWinBet() throws Exception {
        final Bet bet = new Bet(5, BLACK);
        stats.addBet(bet, true);

        assertEquals(bet, stats.lastBet());
        assertFalse(stats.isEmpty());
        assertTrue(stats.isLastWin());
    }

    @Test
    public void addFailBet() throws Exception {
        final Bet bet = new Bet(10, BLACK);
        stats.addBet(bet, false);

        assertEquals(bet, stats.lastBet());
        assertFalse(stats.isEmpty());
        assertFalse(stats.isLastWin());
    }
}