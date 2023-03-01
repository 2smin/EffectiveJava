# Item30

### 이왕이면 제네릭 메소드로 만들라

- 매개변수화 타입을 받는 정적 유틸리티 메소드는 보통 제네릭 메소드
    - Collections의 알고리즘 메소드(binarySearch, sort)는 모두 제네릭임
- 제네릭 메소드만드는 법
    - 문제가 있는 메소드 예시
    
    ```java
    public static Set union(Set s1, Set s2) {
    	Set result = new HashSet(s1);  // raw type HashSet 경고
    	result.addAll(s2);             // raw type Set 경고
    	return result;
    }
    ```
    
    - 제네릭 메소드로 수정 - 타입 매개변수 추가
    
    ```java
    // Generic method - 집합 3개의 타입이 모두 같아야 함
    public static **<E> Set<E>** union(Set**<E>** s1, Set**<E>** s2) {
    	Set**<E>** result = new HashSet**<>**(s1);
    	result.addAll(s2);
    	return result;
    }
    ```
    
    - 한정적 wildcard를 사용하면 더 유연하게 개선 가능
    
    - client 사용 예
    
    ```java
    public static void main(String[] args) {
    	Set<String> guys = Set.of("Tom", "Dick", "Harry");
    	Set<String> stooges = Set.of("Larry", "Moe", "Curly");
    	Set<String> aflCio = union(guys, stooges);
    	System.out.println(aflCio);
    }
    
    // [Moe, Tom, Harry, Larry, Curly, Dick] 출력
    ```
    

- 불변 객체를 여러 타입으로 활용할 수 있게 만들어야 할 경우
    - 제네릭은 런타임에 타입 정보 없어지므로, 하나의 객체를 어떤 타입으로든 매개변수화 가능
    - 타입 매개변수에 맞게 매번 그 객체의 타입을 바꿔주는 정적 팩터리 만들어야 함(제네릭 싱글턴 팩터리)
- 항등함수를 담은 클래스를 만들고 싶은 경우
    - 자바 라이브러리의 Function.identity (item59)
    - 항등함수 객체는 상태가 없으니 요청할때마다 새로 생성하는 것은 낭비
    
    ```java
    // 제네릭 싱글턴 패턴
    private static UnaryOperator<Object> IDENTITY_FN = (t) -> t;
    	**@SuppressWarnings("unchecked")**
    	public static <T> UnaryOperator<T> identityFunction() {
    		return (**UnaryOperator<T>**) IDENTITY_FN;
    }
    ```
    
    - IDENTITY_FN 을 UnaryOperator<T>로 형변환 하면 경고 발생
        - T는 object가 아니기 때문
    - 사용 예시
    
    ```java
    public static void main(String[] args) {
    String[] strings = { "jute", "hemp", "nylon" };
    UnaryOperator<String> sameString = identityFunction();
    
    for (String s : strings)
    	System.out.println(sameString.apply(s));
    
    Number[] numbers = { 1, 2.0, 3L };
    
    UnaryOperator<Number> sameNumber = identityFunction();
    
    for (Number n : numbers)
    	System.out.println(sameNumber.apply(n));
    }
    ```
    
- 자기 자신이 들어간 표현식을 사용하여 타입 매개변수의 허용 범위 한정 (재귀적 타입 한정)
    - 주로 타입의 자연적 순서를 정하는 Comparable 인터페이스와 같이 쓰임
    
    ```java
    public interface Comparable<T> {
    	int compareTo(T o);
    }
    ```
    
    - 타입 매개변수 T는 Comparable<T>를 구현한 타입이 비교할 수 있는 원소 타입을 정의
        - String은 Comparable<String>을 구현, Integer는 Comparable<Integer>를 구현
    - Comparable 인터페이스를 구현하고 Collection<E>을 입력 받는 메소드들은 주로 정렬/검색/최소/최대값 구함
        - 컬렉션에 담긴 모든 원소가 상호 비교될 수 있어야 함
        
        ```java
        // 재귀적 타입 한정을 통해 상호비교 가능을 표현
        public static <**E extends Comparable<E>**> E max(Collection<E> c);
        ```
        
        - 모든 타입 E는 자신과 비교할 수 있다
    - max 메소드 구현 예시
    
    ```java
    public static <E extends Comparable<E>> E max(Collection<E> c) {
    	if (c.isEmpty())
    		throw new IllegalArgumentException("Empty collection");
    
    	E result = null;
    
    	for (E e : c)
    		if (result == null || **e.compareTo(result)** > 0)
    			result = Objects.requireNonNull(e);
    	return result;
    }
    ```