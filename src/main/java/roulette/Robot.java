package roulette;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.Optional;

import static roulette.Utils.awaitFor;

public class Robot {
    private final static Logger log = LogManager.getLogger(Robot.class);

    private final Roulette roulette;
    private final RouletteStrategy strategy;
    private final RouletteStatistics stats = new RouletteStatistics();
    private volatile boolean stop;

    public Robot(Roulette roulette, RouletteStrategy strategy) {
        Objects.requireNonNull(roulette);
        Objects.requireNonNull(strategy);

        this.roulette = roulette;
        this.strategy = strategy;
    }

    public void start() {
        roulette.startGame(strategy.startingAmount());

        while (!stop) {
            try {
                makeBet();
            } catch (RobotBetException e) {
                e.printStackTrace();
            }
        }
    }

    public void makeBet() throws RobotBetException {
        log.info("Making new bet...");

        if (!roulette.status().isGameStarted())
            throw new GameNotStartedException();

        awaitFor(() -> roulette.status() == RouletteStatus.READY_FOR_BETS);

        Optional<Integer> betAmount = strategy.nextBet(roulette.balance(), stats);

        assert betAmount != null;

        if (!betAmount.isPresent()) {
            stop = true;
            return;
        }

        if (betAmount.get() == 0)
            return;

        assert betAmount.get() > 0;

        if (betAmount.get() > roulette.balance()) {
            stop = true;
            return;
        }

        boolean success = roulette.makeBet(betAmount.get());

        if (!success)
            throw new RobotBetException();
    }

    public boolean isStop() {
        return stop;
    }
}
