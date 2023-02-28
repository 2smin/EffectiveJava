# Item31

## 한정적 와일드카드를 사용해 API 유연성을 높이라

### 매개변수화 타입의 한계 - 유연성이 떨어짐

- 매개변수화 타입은 불공변
    - Type1과 Type2가 있을 때 List<Type1>, List<Type2>는 서로의 상위 타입도, 하위 타입도 아님
- 예시
    
    ```java
    public class Stack<E> {
    	public Stack();
    	public void push(E e);
    	public E pop();
    	public boolean isEmpty();
    
    	// 와일드 카드를 사용하지 않는 pushAll 메소드
    	public void pushAll(Iterable<E> src) {
    		for (E e : src)
    			push(e);
    	}
    	// 와일드 카드를 사용하지 않는 pushAll 메소드
    	public void popAll(Collection<E> dst) {
    		while (!isEmpty())
    			dst.add(pop());
    	}
    }
    ```
    
    - pushAll - Stack<Number>로 선언 후 pushAll<intVal>을 호출하면
        - Integer는 Number의 하위타입이지만 동작하지 않음 (매개변수화 타입은 불공변이기 때문)
        - 해결 - 한정적 와일드카드 (생산자)
            
            ```java
            public void pushAll(**Iterable<? extends E>** src) {
            	for (E e : src)
            		push(e);
            }
            ```
            
    - popAll - Stack<Number>를 Object용 컬렉션으로 옮기고 싶다면
        - Collection<Object>는 Collection<Number>의 하위 타입이 아니라는 오류 발생
        - 해결 - 한정적 와일드 카드 (소비자)
        
        ```java
        public void popAll(**Collection<? super E>** dst) {
        	while (!isEmpty())
        		dst.add(pop());
        }
        ```
        

- 유연성을 극대화하려면 원소의 생산자나 소비자용 입력 매개변수에 와일드카드 타입을 사용
    - PECS : producer-extends, consumer-super
    - 매개변수화 타입이 생산자라면 <? extends T>
    - 매개변수화 타입이 소비자라면 <? super T>
    - Comparable, Comparator는 모두 소비자

- 타입 매개변수와 와일드카드에는 공통되는 부분이 있어 메소드 정의할 때 둘 중 어느것을 사용해도 괜찮을 때가 많음
    
    ```java
    public static **<E>** void swap(**List<E> list**, int i, int j);
    public static void swap(**List<?> list**, int i, int j);
    ```
    
    - public API면 간단한 두번째가 나음
- 메소드 선언에 타입 매개변수가 한번만 나오면 와일드카드로 대체
    - 비한정적 타입 매개변수라면 비한정적 와일드카드로
    - 한정적 타입 매개변수라면 한정적 와일드카드로
- 두번째 swap 선언의 문제점 - 해당 구현에서 컴파일 에러 발생
    
    ```java
    public static void swap(List<?> list, int i, int j) {
    	list.set(i, list.set(j, list.get(i)));
    }
    ```
    
    - List<?>에는 null 외에는 어떤 값도 넣을 수 없음
- 해결 - 와일드카드 타입의 실제 타입을 알려주는 private 도우미 메소드 작성하여 활용
    
    ```java
    public static void swap(List<?> list, int i, int j) {
    	swapHelper(list, i, j);
    }
    // 와일드카드 타입을 실제 타입으로 바꿔줌
    private static <E> void swapHelper(List<E> list, int i, int j) {
    	list.set(i, list.set(j, list.get(i)));
    }
    ```
    
    - swapHelper는 이 리스트에서 꺼낸 값의 타입은 항상 E 라는 것을 알고 있어 안전