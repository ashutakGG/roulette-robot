package roulette;

public interface Roulette {
    void startGame(int amount);
    boolean makeBet(Bet bet);

    int balance();
    RouletteStatus status();
}
