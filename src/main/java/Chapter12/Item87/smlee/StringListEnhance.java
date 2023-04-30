package Chapter12.Item87.smlee;

import java.io.*;

public class StringListEnhance implements Serializable {

    private static final long serialVersionUID = 132415L;

    private transient int size = 0;
    private transient Entry head = null;
    private static class Entry {
        String data;
        Entry next;
        Entry previous;
    }

    public final void add(String s){}

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeInt(size);

        for(Entry e = head; e != null; e = e.next){
            stream.writeObject(e.data);
        }
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        int numElement = size;
        for(int i=0; i < numElement; i++){
            add((String)stream.readObject());
        }
    }
}
