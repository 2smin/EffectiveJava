package Chapter12.Item87.smlee;

import java.awt.*;
import java.io.Serializable;

public class StringList implements Serializable {

    private int size = 0;
    private Entry head = null;
    private static class Entry implements  Serializable {
        String data;
        Entry next;
        Entry previous;
    }
}
