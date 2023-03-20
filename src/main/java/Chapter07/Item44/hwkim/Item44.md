# Item 44

# 표준 함수형 인터페이스를 사용하라

자바가 람다를 지원하면서 API를 작성하는 모범 사례도 크게 바뀌었다. 함수 객체를 매개변수로 받아오는 생성자와 메서드를 많이 만들어야 하며 이 때 함수형 매개변수 타입을 올바르게 선택해야한다.

- 템플릿 메서드 패턴이 그 예이다.
    - 템플릿 매서드는 상위 클래스의 기본 메서드를 재정의해 원하는 동작을 구현하는 방법이다.
    - 템플릿 메서드 패턴을 대체하는 현대적인 방법은 같은 효과의 함수 객체를 받는 정적 팩터리나 생성자를 제공하는 것이다.

예제를 통해 살펴보자

**LinkedHashMap 예제**

LinkedHashMap의 protected 메서드인 removeEldestEntry를 재정의하면 캐시로 활용할 수 있다.

```java
protected boolean removeEldestEntry(Map.Entry<K,V> eldest){
		return size() > 100;
}
```

맵에 새로운 key를 추가하는 put 메서드는 이 메서드를 호출하여 true가 반환되면 맵에서 가장 오래된 원소를 제거한다. 위와 같이 removeEldestEntry를 재정의하면 맵에 원소가 100개가 될 때까지 커지다 그 이상이 되면 새로운 키가 더해질 때마다 가장 오래된 원소를 하나씩 제거한다. 즉, 가장 최신 원소 100개를 유지한다.

위의 코드는 잘 동작하지만, 람다를 사용하면 훨씬 잘 해낼 수 있다. removeEldestEntry의 선언을 보면 Map.Entry<K,V>를 받아 boolean을 반환해야 할 것 같지만, 꼭 그렇지는 않다. removeEldestEntry는 size() 메서드를 호출해 맵 안의 원소의 수를 알아내는데, removeEldestEntry가 인스턴스 메서드라서 가능한 방식이다. (이해한 바로는 Map인스턴스의 원소 개수만 판별하면 되기 때문에 eldest가 없어도 size 인스턴스 메서드로 원소의 size를 알 수 있다.)

하지만 생성자에 넘기는 함수 객체는 이 맵의 인스턴스 메서드가 아니다. 팩터리나 생성자를 호출할 때는 맵의 인스턴스가 존재하지 않기 때문이다. 따라서 맵은 자기 자신도 함수 객체에 건네줘야 한다. 이를 반영한 함수형 인터페이스는 다음과 같다.

```java
@FunctionalInterface
interface EldestEntryRemovalFunction<K, V> {
    boolean remove(Map<K, V> map, Map.Entry<K, V> eldest);
}
```

그러나 이렇게 직접 만드는 것보단 표준 함수형 인터페이스를 활용하는 것이 좋다. 예를 들어 Predicate 인터페이스는 predicate들을 조합하는 메서드를 제공한다. 따라서 위의 EldestEntryRemovalFunction 대신 BiPredicate<Map<K, V>, Map.Entry<K, V>> 표준 인터페이스를 사용할 수 있다.

```java
@FunctionalInterface
public interface BiPredicate<T, U> {
  boolean test(T t, U u);

  default BiPredicate<T, U> and(BiPredicate<? super T, ? super U> other) {
    Objects.requireNonNull(other);
    return (T t, U u) -> test(t, u) && other.test(t, u);
  }

  default BiPredicate<T, U> negate() {
    return (T t, U u) -> !test(t, u);
  }

  default BiPredicate<T, U> or(BiPredicate<? super T, ? super U> other) {
    Objects.requireNonNull(other);
    return (T t, U u) -> test(t, u) || other.test(t, u);
  }
}
```

**직접 구현하지 말고 표준 함수형 인터페이스를 활용하라**

java.util.function 패키지에는 다양한 용도의 표준 함수형 인터페이스가 담겨있다. 필요한 용도에 맞는게 있다면 이를 활용하자. 그러면 API가 다루는 개념의 수가 줄어들어 익히기 더 쉬워진다. 또한 표준 함수형 인터페이스들은 유용한 디폴트 메서드를 많이 제공하므로 다른 코드와의 상호운용성도 좋아진다.

java.util.function 패키지에 있는 43개 인터페이스를 모두 기억하긴 어렵겠지만, 기본 인터페이스만 살펴보도록 하자. 이 기본 인터페이스들은 모두 참조 타입용이다.

