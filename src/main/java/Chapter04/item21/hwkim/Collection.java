package Chapter04.item21.hwkim;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;

// 자바 8의 Collection 인터페이스에 추가된 removeIf 디폴트 메서드
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
}
