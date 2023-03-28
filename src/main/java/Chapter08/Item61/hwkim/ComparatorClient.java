package Chapter08.Item61.hwkim;

import java.util.Comparator;

public class ComparatorClient {
    public static void main(String[] args){
        Comparator<Integer> naturalOrder =
                (i, j) -> (i < j) ? -1 : (i == j ? 0 : 1);
        Comparator<Integer> realNaturalOrder = (iBoxed, jBoxed) -> {
            int i = iBoxed, j = jBoxed; // 오토 언박싱
            return i < j ? -1 : (i == j ? 0 : 1);
        };
        System.out.println(
                realNaturalOrder.compare(new Integer(42), new Integer(42))
        );
    }
}
