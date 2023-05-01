# Item90

## 직렬화된 인스턴스 대신 직렬화 프록시 사용을 검토하라

- 직렬화 프록시 패턴을 만드는 법
    - 바깥 클래스의 논리적 상태를 정밀하게 표현하는 중첩 클래스를 설계해 private static으로 선언
    - 이 중첩 클래스가 바깥 클래스의 직렬화 프록시임
    - 중첩 클래스의 생성자는 단 하나여야 하며, 바깥 클래스를 매개변수로 받아야 함
        - 단순히 인수로 넘어온 인스턴스의 데이터를 복사
    - 직렬화 프록시의 기본 직렬화 형태는 바깥 클래스의 직렬화 형태로 쓰기에 이상적
    - 바깥 클래스와 직렬화 프록시 모두 Serializable을 구현
    
    ```java
    // Serialization proxy for Period class
    private static class SerializationProxy implements Serializable {
    	 private final Date start;
    	 private final Date end;
    
    	 SerializationProxy(Period p) {
    		 this.start = p.start;
    		 this.end = p.end;
    	 }
    	 private static final long serialVersionUID =
    	 234098243823485285L; // Any number will do (Item 87)
    }
    ```
    
    - 바깥 클래스에는 writeReplace 메서드 추가
        - 직렬화 프록시를 사용하는 모든 클래스에 다음과 같이 추가하면 됨
    
    ```java
    // writeReplace method for the serialization proxy pattern
    private Object writeReplace() {
    	 return new SerializationProxy(this);
    }
    ```
    
    - 자바의 직렬화 시스템이 바깥 클래스의 인스턴스 대신 SerializationProxy의 인스턴스를 반환하게 하는 역할
    - readObject 메서드를 바깥 클래스에 추가하면 공격도 막을 수 있음
    
    ```java
    private void readObject(ObjectInputStream stream)
     throws InvalidObjectException {
     throw new InvalidObjectException("Proxy required");
    }
    ```
    
    - 중첩 클래스에 바깥 클래스와 논리적으로 동일한 인스턴스를 반환하는 readResolve 추가
    
    ```java
    private Object readResolve() {
    	return new Period(start, end); // Uses public constructor
    }
    ```
    
    - public API를 사용했기에 불변식 검사를 하지 않아도 됨

- 직렬화 프록시의 장점
    - 방어적 복사 처럼 가짜 바이트 스트림 공격과 내부 필드 탈취 공격을 차단해줌
    - 클래스의 필드를 final로 선언해도 되므로 진정한 불변으로 만들 수 있음
    - 고민거리도 없음
    - 역직렬화한 인스턴스와 원래의 직렬화된 인스턴스의 클래스가 달라도 정상 동작 함
        - EnumSet의 사례 : 원소가 64인 enumSet을 직렬화 했다가 5개 추가하여 역직렬화 하면, RegularEnumSet에서 JumboEnumSet으로 바뀜
        
- 직렬화 프록시 패턴의 한계
    - 클라이언트가 멋대로 확장할 수 있는 클래스에는 적용할 수 없음
    - 객체 그래프에 순환이 있는 클래스에도 적용할 수 없음
    - 방어적 복사보다 조금 더 느림