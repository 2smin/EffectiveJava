# Item55

# 옵셔널 반환은 신중히 하라

자바 8 전에는 메서드가 특정 조건에서 값을 반환할 수 없을때 두가지 방법을 쓸 수 있었다.

- 예외 던지기
    - 진짜 예외인 상황에서만 사용해야 한다.
    - stack 추적 전체를 캡쳐하므로 비용이 상당히 든다.
- null 반환
    - 별도의 null 처리 코드를 추가해야한다.
    - 반환된 null을 어딘가에 저장한다고 하면 향후 상관없는 코드에서 NPE가 발생할 수 있다.

### Optional의 등장

optional은 null이 올수있는 클래스를 감싸서 npe를 방지하는 클래스이다.

정확히는 원소를 최대 1개 가질 수 있는 불변 컬렉션이다. Collections<T>를 구현하지는 않았지만 원칙적으로는 컬렉션이다.

보통은 T를 반환해야하지만 특정 조건에는 아무것도 반환해야하지 않을 때 Optional<T>를 반환하도록 선언하면 된다.

```java
public static <E extends Comparable<E>> E max1(Collection<E> c) {
    if(c.isEmpty()){
        throw new IllegalArgumentException("empty collections");
    }

    E result = null;
    for(E e : c){
        if(result == null || e.compareTo(result) > 0){
            result = Objects.requireNonNull(e);
        }
    }
    return result;
}
```

위 코드처럼 c가 null 인 경우 예외를 던지고 있지만 Optional로 감싸서 빈 객체를 던지면 된다.

```java
public static <E extends Comparable<E>> Optional<E> max2(Collection<E> c){
    if(c.isEmpty()){
        return Optional.empty();
    }

    E result = null;
    for(E e : c){
        if(result == null || e.compareTo(result) > 0){
            result = Objects.requireNonNull(e);
        }
    }
    return Optional.of(result);
}
```

### Optional 정적 팩토리

Optional은 정적팩토리를 통해 생성 할 수 있다. 위 코드에서는 두개의 정적팩토리를 사용했다.

- Optional.empty()
    - 빈 optional 객체를 생성하는 정적 팩토리
- Optional.of()
    - 값이 있는 optional 객체를 생성하는 정적 팩토리
    - null을 담을 수 없다. (npe 발생)
- Optional.ofNullable()
    - null을 담을 수 있는 optional 정적팩토리

Optional을 반환하는 메서드에서는 null을 반환하지 말자 (Optional 쓰는 의미가 없다)

### Stream에서의 Optional

스트림의 종단연산 중 상당수가 Optional 을 반환한다.

```java
public static <E extends Comparable<E>> Optional<E> max3(Collection<E> c){
    return c.stream().max(Comparator.naturalOrder());
}
```

### Null이나 Exception 대신 언제 Optional을??

optional은 검사 예외랑 취지가 비슷하다. 반환값이 없을수도 있음을 사용자에게 알려준다. 비검사 예외를 던지거나 null을 반환하면 나중에 어떤 오류가 발생할 수 있을지 모른다.

클라이언트의 경우 Optional을 반환하는 메서드를 사용하면 값을 받지 못했을때 취할 행동을 선택해야한다.

- 기본값 설정하기
- 예외 던지기 (….?)
- 값이 채워져있다고 가정하기

```java
//기본값 설정
String maxWord1 =max3(words).orElse("no word");
//예외 던지기 (예외 안던지는게 좋다면서?)
String maxWord2 =max3(words).orElseThrow(InvalidParameterException::new);
//항상 값이 채워져있다고 가정 (없으면 예외 발생)
String maxWord3 =max3(words).get();
```

기본값 설정하는 비용 자체가 커서 좀 부담스러울때가 있다. 그런경우 Supplier<T>를 인수로 받는 orElseGet을 사용하다. 이 경우 값이 처음 필요할 때 Supplier<T>를 사용하여 생성하므로 초기 설정 비용을 낮출 수 있다.

### 기타 메서드들

filter, map, flatMap, ifPresent

- isPresent
    - 딱히 뭘 써야할지 모르겠다면 얘를 써보자
    - 안전밸브 역할하는 메서드로, optional이 채워져있으면 true, 아니면 false를 반환한다.
    - 근데 대부분은 위에 4가지로 대체 가능하다

```java
Optional<ProcessHandle> parentProcess = ProcessHandle.current().parent();
//기본 출력 코드
System.out.println("parent PID: " +
        (parentProcess.isPresent() ? String.valueOf((parentProcess.get().pid())) : "N/A"));
//Optional의 map 사용
System.out.println(
        parentProcess.map(h -> String.valueOf(h.pid())).orElse("N/A")
);
```

### Optional 다양한 사용 예시

Stream 에 Optional 을 담아서 사용 가능하다.

```java
public static void streamOfOptional(Collection<Optional> collections){
    Stream<Optional> optionalStream = collections.stream();
		//값이 있는 얘만 filter 걸어서 stream에 매핑한다
    optionalStream.filter(Optional::isPresent).map(Optional::get);
		//java9 부터 추가된 Optional의 stream. Optional에서 값이 있는 애만 뽑아서 stream으로 반환
    optionalStream.flatMap(Optional::stream);
}
```

### 무조건적으로 Optional을 반환값으로 사용하지 말자

collection, stream, array, optional 같은 컨테이너 타입은 Optional로 감싸지 말자. 빈 Optional<List<T>> 를 반환하기보다는 그냥 빈 List<T>를 반환하는것이 낫다. (빈 컨테이너를 반환하면 Optional을 반환하지 않아도 됨)

### 어떤 경우에 T 대신 Optional<T>를 반환해야하나?

- 결과가 없을 수 없으며, 클라이언트가 이 상황을 특별하게 처리해야한다면 Optional<T> 반환
    - Optional도 엄밀히 새로 할당해야하는 객체이다. 메서드를 한단계 더 거치니 성능에 영향을 주긴 준다.
- boxing 된 기본타입 Optional은 따로 타입이 있다
    - boxing된 기본타입을 Optional로 감싸면 총 두번 감싸게 되는것이니, 객체가 무겁다.
    - 이런 경우에 대비해 기본객체는 OptionalInt, OptionalLong, OptionalDouble 을 따로 제공한다.

### 기타 주의할점

Optional을 map의 키로 사용하지 말자. map 안에 키가 없다는 사실을 나타내는 방법이 두가지가 되어버린다.

1. key 자체가 없는 경우
2. key로 지정된 Optional이 빈 옵셔널인 경우

두가지 경우가 존재하므로 쓸데없이 복잡해진다.

*그냥 Optional은 Collections의 key, value, 원소나 array의 원소로 사용하는게 적절한 상황은 별로 없다.*

### Optional을 인스턴스필드에 저장하는 경우가 필요한가?

이런 상황 대부분은 필수필드를 갖는 클래스와, 이를 확장하여 선택적 필드를 추가한 하위클래스를 만들어야 하는 경우다.

필드 중 필수가 아니고, 기본타입이라 값이 없음을 나타낼 수 없을 때 Optional을 반환하게 하는것이 더 나은 방법이다.