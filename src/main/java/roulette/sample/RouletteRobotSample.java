package roulette.sample;

import roulette.*;

public class RouletteRobotSample {
    public static void main(String[] args) throws RobotBetException {
        Roulette roulette = new DummyRoulette();
        Robot robot = new Robot(roulette, new MartingaleStrategy(1));

        roulette.startGame(1000);

        for (int i = 0; i < 100; i++) {
            robot.makeBet();
        }
    }
}
