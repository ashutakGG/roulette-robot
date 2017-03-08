package roulette;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        stats.addBet(5, true);

        assertFalse(stats.isEmpty());
        assertEquals(5, stats.lastBet());
        assertTrue(stats.isLastWin());
    }

    @Test
    public void addFailBet() throws Exception {
        stats.addBet(10, false);

        assertFalse(stats.isEmpty());
        assertEquals(10, stats.lastBet());
        assertFalse(stats.isLastWin());
    }
}