1. UnaryOperator : 인수가 1개인 연산에 대한 인터페이스, 반환값과 인수의 타입이 같은 함수를 뜻함.
2. BinaryOperator : 인수가 2개인 연산에 대한 인터페이스, 반환값과 인수의 타입이 같은 함수를 뜻한다.
3. Predicate : 인수를 하나 받아 boolean을 반환하는 함수를 뜻한다.
4. Supplier : 인수를 받지 않고 값을 반환(혹은 제공)하는 함수를 뜻한다.
5. Consumer : 인수를 하나 받고 반환값은 없는 (인수를 소비하는) 함수를 뜻한다.
6. Function : 인수와 반환 타입이 다른 함수를 뜻한다.

| 인터페이스 | 함수 시그니처 | 예 |
| --- | --- | --- |
| UnaryOperator | T apply(T t) | String::toLowerCase |
| BinaryOperator | T apply(T t1, T t2) | BigInteger::add |
| Predicate | boolean test(T t) | Collection::isEmpty |
| Function<T, R> | R apply(T t) | Array::asList |
| Supplier | T get() | Instant::now |
| Consumer | void accept(T t) | System.out::println |

표준 함수형 인터페이스 대부분은 기본타입(int, long, double)만 지원한다. 그 이름들도 기본 인터페이스의 이름 앞에 해당 기본타입의 이름을 붙여 지었다. (Ex : int를 받는 Predicate는 IntPredicate)

그렇다고 **기본 함수형 인터페이스에 박싱된 기본 타입을 넣어 사용하지는 않도록 한다.** 계산량이 많을 때는 성능이 저하의 원인이 된다. 표준 인터페이스 중 필요한 용도에 맞는게 없다면 직접 작성해야 하며, 구조적으로 똑같은 표준 함수형 인터페이스가 있더라도 직접 작성해야만 할 때가 있다.

Comparator<T> 인터페이스의 경우, 구조적으로 ToIntBiFunction<T, U>와 동일하지만 독자적인 인터페이스로 존재해야 하는 이유가 몇 개 있다.

- 첫 번째. API에서 굉장히 자주 사용되는데, 이름이 그 용도를 아주 훌륭히 설명해준다.
- 두 번째. 구현하는 쪽에서 반드시 지켜야 할 규약을 담고 있다.
- 세 번째. 비교자들을 변환하고 조합해주는 유용한 디폴트 메서드들을 많이 담고 있다.

Comparator의 특성을 정리하면 아래와 같다. 이 중 하나 이상을 만족한다면 전용 함수형 인터페이스를 구현해야 하는 건 아닌지 고민해보도록 해야 한다.

- 자주 쓰이며, 이름 자체가 용도를 명확히 설명해준다.
- 반드시 따라야 하는 규약이 있다.
- 유용한 디폴트 메서드를 제공할 수 있다.

만약 전용 함수형 인터페이스를 작성하기로 했다면, '인터페이스'임을 명심해야 한다. 아주 주의해서 설계해야 한다.

**@FunctionalInterface 애너테이션**

이 애너테이션을 사용하는 이유는 @Override를 사용하는 이유와 비슷하다. 프로그래머의 의도를 명시하는 것으로 크게 세가지 목적이 있다.

- 첫 번째. 해당 클래스의 코드나 설명 문서를 읽을 이에게 그 인터페이스가 람다용으로 설계된 것임을 알려준다.
- 두 번째. 해당 인터페이스가 추상 메서드를 오직 하나만 가지고 있어야 컴파일되게 해준다.
- 세 번째. 유지보수 과정에서 누군가 실수로 메서드를 추가하지 못하게 막아준다.

그러니 **직접 만든 함수형 인터페이스에는 항상 @FunctionalInterface 애너테이션을 사용하라**

**함수형 인터페이스를 API에서 사용할 때의 주의점**

서로 다른 함수형 인터페이스를 같은 위치의 인수로 받는 메서드들을 다중 정의해서는 안 된다. 클라이언트에게 불필요한 모호함만 줄 뿐이며, 이 때문에 실제로 문제가 일어나기도 한다.

ExecutorService의 sumit 메서드는 Callable<T>를 받는 것과 Runnable을 받는 것을 다중정의했다. 그래서 올바른 메서드를 알려주기 위해 형변환해야 할 때가 왕왕 생긴다. 이런 문제를 피하기 위해 인수로 받는 메서드들을 다중 정의해서는 안된다.