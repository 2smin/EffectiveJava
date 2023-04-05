# Item62

## 다른 타입이 적절하다면 문자열 사용을 피하라

- 문자열은 다른 값 타입을 대신하기에 적합하지 않음
    - 파일, 네트워크, 키부드로부터 데이터를 받을 때 문자열을 많이 사용하는데, 입력받는 데이터가 진짜 문자열인 경우에만 String 사용하기
- 문자열은 열거 타입을 대신하기에 적합하지 않음
    - 상수 열거시, 열거 타입이 월등히 좋음 (Item34)
- 문자열은 혼합타입을 대신하기에 적합하지 않음
    - 여러 요소가 혼합된 데이터를 하나의 문자열로 표현하는 것
    
    ```java
    String compoundKey = className + "#" + i.next();
    ```
    
    - 각 요소 개별 접근시 파싱 필요하여 느리고 오류 가능성 높아짐
    - equals, toString, equalTo 메소드 제공 불가능
    - 전용 클래스를 새로 만들어 private 정적 멤버 클래스로 두는 것이 좋음
- 문자열은 권한(capacity)을 표현하기에 적합하지 않음
    - 잘못된 스레드 지역 변수 기능
        
        ```java
        public class ThreadLocal {
        	private ThreadLocal() { } 
        
        	// 현 스레드의 값을 키로 구분해 저장
        	public static void set(String key, Object value);
        
        	// 키가 가리키는 현 스레드의 값을 반환
        	public static Object get(String key);
        }
        ```
        
        - 스레드 구분용 문자열 키가 전역 이름 공간에서 공유
        - 클라이언트에서 같은 키 값 넘겨줄 가능성이 높고, 같은 변수를 공유하게 됨
        - 보안도 취약하여 의도적으로 같은 키 사용하면 다른 클라이언트의 값 가져올 수 있음
    - 해결 - 위조할 수 없는 키 생성
        
        ```java
        public class ThreadLocal {
        	private ThreadLocal() { }
        
        	public static class Key { // (권한)
        		Key() { }
        	}
        
        	// 위조 불가능한 고유 키를 생
        	public static Key getKey() {
        	return new Key();
        	}
        	public static void set(Key key, Object value);
        	public static Object get(Key key);
        }
        ```
        
        - 개선 1 - Key 안에 get/set 메소드 넣고,  Key 자체를 스레드 화
        
        ```java
        public final class ThreadLocal {
        	public ThreadLocal();
        	public void set(Object value);
        	public Object get();
        }
        ```
        
        - 개선 2 - ThreadLocal을 매개변수화 타입으로 선언
        
        ```java
        public final class ThreadLocal<T> {
        	public ThreadLocal();
        	public void set(T value);
        	public T get();
        }
        ```