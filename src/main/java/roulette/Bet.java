package roulette;

import static roulette.BetKind.RED;

public class Bet {
    private static final Bet SKIP_BET = new Bet(1, RED);
    private static final Bet STOP_BET = new Bet(1, RED);

    private final int amount;
    private final BetKind kind;

    public Bet(int amount, BetKind kind) {
        if (amount <= 0)
            throw new IllegalArgumentException("Amount: " + amount);

        this.amount = amount;
        this.kind = kind;
    }

    public static Bet skipBet() {
        return SKIP_BET;
    }

    public static Bet stopBet() {
        return STOP_BET;
    }

    public int amount() {
        if (isSkipBet()) throw new IllegalStateException("Skip bet.");
        if (isStopBet()) throw new IllegalStateException("Stop bet.");

        return amount;
    }

    public BetKind kind() {
        if (isSkipBet()) throw new IllegalStateException("Skip bet.");
        if (isStopBet()) throw new IllegalStateException("Stop bet.");

        return kind;
    }

    public boolean isSkipBet() {
        return this == SKIP_BET;
    }

    public boolean isStopBet() {
        return this == STOP_BET;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bet)) return false;

        Bet bet = (Bet) o;

        if (amount != bet.amount) return false;
        return kind == bet.kind;

    }

    @Override
    public int hashCode() {
        int result = amount;
        result = 31 * result + kind.hashCode();
        return result;
    }
}
