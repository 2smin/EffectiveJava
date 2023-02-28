package Chapter05.Item28.ghlim;

import java.util.ArrayList;
import java.util.List;

public class Client {

    public static void main(String[] args) {

        List<Integer> a = new ArrayList<>();
        a.add(1);
        a.add(2);

        Chooser chooser = new Chooser(a);

        Integer b = (Integer) chooser.choose();
    }

}
