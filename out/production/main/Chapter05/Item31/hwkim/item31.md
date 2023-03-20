# item 31

# 한정적 와일드카드를 사용해 API유연성을 높이라

매개변수화 타입은 불공변이다. 서로다은 Type1과 Type2가 있을 때 Type1과 Type2가 상속관계더라도 List<Type1>은 List<Type2>의 하위 타입도 상위 타입도 아니다.

**List<String>과 List<Object>를 생각해보자**

List<Object>는 어떤 객체든 넣을 수 있지만, List<String>은 String 문자열만 넣을 수 있다. 즉 List<String>은 List<Object>가 하는 일을 제대로 수행할 수 없으니 하위 타입이 될 수 없다.(리스코프 치환 원칙 위배) 하지만 때로는 불공변 방식보다 유연한 무언가가 필요하다. 

## Stack을 활용한 한정적 와일드카드 예제

stack의 public 메서드

```java
public class Stack<E> {
    public Stack();
    public void push(E e);
    public E pop();
    public boolean isEmpty();
}
```

여기에 여러 원소들을 스택에 넣는 public 메서드를 추가한다면 다음과 같다.

```java
public class Stack<E> {
    public Stack();
    public void push(E e);
    public E pop();
    public boolean isEmpty();
    public void pushAll(Iterable<E> src) {
        for (E e : src)
            push(e);
        }
    }
}
```

이 매서드는 깨끗이 컴파일되지만 완벽하진 않다. Iterable<E> src의 원소 타입이 스택의 원소타입과 일치하지 않는 예제를 살펴보자

Stack<Number>로 선언한 후 Integer형 List를 pushAll()의 매개변수로 호출할 경우Integer는 Number의 하위 타입이기 때문에 잘 동작할 것 같다. 하지만 매개변수화 타입이 불공변 이기 때문에 에러가 발생한다.

이런 경우 한정적 와일드카드 타입이라는 특별한 매개변수화 타입을 통해 해결할 수 있다.

```java
public void pushAll(Iterable<? extends E> src) {
    for (E e : src) {
        push(e);
    }
}
```

pushAll의 입력 매개변수 타입은 “E의 하위 타입의 Iterable”여야 하며 “Iterable<? extends E>”로 나타낸다.

다음은 Stack안에 모든 원소를 주어진 Collection에 옮겨 담는 popAll을 작성해보자

```java
public void popAll(Collection<E> dst) {
    while (!isEmpty()) {
        dst.add(pop()); // compile error 
    }
}
```

이 경우 역시 Stack<Number>의 원소들을 Object형 컬렉션이 옮겨 담을 때 위와 같이 에러가 발생한다. 이번에는 Number의 상위 타입인 Object의 Collection에 원소를 옮기기 때문에 ‘E의 상위 타입의 Collection’ 으로 표시해야하고 와일드카드를 이용하면 “Collection<? super E>”로 작성할 수 있다.

```java
public void popAll(Collection<? super E> dst) {
    while (!isEmpty()) {
      dst.add(pop());
    }
}
```

결론은 **유연성을 극대화하려면 원소의 생산자나 소비자용 입력 매개변수에 와일드카드를 사용해야한다.**

## PECS(producer-extends, consumer-super)

와일드카드를 사용하는데 도움이 되는 공식이다.

1. 매개변수화 타입 T가 생산자라면 <? extends T>
2. 매개변수화 타입 T가 소비자라면 <? super T>

앞선 Stack의 예제에서 pushAll의 매개변수 src는 Stack이 사용할 E 인스턴스를 생산하므로 <? extends T>를 사용했다. (반복문의 e) 반면에 popAll의 매개변수 dst는 Stack으로부터 E 인스턴스를 소비하므로 <? super T>가 적절한 타입이다. 이를 다른말로 겟풋원칙(Get and Put Principle)이라 한다.

**PECS 예제(item30의 max 메서드)**

```java
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
```

타입 한정자인 <E extends Comparable<E>>는 “모든 타입 E는 자신과 비교할 수 있다”와 동일한 의미였다. 이를 와일드 카드를 이용해 다듬어 보자면 다음과 같다.

```java
public static <E extends Comparable<? super E>> E max(List<? extends E> c){
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
```

우선 pushAll의 예제와 같이 매개변수 c를 이용하여 E 인스턴스(반복문의 e)를 생성한다. 따라서 c의 와일드 카드는 <? extends E>를 이용해야 한다. 

반면에 <E extends Comparable<E>>는 “모든 타입 E는 자신과 비교할 수 있다”에서 <E extends Comparable<? super E>>로 변경되었다. popAll과 같이  E 인스턴스를 소비한다. (e.compareTo(result))  Comparable은 언제나 소비자 이기 때문에 Comparable<E>보다 Comparable<? super E>를 써주는 것이 좋다.

## 메서드 선언에 타입 매개변수가 한 번만 나오면 와일드카드로 대체하라

타입 매개변수와 와일드카드에는 공통되는 부분이 있어서, 메서드를 정의할 때 둘 중 어느것을 사용해도 괜찮을 때가 많다. 예를 들어 주어진 리스트에서 명시한 두 인덱스의 아이템들을 교환하는 swap static 메서드를 정의해보자

```java
// item30의 비한정적 타입 매개변수
public static <E> void swap(List<E> list, int i, int j);
// 비한정적 와일드카드
public static void swap(List<?> list, int i, int j);
```

public API라면 간단한 두번째가 낫다. 어떤 리스트든 이 메서드에 넘기면 명시한 인덱스의 원소들을 교환해줄 것이다. 이렇게 메서드 선언에 타입 매개변수가 한 번만 나오면 와일드카드로 대체하는 것이 기본 규칙이다. 비한정적 타입 매개변수라면 비한정적 와일드카드로, 한정적 타입매개변수라면 한정적 와일드카드로 바꾸면 된다.

**private static helper 메서드**

위의 비한정적 와일드카드로 작성한 swap메서드를 아래와 같이 구현할 수 있다.

```java
public static void swap(List<?> list, int i, int j){
    list.set(i, list.set(j, list.get(i)));
}
```

그러나 이 코드는 컴파일 시 오류가 발생하는데, ?에 삽입되는 객체의 유형을 컴파일러가 확인할 수 없기 때문에 발생한다. 따라서 컴파일러가 와일드카드에 할당되는 실제 타입을 알 수 있도록 하는 priavate helper 메서드를 작성해 주도록한다.

```java
public static void swap(List<?> list, int i, int j){
    swapHelper(list, i, j);
}

public static <E> void swapHelper(List<E> list, int i, int j){
    list.set(i, list.set(j, list.get(i)));
}
```

실제 타입을 알아내려면 이 hepler 메서드는 제네릭 메서드여야 한다. 또한 이렇게 구현함으로 인해 swap 메서드를 호출하는 클라이언트는 복잡한 swapHelper의 존재를 모른채 혜택을 누릴 수 있다.