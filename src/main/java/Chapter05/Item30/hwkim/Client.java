package Chapter05.Item30.hwkim;

import java.util.Set;

public class Client {
    public static void main(String[] args){
        Set<String> guys = Set.of("톰", "딕", "해리");
        Set<String> stooges = Set.of("래리", "모에", "컬리");
        Set<String> aflCio = SimpleGenericExample.union(guys, stooges);
        System.out.println(aflCio);
    }
}
