package roulette;

import java.util.function.BooleanSupplier;

public class Utils {
    private Utils() {
        // No-op.
    }

    public static void awaitFor(BooleanSupplier supplier) {
        while (!supplier.getAsBoolean()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
