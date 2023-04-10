# Item69

## 예외는 진짜 예외 상황에만 사용하라

- 예외를 사용한 반복문과 일반 반복문

```java
// 예외 사용
try {
	int i = 0;
	while(true)
	range[i++].climb();
} catch (ArrayIndexOutOfBoundsException e) {
}

// 표준 관용구 사용
for (Mountain m : range)
	m.climb();
```

- 일반적인 반복문이더라도 JVM은 배열에 접근할 때 마다 경계를 넘지 않는지 검사

- 예외는 예외 상횡에 쓸 용도로 설계되었으므로, 명확한 검사인 만큼 느리다
    - 100개짜리 원소의 배열인 경우, 예외를 사용한 코드가 2배정도 느림
- 코드를 try-catch로 감싸면 JVM이 적용할 수 있는 최적화가 제한되고, 버그를 찾아낼 가능성이 적어진다.
    - 반복문 몸체에 호출한 매서드가 내부에서 관련 없는 배열을 사용했다가  ArrayIndexOutOfBoundsException을 내면 반복문 종료인 것으로 알 수 있음
- 배열을 순회하는 표준 관용구는 JVM이 알아서 최적화 해준다

- **절대로 일상적인 제어 흐름용으로 사용되어선 안됨**
    - API 설계도 마찬가지
        - 클라이언트가 정상적인 제어 흐름에서 예외를 사용할 일이 없게 해야 함
    - 상태 의존적 메서드를 제공하는 클래스는 상태검사 메서드도 함께 제공해야 함
        - Iterator의 next(상태 의존적 메서드)와 hasNext(상태 검사 메서드)
    
    ```java
    // Iterator의 hasNext가 있어서 다음과 같은 코드 사용 가능
    for (Iterator<Foo> i = collection.iterator(); i.hasNext(); ) {
    	Foo foo = i.next();
    }
    
    // hasNext가 없었더라면 클라이언트가 일을 대신 해야 함
    try {
    	Iterator<Foo> i = collection.iterator();
    	while(true) {
    	Foo foo = i.next();
    	...
    }
    } catch (NoSuchElementException e) {
    }
    ```
    
    - 상태 검사 메서드 대신에 올바르지 않은 상태일 때 빈 옵셔널 (item55)나 null 을 반환하는 방법이 있음

- 외부 동기화 없이 여러 스레드가 동시에 접근할 수 있거나 외부 요인으로 상태가 변할 수 있다
    - 옵셔널이나 특정 값 사용
- 성능이 중요한 상황에서 상태 검사 메서드가 상태 의존적 메서드의 작업 일부를 중복 수행
    - 옵셔널이나 특정 값 사용
- 다른 모든 경우
    - 상태 검사 메서드 방식