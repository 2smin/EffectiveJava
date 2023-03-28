# Item50

## 적시에 방어적 복사본을 만들라

- 자바는 안전한 언어이지만, 다른 클래스로부터의 침범이 있을 수 있다
    - 클라이언트가 불변식을 깨뜨리려 혈안이 되어 있다고 가정을 하고 방어적으로 프로그래밍 해야 함
    - 혹은 순전한 실수로 클래스를 오작동하게 만들 수 있음

- 객체의 허락 없이는 외부에서 내부를 수정하는 일은 불가능 하지만, 본인도 모르게 내부를 허락하는 경우가 발생할 수도 있음
- 불변식을 지키지 못하는 클래스 예시

```java
public final class Period {
	private final Date start;
	private final Date end;
	/**
	* @param start 시작 시각
	* @param end 종료 시각. 시작 시각보다 뒤여야 한다.
	* @throws IllegalArgumentException 시작 시각이 종료 시각보다 늦을 때 발생
	* @throws NullPointerException start나 end가 null이면 발생
	*/
	public Period(Date start, Date end) {
		if (start.compareTo(end) > 0)
			throw new IllegalArgumentException(start + " after " + end);
		this.start = start;
		this.end = end;
	}
	public Date start() {
		return start;
	}
	public Date end() {
		return end;
	}
	... // 생략
}
```

- Date가 가변이기 때문에, 불변식을 깨뜨릴 수 있음

```java
Date start = new Date();
Date end = new Date();
Period p = new Period(start, end);
end.setYear(78); // Modifies internals of p
```

- Date의 setter 함수로 객체 내부 조작
- 해결 방법
    - Date의 경우, 불변인 Instant나 LocalDateTime, ZonedDateTime으로 대체 가능 (자바8 이후)
    - 자바 8 이전이면 Date 참조 대신 Date.getTime()이 반환하는 long 정수 사용
    - Date는 낡은 API이라 더 이상 사용하면 안되지만, 사용 기간이 길어 이미 많은 API 내부 구현이 Date로 남아있음

### 낡은 코드를 대처

1. 생성자에서 받은 가변 매개변수 각각을 방어적으로 복사
    
    ```java
    public Period(Date start, Date end) {
    	// 방어적 복사
    	this.start = new Date(start.getTime());
    	this.end = new Date(end.getTime());
    
    	// 유효성 검사
    	if (this.start.compareTo(this.end) > 0)
    		throw new IllegalArgumentException(this.start + " after " + this.end);
    }
    ```
    
    - 순서 중요! TOCTOU(time-to-check/time-to-use) 공격에서 방어
        - 반대의 순서라면, 멀티 스레딩 환경이라면 원본 객체의 유효성을 검사 한 뒤 복사하기 전에 다른 스레드가 원본 값을 수정할 위험이 있음
    - clone 메소드를사용하지 않은 이유
        - Date는 final이 아니기 때문에 매개변수가 제3자에 의해 확장될 수 있는 타입이기 때문
        - Date말고 Date의 하위 클래스 인스턴스가 매개변수로 들어올 가능성 있음
        - 하위 인스턴스의 clone 내부 구현에 start와 end를 private 정적 리스트에 담아두며 공격자에세 리스트에 접근하는 길을 만들어 줄 수 있음
2. 접근자가 가변 필드의 방어적 복사본 반환
- 접근자 메소드가 내부의 가변 정보 수정 가능하므로, 접근자도 복사본  반환해야함
    
    ```java
    // 안전하지 않은 코드
    Date start = new Date();
    Date end = new Date();
    Period p = new Period(start, end);
    p.end().setYear(78); // Modifies internals of p!
    ```
    
- 수정한 접근자
    
    ```java
    public Date start() {
    	return new Date(start.getTime());
    }
    public Date end() {
    	return new Date(end.getTime());
    }
    ```
    
    - 접근자 메소드에는 clone() 사용 가능
        - Period가 가지고 있는 Date 객체는 java.util.Date가 확실하기 때문
        - 그렇다 하더라도 인스턴스를 복사하는데에는 일반적으로 생성자나 정적 팩토리를 사용하는 것이 좋다(Item13)

→ Period 자신 말고는 가변 필드에 접근할 방법이 없음. 모든 필드가 객체 안에 캡슐화

### 복사본을 만들어야 하는 다른 경우

- 클라이언트가 제공한 객체의 참조를 내부 자료구조에 보관해야 할 때
    - 내부의 Set인스턴스에 저장하거나 Map 인스턴스의 키로 사용한다면, 추후 그 객체가 변경될 경우 불변식이 깨짐
- 내부 객체를 클라이언트에 건네주기 전에 방어적 복사본을 만드는 이유도 마찬가지
    - 내부에서 사용하는 배열을 클라이언트에 반환해야 할 때는 반드시 방어적 복사 수행
    - 혹은 배열의 불변 뷰 반환

### 방어적 복사의 단점

- 성능 저하
- 항상 쓸 수 있는 것도 아님 (같은 패키지에 속하는 등의 이유로)

### 방어적 복사가 생략 가능한 경우

- 호출자가 컴포넌트 내부를 수정하지 않으리라 확신하면 방어적 복사 생략 가능
    - 해당 매개변수나 반환값을 수정하지 말라고 명확히 문서화도 해놔야 함
- 다른 패키지에서 사용한다고 해서 넘겨받은 가변 매개변수를 항상 방어적으로 복사해 저장해야 하는 것은 아님
    - 메소드나 생성자의 매개변수로 넘기는 행위가 그 객체의 통제권을 명백히 이전함을 뜻하기도 함
    - 통제권을 이전하는 메소드를 호출하는 클라이언트는 해당 객체를 더 이상 직접 수정하는 일이 없다고 약속해야 함
    - 책임이 클라이언트에 있음을 분명히 명시해야 함
- 가변 객체를 넘겨받은 클래스와 그 클라이언트가 상호 신뢰할 수 있을 때, 혹은 불변식이 깨지더라도 그 영향이 오직 호출한 클라이언트로 국한될 때 생략 가능