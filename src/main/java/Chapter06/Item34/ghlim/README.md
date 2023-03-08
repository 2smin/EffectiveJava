# Item34

## int 상수 대신 열거 타입을 사용하라

- 정수 열거 패턴의 단점
    - 평번한 상수를 나열한 것 뿐이라 컴파일하면 그 값이 클라이언트 파일에 그대로 새겨짐
    - 상수 값이 바뀌면 클라이언트도 다시 컴파일 필요
    - 값 출력 혹은 디버거로 살펴보면 숫자로만 보여서 도움이 안됨
- 문자열 열거 패턴의 단점
    - 상수의 의미를 출력할 수 있으나 문자열 값 그대로 하드코딩할 수 있음
    - 하드코딩한 문자열에 오타가 있어도 컴파일러는 확인 못함

### 자바의 열거 타입

- 완전한 형태의 클래스 (다른 언어의 열거 타입보다 훨씬 강력)
- 상수 하나당 자신의 인스턴스를 하나씩 만들어 public static final 필드로 공개
- 밖에서 접근할 수 있는 생성자 제공하지 않음 (사실상 final)
- 열거 타입 선언으로 만들어진 인스턴스들은 딱 하나씩만 존재
    - 싱글턴은 원소가 하나뿐인 열거 타입
    - 열거타입은 싱글턴을 일반화한 형태
- 타입 안정성 제공
    - A 열거 타입을 매개변수로 하는 메소드에 다른 열거타입 전달 시, 컴파일 에러
- 열거 타입에는 각자의 이름공간 존재
    - 이름이 같은 상수 평화롭게 공존 (어떻게?)
- 새로운 상수를 추가하거나 순서를 바꿔도 다시 컴파일 하지 않음
    - 필드의 이름만 공개되는 것이므로 상수값이 클라이언트로 컴파일되지 않기 때문
- toString() 메소드 있음
- 임의의 메소드나 필드 추가 가능
- 임의의 인터페이스 구현 가능

### 열거타입의 멤버 메소드, 멤버 필드

- 열거타입에 메소드나 필드 추가가 필요한 경우
    
    ```java
    public enum Planet {
    	MERCURY(3.302e+23, 2.439e6),   // 생성자에 질량, 반지름 넣어줌
    	VENUS(4.869e+24, 6.052e6),
    	EARTH(5.975e+24, 6.378e6),
    	MARS(6.419e+23, 3.393e6),
    	JUPITER(1.899e+27, 7.149e7),
    	SATURN(5.685e+26, 6.027e7),
    	URANUS(8.683e+25, 2.556e7),
    	NEPTUNE(1.024e+26, 2.477e7);
    	
    	// 멤버 필드
    	private final double mass;
    	private final double radius; 
    	private final double surfaceGravity;
    	private static final double G = 6.67300E-11;
    
    	// 생성자
    	Planet(double mass, double radius) {
    		this.mass = mass;
    		this.radius = radius;
    		surfaceGravity = G * mass / (radius * radius);
    	}
    
    	// 멤버 메소드
    	public double mass() { return mass; }
    	public double radius() { return radius; }
    	public double surfaceGravity() { return surfaceGravity; }
    	public double surfaceWeight(double mass) {
    		return mass * surfaceGravity; // F = ma
    	}
    }
    ```
    
- 열거 타입 상수 각각을 특정 데이터와 연결지으려면생성자에서 데이터를 받아 인스턴스 필드에 저장하면 됨
- 열거타입은 근본적으로 불변이라 모든 필드는 final 이어야함
- 클라이언트 사용 예시
    
    ```java
    public class WeightTable {
    	public static void main(String[] args) {
    		double earthWeight = Double.parseDouble(args[0]);
    		double mass = earthWeight / Planet.EARTH.surfaceGravity();
    
    		// values로 상수 p 를 돌 수 있음
    		for (Planet p : Planet.values()) {
    				System.out.printf("%s에서의 무게는 %f이다.%n", p, p.surfaceWeight(mass));
    		}
    
    	}
    }
    ```
    
- 열거타입의 values 메소드
    - 자신 안에 정의된 상수들의 값을 배열에 담아 반환하는 정적 메소드
    
- 상수가 제거 된다면? (예시 : 명왕성 제거)
    - 명왕성을 참조하지 않는 클라이언트는 문제 없음
    - 참조하는 클라이언트는 프로그램 다시 컴파일시 컴파일에러로 잡을 수 있음 (혹은 런타임에러로)

- 상수별로 동작하는 코드를 다르게 하고 싶다면? (예: 사칙연산 열거 타입)
    - switch-case문은 오류 발생 쉬움
    - apply 추상 메소드 선언 및 각 상수에서 재정의
        
        ```java
        public enum Operation {
        	PLUS {public double apply(double x, double y){return x + y;}},
        	MINUS {public double apply(double x, double y){return x - y;}},
        	TIMES {public double apply(double x, double y){return x * y;}},
        	DIVIDE{public double apply(double x, double y){return x / y;}};
        
        	public abstract double apply(double x, double y);
        }
        ```
        
    - 새로운 상수 추가시 잊을 수 없음
    - 잊더라도, apply가 추상메소드이므로 컴파일 에러 발생
