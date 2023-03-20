package Chapter05.Item28.smlee;

import java.util.ArrayList;
import java.util.List;

public class Client {

    public static void main(String[] args) {

        Object[] objectsArray = new Long[1];
        objectsArray[0] = "wer";

//        List<Object> o1 = new ArrayList<Long>();
//
//        List<String>[] stringList = new List<String>[1];
        List<Integer> intList = List.of(42);
//        Object[] objects = stringList;
//        objects[0] = intList;

    }
}
