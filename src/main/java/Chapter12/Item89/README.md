# Item89

## 인스턴스 수를 통제해야 한다면 readResolve보다는 열거 타입을 사용하라

- 싱글턴 클래스에 implement Serializable을 추가하는 순간, 더이상 싱글턴이 아니게 됨
- 기본 직렬화를 쓰지 않아도, readObject를 제공해도 소용이 없음
- readResolve()로 인스턴스 통제 가능
    - readObject가 만들어낸 인스턴스를 다른것으로 대체 가능
    - 역직렬화 후 새로 생성된 객체를 인수로 이 메서드가 호출되고, 이 메서드가 반환한 객체 참조가 새로 생성된 객체를 대신해 반환함
    - 싱글턴 속성 유지 가능
    
    ```java
    public class Elvis {
    	public static final Elvis INSTANCE = new Elvis();
    	private Elvis() { ... }
    
    	// 진짜 Elvis를 반환하고, 가짜 Elvis는 가비지 컬렉터에 맡김
    	private Object readResolve() {
    		return INSTANCE;
    	}
    }
    ```
    
- readResolve를 인스턴스 통제 목적으로 사용한다면 객체 참조 타입 인스턴스 필드를 transient로 선언해야함
    - 싱글턴이 transient이 아닌 필드가 있을 때, 공격 당할 수 있음
    - 역직렬화 시, 필드의 내용은 readResolve 메서드가 실행되기 전에 역직렬화됨
    - 잘 조작된 스트림을 써서 해당 참조 필드의 내용이 역직렬화 되는 시점에 그 역직렬화된 인스턴스의 참조를 훔쳐올 수 있음
- readResolve 보다는 열거 타입으로 바꾸는 편이 나은 선택
    - 선언한 상수 외의 다른 객체는 존재하지 않음을 자바가 보장해줌
- readResolve가 필요한 경우
    - 직렬화 가능 인스턴스 통제 클래스를 작성해야되는데, 컴파일 타임에는 어떤 인스턴스들이 있는지 알 수 없는 경우
- readResolve 메서드의 접근성은 매우 중요
    - final클래스에서라면 readResolve 메서드는 private이어야 함
    - final이 아닌 클래스
        - private으로 선언하면 하위클래스에서 사용 불가
        - package-private으로 선언하면 같은 패키지에 속한 하위 클래스에서만 사용 가능
        - protected나 public으로 선언하면 이를 재정의하지 않은 모든 하위 클래스에서 사용 가능
            - 하위 클래스에서 재정의하지 않았다면, 하위클래스의 인스턴스를 역직렬화하면 상위클래스의 인스턴스를 생성하여 ClassCastException 일으킬 수 있음