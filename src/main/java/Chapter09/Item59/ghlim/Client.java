package Chapter09.Item59.ghlim;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;

public class Client {
    enum Face { ONE, TWO, THREE, FOUR, FIVE, SIX }
    public static void main(String[] args){

        Collection<Face> faces = EnumSet.allOf(Face.class);

        for (Iterator<Face> i = faces.iterator(); i.hasNext(); )
            for (Iterator<Face> j = faces.iterator(); j.hasNext(); ) {
                System.out.println(i.next() + " " + j.next());
            }

        System.out.println(" ");
//                System.out.println(i.next() + " " + j.next());
    }

}
