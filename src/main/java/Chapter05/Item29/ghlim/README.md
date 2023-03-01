# Ite29

### 이왕이면 제네릭 타입으로 만들라

- stack 클래스를 제네릭으로

```java
// 1단계 : 클래스 선언에 타입 매개변수 추가 (컴파일 안됨)
public class **Stack<E>** {
	private **E[]** elements;
	private int size = 0;
	private static final int DEFAULT_INITIAL_CAPACITY = 16;

	public Stack() {
		elements = new E[DEFAULT_INITIAL_CAPACITY]; // 컴파일 에러 발생 지점
	}
	public void push(**E** e) {
		ensureCapacity();
		elements[size++] = e;
	}
	public **E** pop() {
		if (size == 0)
			throw new EmptyStackException();
		E result = elements[--size];
		elements[size] = null; // 다 쓴 참조 해제
		return result;
	}
	
	// isEmpty / ensureCapacity 동일
}
```

- E 와 같은 실체화 불가 타입으로는 배열 만들 수 없음
- 해결 1: 제네릭 배열 생성을 금지하는 제약을 우회 (비검사 형변환)
    
    ```java
    elements = **(E[])** new Object[DEFAULT_INITIAL_CAPACITY];
    ```
    
    - 일반적으로 타입 안전하지 않아 경고 뱉음
    - 해당 예제에서는 타입 안전
        - element는 private 필드이며, 다른 메소드에 전달되는 일이 전혀 없음
        - push메소드를 통해 배열에 저장되는 원소의 타입은 항상 E
    - @SurpressWarnings(”unchecked”) 를 통해 경고 숨기면 됨
- 해결 2: elements의 필드 타입을 Object[]로 변경
    
    ```java
    private **Object[]** elements;
    ```
    
    - pop()에서 오류 발생 및 경고로 바꾸기
    
    ```java
    // 오류
    E result = elements[--size];
    
    // 캐스팅을 통해 경고로 바꿈
    E result = **(E)** elements[--size];
    
    // 경고 없애기; push에서는 E 타입만 허용하므로 이 형변환은 안전
    **@SuppressWarnings("unchecked")** E result = (E) elements[--size];
    ```
    
- 두 방법의 장단점
    - 첫번째
        - 가독성 좋고 코드 짧음
        - 형변환 한번만 해주면 됨
        - 배열의 런타임 타입이 컴파일타임 타입과 달라 *힙오염(Item32)* 일으킴
    - 두번째
        - 배열에서 원소를 읽을때마다 형변환
        - 힙오염 없음
- 제네릭 stack 클래스를 사용하는 예제
    
    ```java
    public static void main(String[] args) {
    	Stack<String> stack = new Stack<>();
    	for (String arg : args)
    		stack.push(arg);
    
    	while (!stack.isEmpty())
    		System.out.println(**stack.pop().toUpperCase()**);
    }
    ```
    
    - toUppeCase()를 사용할 때 마다 명시적 형변환 필요 없음
    
- “배열보다 리스트를 우선하라”라는 item28 과는 조금 상충
    - 제네릭 타입 안에서 리스트를 사용하는게 항상 가능하더라도, 꼭 더 좋은 것도 아님
    - 자바의 기본 타입에는 리스트가 없어 ArrayList같은 제네릭 타입도 결국 기본 타입인 배열을 사용해 구현
    - HashMap 또한 성능을 높일 목적으로 배열을 사용
    
- 대다수의 제네릭 타입은 매개변수에 아무런 제약을 두지 않음
    - Stack<**Object**>, Stack<**int[]**>, Stack<**List<String>**> 등 가능
    - 기본타입은 사용 불가
        - Stack<int>, Stack<double> 대신 박싱된 기본타입 사용
        - Stack<Integer>, Stack<Double>
- 타입 매개변수에 제약을 두는 제네릭 타입
    - DelayQueue
    
    ```java
    class DelayQueue**<E extends Delayed>** implements BlockingQueue<E>
    ```
    
    - Delayed의 하위 타입만 받는다는 뜻
    - DelayQueue와 DelayQueue를 사용하는 클라이언트는 DelayQueue의 원소에서 형변환 없이 곧바로 Delay 클래스의 메소드 호출 가능
    - 한정적 타입 매개변수 - ClassCastException 발생 안함
    - DelayQueue<Delayed> 도 가능