package Chapter07.Item46;

import Chapter06.Item38.smlee.Operation;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

public class CollectorsExample {

    enum Type {
        APPLE,
        SAMSUNG,
        HWAWEI,
        BLACKBERRY
    }

    public static void main(String[] args) {;

        run();
        System.out.println(stringToEnumMerge.entrySet());
    }
    public static Map<String, Type> stringToEnum =
            Stream.of(Type.values()).collect(toMap(Objects::toString, e -> e));

    public static Map<Integer, String> stringToEnumMerge;
    public static void run(){

        Map<String,Integer> map = new HashMap<>();
        map.put("Apple",1);
        map.put("Samsung",2);
        map.put("BlackBerry",4);
        map.put("Hwawei",3);

        stringToEnumMerge = Stream.of(map.keySet()).
                collect(
                toMap(map::get, e -> e.toString(), (s, s2) -> s + "/" + s2
                ));
    }

    public static void run2(){
        Map<String,Integer> map = new HashMap<>();
        stringToEnumMerge = Stream.of(map.keySet()).
                collect(toMap(map::get, e -> e.toString(), (oldVal, newVal) -> newVal));

        stringToEnumMerge = Stream.of(map.keySet()).
                collect(toMap(map::get, e -> e.toString(), BinaryOperator.maxBy(comparing(map::get))));

        stringToEnumMerge = Stream.of(map.keySet()).
                collect(toMap(map::get, e -> e.toString(), BinaryOperator.maxBy(comparing(map::get)), () -> new HashMap<Integer,String>()));

    }
}
