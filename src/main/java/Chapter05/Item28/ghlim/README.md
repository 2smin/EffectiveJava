# Item28

### 배열보다는 리스트를 사용하라

- 배열과 제네릭 타입의 차이
    - 공변/ 불공변
        - 배열 : 공변 가능 (함께 변한다는 뜻)
            - Sub가 Super의 하위 타입이라면 배열 Sub[]는 배열 Super[]의 하위 타입이 됨
            - 런타임에 실패할 수 있는 위험
        - 제네릭 : 불공변
            - List<Type1>은 List<Type2>의 하위타입도 아니고 상위 타입도 아님
            - 컴파일 에러 발생
        
        ```java
        // 런타임 에러
        Object[] objectArray = new Long[1];
        objectArray[0] = "I don't fit in"; // Throws ArrayStoreException
        
        // 컴파일 타임 에러
        List<Object> ol = new ArrayList<Long>(); // Incompatible types
        ol.add("I don't fit in");
        ```
        
    - 배열은 실체화(reify) 됨
        - 배열은 자신이 담기로 한 원소의 타입을 인지하고 확인 함
        - 제네릭은 타입 정보가 런타임에는 소거됨 (컴파일타임에만 인지)
- 이러한 차이 때문에 배열과 제네릭은 잘 어우러지지 못함
    - new List<E>[], new List<String>[], new E[] 등을 만들 수 없음
    - 이를 허용하면, 제네릭은 타입 안정성이 없기 때문에, 자동 생성한 형변환 코드에서 런타임 에러 발생 가능
    - 런타임 에러를 발생시키지 않겠다는 제네릭의 취지에 안맞음
    
    ```java
    List<String>[] stringLists = new List<String>[1]; // stringLists = [List<String>]
    List<Integer> intList = List.of(42);              // intList = [42]
    Object[] objects = stringLists;                   // objects = stringLists
    objects[0] = intList;                             // objects[0] = [42]
    String s = stringLists[0].get(0);                 // 42는 string이 아님
    ```
    
    - E, List<E>, List<String> 등을 실체화 불가 타입이라고 함 - 런타임에는 컴파일타임보다 타입 정보를 적게 가짐
- 배열을 제네릭으로 만들 수 없어 귀찮을 때도 있음
    - 제네릭 컬렉션에서는 자신의 원소 타입을 담은 배열을 반환하는게 보통 불가능
    - 제네릭 타입과 가변인수 메소드를 함께 쓰면 경고 메세지 받음
- 배열로 형변환시, 오류나 경고 뜨는 경우 E[] 대신 List<E>를 사용하면 해결 됨
    
    ```java
    // 제네릭을 사용하지 않고 구현
    public class Chooser {
    	private final Object[] choiceArray;
    	public Chooser(Collection choices) {
    		choiceArray = choices.toArray();
    	}
    
    	// choose 호출 마다 object를 원하는 타입으로 형변환 필요
    	public **Object choose()** {
    		Random rnd = ThreadLocalRandom.current();
    		return choiceArray[rnd.nextInt(choiceArray.length)];
    	}
    }
    ```
    
    ```java
    // Chooser를 제네릭으로 1차 시도 - 컴파일 안됨
    public class **Chooser<T>** {
    	private final **T[]** choiceArray;
    
    	public Chooser(**Collection<T>** choices) {
    		choiceArray = choices.toArray(); // 에러 발생 지점
    	}
    
    	// choose method 동일
    }
    ```
    
    ```java
    // Chooser를 제네릭으로 2차 시도
    public class Chooser<T> ****{
    	private final T[] choiceArray;
    
    	public Chooser(Collection<T> choices) {
    		// 에러는 없으나 경고 발생; 런타임시 무슨 타입인지 알 수 없음
    		choiceArray = **(T[])** choices.toArray(); 
    	}
    
    	// choose method 동일
    }
    ```
    
    ```java
    // 리스트 기반 chooser - 타입 안정성 확보
    public class Chooser<T> {
    	private final **List<T>** choiceList;
    	public Chooser(Collection<T> choices) {
    		choiceList = **new ArrayList<>(choices)**;
    	}
    	public **T** choose() {
    		Random rnd = ThreadLocalRandom.current();
    		return **choiceList.get(rnd.nextInt(choiceList.size()));**
    	}
    }
    ```
    
    - 코드 양이 조금 늘고 조금 더 느리지만, 런타임 에러 발생하지 않으니 그만큼 가치가 있음