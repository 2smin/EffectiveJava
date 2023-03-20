package Chapter04.Item20.hwkim.SkeltonImpl;

import java.util.AbstractList;
import java.util.List;
import java.util.Objects;

public class ListSkeleton {
    static List<Integer> intArrayAsList(int[] a) {
        Objects.requireNonNull(a);
        return new AbstractList<>() {
            @Override public Integer get(int i) {
                return a[i];       // 오토 박싱
            }

            @Override public Integer set(int i, Integer val) {
                int oldVal = a[i]; // 오토 언박싱
                a[i] = val;        // 오토 박싱
                return oldVal;
            }

            @Override public int size() {
                return a.length;
            }
        };
    }
}
