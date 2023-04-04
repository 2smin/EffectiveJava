# Item42

## 익명 클래스보다는 람다를 사용하라

- 익명클래스 예시
    
    ```java
    Collections.sort(words, new Comparator<String>() {
    	public int compare(String s1, String s2) {
    	return Integer.compare(s1.length(), s2.length());
    	}
    });
    ```
    
    - 익명 클래스의 인스턴스를 함수 객체로 사용하는 낡은 기법
    - Comparator 인터페이스가 정렬을 담당하는 추상 전략을 뜻함
    - 문자열을 정렬하는 구체적인 전략을 익명 클래스로 구현
    - 익명 클래스는 코드가 너무 길기 때문에 적합하지 않음

### 람다

```java
Collections.sort(words,
	(s1, s2) -> Integer.compare(s1.length(), s2.length()));
```

- 람다, 매개변수, 반환값의 타입은 각각 Comparator<String>, String, int 지만 코드에는 언급이 없음
- 타입을 명시해야 하는 코드가 더 명확할 때만 제외하고는, 람다의 모든 매개변수 타입은 생략
    - 컴파일러가 타입 추론 진행
    - 컴파일러가 타입 결정 못한다는 오류가 발생하면 그 때 프로그래머가 직접 명시
- 람다 자리에 비교자 생성 메소드 사용 (훨씬 더 깔끔)
    
    ```java
    Collections.sort(words, comparingInt(String::length));
    ```
    
- List 인터페이스에 추가된 sort 메소드 이용
    
    ```java
    words.sort(comparingInt(String::length));
    ```
    
- Operation Enum에 인스턴스 필드로 람다 두기
    
    ```java
    public enum Operation {
    	PLUS ("+", (x, y) -> x + y),
    	MINUS ("-", (x, y) -> x - y),
    	TIMES ("*", (x, y) -> x * y),
    	DIVIDE ("/", (x, y) -> x / y);
    
    	private final String symbol;
    	private final DoubleBinaryOperator op;
    
    	Operation(String symbol, DoubleBinaryOperator op) {
    		this.symbol = symbol;
    		this.op = op;
    	}
    	@Override public String toString() { return symbol; }
    
    	public double apply(double x, double y) {
    		return op.applyAsDouble(x, y);
    	}
    }
    ```
    
    - apply 메소드에서 필드에 저장된 람다를 호출
    - DoubleBinaryOperator는 java.util.funtion 패키지가 제공하는 다양한 함수 인터페이스 중 하나
    - 생성자에 넘겨지는 인수들의 타입도 컴파일 타임에 추론 됨
        - 열거 타입 생성자 안의 람다는 열거 타입의 인스턴스 멤버에 접근 불가

- 람다를 사용하지 말아야 하는 경우
    - 람다는 이름도 없고 문서화 하지 못함
    - 코드 자체로 동작이 명확히 설명되지 않거나 코드 줄 수가 많아진다면 지양
        - 한 줄이 제일 좋고, 세 줄 넘어가면 가독성이 심하게 떨어짐
        - 람다가 길거나 읽기 어려우면 간단히 줄이거나 람다를 사용하지 않는 방향으로 리팩터링 필요
    - 인스턴스 필드나 메소드를 사용해야 하는 상황

- 익명 클래스를 사용해야 하는 상황
    - 추상클래스의 인스턴스를 만들 때 람다 사용 불가능 (익명 클래스 사용)
    - 추상 메소드가 여러개인 인터페이스의 인스턴스를 만들 때에도 익명 클래스
    - 람다는 자신 참조 불가
        - this는 바깥 인스턴스를 가리킴
    - 직렬화 형태가 구현별로 다를 수 있음(익명 클래스도 마찬가지)
        - 직렬화 하는 일은 삼가
        - 직렬화가 필요하면 private 정적 중첩 클래스(Item 24)의 인스턴스 사용

*함수형 인터페이스*

[Java 8 함수형 인터페이스 (Functional Interface)](https://bcp0109.tistory.com/313)