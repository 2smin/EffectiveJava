package Chapter07.Item45.smlee;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnagramWithStream {

    public static void main(String[] args) throws IOException {
    }

    public static void stream1() throws IOException{
        List<String> readline = Files.readAllLines(Paths.get(
                "C:\\Users\\tmax\\git\\EffectiveJava\\src\\main\\java\\Chapter07\\Item45\\word.txt"));
        int minGroupSize = 3;

        Stream<String> words = readline.stream();
        words.collect(
                        Collectors.groupingBy(word -> word.chars().sorted()
                                .collect(StringBuilder::new,(sb,c) -> sb.append((char)c),
                                        StringBuilder::append).toString()))
                .values().stream()
                .filter(group -> group.size() >= minGroupSize
                );
    }

    public static void stream2() throws IOException{
        List<String> readline = Files.readAllLines(Paths.get(
                "C:\\Users\\tmax\\git\\EffectiveJava\\src\\main\\java\\Chapter07\\Item45\\word.txt"));
        int minGroupSize = 3;

        Stream<String> words = readline.stream();
        words.collect(
                        Collectors.groupingBy(word -> Anagrams.alphabetize(word)))
                .values().stream()
                .filter(group -> group.size() >= minGroupSize)
                .forEach(System.out::println);
    }
}
