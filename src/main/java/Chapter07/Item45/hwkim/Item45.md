# Item 45

# 스트림은 주의해서 사용하라

스트림은 다량의 데이터르 처리 작업을 돕고자 자바8에 추가되었다. 이 API가 제공하는 추상 개념 중 핵심은 두 가지다.

1. 스트림은 데이터 원소의 유한 혹은 무한 시퀀스를 뜻한다.
2. 스트림 파이프라인은 이 원소들로 수행하는 연산 단계를 표현하는 개념이다.

스트림의 원소들은 어디로부터든 올 수 있다. 대표적으로는 컬렉션, 배열, 파일, 정규표현식 패턴 매처, 난수 생성기, 혹은 다른 스트림이 있다.

스트림 안의 데이터 원소들은 객체 참조나 기본 타입 값이다.

스트림 파이프라인은 소스 스트림에서 시작해 종단 연산으로 끝나며, 그 사이에 하나 이상의 중간 연산이 있을 수 있다. 각 중간 연산은 스트림을 어떤 방식으로 변환한다.

예컨데 각 원소에 함수를 적용하거나 특정 조건을 만족 못하는 원소를 걸러낼 수 있다. 변환된 스트림 원소 타입은 변환전 스트림의 원소 타입과 같을 수도 있고 다를 수도 있다. 종단 연산은 마지막 중간 연산이 내놓은 스트림에 최후의 연산을 가한다. 원소를 정렬해 컬렉션에 담거나, 특정 원소 하나를 선택하거나, 모든 원소를 출력하는 식이다.

**스트림의 지연 평가**

스트림 파이프라인은 지연 평가된다. 평가는 종단 연산이 호출될 때 이뤄지며, 종단 연산에 쓰이지 않는 데이터 원소는 계산에 쓰이지 않는다. 이러한 지연 평가가 무한 스트림을 다룰 수 있게 해주는 열쇠다. **종단 연산(terminal operation)이 없는 스트림 파이프라인은 아무 일도 일어나지 않는다.**

**스트림 API**

스트림 API는 메서드 연쇄를 지원하는 플루언트 API다. 즉 파이프라인 하나를 구성하는 모든 호출을 연결하여 단 하나의 표현식으로 완성할 수 있다. 파이프라인 여러개를 연결해 표현식을 하나로 만들 수도 있다.

기본적으로 스트림 파이프라인은 순차적으로 수행된다. 파이프라인을 병렬로 실행하려면 파이프라인을 구성하는 스트림 중 하나에서 parallel 메서드를 호출해 주기만 하면 되나, 효과를 볼 수 있는 상황은 많지 않다.

### 스트림의 사용 노하우

스트림 API는 다재다능하여 사실상 어떠한 계산이라도 해낼 수 있으나 할 수 있다는 뜻이지 해야 한다는 뜻은 아니다. 스트림을 제대로 사용하면 프로그램이 짧고 깔끔해지지만, 잘못 사용하면 읽기 어렵고 유지보수도 힘들어진다.

```java
public class Anagrams {
	  public static void main(String[] args) throws IOException {
      File dictionary = new File(args[0]);
      int minGroupSize = Interger.parseInt(args[1]);
      
      Map<String, Set<String>> groups = new HashMap<>();
      try(Scanner s = new Scanner(dictionary)){
      	while(s.hasNext()){
        	String word = s.next();
            **groups.computeIfAbsent
							(alphabetize(word), (unused) -> new TreeSet<>()).add(word);**
        }
      }
      
      for(Set<String> group : groups.values())
      	if(group.size() >= minGroupSize)
        	System.out.println(group.size() + ":" + group);
    }
    
    public static String alphabetie(String s){
    	char[] a = s.toCharArray();
        Arrays.sort(a);
        return new String(a);
    }
}
```

위의 코드는 사전 파일에서 단어를 읽어 사용자가 지정한 문턱값보다 원소 수가 많은 아나그램 그룹을 출력한다.(아나그램은 철자를 구성하는 알파벳은 같고 그 순서만 다른 단어를 의미한다.)

