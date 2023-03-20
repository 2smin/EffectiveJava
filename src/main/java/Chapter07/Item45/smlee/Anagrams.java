package Chapter07.Item45.smlee;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Anagrams {

    public static void main(String[] args) throws IOException {
        List<String> readline = Files.readAllLines(Paths.get(
                "C:\\Users\\tmax\\git\\EffectiveJava\\src\\main\\java\\Chapter07\\Item45\\word.txt"));
        int minGroupSize = 3;

        Map<String, Set<String>> groups = new HashMap<>();

        for(String s : readline){
            groups.computeIfAbsent(alphabetize(s), (unused) -> new TreeSet<>()).add(s);
        }

        for(Set<String> group : groups.values()){
            if(group.size() >= minGroupSize){
                System.out.println(group.size() + ": " + group);
            }
        }
    }

    public static String alphabetize(String s){
        char[] a = s.toCharArray();
        Arrays.sort(a);
        return new String(a);
    }
}
