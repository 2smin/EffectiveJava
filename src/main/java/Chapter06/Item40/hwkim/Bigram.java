package Chapter06.Item40.hwkim;

import java.util.HashSet;
import java.util.Set;

public class Bigram {
    private final char first;
    private final char second;
    public Bigram(char first, char second) {
        this.first = first;
        this.second = second;
    }

    // @Override 애너테이션을 빠트려 다중정의(오버로드) 되었다.
    // @Override 애너테이션 없이 재정의 하기 위해서는 부모 equals와 동일한 매개변수와 리턴타입을 가져야 한다.
    public boolean equals(Bigram b) {
        return b.first == first && b.second == second;
    }
    public int hashCode() {
        return 31 * first + second;
    }
    public static void main(String[] args) {
        Set<Bigram> s = new HashSet<>();
        for (int i = 0; i < 10; i++)
            for (char ch = 'a'; ch <= 'z'; ch++)
                s.add(new Bigram(ch, ch));
        System.out.println(s.size());
    }
}