이 프로그램은 사용자가 명시한 사전 파일에서 각 단어를 읽어 맵에 저장한다. 맵의 키는 그 단어를 구성하는 철자들을 알파벳순으로 정렬한 값이다. 맵의 값은 같은 키를 공유하는 단어들을 담은 집합이다. 이 프로그램은 map의 values() 메서드로 아라그램 집합들을 얻어 원소수가 문턱값보다 많은 집합들을 출력한다.

```java
예를 들어 staple과 petals 모두 키로 aelpst가 되고 아나그램끼리 같은 map의 키를 공유한다.
map의 값은 {staple, petals} 집합으로 구성될 것이다.

```

맵에 각 단어를 삽입할 때 자바 8에서 추가된 computeIfAbsent 메서드를 사용했다. 이 메서드는 맵 안에 키가 있는지 찾은 다음, 있으면 키에 매핑된 값을 반환하고 키가 없을 경우 건네진 함수 객체를 키에 적용하여 값을 계산해낸 다음 그 키와 값을 매핑해 놓고 계산된 값을 반환한다.

즉 computeIfAbsent 메서드를 활용하며누 각 키에 다수의 값을 매핑하는 맵을 쉽게 구현할 수 있다. 반면 다음과 같이 스트림을 과하게 사용한 예를 살펴보자

```java
public class Anagrams {
    public static void main(String[] args) throws IOException {
        Path dictionary = Paths.get(args[0]);
        int minGroupSize = Integer.parseInt(args[1]);

        //사전 파일을 제대로 닫기 위해 try-with-resources 활용
        try (Stream<String> words = Files.lines(dictionary)) {
            words.collect(
                    groupingBy(word -> word.chars().sorted()
                            .collect(StringBuilder::new,
                                    (sb, c) -> sb.append((char) c),
                                    StringBuilder::append).toString()))
                    .values().stream()
                    .filter(group -> group.size() >= minGroupSize)
                    .map(group -> group.size() + ": " + group)
                    .forEach(System.out::println);
        }
    }
}
```

사전 파일을 여는 부분만 제외하면 프로그램 전체가 단 하나의 표현식으로 처리된다. 사전을 여는 부분을 분리한 이유는 그저 try-with-resource 문을 사용하여 사전 파일을 제대로 닫기 위해서다. 이처럼 스트림을 과하게 사용하면 프로그램을 읽거나 유지보수하기 어려워진다.

반면 스트림을 적절히 사용하여 짧고 명확하게 작성할 수도 있다 다음의 예를 보자

```java
public class Anagrams {
    public static void main(String[] args) throws IOException {
        Path dictionary = Paths.get(args[0]);
        int minGroupSize = Integer.parseInt(args[1]);

        try (Stream<String> words = Files.lines(dictionary)) {
            words.collect(groupingBy(word -> alphabetize(word)))
                    .values().stream()
                    .filter(group -> group.size() >= minGroupSize)
                    .forEach(g -> System.out.println(g.size() + ": " + g));
        }
    }
    // alphabetize 메서드는 위의 코드와 동일
}
```

이 스트림의 파이프라인에는 중간 연산이 없으며, 종단 연산에서는 모든 단어를 수집해 맵으로 모으고 있다. 이후 맵의 values()가 반환한 값으로부터 새로운 Stream<List<String>> 스트림을 열며, 스트림의 원소는 아나그램 리스트가 된다.

또한 alphabetize() 과 같은 세부 구현은 주 프로그램 로직 밖으로 빼내 전체적인 가독성을 높였다. 이처럼 도우미 메서드를 적절히 활용하는 일의 중요성은 일반 반복 코드에서보다는 스트림 파이프라인에서 훨씬 커진다.

char 값을 처리할 때는 스트림을 사용해서는 안된다. 자바가 기본타입인 char용 스트림을 지원하지 않기 때문이다. 다음의 예를 살펴보자

```java
"Hello World!".chars().forEach(System.out::print);
```

Hello World!가 출력될 것으로 기대하겠지만 정수값이 출력된다. "Hello World!".chars()가 반환하는 스트림의 원소는 int값이기 때문이다. 올바른 코드는 다음과 같다.