- 메소드 구현을 상수별 데이터와 결합
    
    ```java
    // Enum type with constant-specific class bodies and data
    public enum Operation {
    	PLUS("+") {
    		public double apply(double x, double y) { return x + y; }
    	},
    	MINUS("-") {
    		public double apply(double x, double y) { return x - y; }
    	},
    	TIMES("*") {
    		public double apply(double x, double y) { return x * y; }
    	},
    	DIVIDE("/") {
    		public double apply(double x, double y) { return x / y; }
    	};
    
    	//상수별 데이터
    	private final String symbol;
    
    	// 생성자에 데이터 주입
    	Operation(String symbol) { this.symbol = symbol; }
    
    	// toString 재정의
    	@Override public String toString() { return symbol; }
    
    	// apply 추상 메소드
    	public abstract double apply(double x, double y);
    }
    ```
    
- toString을 재정의 하여 제공하면 fromString도 고려하기
    - 문자열을 해당 열거 타입 상수로 반환
    
    ```java
    private static final Map<String, Operation> stringToEnum =
    	Stream.of(values()).collect(
    		toMap(Object::toString, e -> e));
    
    // 지정한 문자열에 해당하는 Operation이 있으면 반환
    public static Optional<Operation> fromString(String symbol) {
    	return Optional.ofNullable(stringToEnum.get(symbol));
    }
    ```
    

### 상수별 메소드 구현의 단점

- 열거 타입 상수끼리 코드를 공유하기 어려움
- 예시
    - 급여명세서에 쓸 요일을 표현하는 열거 타입
    - 직원의 기본 임금과 그날 일한 시간이 주어지면 일당 계선
    - 주중 오버타임 - 잔업 수당, 주말 - 잔업수당
- switch-case문 사용
    
    ```java
    enum PayrollDay {
    	MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;
    	private static final int MINS_PER_SHIFT = 8 * 60;
    
    	int pay(int minutesWorked, int payRate) {
    		int basePay = minutesWorked * payRate;
    		int overtimePay;
    
    		switch(this) {
    			case SATURDAY: case SUNDAY: // Weekend
    				overtimePay = basePay / 2;
    			break;
    			default: // Weekday
    				overtimePay = minutesWorked <= MINS_PER_SHIFT ?
    					0 : (minutesWorked - MINS_PER_SHIFT) * payRate / 2;
    		}
    		return basePay + overtimePay;
    	}
    }
    ```
    
    - 간결하지만, 관리 관점에서는 위험 (휴가나 공휴일에 case 문 추가 해야함)
- 상수별 메소드 구현으로 급여를 정확히 계산하려면
    1. 잔업수당을 계산하는 코드를 모든 상수에 중복해서 넣기
    2. 계산 코드를 평일용/주말용 나눠 각각을 도우미 메소드로 작성한 후, 각 상수가 호출
    
    → 두 방식 모두 가독성이 떨어지고 오류 발생 가능성이 높아짐
    
    - 가장 깔끔한 방법 - 잔업수당 계산을 private 중첩 열거 타입으로 옮기기
    
    ```java
    
    enum PayrollDay {
    	MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY,
    	SATURDAY(PayType.WEEKEND), SUNDAY(PayType.WEEKEND);
    	private final PayType payType;
    
    	PayrollDay(PayType payType) { this.payType = payType; }
    
    	PayrollDay() { this(PayType.WEEKDAY); } // Default
    
    	int pay(int minutesWorked, int payRate) {
    		return payType.pay(minutesWorked, payRate);
    	}
    
    	// 전략 열거 타입
    	private enum PayType {
    		WEEKDAY {
    			int overtimePay(int minsWorked, int payRate) {
    				return minsWorked <= MINS_PER_SHIFT ? 0 :
    					(minsWorked - MINS_PER_SHIFT) * payRate / 2;
    			}
    		},
    		WEEKEND {
    			int overtimePay(int minsWorked, int payRate) {
    				return minsWorked * payRate / 2;
    			}
    		};
    
    		abstract int overtimePay(int mins, int payRate);
    		private static final int MINS_PER_SHIFT = 8 * 60;
    
    		int pay(int minsWorked, int payRate) {
    			int basePay = minsWorked * payRate;
    			return basePay + overtimePay(minsWorked, payRate);
    		}
    	}
    }
    ```
    
- switch-case문이 좋은 선택인 경우
    - 기존 열거 타입에 상수별 동작을 혼합해 넣을 경우
    
    ```java
    public static Operation inverse(Operation op) {
    	switch(op) {
    		case PLUS: return Operation.MINUS;
    		case MINUS: return Operation.PLUS;
    		case TIMES: return Operation.DIVIDE;
    		case DIVIDE: return Operation.TIMES;
    		default: throw new AssertionError("Unknown op: " + op);
    	}
    }
    ```
    
- 열거타입을 언제 사용해야 할까
    - **필요한 원소를 컴파일타임에 다 알 수 있는 상수 집합이라면 항상 열거 타입을 사용하자**