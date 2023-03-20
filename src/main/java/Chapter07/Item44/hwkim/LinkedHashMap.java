package Chapter07.Item44.hwkim;

import java.util.Map;

public class LinkedHashMap<K,V> extends java.util.LinkedHashMap<K,V> {
    // LinkedHashMap에서 cache를 위해 지원하는 메서드를 Override
    @Override
    protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
        return size() > 10;
    }

    // 아마 LinkedHashMap의 put 호출 시 아래와 같은 함수를 호출하여 오래된 엔트리를 제거할 것이다.
//    void addEntry(int hash, K key, V value, int bucketIndex) {
//        createEntry(hash, key, value, bucketIndex);
//
//        // Remove eldest entry if instructed, else grow capacity if appropriate
//        Entry<K,V> eldest = header.after;
//        if (removeEldestEntry(eldest)) { -->
//            removeEntryForKey(eldest.key);
//        } else {
//            if (size >= threshold)
//                resize(2 * table.length);
//        }
//    }

    // LinkedHashMap을 현대적으로 새로 작성한다면 removeEldestEntry()를 호출하는 대신 아래의 함수형 인터페이스를 호출했을 것이다.
    // 혹은 표준 함수형 인터페이스 BiPredicate을 이용했을 것이다.
    @FunctionalInterface
    interface ㄸEldestEntryRemovalFunction<K, V> {
        boolean remove(Map<K, V> map, Map.Entry<K, V> eldest);
    }
}
