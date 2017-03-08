package roulette;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;
import static roulette.BetKind.RED;
import static roulette.RouletteStatus.GAME_NOT_STARTED;

public class RobotTest {
    private static final int STARTING_BALANCE = 10_000;
    public static final Bet BET = new Bet(10, RED);

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
        when(roulette.makeBet(any())).thenReturn(true);
        when(roulette.balance()).thenReturn(STARTING_BALANCE);
    }

    @Test
    public void makeWinBet() throws Exception {
        when(rouletteStrategy.nextBet(anyInt(), any())).thenReturn(BET);
        when(roulette.balance()).thenReturn(STARTING_BALANCE, STARTING_BALANCE + BET.amount());

        robot.makeBet();

        verify(roulette).makeBet(BET);
        assertFalse(robot.isStop());
        verify(rouletteStrategy).nextBet(anyInt(), eq(stats));
        verify(stats).addBet(BET, true);
    }

    @Test
    public void makeFailBet() throws Exception {
        when(rouletteStrategy.nextBet(anyInt(), any())).thenReturn(BET);
        when(roulette.balance()).thenReturn(STARTING_BALANCE, STARTING_BALANCE - BET.amount());

        robot.makeBet();

        verify(roulette).makeBet(BET);
        assertFalse(robot.isStop());
        verify(rouletteStrategy).nextBet(anyInt(), eq(stats));
        verify(stats).addBet(BET, false);
    }

    @Test
    public void tryMakeBetAndStop() throws Exception {
        when(rouletteStrategy.nextBet(anyInt(), any())).thenReturn(BET);
        when(roulette.balance()).thenReturn(1);

        robot.makeBet();

        verify(roulette, never()).makeBet(any());
        assertTrue(robot.isStop());
        verify(rouletteStrategy).nextBet(anyInt(), eq(stats));
        verify(stats, never()).addBet(any(), anyBoolean());
    }

    @Test
    public void testNoMoreBetsAfterStop() throws RobotBetException {
        robot.setStop(true);
        try {
            robot.makeBet();
            fail();
        }
        catch (IllegalStateException e) {
            verify(roulette, never()).makeBet(any());
            assertTrue(robot.isStop());
            verify(rouletteStrategy, never()).nextBet(anyInt(), any());
            verify(stats, never()).addBet(any(), anyBoolean());
        }
    }

    @Test
    public void skipBet() throws Exception {
        when(rouletteStrategy.nextBet(anyInt(), any())).thenReturn(Bet.skipBet());

        robot.makeBet();

        verify(roulette, never()).makeBet(any());
        assertFalse(robot.isStop());
        verify(rouletteStrategy).nextBet(anyInt(), eq(stats));
        verify(stats, never()).addBet(any(), anyBoolean());
    }

    @Test
    public void testNoMoreBetsAndStop() throws Exception {
        when(rouletteStrategy.nextBet(anyInt(), any())).thenReturn(Bet.stopBet());

        robot.makeBet();

        verify(roulette, never()).makeBet(any());
        assertTrue(robot.isStop());
        verify(rouletteStrategy).nextBet(anyInt(), eq(stats));
        verify(stats, never()).addBet(any(), anyBoolean());
    }

    @Test
    public void throwExceptionIfBetNotSuccess() throws Exception {
        when(rouletteStrategy.nextBet(anyInt(), any())).thenReturn(BET);
        when(roulette.makeBet(any())).thenReturn(false);

        try {
            robot.makeBet();
            fail();
        }
        catch (RobotBetException e) {
            verify(roulette).makeBet(BET);
            verify(rouletteStrategy).nextBet(anyInt(), eq(stats));
            verify(stats, never()).addBet(any(), anyBoolean());
        }
    }

    @Test
    public void throwExceptionIfGameNotStarted() throws Exception {
        when(rouletteStrategy.nextBet(anyInt(), any())).thenReturn(BET);
        when(roulette.status()).thenReturn(GAME_NOT_STARTED);

        try {
            robot.makeBet();
            fail();
        }
        catch (GameNotStartedException e) {
            verify(roulette, never()).makeBet(any());
            verify(roulette, never()).startGame(anyInt());
            verify(stats, never()).addBet(any(), anyBoolean());
        }
    }
}