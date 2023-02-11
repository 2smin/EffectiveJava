package Chapter02.Item05;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SpellChecker {

    private final Lexicon dictionary;

    // 생성자를 통한 의존 객체 주입
    public SpellChecker(Lexicon dictionary) {
        this.dictionary = Objects.requireNonNull(dictionary);
    }
    public boolean isValid(String word) {
        return false;
    }
    public List<String> suggestions(String typo) {
        return new ArrayList<>();
    }
}
