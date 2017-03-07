package roulette;

import java.util.Random;

import static roulette.RouletteStatus.READY_FOR_BETS;

public class DummyRoulette implements Roulette {
    private static final Random R = new Random();
    private int balance;
    private int bet;

    public void startGame(int amount) {
        this.balance = amount;
    }

    public boolean makeBet(int bet) {
        if (bet > balance)
            return false;

        balance -= bet;
        this.bet = bet;

        return true;
    }

    public int balance() {
        return balance;
    }

    public RouletteStatus status() {
        boolean win = R.nextBoolean();
        if (win)
            balance+=bet*2;

        return READY_FOR_BETS;
    }
}
