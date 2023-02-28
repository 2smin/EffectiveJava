package Chapter05.Item26.smlee;

import java.util.*;

public class Client {

    private static ArrayList<Object> arrList = new ArrayList();

    private static ArrayList<?> wildList = new ArrayList<>();

    public static void main(String[] args) {

        arrList.add("a");
        arrList.add(new Client());

        for(Iterator i = arrList.iterator(); i.hasNext();){
            String str = (String)i.next();
        }
        List<String> strList = new ArrayList<>();

    }

    public static void printList1(List<Object> list){
        for(Object o : list){
            System.out.println("element: " + o);
        }
    }

    public static void printList2(List<Object> list){
        for(Object o : list){
            System.out.println("element: " + o);
        }
    }
}
