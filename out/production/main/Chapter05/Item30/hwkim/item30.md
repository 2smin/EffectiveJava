# item 30

# 이왕이면 제네릭 메서드로 만들라

클래스와 마찬가지로, 메서드도 제네릭으로 만들 수 있다. 매개변수와 타입을 받는 정적 유틸리티 메서드는 보통 제네릭이다. 예컨데 Collections의 ‘알고리즘’ 메서드(binarySearch, sort 등)는 모두 제네릭이다.

```java
// 정렬된 리스트에서 키에 해당하는 원소의 인덱스를 리턴한다.
// 오름차순 정렬을 지원한다.
public static int binarySearch(List slist, T key)
```

## 제네릭 메서드의 작성법

### 단순한 제네릭 메서드

```java
// 매개변수 목록 <E>와 반환타입 Set<E>
public static <E> Set<E> union(Set<E> s1, Set<E> s2) {
    Set<E> result = new HashSet<>(s1);
		result.addAll(s2);
		return result;
}
```

메서드의 선언에서의 세 집합(입력 2, 반환 1개)의 원소 타입을 타입 매개변수로 명시하고, 메서드 안에서도 이 타입 매개변수만 사용하게 작성한다. **타입 매개변수 목록은 메서드의 제한자와 반환 타입 사이에 온다.**

### 제네릭 싱글턴 팩터리

때로 불변 객체를 여러 타입으로 활용할 수 있게 만들어야 할 때, 제네릭은 런타임에 타입 정보가 소거되므로 하나의 객체를 어떤 타입으로든 매개변수화 할 수 있다. 하지만, 이 방법은 요청한 타입 매개변수에 맞게 매번 그 객체의 타입을 바꿔주는 정적 팩터리를 만들어야 한다.

이 패턴을 **제네릭 싱글턴 팩터리** 라고 하며,

Collections.reverserOrder와 같은 함수객체나 Collections.emptySet과 같은 컬렉션 용으로 사용한다.

**항등함수(input을 그대로 return하는 함수)를 담은 클래스 예제**

항등함수 객체는 상태가 없으니 요청할 때 마다 생성하는 것은 낭비다. → 제네릭 메서드는 static으로 선언하여 1개의 함수 객체를 할당할 수 있으며, static method의 경우 호출 시에 매개 타입을 지정하기 때문에 타입별로 여러 객체를 생성하지 않아도 된다.

따라서 이런 메서드의 경우 제네릭 싱글턴이면 충분하다.

```java
private static UnaryOperator<Object> IDENTITY_FN = (t) ->t;
    
@SuppressWarnings("unchecked")
public static <T> UnaryOperator<T> identityFunction(){
    // IDENTITY_FN을 UnaryOperator<T> 형으로 형변환을 할 때 비검사 형변환 경고가 발생
    // UnaryOperator<T> != UnaryOperator<Object>이기 때문이다.
    // 그러나 항등 연산은 그 타입을 그대로 반환해야 하는 특별한 함수이기 때문에
    // @SuppressWarnings("unchecked")를 통해 경고 없이 컴파일을 하도록 한다.
    return (UnaryOperator<T>) IDENTITY_FN;
}
```

 

**재귀적 타입 한정**

상대적으로 드물긴 하지만, 자기 자신이 들어간 표현식을 사용하여 타입 매개변수의 허용 범위를 한정할 수 있다. 재귀적 타입 한정은 주로 타입의 자연적 순서를 정하는 Comparable 인터페이스와 함께 쓰인다.

```java
public static <E extends Comparable<E>> E max(Collection<E> c){
    if(c.isEmpty())
        throw new IllegalArgumentException("컬렉션이 비었습니다.");
        
    E result = null;
    for(E e : c){
        if(result == null || e.compareTo(result) >0){
            result = Objects.requireNonNull(e);
        }
    }
    return result;
}
```

타입 한정자인 <E extends Comparable<E>>는 “모든 타입 E는 자신과 비교할 수 있다”라고 읽을 수 있다. → Comparable을 구현한 클래스 타입들에 대해서만 Collection을 입력받아 최대값을 리턴한다.