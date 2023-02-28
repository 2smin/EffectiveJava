package Chapter05.Item26.smlee;

import java.util.*;

public class Client {

    private static ArrayList<Object> arrList = new ArrayList();

    private static ArrayList<?> wildList = new ArrayList<>();

    public static void main(String[] args) {

        numElementTest();

    }

    public static void numElementTest(){
        Set s1 = new HashSet();
        Set<Integer> s2 = new HashSet();
        s1.add("a");
        s1.add(3);
        s2.add(1);
        s2.add(3);


        System.out.println(numElementsInCommon1(s1,s2));
        System.out.println(numElementsInCommon2(s1,s2));


    }

    public static int numElementsInCommon1(Set s1, Set s2){
        int result = 0;
        for(Object o1 : s1){
            if(s2.contains(o1)){
                result++;
            }
        }
        return result;
    }

    public static int numElementsInCommon2(Set<?> s1, Set<?> s2){
        int result = 0;
        for(Object o1 : s1){
            if(s2.contains(o1)){
                result++;
            }
        }
        return result;
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
