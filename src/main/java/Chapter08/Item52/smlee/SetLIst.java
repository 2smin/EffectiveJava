package Chapter08.Item52.smlee;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SetLIst {

    public static void main(String[] args) {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        for(int i=-3; i<3; i++){
            list1.add(i);
            list2.add(i);
        }

        for(int i=0; i<3; i++){
            list1.remove(i);
            list2.remove((Integer) i);
        }
        System.out.println(list1);
        System.out.println(list2);

    }
}
