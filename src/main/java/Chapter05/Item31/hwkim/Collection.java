package Chapter05.Item31.hwkim;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public interface Collection<E> extends Iterable<E> {
    default boolean removeIf(Predicate<? super E> filter) {
        Objects.requireNonNull(filter);
        boolean result = false;
        for (Iterator<E> it = iterator(); it.hasNext(); ) {
            if (filter.test(it.next())) {
                it.remove();
                result = true;
            }
        }
        return result;
    }

    // Item 31
    // 모든 타입 E는 Comparable을 상속받아 자신과 같은 인스턴스간 비교가 가능하며
    // E 타입의 List c를 받아 최대값을 찾아 출력한다.
    public static <E extends Comparable<E>> E max(List<E> c){
        if(c.isEmpty())
            throw new IllegalArgumentException("컬렉션이 비었습니다.");

        E result = null;
        for(E e : c){
            if(result == null || e.compareTo(result) >0){
                result = Objects.requireNonNull(e);
            }
        }
        return result;
    }
}
