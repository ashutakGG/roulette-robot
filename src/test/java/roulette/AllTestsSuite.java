package roulette;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        DummyRouletteTest.class,
        RobotTest.class,
})

public class AllTestsSuite {
    // The class remains empty,
    // Used only as a holder for the above annotations
}
