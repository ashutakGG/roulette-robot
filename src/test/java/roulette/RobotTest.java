package roulette;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;
import static roulette.RouletteStatus.GAME_NOT_STARTED;

public class RobotTest {
    private static final int STARTING_BALANCE = 10_000;
    private static final int BET = 10;

    private Robot robot;
    private Roulette roulette;
    private RouletteStrategy rouletteStrategy;
    private RouletteStatistics stats;

    @Before
    public void setUp() throws Exception {
        roulette = mock(Roulette.class);
        rouletteStrategy = mock(RouletteStrategy.class);
        stats = mock(RouletteStatistics.class);

        robot = new Robot(roulette, rouletteStrategy);
        robot.setStatistics(stats);

        when(roulette.status()).thenReturn(RouletteStatus.READY_FOR_BETS);
        when(roulette.makeBet(anyInt())).thenReturn(true);
        when(roulette.balance()).thenReturn(STARTING_BALANCE);
    }

    @Test
    public void makeWinBet() throws Exception {
        when(rouletteStrategy.nextBet(anyInt(), any())).thenReturn(Optional.of(BET));
        when(roulette.balance()).thenReturn(STARTING_BALANCE, STARTING_BALANCE + BET);

        robot.makeBet();

        verify(roulette).makeBet(BET);
        assertFalse(robot.isStop());
        verify(rouletteStrategy).nextBet(anyInt(), eq(stats));
        verify(stats).addBet(BET, true);
    }

    @Test
    public void makeFailBet() throws Exception {
        when(rouletteStrategy.nextBet(anyInt(), any())).thenReturn(Optional.of(BET));
        when(roulette.balance()).thenReturn(STARTING_BALANCE, STARTING_BALANCE - BET);

        robot.makeBet();

        verify(roulette).makeBet(BET);
        assertFalse(robot.isStop());
        verify(rouletteStrategy).nextBet(anyInt(), eq(stats));
        verify(stats).addBet(BET, false);
    }

    @Test
    public void tryMakeBetAndStop() throws Exception {
        when(rouletteStrategy.nextBet(anyInt(), any())).thenReturn(Optional.of(BET));
        when(roulette.balance()).thenReturn(1);

        robot.makeBet();

        verify(roulette, never()).makeBet(anyInt());
        assertTrue(robot.isStop());
        verify(rouletteStrategy).nextBet(anyInt(), eq(stats));
        verify(stats, never()).addBet(anyInt(), anyBoolean());
    }

    @Test
    public void zeroBetShouldBeSkipped() throws Exception {
        when(rouletteStrategy.nextBet(anyInt(), any())).thenReturn(Optional.of(0));

        robot.makeBet();

        verify(roulette, never()).makeBet(anyInt());
        assertFalse(robot.isStop());
        verify(rouletteStrategy).nextBet(anyInt(), eq(stats));
        verify(stats, never()).addBet(anyInt(), anyBoolean());
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
            verify(rouletteStrategy).nextBet(anyInt(), eq(stats));
            verify(stats, never()).addBet(anyInt(), anyBoolean());
        }
    }

    @Test
    public void throwExceptionIfBetNotSuccess() throws Exception {
        when(rouletteStrategy.nextBet(anyInt(), any())).thenReturn(Optional.of(BET));
        when(roulette.makeBet(anyInt())).thenReturn(false);

        try {
            robot.makeBet();
            fail();
        }
        catch (RobotBetException e) {
            verify(roulette).makeBet(BET);
            verify(rouletteStrategy).nextBet(anyInt(), eq(stats));
            verify(stats, never()).addBet(anyInt(), anyBoolean());
        }
    }

    @Test
    public void throwExceptionIfGameNotStarted() throws Exception {
        when(rouletteStrategy.nextBet(anyInt(), any())).thenReturn(Optional.of(BET));
        when(roulette.status()).thenReturn(GAME_NOT_STARTED);

        try {
            robot.makeBet();
            fail();
        }
        catch (GameNotStartedException e) {
            verify(roulette, never()).makeBet(anyInt());
            verify(roulette, never()).startGame(anyInt());
            verify(stats, never()).addBet(anyInt(), anyBoolean());
        }
    }
}