package Chapter04.item21.hwkim;

import java.util.function.Predicate;

public interface SynchronizedCollection<E> extends Collection<E>{

    @Override
    public default boolean removeIf(Predicate<? super E> filter) {
        //synchronized (mutex) {
            return Collection.super.removeIf(filter);
        //}
    }
}
