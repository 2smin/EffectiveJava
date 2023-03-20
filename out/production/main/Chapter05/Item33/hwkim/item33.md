# item 33

# 타입 안전 이종 컨테이너를 고려하라

제네릭은 Set<E>, Map<K,V> 등의 컬렉션과 ThreadLocal<T>, AtomicReference<T> 등의 단일 원소 컨테이너에도 흔히 쓰인다. 이런 모든 쓰임에서 매개변수화되는 대상은 컨테이너 자신이다.(원소가 아님)

그러나 종종 더 유연한 수단이 필요할 때도 있다. 예를 들어 데이터베이스는 행과 열로 이루어지는데, 행과 열이 각각 임의의 개수를 가질 수 있다.(행을 원소로 하더라도 테이블마다 열의 개수가 다를 수 있다.) 이를 타입 안전하게 이용할 수 있다면 좋을 것이다.

다행히 쉬운 해법이 존재한다. 컨테이너 대신 키를 매개변수화 한 다음, 컨테이너에 값을 넣거나 뺄 때 매개변수화 한 키를 함께 제공하면 된다. 이런 설계 방식을 **타입 안전 이종 컨테이너 패턴**(”두 이”가 아닌 ”다를 이”를 쓴다.)이라 한다. 다음의 예를 살펴보자

```java
public class Favorite {
	  private Map<Class<?>, Object> favorites = new HashMap<>();

	  public <T> void putFavorite(Class<T> type, T instance) {
		    favorites.put(Objects.requireNonNull(type), instance);
	  }
	  public <T> T getFavorite(Class<T> type) {
		    return type.cast(favorites.get(type));
	  }
}
```

위의 코드에서 Favorites가 사용하는 private 맵 변수인 favorites의 타입은 Map<Class<?>, Object>이다. 비한정적 와일드카드 타입이기 때문에 맵 안에 아무것도 넣을 수 없다고 생각할 수 있지만, 그 반대이다.

와일드카드 타입이 중첩되었다는 점을 깨달아야 한다.

1. 맵의 키가 와일드카드 타입인 것이다. 이는 모든 키가 서로 다른 매개변수화 타입일 수 있다는 뜻으로, 첫 번째는 Class<String>, 두 번째는 Class<Integer> 식으로 될 수 있다.
2. 그 다음으로 이 맵의 value는 Object 타입이다. 이 맵은 키와 값 사이의 타입 관계를 보증하지 않는다. 사실 자바의 타입 시스템에서는 이 관계를 명시할 방법이 없다. 하지만 우리는 이 관계가 성립함을 알고 있고, favorites 맵을 검색할 때 그 이점을 누리게 된다.

**getFavorite 메서드**

putFavorite은 굉장히 간단하다. key와 value를 map에 추가하여 관계를 지으면 끝이다. 반면 getFavorite은 주어진 key 객체에 해당하는 value 객체를 꺼낸다. 이 객체가 바로 반환해야 할 객체이긴 하지만, 잘못된 컴파일타임 타입을 가지고 있다. 이 객체의 타입은 Object이나 우리는 이를 T로 바꿔 반환해야 한다.

따라서 getFavorite 메서드의 구현은 Class의 cast 메서드를 사용해 이 객체 참조를 Class객체가 가리키는 타입으로 동적 형변환한다.

cast 메서드는 형변환 연산자의 동적 버전이다. 이 메서드는 단순히 주어진 인수가 Class 객체가 알려주는 타입의 인스턴스인지 검사한 다음, 맞다면 그 인수를 그대로 반환하고, Class 객체가 알려주는 타입의 인스턴스가 아니라면 ClassCastException을 던진다.

클라이언트 코드가 깔끔히 컴파일 된다면 getFavorite이 호출하는 cast는 ClassCastException을 던지지 않을 것임을 우리는 알고 있다. (**put에서 key로 instance의 타입을 value로 instance를 넣기 때문에**)

**cast 메서드**

단순히 인수를 그대로 반환함에도 cast 메서드를 사용하는 이유는 **cast 메서드의 시그니처가 Class 클래스가 제네릭이라는 이점을 완벽히 활용하기 때문이다.** cast의 반환 타입은 Class객체의 타입 매개변수와 같다.

```java
public Class Class<T> {
    T cast(Object obj);
}
```

T로 비검사 형변환하는 손실 없이도 Favorites를 타입 안전하게 만들어 준다.

**Favorites 클래스의 제약**

예제인 Favorites클래스에 알아두어야 할 제약이 두 가지 있다.

