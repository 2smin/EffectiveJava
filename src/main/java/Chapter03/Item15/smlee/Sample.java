package Chapter03.Item15.smlee;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Sample {
    public static final String[] ARRAY1 = {"a","b","c"};

    private static final String[] ARRAY2 = {"1","2","3"};

    public static final List<String> ARRAY_AS_LIST =
            Collections.unmodifiableList(Arrays.asList(ARRAY2));

    public static final String[] returnArray() {
        return ARRAY2.clone();
    }
}
