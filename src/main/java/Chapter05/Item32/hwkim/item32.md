# item 32

# 제네릭과 가변인수를 함께 쓸 때는 신중하라

가변인수는 메서드에 넘기는 인수의 개수를 클라이언트가 조절할 수 있게 해주는데, 구현 방식에 허점이 있다. **가변인수 메서드를 호출하면 가변인수를 담기 위한 배열이 자동으로 하나 만들어 진다.** 그런데 내부로 감춰야 했을 이 배열을 클라이언트에 노출하는 문제가 있다. 그 결과 varargs 매개변수에 제네릭이나 매개변수화 타입이 포함되면 알기 어려운 컴파일 경고가 발생한다.

```java
warning: [unchecked] Possible heap pollution from parameterized vararg type List<String>
```

매개변수화 타입의 변수가 타입이 다른 객체를 참조하면 힙 오염이 발생한다. 이렇게 다른 타입 객체를 참조하는 상황에서는 컴파일러가 자동 생성한 형변환이 실패할 수 있으니, 제네릭 타입 시스템이 약속한 타입 안전성의 근간이 흔들린다. 다음의 예를 통해 살펴보자

제네릭과 varargs를 혼용한 예제

```java
static void danferous(List<String>... stringLists) {
    List<Integer> intList = List.of(42);
    Object[] objects = stringLists;
    objects[0] = intList; // 힙 오염 발생
    String s = stringLists[0].get(0); // ClassCastException
}
```

이 매서드에서는 형변환하는 곳이 보이지 않는데도 인수를 건네 호출하면 ClassCastException이 발생한다. 마지막 줄에 컴파일러가 생성한 형변환이 숨어있기 때문이다. 이 예제를 통해 알 수 있는 것은 **제네릭 varargs 배열 매개변수에 값을 저장하는 것은 안전하지 않다**는 것이다.

제네릭 배열을 프로그래머가 직접 생성하는 것은 허용하지 않으면서 제네릭 varargs 매개변수를 받는 메서드를 선언할 수 있게 한 이유는 무엇일까? 그 답은 **제네릭이나 매개변수화 타입의 varargs 매개변수를 받는 메서드가 실무에서 매우 유용하기 때문이다.** 사실 자바 라이브러리도 이런 메서드를 여럿 제공하는데, 다음과 같은 메서드가 그 대표적인 예이다.

```java
Arrays.asList(T... a)
Collections.addAll(Collection<? super T> c, T... elements)
EnumSet.of(E first, E...rest)
```

그래서 사용자는 이 경고 들을 그냥 두거나 호출하는 곳마다 @SuppressWarnings(”unchecked”) 어노테이션이나 @SafeVarargs 어노테이션을 추가하여 경고를 숨겨야 했다. @SafeVarargs는 매서드 작성자가 그 메서드가 타입 안전함을 보장하는 장치이다. **컴파일러는 이 약속을 믿고 그 매서드가 안전하지 않을 수 있다는 경고를 더 이상 하지 않는다.** 메서드가 안전한게 확실하지 않다면 절대 @SafeVarargs를 달아서는 안된다.

## 메서드가 타입 안전한지 알 수 있는 방법

메서드를 호출할 때 varargs 매개변수를 담는 제네릭 배열이 만들어진다. 메서드가 이 제네릭 배열에 아무것도 저장하지 않고 그 배열의 참조가 밖으로 노출되지 않는다면 타입안전하다.

다시 말해 메서드를 호출할 때 생성된 가변인수 배열을 그 매서드가 덮어쓰지 않거나 신뢰할 수 없는 코드가 가변인수 배열에 접근할 수 없다면 타입안전하다.

이 때 varargs 매개변수 배열에 아무것도 저장하지 않고도 타입 안정성을 깰 수 있으니 주의가 필요하다

```java
static <T> T[] toArray(T... args) {
    return args;
} 
```

위의 코드는 얼핏 보기에 편리한 유틸리티로 보이지만 보기와 달리 위험하다. 이 메서드가 반환하는 배열의 타입은 이 메서드에 인수를 넘기는 컴파일 타임에 결정되는데, 그 시점에는 컴파일러에게 충분한 정보가 주어지지 않아 타입을 잘못판단할 수 있다.

따라서 자신의 varargs를 그대로 반환하면 힙 오염을 이 메서드를 호출한 쪽의 콜스택으로까지 전이하는 결과를 낳을 수 있다.

