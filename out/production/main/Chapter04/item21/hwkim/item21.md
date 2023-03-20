# item 21

# 인터페이스는 구현하는 쪽을 생각해 설계하라

자바8 이전에는 기존 구현체를 깨뜨리지 않고 인터페이스에 메서드를 추가할 방법이 없었다. 인터페이스에 메서드를 추가하면 추가된 메서드가 우연히 기존 구현체에 존재할 가능성이 낮기 때문에 대부분 컴파일 오류가 발생한다.

자바8에 와서 기존 인터페이스에 메서드를 추가할 수 있도록 디폴트 메서드를 소개했으나, 위와 같은 위험이 완전히 사라진 것은 아니다. 디폴트 메서드를 선언하면, 그 인터페이스를 구현한 후 디폴트 메서드를 재정의하지 않은 모든 클래스에서 디폴트 구현이 쓰이게 된다. 그러나 이러한 디폴트 구현이 모든 기존 구현체와 매끄럽게 연동되리라는 보장은 없다. **생각할 수 있는 모든 상황에서 불변식을 해치지 않는 디폴트 메서드를 작성하기란 어려우 법이다.**

다음의 예를 통해 좀 더 살펴보자

자바8의 Collection 인터페이스에 추가된 removeIf 디폴트 메서드

```java
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
```

부가 설명

Predicate는 Type T 인자를 받고 boolean을 리턴하는 함수형 인터페이스이다.

```java
public interface Predicate<T> {

    /**
     * Evaluates this predicate on the given argument.
     *
     * @param t the input argument
     * @return {@code true} if the input argument matches the predicate,
     * otherwise {@code false}
     */
    boolean test(T t);

    ....
}
```

위의 removeIf에서는 Iterable을 상속받는 모든 Collection 구현체를 대상으로 filter 조건에 true인 원소를 제거한다. 그러나 **`APACHE.COMMONS.COLLECTIONS4.COLLECTION.SYNCHRONIZEDCOLLECTION`**의 경우 위의 디폴트 메서드와 매끄럽게 연동되어지지 않는다.

SynchronizedCollection은 클라이언트가 제공한 객체로 락을 거는 능력을 추가로 제공한다. 또한 모든 메서드에서 주어진 락 객체로 동기화 한 후 내부 컬렉션 객체에 위임하는 래퍼클래스이다.

그러나 위의 removeIf의 구현은 동기화에 대한 고려가 되어있지 않다. SynchronizedCollection에서 removeIf 디폴트 메서드를 사용하기 위해서는 아래와 같이 오버라이드를 해야할 것이다.

```java
@Override
public boolean removeIf(Predicate<? super E> filter) {
   synchronized (mutex) {
        return c.removeIf(filter);
     }
}
```

**디폴트 메서드는 컴파일에 성공하더라도 기존 구현체에 런타임 오류를 일으킬 수 있다.**

- 기존 인터페이스에 디폴트 메서드로 새 매서드를 추가하는 일은 꼭 필요한 경우가 아니면 피한다.
- 디폴트 메서드는 인터페이스로부터 메서드를 제거하거나 기존 메서드의 시그니처를 수정하는 용도가 아니다. 이를 고려하지 않으면 기존 클라이언트를 망가뜨리게 된다.
- 새로운 인터페이스라면 릴리즈 전에 반드시 테스트를 거쳐야한다.