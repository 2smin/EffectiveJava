# Item35

## ordinal 메서드 대신 인스턴스 필드를 사용하라

- ordinal 메소드
    - 해당 상수가 그 열거 타입에서 몇 번째 위치인지를 반환

```java
public enum Ensemble {
	SOLO, DUET, TRIO, QUARTET, QUINTET,
	SEXTET, SEPTET, OCTET, NONET, DECTET;
	public int numberOfMusicians() { return ordinal() + 1; }
}
```

- 상수 선언 순서를 바꾸면 numberOfMusicians() 오동작
- 같은 상수 추가할 수 없음
    - 8중주와 복4중주(double quartet)
- 값을 중간에 비울 수 없음
    - 3중 4중주(triple quartet)을 넣으려면 dummy 11번째를 넣어야 함
- 인스턴스 필드로 해결 가능
    
    ```java
    public enum Ensemble {
    	SOLO(1), DUET(2), TRIO(3), QUARTET(4), QUINTET(5),
    	SEXTET(6), SEPTET(7), OCTET(8), DOUBLE_QUARTET(8),
    	NONET(9), DECTET(10), TRIPLE_QUARTET(12);
    
    	private final int numberOfMusicians;
    	Ensemble(int size) { this.numberOfMusicians = size; }
    	public int numberOfMusicians() { return numberOfMusicians; }
    }
    ```
    
- ordinal의 원래 목적
    - 대부분의 프로그래머는 이 메소드를 쓸 일이 없음
    - EnumSet, EnumMap 과 같이 열거 타입 기반의 범용 자료구조에 쓸 목적으로 설계 됨