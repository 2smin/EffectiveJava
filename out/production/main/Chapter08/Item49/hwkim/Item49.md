# Item 49

# 매개변수가 유효한지 검사하라

메서드와 생성자 대부분은 입력 매개변수의 값이 특정 조건을 만족하기를 바란다. 예컨대 인덱스 값은 음수이면 안되며, 객체 참조는 null이 아니어야 한다. 이런 제약은 반드시 문서화해야 하며 메더드 몸체가 시작되기 전에 검사해야 한다. 이는 “오류는 가능한 빨리 (발생한 곳에서) 잡아야 한다.”는 일반 원칙의 한 사례 이기도 하다. 오류가 발생한 즉시 잡지 못하면 해당 오류를 감지하기 어려워지고, 감지하더라도 오류 발생 지점을 찾기 어려워진다.

### 매개변수 검사의 중요성

1. 메서드 몸체가 실행되기 전에 매개변수를 확인한다면 잘못된 값이 넘어왔을 때 즉각적이고 깔끔한 방식으로 예외를 던질 수 있다.
2. 매개 변수 검사를 제대로 하지 않을 경우 아래와 같이 원자성을 어기는 여러 문제가 생길 수 있다.
    - 메서드가 수행되는 중간에 모호한 예외를 던지며 실패할 수 있다.
    - 메서드가 잘 수행되지만 잘못된 결과를 반환할 수 있다.
    - 메서드가 잘 수행됐지만 어떤 객체를 이상한 상태로 만들어 놓아 미래에 이 메서드와는 관련이 없는 오류를 낼 수 있다.

public과 protected 메서드는 매개변수 값이 잘못됐을 때 던지는 예외를 @throws를 통해 문서화해야 한다. 다음과 같은 예외가 보통 사용될 것이다.

- IllegalArgumentException
- IndexOutOfBoundsException
- NullpointerException

매개변수의 제약을 문서화 한다면 그 제약을 어겼을 때 발생하는 예외도 함께 기술해야한다.

```java
public class IntegerExample {
    Integer i;
    /**
     * (현재 값 mod m) 값을 반환한다. 이 메서드는
     * 항상 음이 아닌 BigInteger를 반환한다는 점에서 remainder 메서드와 다르다.
     *
     * @param m 계수(양수여야 한다.)
     * @return 현재 값 mod m
     * @throws ArithmeticException m이 0보다 작거나 같으면 발생한다.
     */
    public BigInteger mod(BigInteger m) {
        if (m.signum() <= 0)
            throw new ArithmeticException("계수(m)은 양수여야 합니다. " + m);
        return null;
    }
}
```

위의 mod() 메서드는 m이 null이면 m.signum() 호출 때 NullPointException을 던진다. 그러나 m이 null일 때의 예외에 대해 설명이 없다. 그 이유는 이 설명을 BigInteger 클래스 수준에서 기술했기 때문이다. 클래스 수준 주석은 그 클래스의 모든 public 메서드에 적용되므로 각 메서드에 일일이 기술하는 것보다 훨씬 깔끔한 방법이다.

자바 7에서 추가된 java.util.Objects.requireNonNull 메서드는 유연하고 사용하기도 편하니, 더 이상 null 검사를 수동으로 하지 않아도 된다. 원하는 메세지도 지정할 수 있고 입력을 그대로 반환하므로 값을 사용하는 동시에 null 검사 수행이 가능하다.

```java
this.startegy = Objects.requireNonNull(startegy, "전략");
```

자바 9에서는 Objects에 범위 검사 기능도 더해졌다. checkFromIndexSize, checkFromToIndex, checkIndex라는 메서드들인데, null 검사 메서드만큼 유연하지는 않다. 리스트와 배열 전용이며, 예외 메세지 지정이 불가하다. 또한 닫힌 범위(양 끝단 값을 포함하는 범위)는 다루지 못한다.

public이 아닌 메서드라면 assert문을 사용해 매개변수 유효성을 검사할 수 있다.

```java
private static void sort(long a[], int offset, int length){
    assert a != null;
    assert offset >= 0 && offset <= a.length;
    assert length >= 0 && length <= a.length - offset;
}
```

여기서의 핵심은 이 단언문들은 자신이 단언한 조건이 무조간 참이라고 선언한다는 것이다. 따라서 실패하면 AssertionError를 던지며 런타임에 아무런 효과도, 성능 저하도 없다.