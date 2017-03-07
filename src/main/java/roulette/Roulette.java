package roulette;

public interface Roulette {
    void startGame(int amount);
    boolean makeBet(int amount);

    int balance();
    RouletteStatus status();
}