```java
static <T> T[] pickTwo(T a, T b) {
    switch(ThreadLocalRandom.current().nextInt(3)){
        case 0: return toArray(a, b);
        case 1: return toArray(a, c);
        case 2: return toArray(b, c);
    }
    // 도달할 수 없다.
    throw new AssertionError();
}
```

이 제네릭 메서드는 제네릭 가변인수를 받는 toArray 메서드를 호출한다는 점만 빼면 위험하지 않고 경고도 내지 않을 것이다. 이 메서드에 대해 컴파일러는 toArray에 넘길 T 인스턴스 2개를 담을 varargs 매개변수 배열을 만드는 코드를 생성한다. 이 코드가 만드는 배열의 타입은 Object[] 인데, pickTwo에 어떤 타입의 객체를 넘기더라도 담을 수 있는 가장 구체적인 타입이기 때문이다.

그리고 toArray 메서드가 돌려준 이 배열이 그대로 pickTwo를 호출한 클라이언트까지 전달된다. 즉, pickTwo는 항상 Object[] 타입 배열을 반환한다.

만약 이때 다음과 같이 클라이언트에서 pickTwo를 호출하게 된다면

```java
public static void main(String[] args) {
    String[] attrs = pickTwo("좋은", "빠른", "저렴한");
}
```

컴파일은 되지만 실행시 ClassCastException을 던진다. pickTwo의 반환값을 attrs에 저장하기 위해 Object[]를 String[]로 형변환하는 코드를 컴파일러가 생성하기 때문이다. String[]은 Object[]의 하위 타입이 아니기 때문에 이 형변환은 실패한다.

이 예는 **제네릭 varargs 매개변수 배열에 다른 메서드가 접근하도록 허용하면 안전하지 않다**는 점을 상기시킨다.

단, 아래 두 경우는 예외이다.

1. @SafeVarargs로 선언된 타입 안전성이 보장된 또 다른 varargs 메서드에 넘기는 것은 안전하다.
2. 배열 내용의 일부 함수를 호출만 하는 (varargs를 받지않는) 일반 메서드에 넘기는 것도 안전하다.

## 제네릭 varargs 매개변수를 안전하게 사용하는 법

### @SafeVarargs 사용

다음의 코드는 제네릭 varargs를 안전하게 사용하는 예다.

```java
@SafeVarargs
static <T> List<T> flatten(List<? extends T>... lists) {
    List<T> result = new ArrayList<>();
    for (List<? extends T> list : lists) {
        result.addAll(list);
    }
    return result;
}
```

@SafeVarargs를 사용하는 규칙은 제네릭이나 매개변수화 타입의 varargs 매개변수를 받는 모든 메서드에 @SafeVarargs를 다는 것이다. 따라서 안전하지 않은 메서드에 @SafeVarargs를 달고 그 안전하지 않은 메서드를 기반으로 또다른 메서드에 @SafeVarargs를 달 수 있기 따문에 반드시 안전한 것을 확인한 뒤 어노테이션을 사용해야 한다.

### varargs 매개변수를 List 매개변수로 바꾸기

```java
static <T> List<T> flatten(List<List<? extends T>> lists) {
    List<T> result = new ArrayList<>();
    for (List<? extends T> list : lists) {
        result.addAll(list);
    }
    return result;
}
```

정적 팩터리 메서드인 List.of를 활용하면 다음 코드와 같이 varargs를 넘길 수 있다. List.of에도 @SafeVarargs 어노테이션이 달려있기 때문이다.

```java
audience = flatten(List.of(friends, romans, countrymen));
```

이 방법은 컴파일러가 이 메서드의 타입 안전성을 검증할 수 있다는 장점이 있다. 또한 위의 pickTwo의 toArray와 같이 varargs 메서드를 안전하게 작성하는게 불가능한 상황에도 쓸 수 있다. 반면에 코드가 지저분해 지고 약간의 속도 저하가 있을 수 있다는 것이다.

```java
// 리턴형이 T[] -> List<T>
static <T> List<T> pickTwo(T a, T b, T c) {
    switch (ThreadLocalRandom.current().nextInt(3)) {
        case 0: return List.of(a, b);
        case 1: return List.of(a, c);
        case 2: return List.of(b, c);
    }
    throw new AssertionError();
}

public static void main(String[] args) {
    List<String> attrs = pickTwo("좋은", "빠른", "저렴한");
}
```