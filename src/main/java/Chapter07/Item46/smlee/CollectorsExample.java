package Chapter07.Item46.smlee;

import Chapter07.Item45.smlee.Anagrams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;

public class CollectorsExample {

    enum Type {
        APPLE,
        SAMSUNG,
        HWAWEI,
        BLACKBERRY
    }

    public static void main(String[] args) throws IOException{;

        run3();
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

    public static void run3() throws IOException {
        List<String> readline = Files.readAllLines(Paths.get(
                "C:\\Users\\tmax\\git\\EffectiveJava\\src\\main\\java\\Chapter07\\Item45\\word.txt"));
        Stream<String> words = readline.stream();
//        Map<Object,List<String>> result1 = words.collect(groupingBy(word -> Anagrams.alphabetize(word)));
//
//        Map<Object,Set<String>> result2 = words.collect(
//                groupingBy(word -> Anagrams.alphabetize(word), () -> new TreeMap<Object,Set<String>>(),toSet())
//        );

        Map<Boolean,List<String>> result3 = words.collect(
                partitioningBy(word -> Anagrams.alphabetize(word).length() > 5)
        );

        System.out.println(result3.entrySet());
    }
}
