# Item52

## 다중정의는 신중히 사용하라

```java
public class CollectionClassifier {

	// Classyfy 함수 3가지 버전으로 다중 정의
	public static String classify(Set<?> s) {
		return "Set";
	}
	public static String classify(List<?> lst) {
		return "List";
	}
	public static String classify(Collection<?> c) {
		return "Unknown Collection";
	}

	public static void main(String[] args) {

		Collection<?>[] collections = {
			new HashSet<String>(),
			new ArrayList<BigInteger>(),
			new HashMap<String, String>().values()
		};
	
		// for문으로 각각을 호출 (예상)
		for (Collection<?> c : collections)
			System.out.println(classify(c));
		}
}
```

- 코드가 예상대로 돌아가지 않고, Unknown Collection만 세 번 호출
- 예상대로 돌아가지 않는 이유
    - 다중정의(오버로딩)한 메소드는 컴파일 시점에 어느 것이 호출될 지 정해짐
    - for문 안의 c는 컴파일 타임에 Collection<?> 타입이라 예상대로 동작하지 않음

- 재정의한 메소드는 동적으로 선택되고, 다중정의한 메소드는 정적으로 선택
- 원래의 의도대로 동작하게 하려면 (정적 메소드를 사용해도 좋다면)
    
    ```java
    public static String classify(Collection<?> c) {
    	return c instanceof Set ? "Set" :
    		c instanceof List ? "List" : "Unknown Collection";
    }
    ```
    
    - classify 메소드를 하나로 합친 후 instanceof 로 명시적으로 형검사

- 공개 API일수록 헷갈리는 코드는 작성하지 말자
- 매개변수 수가 같은 다중정의는 만들지 말자
    - 가변인수(varargs)를 사용하는 메서드라면 다중정의를 아예 하지 말아야 함(Item53에 예외 나옴)
- 다중정의하는 대신 메서드 이름을 다르게 지어주자
    - ObjectOutputStream의  writeBoolean(boolean), writeInt(int), writeLong(long)과  이에 대응하는 readBoolean(), readInt(), readLong()
- 생성자의 경우
    - 정적팩터리라는 대안 사용 가능
    - 매개변수 수가 같아도, 하나 이상이 근본적으로 다르면 헷갈리지 않음
        - type의 값이 서로 어느쪽으로든 형변환이 불가능 한 경우
        - ArrayList의 int 생성자와 Collection 생성자는 타입이 달라 어느것이 호출될지 헷갈리지 않음

- 오토박싱
    
    ```java
    public class SetList {
    	public static void main(String[] args) {
    
    		Set<Integer> set = new TreeSet<>();
    		List<Integer> list = new ArrayList<>();
    
    		for (int i = -3; i < 3; i++) {
    			set.add(i);  // [-3, -2, -1, 0, 1, 2]
    			list.add(i); // [-3, -2, -1, 0, 1, 2]
    		}
    
    		for (int i = 0; i < 3; i++) {
    			set.remove(i);
    			list.remove(i);
    		}
    
    		System.out.println(set + " " + list);
    	}
    }
    ```
    
    - 출력 결과
        - [-3, -2, -1] [-2, 0, 2]
    - set과 list의 동작 차이
        - set.remove(i) 는 remove(Object)라 해당 **값**을 제거
        - list.remove(i)는 remove(int index)를 선택하여 해당 **위치**를 제거
    - 해결 방법
        - list.remove의 인수를 Integer로 형변환 하여 올바른 메소드를 선택하게 함
        
        ```java
        for (int i = 0; i < 3; i++) {
        	set.remove(i);
        	list.remove((Integer) i); // or remove(Integer.valueOf(i))
        }
        ```
        
    - List<E>는 remove(Object), remove(int)가 있는데, 제네릭과 오토박싱이 등장하며 두 메소드의 매개변수 타입이 근본적으로 다르지 않아 졌음 → List 인터페이스 취약해짐
    

### 람다와 메소드 참조

```java
// Thread의 생성자 호출
new Thread(System.out::println).start();

// ExecuterService의 submit 메소드 호출
ExecutorService exec = Executors.newCachedThreadPool();
exec.submit(System.out::println);
```

- 2번은 컴파일 오류
    - submit 다중정의 메소드에는 Callable<T>를 받는 메소드도 있기 때문
    - println의 리턴값은 void이고, callable은 리턴 값이 있으니 헷갈릴 수 없다고 생각하지만 컴파일러의 다중정의 해소는 그렇게 동작하지 않음 (자세한 동작은 몰라도 됨..)
    - println이 다중정의 없이 하나만 존재했다면 제대로 컴파일됐을 것
        - 참조된 메소드(println)와 호출한 메소드(submit) 양쪽 다 다중정의 되어 기대처럼 동작하지 않는 것
- 다중정의된 메소드들이 함수형 인터페이스를 인수로 받을 때, 서로 다른 함수형 인터페이스라도 인수 위치가 같으면 혼란이 생김
    - 메소드 다중정의 할 때, 서로 다른 함수형 인터페이스라도 같은 위치의 인수로 받으면 안됨

- 어떤 다중정의 메소드가 불리는지 몰라도 기능이 같으면 신경쓸 것이 없음
    - String의 contentEquals()
    
    ```java
    public boolean contentEquals(StringBuffer sb) {
    	return contentEquals((CharSequence) sb);
    }
    ```
    
    - 다중정의 되었지만, 덜 특수한(더 일반적인) 다중정의 메소드로 일을 forward 하여 동일한 일을 하도록 보장
- 다중정의를 올바르게 사용하지 못한 예
    - String의 valueOf(char[]), valueOf(Object)