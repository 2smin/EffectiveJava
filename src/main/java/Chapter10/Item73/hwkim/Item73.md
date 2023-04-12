# Item 73

# 추상화 수준에 맞는 예외를 던지라

상위 계층에서는 저수준(Low level) 예외를 잡아 자신의 추상화 수준에 맞는 예외로 바꾸어 던져야 한다. 

- 수행하려는 일과 관련이 없는 예외가 튀어나오면 당황스럽다.
- 내부 구현 방식을 드러내어 윗 레벨 API를 오염시킨다.
- 다음 릴리즈에서 구현방식을 바꾸면 다른 예외가 튀어나와 기존의 클라이언트 프로그램을 깨지게 할 수 있다.

이를 예외 번역이라고 부르며 아래와 같은 형태를 가진다.

```java
try{
		...
} catch(LowerLevelException e){
		throw new HigherLevelException(...);
}
```

```java
public E get(int index){
		ListIterator<E> i = listIterator(index);
		try{
				return i.next();
		} catch(NoSuchElementException e){
				throw new IndexOutOfBoundsException("인덱스 : " + index);
		}
}
```

### 예외 연쇄

저수준의 예외가 디버깅에 도움을 준다면 예외 연쇄를 사용하는 것이 좋다. 문제의 근본 원인인 저수준 예외를 고수준 예외에 실어보내는 방식을 예외 연쇄라 한다. 이렇게 하면 별도의 접근자 메서드(Throwable의 getCause 메서드)를 통해 필요할 경우 언제든 저수준 예외를 꺼내볼 수 있다.

예외 연쇄는 다음과 같은 형태이다.

```java
try {
    ...
} catch (LowerLevelException cause) {
    throw new HighetLevelException(cause);
}
```

고수준 예외의 생성자는 예외 연쇄용으로 설계된 상위 클래스의 생성자에 이 원인을 건네주어, 최종적으로 Throwable 생성자 까지 건네지게 한다.

예외 연쇄용 생성자는 다음과 같은 형태이다.

```java
class HigherLevelException extends Exception {
    //고수준 예외 연쇄용 생성자
    HigherLevelException(Throwable cause) {
        super(cause);
    }
}
```

대부분의 표준 예외는 예외 연쇄용 생성자를 갖추고 있다. 만약 그렇지 않은 예외라도 Throwable의 initCause 메서드를 이용해 원인을 직접 못박을 수 있다.

**무턱대고 예외를 전파하는 것보다 예외 번역이 우수한 방법이나, 남용해서는 곤란하다.** 가능하다면 저수준 메서드가 반드시 성공하도록 하여 아래 계층에서는 예외가 발생하지 않도록 하는 것이 최선이다. 아래 계층에서의 예외를 피할 수 없다면, 차선책으로 상위 계층에서 그 예외를 조용히 처리하여 문제를 API 호출자에까지 전파하지 않는 방법이 있다. 이 경우는 java.util.logging 같은 적절한 로깅 기능을 활용하여 기록해두면 클라이언트 코드와 사용자에게 문제를 전파하지 않으면서도 프로그래머가 로그를 분석해 추가 조치를 취할 수 있게 해준다.