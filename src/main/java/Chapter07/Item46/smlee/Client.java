package Chapter07.Item46.smlee;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;

public class Client {

    public static void main(String[] args) throws IOException{
//        donotUseLikeThis();
        useStreamLikeThis();
    }


    public static void donotUseLikeThis() throws IOException{
        Map<String,Integer> freq = new HashMap<>();
        List<String> readline = Files.readAllLines(Paths.get(
                "/Users/smlee/Documents/git/EffectiveJava/src/main/java/Chapter07/Item46/word.txt"));
        readline.stream().forEach( word ->
                freq.merge(word.toLowerCase(), 1, Integer::sum)
        );
        System.out.println(freq.entrySet());
    }
    public static void useStreamLikeThis() throws IOException {
        Map<String,Long> freq = new HashMap<>();
        List<String> readline = Files.readAllLines(Paths.get(
                "/Users/smlee/Documents/git/EffectiveJava/src/main/java/Chapter07/Item46/word.txt"));
        Stream<String> readStream = readline.stream();
        freq = readStream.collect(groupingBy(String::toLowerCase, counting()));
        System.out.println(freq.entrySet());
        getMostFrequentWord(freq);
    }

    public static void getMostFrequentWord(Map<String,Long> freq){
        List<String> result = freq.keySet().stream()
                .sorted(comparing(freq::get).reversed())
                .limit(2)
                .collect(Collectors.toList());
        result.forEach(System.out::println);
    }
}
