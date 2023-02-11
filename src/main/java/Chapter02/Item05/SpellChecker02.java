package Chapter02.Item05;

import java.util.ArrayList;
import java.util.List;


public class SpellChecker02 {
    // 싱글턴 방식
    private final Lexicon dictionary = new Lexicon();

    private SpellChecker02() {}

    public static SpellChecker02 INSTANCE = new SpellChecker02();

    public boolean isValid(String word) {
        return false;
    }
    public List<String> suggestions(String typo) {
        return new ArrayList<String>();
    }
}