1. 악의적인 클라이언트가 Class 객체를 제네릭 제네릭이 아닌 raw타입으로 넘기면 Favorites 인스턴스의 타입 안전성이 쉽게 깨진다.
    - 하지만 이는 클라이언트 코드에서 컴파일할 때 비검사 **경고**가 뜰 것이다.
    - Favorites가 타입 불변식을 어기는 일이 없도록 보장하기 위해서는 putFavorite 메서드를 다음과 같이 instance의 타입이 type으로 명시한 타입과 같은지 확인한다.
        
        ```java
        public <T> void putFavorite(Class<T> type, T instance) {
        		favorites.put(Objects.requireNonNull(type), type.cast(instance));
        }
        ```
        
    - java.util.collections 에는 checkedSet, checkedList, checkedMap과 같은 메서드가 있는데 바로 위와 같은 방식을 적용한 컬렉션 wrapper들이다.
        - 이 래퍼들은 제네릭과 raw타입을 섞어 사용하는 애플리케이션에서 클라이언트 코드가 컬렉션에 잘못된 타입의 원소를 넣지 못하게 추적하는 것을 도와준다.
2. Favorite 클래스의 두 번째 제약은 실체화 불가 타입에는 사용할 수 없다는 것이다.
    - 즐겨찾는 String이나 String[]은 저장할 수 있어도 List<String>은 저장할 수 없다. List<String>을 저장하려는 코드는 List<String>용 Class 객체를 얻을 수 없기 때문에 컴파일 되지 않는다.
    - List<Integer>나 List<String>이나 모두 List.class라는 같은 Class 객체를 공유하기 때문이다.
    - 만약 List<Integer>.class와 List<Integer>.class를 허용해서 둘 다 똑같은 타입의 객체 참조를 반환한다면 Favorites 객체의 내부는 아수라장이 될 것이다.

**타입 안전 이종 컨테이너에 실체화 불가타입을 적용하는 방법**

슈퍼 타입 토큰(옮긴이가 제안한 방법)

[https://sungminhong.github.io/spring/superTypeToken/](https://sungminhong.github.io/spring/superTypeToken/)

**한정적 타입 토큰**

Favorites가 사용하는 타입 토큰은 어떤 Class객체든 받아들인다 == 비한정적이다. 때로는 이 메서드들이 허용하는 타입을 제한하고 싶을 수 있는데, 한정적 타입 토큰을 활용하면 가능하다.

한정적 타입 토큰이란 한정적 타입 매개변수(item 29)나 한정적 와일드카드(item 31)를 사용하여 표현 가능한 타입을 제한하는 타입토큰이다.

```java
public <T extends Annotation> T getAnnotation(Class<T> annotationType);
```

어노테이션 API는 한정적 타입토큰을 적극적으로 사용한다. 위의 코드는 AnotatedElement 인터페이스에 선언된 메서드로, 대상 요소에 달려 있는 어노테이션을 런타임에 읽어 오는 기능을 한다. 위 매서드는 리플렉션의 대상이 되는 다음과 타입들 같이 프로그램 요소를 표현하는 타입들에서 구현한다.

- java.lang.Class<T>
- java.lang.reflect.Method
- java.lang.reflect.Field

여기서 annotationType 인수는 어노테이션 타입을 뜻하는 한정적 타입 토큰이다. 대상 요소에 달려있는 어노테이션을 런타임에 읽어오는 기능을 한다.

이 메서드는 토큰으로 명시한 타입의 어노테이션이 대상 요소에 달려있으면 그 어노테이션을 반환하고, 없다면 null을 반환한다. 즉, 어노테이션된 요소는 그 키가 어노테이션 타입인 “타입 이종 컨테이너”이다.

만약 Class<?> 타입의 객체가 있고 이를 한정적 타입 토큰을 받는 메서드에 넘기려면 어떻게 해야할까?

메서드에 넘기기 위해서는 객체를 Class<? extends Annotation>으로 형변환 해야한다. 다행히 Class 클래스는 이런 형변환을 안전하고 동적으로 수행해주는 메서드를 제공한다.

**asSubclass 메서드**

호출한 인스턴스 자신의 Class 객체를 인수가 명시한 클래스로 형변환한다. 형변환에 성공하면 인수로 받은 클래스 객체를 반환하고, 실패하면 ClassCastException을 던진다. 다음은 컴파일 시점에서는 타입을 알 수 없는 어노테이션을 asSubclass 메서드를 이용해 런타임에 읽어내는 예다.

```java
static Annotation getAnnotation(AnnotatedElement element,String annotationTypeName) { 
	  Class<?> annotationType = null; // 비한정적 타입 토큰
	  try {
		    annotationType = Class.forName(annotationTypeName);
	  } catch (Exception ex) {
		    throw new IllegalArgumentException(ex);
	  } 
	  return element.getAnnotation(annotationType.asSubclass(Annotation.class));
}
```