package Chapter04.Item19.smlee;

import java.time.Instant;

public final class TestOverride extends Test{

    private final Instant instant;

    public TestOverride() {
        super();
        this.instant = Instant.now();
    }

    @Override
    public void overridMe() {
        System.out.println(instant);
    }

    public static void main(String[] args) {

        TestOverride testOverride = new TestOverride();
        testOverride.overridMe();
    }
}
