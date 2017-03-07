package roulette;

public enum RouletteStatus {
    GAME_NOT_STARTED,
    READY_FOR_BETS,
    NOT_READY_FOR_BETS;

    public boolean isGameStarted() {
        return this != GAME_NOT_STARTED;
    }
}
