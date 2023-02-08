package Chapter01.Item05;


import java.util.ArrayList;
import java.util.List;

public class SpellChecker01 {
    // 정적 유틸리티 클래스로 구현
    private static final Lexicon dictionary = new Lexicon();

    // 인스턴스 생성 불가
    private SpellChecker01() {}

    public static boolean isValid(String word) {
        return false;
    }
    public static List<String> suggestions(String typo) {
        return new ArrayList<String>();
    }
}
