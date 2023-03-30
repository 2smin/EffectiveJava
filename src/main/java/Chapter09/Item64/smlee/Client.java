package Chapter09.Item64.smlee;

import Chapter08.Item54.smlee.Cheese;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class Client {

    public static void main(String[] args) {

        //좋은 예시
        Set<Cheese> cheeseSet = new HashSet<>();

        //올바르지 않음. 객체의 유연성이 떨어진다.
        LinkedHashSet<Cheese> cheeseLinkedHashSet = new LinkedHashSet<>();
    }
}
