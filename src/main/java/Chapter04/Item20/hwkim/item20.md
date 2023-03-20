# item 20

# 추상 클래스보다는 인터페이스를 우선하라

자바의 다중구현 매커니즘은 두가지이다.

- 추상 클래스
- 인터페이스

추상 클래스를 상속받아 추상 클래스가 정의한 타입을 구현하는 클래스는 반드시 추상 클래스의 하위 클래스가 되어야한다. 자바는 단일 상속만 지원하여 이 때문에 새로운 타입을 정의하는데 큰 제약이 된다. 기존의 클래스에 새로운 추상 클래스를 끼워넣기 위해서는 그 추상 클래스를 상속받는 다른 클래스와 공통 조상을 갖는 것임으로 추상 클래스에 변경이 생길 경우 모든 자손들이 상속하게 된다.(적절하지 않은 상황에서도 강제로 상속을 받는다.)

## 인터페이스는 기존 클래스에 적용하고 구현하는 것이 쉽다.

인터페이스는 인터페이스가 선언한 메서드를 모두 정의하고 그 일반 규약(implements 문 추가)을 잘 지킨다면 다른 어떤 클래스를 상속받더라도 같은 타입으로 취급된다. **따라서 기존 클래스에도 손쉽게 새로운 인터페이스를 구현해 넣을 수 있다.**

## 인터페이스는 Mix in 정의에 안성맞춤이다.

믹스인이란 클래스가 구현할 수 있는 타입으로, 믹스인을 구현한 클래스에서 원래의 ‘주된 타입’ 외에도 특정 선택적 행위를 제공한다고 선언하는 효과를 준다.

예를 들어 Comparable Interface는 자신이 구현한 클래스의 인스턴스끼리는 순서를 정할 수 있다고 선언하는 Mix in 인터페이스이다. 대상 타입의 주된 기능에 선택적 기능을 혼합하기 때문에 Mix in이라 부른다.

추상클래스로 Mix in 정의를 한다고 생각해 보자면, 클래스는 두 부모를 섬길 수 없고 기존의 클래스에 추상클래스를 상속한다고 했을 때, 다른 자식 클래스를 위해 정의된 불필요한 요소가 강제로 상속될 수 있다.

## 인터페이스로는 계층구조가 없는 타입 프레임워크를 만들 수 있다.

타입을 계층적으로 정의하면 수많은 개념을 구조적으로 잘 표현할 수 있지만, 현실에서는 계층을 엄격히 구분하기 어려운 개념도 있다. 아래의 Singer와 Songwriter를 살펴보면

```java
public abstract class Singer {
   abstract AudioClip sing(Song song);
}

public abstract class Songwriter {
    abstract Song compose(int chartPosition);
}

// 두 클래스간 계층이 있다고 보기 어렵다 따라서 다음과 같은 구조는 논리적으로 맞지 않다.
//이런 구조는 논리적으로 맞지 않다.
public abstract class Singer extends Songwriter{
   abstract AudioClip sing(Song song);
}

// 작곡을 하는 가수를 정의할 때 두 클래스를 동시에 상속받을 수 없다.
public abstract class SingerSongwriter {
    // Singer
    abstract AudioClip sing(Song song);
    // Songwriter
    abstract Song compose(int chartPosition);
    
    abstract AudioClip strum();
    abstract void actSensitive();
}
```

이렇게 엄격히 구분하기 어려운 개념을 계층적으로 만들려면 가능한 조합 전부를 각각 클래스로 정의하여 고도비만 게층 구조가 만들어 질 수 있다. 반면 인터페이스를 이용하면 간단히 해결할 수 있다.

```java
public interface Singer  {
    AudioClip sing(Song s);
}

public interface Songwriter  {
    Song compose(int chartPosition);
}

public interface SingerSongwriter extends Singer, Songwriter  {
    AudioClip strum();
    void actSensitive();
}
```

## 래퍼 클래스 관용구와 함께 사용하면 인터페이스는 기능을 향상시키는 안전하고 강력한 수단이 된다.

타입을 추상 클래스로 정의해두면 그 타입에 기능을 추가하기 위해서는 상속을 받아야한다. 이렇게 상속을 통해 만든 클래스는 래퍼 클래스보다 활용도가 떨어지고 깨지기 쉽다.

item 18의 InstrumentedSet 클래스는 ForwardingSet 인스턴스를 감싸고 있고 ForwardingSet은 Set interface를 implements 하고 있다.

```java
public class ForwardingSet<E> implements Set<E> {
    private final Set<E> s;
    public ForwardingSet(Set<E> s) { this.s = s; }
    public void clear() { s.clear(); }
    public boolean contains(Object o) { return s.contains(o); }
    public boolean isEmpty() { return s.isEmpty(); }
    public int size() { return s.size(); }
    public Iterator<E> iterator() { return s.iterator(); }
    public boolean add(E e) { return s.add(e); }
    public boolean remove(Object o) { return s.remove(o); }
    public boolean containsAll(Collection<?> c){
            return s.containsAll(c);
    }
    public boolean addAll(Collection<? extends E> c){
        return s.addAll(c);
    }
    public boolean removeAll(Collection<?> c){
        return s.removeAll(c);
    }
    
    public boolean retainAll(Collection<?> c){
        return s.retainAll(c);
    }
    public Object[] toArray() { return s.toArray(); }
    public <T> T[] toArray(T[] a) { return s.toArray(a); }

    @Override
    public boolean equals(Object o)
    { return s.equals(o); }
    @Override
    public int hashCode() { return s.hashCode(); }
    @Override
    public String toString() { return s.toString(); }
}
```

