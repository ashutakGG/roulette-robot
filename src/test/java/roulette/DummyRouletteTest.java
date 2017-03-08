package roulette;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static roulette.BetKind.BLACK;
import static roulette.BetKind.RED;

public class DummyRouletteTest {
    private static final int START_AMOUNT = 100;
    private Roulette roulette;

    @Before
    public void setUp() throws Exception {
        roulette = new DummyRoulette();

        roulette.startGame(START_AMOUNT);
    }

    @Test
    public void testStartBalance() throws Exception {
        assertEquals(START_AMOUNT, roulette.balance());
    }

    @Test
    public void makeBet() throws Exception {
        assertTrue(roulette.makeBet(new Bet(3, BLACK)));

        assertEquals(START_AMOUNT - 3, roulette.balance());
    }

    @Test
    public void betAll() throws Exception {
        assertTrue(roulette.makeBet(new Bet(START_AMOUNT, RED)));

        assertEquals(0, roulette.balance());
    }

    @Test
    public void betMoreThanBalance() throws Exception {
        assertFalse(roulette.makeBet(new Bet(START_AMOUNT + 1, RED)));

        assertEquals(START_AMOUNT, roulette.balance());
    }

    @Test
    public void checkResults() throws Exception {
        boolean winResult = false;
        boolean lossResult = false;
        for (;(!winResult || !lossResult) && roulette.balance() > 0;) {
            int balance = roulette.balance();
            roulette.makeBet(new Bet(1, RED));
            Utils.awaitFor(() -> roulette.status() == RouletteStatus.READY_FOR_BETS);
            int newBalance = roulette.balance();

            if (newBalance > balance) {
                winResult = true;
                assertEquals(balance + 1, newBalance);
            }
            else {
                lossResult = true;
                assertEquals(balance - 1, newBalance);
            }
        }
    }
}