```java
"Hello World!".chars().forEach(x -> System.out.print((char) x));
```

형변환을 명시적으로 하여 Hello World!가 출력될 것이다. 그러나 char값을 처리할 때는 스트림을 삼가하는 것이 낫다.

**스트림을 적절히 활용하는 법**

1. 스트림을 적용이 좋지 않은 경우
    1. 코드 블록에서는 범위 안의 지역변수를 읽고 수정할 수 있다. 하지만 람다에서는 사실상 final인 변수만 읽을 수 있고, 지역변수 수정은 불가능하다.
    2. 코드 블록에서는 return, break, continue 문으로 블록의 흐름을 제어하거나, 메서드 선언에 명시된 검사 예외를 던질 수 있다. 하지만 람다는 모두 불가능하다.
2. 스트림을 적용이 좋은 경우(아래중 하나를 수행하는 로직이라면 스트림 적용이 도움될 수 있다.)
    1. 원소들의 시퀀스를 일관되게 변환해야 하는 경우
    2. 원소들의 시퀀스를 필터링 해야 하는 경우
    3. 원소들의 시퀀스를 하나의 연산을 사용해 결합해야 하는 경우(더하기, 연결하기, 최솟값 구하기 등)
    4. 원소들의 시퀀스를 컬렉션에 모으는 경우(공통된 속성을 기준으로)
    5. 원소들의 시퀀스에서 특정 조건을 만족하는 원소를 찾을 경우
3. 스트림을 적용하기 어려운 경우
    - 한 데이터가 파이프라인의 여러 단계를 통과할 때 이 데이터의 각 단계에서의 값들에 동시에 접근하기는 어려운 경우
        - 스트림 파이프라인은 일단 한 값을 다른 값에 매핑하고 나면 원래의 값은 잃는 구조이기 때문
        - 매핑 객체가 필요한 단계가 여러 곳이라면 특히 더 어렵다.
    
    ```java
    public class MersennePrimes {
        static Stream<BigInteger> primes() {
            return Stream.iterate(TWO, BigInteger::nextProbablePrime);
        }
     
        public static void main(String[] args) {
            primes().map(p -> TWO.pow(p.intValueExact()).subtract(ONE))
                    .filter(mersenne -> mersenne.isProbablePrime(50))
                    .limit(20)
                    .forEach(System.out.println());
        }
    }
    ```
    
    위의 코드는 처음 20개의 메르센 소수를 출력하는 프로그램이다. 메르센 수는 (2^p) - 1 형태의 수로 p가 소수일 경우 메르센 수도 소수일 수 있는데, 소수일 경우 메르센 소수라 한다. 
    
    primes()에서 모든 소수 원소를 갖는 무한 스트림을 반환한다. Strean.iterate()를 사용하는데 첫번째 인수는 스트림의 첫 번째 원소이고 두 번째 매개변수는 스트림에서 다음 원소를 함께 생성해주는 함수다.
    
    main 함수에서 primes()를 이용해 소수 원소들을 갖는 무한 스트림을 받고 이 소수들을 통해 메르센수를 계산한다. 계산 후 filter를 통해 소수인 경우만을 남긴 후 20개가 차면 그 원소들을 하나씩 출력한다.
    
    만약 여기서 p를 출력하고 싶을경우 종단 연산 forEach에서 p에 접근할 수 없기 때문에 스트림으로 해결하기 어렵다. 이런 경우 앞단계의 값을 알기 위해 매핑을 거꾸로 수행할 수 있다.
    
    ```java
    public class MersennePrimes {
        static Stream<BigInteger> primes() {
            return Stream.iterate(TWO, BigInteger::nextProbablePrime);
        }
     
        public static void main(String[] args) {
            primes().map(p -> TWO.pow(p.intValueExact()).subtract(ONE))
                    .filter(mersenne -> mersenne.isProbablePrime(50))
                    .limit(20)
                    .forEach(
    									mp -> System.out.println(mp.bitLength() + ": " + mp
    								)
    				);
        }
    }
    ```