# Item 61

# 박싱된 기본 타입보다는 기본 타입을 사용하라

자바의 데이터 타입은 크게 두가지로 나눌 수 있다. 바로 int, double, boolean과 같은 기본 타입과 String, List 같은 참조 타입이다. 그리고 각각의 기본 타입에는 대응하는 참조 타입이 하나씩 있으며, 이를 박싱된 기본타입이라고 한다.(Integer, Double, Boolean)

오토박싱과 오토언박싱 덕분에 두 타입을 크게 구분하지 않고 사용할 수 있지만, 그렇다고 차이가 사라지는 것은 아니다. 기본 타입과 박싱된 기본 타입의 차이는 크게 세 가지다.

1. 기본타입은 값만 가지고 있으나, 박싱된 기본타입은 값에 더해 식별성이란 속성을 갖는다. 달리 말하면 박싱된 기본 타입의 두 인스턴스는 값이 같아도 서로 다르다고 식별될 수 있다.
2. 기본 타입의 값은 언제나 유효하나, 박싱된 기본 타입은 유효하지 않은 값, 즉 null 값을 가질 수 있다.
3. 기본 타입이 박싱된 기본 타입보다 시간과 메모리 사용면에서 더 효율적이다.

## 박싱된 기본 타입이 좋지 않은 경우

위의 3가지 차이 때문에 박싱된 기본 타입과 기본 타입을 주의해서 사용하지 않으면 문제가 발생할 수 있다.

다음은 Integer값을 오름차순으로 정렬하는 잘못된 비교자이다.

```java
Comparator<Integer> naturalOrder = (i, j) -> (i < j) ? -1 : (i == j ? 0 : 1);
```

별다른 문제를 찾기 어렵고 실제로 이것저것 테스트를 해봐도 잘 통과한다. 그러나 다음과 같은 코드에서 문제가 발생한다.

```java
public class ComparatorClient {
    public static void main(String[] args){
        Comparator<Integer> naturalOrder =
                (i, j) -> (i < j) ? -1 : (i == j ? 0 : 1);
        System.out.println(
                naturalOrder.compare(new Integer(42), new Integer(42))
        );
    }
}
```

값이 42로 같기 때문에 0이 출력될 것으로 생각하지만 실제로는 1이 출력된다.(첫번째 인자가 두번째 보다 크다) 이 문제의 원인은 다음과 같다. (i < j) 수행 시에는 오토박싱된 Integer 인스턴스가 기본 타입 값으로 변환된다. 그러나 (i == j)가 수행 될 때 **객체 참조**의 식별성을 검사하게 된다. i와 j가 서로 다른 Integer 인스턴스라면 이 비교의 결과는 false가 되고 비교자는 1을 반환한다. **박싱된 기본타입에 == 연산자를 사용하면 오류가 일어날 수 있다.**(String과 같이)

위의 문제를 수정한 비교자 코드는 다음과 같다.

```java
public class ComparatorClient {
    public static void main(String[] args){
        Comparator<Integer> realNaturalOrder = (iBoxed, jBoxed) -> {
            int i = iBoxed, j = jBoxed; // 오토 언박싱
            return i < j ? -1 : (i == j ? 0 : 1);
        };
        System.out.println(
                realNaturalOrder.compare(new Integer(42), new Integer(42))
        );
    }
}
```

다음은 오류가 발생하는 간단한 코드이다.

```java
public class TragicSampleCode {
    static Integer i;
    public static void main(String[] args){
        if(i == 42)
            System.out.println("믿을 수 없군!");
    }
}
```

이 프로그램은 i의 초기값이 null이기 때문에 NullPointException을 던진다. 위의 예제 에서는 객체 참조의 식별성을 검사했지만, 이번 예제 코드에서는 Integer와 int를 비교하기 때문에 오토언박싱이 발생하게 되고 i가 null이기 때문에 NullPointException을 던진다. 즉 **기본 타입과 박싱된 기본 타입을 혼용한 연산에서는 오토언박싱이 수행된다.**

다음은 성능 저하를 발생시키는 예제 코드이다.

```java
public static void main(String[] args){
		Long sum = 0L;
		for(long i =0; i <= Integer.MAX_VALUE; i++){
				sum += i;
		}
		System.out.println(sum);
}
```

이 코드에서는 sum을 박싱된 기본타입으로 선언했다. 이로 인해 sum += i에서 박싱과 언박싱이 반복해서 일어나기 때문에 성능저하가 발생한다.

## 박싱된 기본 타입을 사용해야 하는 경우

1. 컬렉션의 원소, 키, 값으로 쓰는 경우
    1. 컬렉션은 기본 타입을 담을 수 없기 때문에 박싱된 기본 타입을 사용해야 한다.
2. 리플렉션을 통해 메서드를 호출할 때