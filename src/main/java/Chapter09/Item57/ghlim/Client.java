package Chapter09.Item57.ghlim;

import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.Iterator;

public class Client {

    public void iteratorTest(ArrayList<String> c, ArrayList<String> c2) {
        for (Iterator<String> i = c.iterator(); i.hasNext(); ) {
            String s = i.next();
        }
        for (Iterator<String> i2 = c2.iterator(); i2.hasNext(); ) {
            String s2 = i2.next();

        }
    }

}
