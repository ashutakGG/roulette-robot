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

    public boolean makeBet(Bet bet) {
        if (bet.amount() > balance)
            return false;

        balance -= bet.amount();
        this.bet = bet.amount();

        return true;
    }

    public int balance() {
        return balance;
    }

    public RouletteStatus status() {
        boolean win = R.nextBoolean();
        if (win)
            balance += bet * 2;

        return READY_FOR_BETS;
    }
}
