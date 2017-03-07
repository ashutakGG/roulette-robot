package roulette;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DummyRouletteTest {
    private static final int START_AMOUNT = 100;
    private Roulette roulette;

    @Before
    public void setUp() throws Exception {
        roulette = new DummyRoulette();

        roulette.startGame(START_AMOUNT);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void startBalance() throws Exception {
        assertEquals(START_AMOUNT, roulette.balance());
    }

    @Test
    public void bet() throws Exception {
        assertTrue(roulette.makeBet(3));

        assertEquals(START_AMOUNT - 3, roulette.balance());
    }

    @Test
    public void betAll() throws Exception {
        assertTrue(roulette.makeBet(START_AMOUNT));

        assertEquals(0, roulette.balance());
    }

    @Test
    public void betMoreThanBalance() throws Exception {
        assertFalse(roulette.makeBet(START_AMOUNT + 1));

        assertEquals(START_AMOUNT, roulette.balance());
    }

    @Test
    public void checkResults() throws Exception {
        boolean winResult = false;
        boolean lossResult = false;
        for (;(!winResult || !lossResult) && roulette.balance() > 0;) {
            int balance = roulette.balance();
            roulette.makeBet(1);
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