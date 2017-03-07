package roulette;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;
import static roulette.RouletteStatus.GAME_NOT_STARTED;

public class RobotTest {
    private Robot robot;
    private Roulette roulette;
    private RouletteStrategy rouletteStrategy;

    @Before
    public void setUp() throws Exception {
        roulette = Mockito.mock(Roulette.class);
        rouletteStrategy = Mockito.mock(RouletteStrategy.class);
        robot = new Robot(roulette, rouletteStrategy);

        when(roulette.status()).thenReturn(RouletteStatus.READY_FOR_BETS);
        when(roulette.makeBet(anyInt())).thenReturn(true);

        when(roulette.balance()).thenReturn(9999);
    }

    @Test
    public void makeBet() throws Exception {
        when(rouletteStrategy.nextBet(anyInt(), any())).thenReturn(Optional.of(10));

        robot.makeBet();

        verify(roulette).makeBet(10);
        assertFalse(robot.isStop());
    }

    @Test
    public void tryMakeBetAndStop() throws Exception {
        when(rouletteStrategy.nextBet(anyInt(), any())).thenReturn(Optional.of(10));
        when(roulette.balance()).thenReturn(1);

        robot.makeBet();

        verify(roulette, never()).makeBet(anyInt());
        assertTrue(robot.isStop());
    }

    @Test
    public void zeroBetShouldBeSkipped() throws Exception {
        when(rouletteStrategy.nextBet(anyInt(), any())).thenReturn(Optional.of(0));

        robot.makeBet();

        verify(roulette, never()).makeBet(anyInt());
        assertFalse(robot.isStop());
    }

    @Test
    public void exceptionForNegativeBet() throws Exception {
        when(rouletteStrategy.nextBet(anyInt(), any())).thenReturn(Optional.of(-1));

        try {
            robot.makeBet();
            fail("Exception was expected");
        }
        catch (AssertionError e) {
            verify(roulette, never()).makeBet(anyInt());
        }
    }

    @Test
    public void throwExceptionIfBetNotSuccess() throws Exception {
        when(rouletteStrategy.nextBet(anyInt(), any())).thenReturn(Optional.of(10));
        when(roulette.makeBet(anyInt())).thenReturn(false);

        try {
            robot.makeBet();
            fail();
        }
        catch (RobotBetException e) {
            verify(roulette).makeBet(10);
        }
    }

    @Test
    public void throwExceptionIfGameNotStarted() throws Exception {
        when(rouletteStrategy.nextBet(anyInt(), any())).thenReturn(Optional.of(10));
        when(roulette.status()).thenReturn(GAME_NOT_STARTED);

        try {
            robot.makeBet();
            fail();
        }
        catch (GameNotStartedException e) {
            verify(roulette, never()).makeBet(anyInt());
            verify(roulette, never()).startGame(anyInt());
        }
    }
}