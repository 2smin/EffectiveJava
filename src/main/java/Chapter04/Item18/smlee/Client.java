package Chapter04.Item18.smlee;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Client {

    public static void main(String[] args) {

        ExtendedHashSet<Integer> extendedHashSet = new ExtendedHashSet<>(5,5);

        List<Integer> intList = new ArrayList<>();
        intList.add(1);
        intList.add(2);
        intList.add(3);

        extendedHashSet.addAll(intList);
        System.out.println(extendedHashSet.getAddCount());

        Properties p = new Properties();
    }
}