```java
public class InstrumentedSet<E> extends ForwardingSet<E> {

    private int addCount = 0;

     public InstrumentedSet(Set<E> s) {
	       super(s);
     }

     @Override public boolean add(E e) {
	       addCount++;
	       return super.add(e);
     }

     @Override public boolean addAll(Collection<? extends E> c) {
	       addCount += c.size();
	       return super.addAll(c);
     }

     public int getAddCount() {
	       return addCount;
     }
}
```

### 인터페이스의 디폴트 메서드

인터페이스의 메서드 중 구현 방법이 명확한 것이 있다면, 그 구현을 디폴트 메서드로 제공해 프로그래머들의 일감을 덜어줄 수 있다. 디폴트 메서드를 제공할 때는 상속하려는 사람을 위한 설명을 @implSpec 자바독 태그를 붙여 문서화해야한다.

디폴트 메서드에도 다음과 같은 제약이 있다.

- 많은 인터페이스가 equals, hashCode 같은 Object의 메서드를 정의하고 있지만, 이들은 디폴트 메서드로 제공해서는 안된다.
- 인터페이스는 인스턴스 필드를 가질 수 없고 public이 아닌 정적 멤버도 가질 수 없다.
- 직접 만들지 않은 인터페이스에는 디폴트 메서드를 추가할 수 없다.

### 추상 골격 구현(skeletal implementation)

인터페이스와 추상 골격 구현 클래스를 함께 제공하는 식으로 인터페이스와 추상 클래스의 장점을 모두 취하는 방법이 있다. 

인터페이스로는 타입을 정의하고 필요하다면 디폴트 메서드도 함께 제공한다. 그리고 골격 구현 클래스로 나머지 메서드들까지 구현하여 제공하는 방법이다. 이렇게 제공할 경우 골격 구현을 확장하는 것만으로 이 인터페이스를 구현하는데 필요한 일이 대부분 완료된다. → 템플릿 메서드 패턴

관례상 인터페이스 이름이 Interface라면 그 골격 구현 클래스의 이름은 AbstractInterface로 짓는다.

Ex) AbstractCollection, AbstractSet, AbstractList, AbstractMap 각각이 핵심 컬렉션 인터페이스의 골격 구현이다.

**완벽히 동작하는 List 구현체를 반환하는 정적 팩터리 메서드**

```java
static List<Integer> intArrayAsList(int[] a) {
	Object.requireNotNull(a);

	return new AbstractList<>() {
		@Override pubvlic Integer get(int i) {
			return a[i];       // 오토 박싱
		}
		
		@Override public Integer set(int i, Integer val) {
			int oidVal = a[i]; // 오토 언박싱
			a[i] = val;        // 오토 박싱
			return oldVal;
		}

		@Override public int size() {
			return a.length;
		}
	};
}
```

int형 배열을 받아 Integer 인스턴스의 리스트 형태로 보여주는 어댑터이다. int값과 Integer 인스턴스 사이의 변환 때문에 성능이 그리 좋지 않다. 골격 구현 클래스의 장점은 추상 클래스처럼 구현을 도와주는 동시에 추상 클래스로 타입을 정의할 때의 제약에서 자유롭다는 점이다.

골격 구현을 확장하지 못하는 상황일 경우 인터페이스를 직접 구현해야한다. 이런 경우에도 인터페이스가 직접 제공하는 디폴트 메서드의 이점을 여전히 누릴 수 있다. 또한 골격 구현 클래스를 우회적으로 이용할 수도 있다. 인터페이스를 구현한 클래스에서 해당 골격 구현을 확장한 private 내부 클래스를 정의하고, 각 메서드 호출을 내부 클래스의 인스턴스에 전달하는 것이다. 위의 래퍼 클래스와 비슷한 이 방식을 시뮬레이트한 다중 상속이라 한다.

**골격 구현 작성**

1. 인터페이스를 확인해 다른 메서드들의 구현에 사용되는 기반 메서드들을 선정한다

2. 기반 메서드들을 사용해 직접 구현할 수 있는 메서드를 모두 디폴트 메서드로 제공한다. 단, equals와 hashCode같은 Object의 메서드는 디폴트 메서드로 제공하면 안 된다.

3. 만약 인터페이스의 메서드 모두가 기반 메서드와 디폴트 메서드가 된다면 골격 구현 클래스를 별도로 만들 이유는 없다.

4. 기반 메서드나 디폴트 메서드로 만들지 못한 메서드가 남아 있다면, 이 인터페이스를 구현하는 골격 클래스를 하나만들어 남은 메서드들을 작성해 넣는다. 골격 구현 클래스에 필요하면 public이 아닌 필드와 메서드를 추가해도 된다.

Ex) Map.Entry 인터페이스

```java
public abstract class AbstractMapEntry<K,V> implements Map.Entry<K,V> {
	
    @Override 
    public V setValue(V value)  {
    	throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean equals(Object o)  {
    	if(o == this)
        	return false;
        if(!(o instanceof Map.Entry))
        	return false;
        Map.Entry<?, ?> e = (Map.Entry) o;
        return Objects.equals(e.getKey(), getKey())
        	&& Objects.equals(e.getValue(), getValue());
    }
    
    @Override 
    public int hashCode()  {
    	return Objects.hashCode(getKey())
        	^ Objects.hashCode(getValue());
    }
    
    @Override
    public String toString()  {
    	return getKey() + '=' + getValue();
    }
}
```

골격 구현은 기본적으로 상속해서 사용하는 걸 가정하므로 설계 및 문서화 지침을 모두 따라야한다. 인터페이스에 정의한 디폴트 메서드든 별도의 추상 클래스든, 골격 구현은 반드시 그 동작 방식을 잘 정리해 문서로 남겨야한다.