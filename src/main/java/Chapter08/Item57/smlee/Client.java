package Chapter08.Item57.smlee;

import Chapter08.Item54.smlee.Cheese;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Client {

    public static void main(String[] args) {

        List<Cheese> cheeseList = new ArrayList<>();

        for(Cheese c : cheeseList){
        }

        for(Iterator<Cheese> iterator = cheeseList.iterator(); iterator.hasNext();){
            Cheese c = iterator.next();
        }

        Iterator<Cheese> i1 = cheeseList.iterator();
        while(i1.hasNext()){
            //...
        }

        Iterator<Cheese> i2 = cheeseList.iterator();
        while(i1.hasNext()){
            //...
        }
    }
}
