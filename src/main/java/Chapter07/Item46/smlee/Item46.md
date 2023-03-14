# Item46

# 스트림에서는 부작용 없는 함수를 사용하라

스트림 패러다임의 핵심은 계산을 일련의 변환으로 재구성하는 부분이다. 각 변환 단계는 가능한 한 이전 단계의 결과를 받아서 처리하는 순수 함수여야 한다.

### 순수 함수란?

오직 입력만이 결과에 영향을 주는 함수로써, 다른 가변 상태를 참조하지 않고, 함수 스스로도 다른 상태를 변경하지 않는다.

### 잘못된 Stream 사용 예시

```java
public static void donotUseLikeThis() throws IOException{
    Map<String,Integer> freq = new HashMap<>();
    List<String> readline = Files.readAllLines(Paths.get(
            "/Users/smlee/Documents/git/EffectiveJava/src/main/java/Chapter07/Item46/word.txt"));
    readline.stream().forEach( word ->
            freq.merge(word.toLowerCase(), 1, Integer::sum)
    );
    System.out.println(freq.entrySet());
}
```

이 stream은 중간 연산 없이 최종연산인 forEach 하나만 쓰고 있다. Stream의 이점을 살리지 못한 반복문 코드일 뿐이다.

forEach에서는 외부 상태(freq)를 변경하는 일을 발생 시키고 있다. 이렇게 쓰지 말자.

forEach는 그 자체로 반복적이기 때문에 가장 stream 스럽지 않고 병렬화도 불가능하다. forEach는 스트림 계산 결과를 보고(출력) 할때만 사용하자.

### 좋은 Stream 사용 예시

```java
public static void useStreamLikeThis() throws IOException {
    Map<String,Long> freq = new HashMap<>();
    List<String> readline = Files.readAllLines(Paths.get(
            "/Users/smlee/Documents/git/EffectiveJava/src/main/java/Chapter07/Item46/word.txt"));
    Stream<String> readStream = readline.stream();
    freq = readStream.collect(groupingBy(String::toLowerCase,counting()));
    System.out.println(freq.entrySet());
}
```

stream에서 collector를 사용하여 스트림의 원소를 collection으로 만들어 준다.

이런식으로 사용하여 연산 결과를 freq에 담아주고 그 객체를 따로 출력하는 방식이 더 좋다. (혹은 forEach로 출력)

### Stream 예시 2

```java
public static void getMostFrequentWord(Map<String,Long> freq){
    List<String> result = freq.keySet().stream()
            .sorted(comparing(freq::get).reversed())
            .limit(2)
            .collect(Collectors.toList());
    result.forEach(System.out::println);
}
```

comparing 메서드는 키 추출 함수를 받는 비교자 생성 메서드이다. freq::get 으로 추출한 키에서 갯수를 가져오고 reverse로 역정렬 한다. (비교자를 사용하면서 이미 정렬이 되어있다)

### Collectors 메서드 정리

- toMap
    
    가장 간단한 map Collector로 스트림 원소를 key와 value에 각각 매핑하는 함수를 인자로 받는다.
    
    ```java
    public static Map<String, Type> stringToEnum=
            Stream.of(Type.values()).collect(toMap(Objects::toString, e -> e));
    ```
    
    위 형태는 스트림 원소가 각각 고유한 키에 매핑 가능할 때 적합하다. 여러 원소가 하나의 키에 매핑된다면 exception이 발생한다.
    
    같은 키를 공유하는 값들은 BinaryOperator<U> 를 사용하여 value를 병합 가능하다.
    
    ```java
    stringToEnumMerge= Stream.of(map.keySet()).
            collect(
    toMap(map::get, e -> e.toString(), (s, s2) -> s + "/" + s2
            ));
    
    ```
    
    (근데 왜 병합 안되지…? map::get 이 안먹힘)
    
    인수가 3개인 toMap 메서드는 키에 연관된 원소 중 하나를 고르는데 유용하다.
    
    ```java
    stringToEnumMerge= Stream.of(map.keySet()).
            collect(toMap(map::get, e -> e.toString(), BinaryOperator.maxBy(comparing(map::get))));
    ```
    
    ```java
    stringToEnumMerge = Stream.of(map.keySet()).
                    collect(toMap(map::get, e -> e.toString(), (oldVal, newVal) -> newVal));
    ```
    
    ```java
    stringToEnumMerge= Stream.of(map.keySet()).
            collect(toMap(map::get, e -> e.toString(), BinaryOperator.maxBy(comparing(map::get)), () -> new HashMap<Integer,String>()));
    ```
    
- groupingBy
    
    입력으로 분류 함수를 받고 출력으로 collector를 반환한다.
    
    ```java
    Stream<String> words = readline.stream();
    words.collect(groupingBy(word -> Anagrams.alphabetize(word)));
    ```
    
    ```java
    Stream<String> words = readline.stream();
    Map<Object,Set<String>> result = words.collect(groupingBy(word -> Anagrams.alphabetize(word),toSet()));
    ```
    
    ```java
    Stream<String> words = readline.stream();
    Map<Object,Set<String>> result = words.collect(
    		groupingBy(word -> Anagrams.alphabetize(word), () -> new TreeMap<Object,Set<String>>(),toSet())
    );
    ```
    
    위 3가지 method를 concurrent 방식으로 사용 가능한 groupingByConcurrent도 사용 가능하다.
    
- partitioningBy
    
    특정 조건을 만족하는지에 따라 true, false를 반환한다
    
    ```java
    Map<Boolean,List<String>> result3 = words.collect(
    		partitioningBy(word -> Anagrams.alphabetize(word).length() > 5)
    );
    ```
    
    groupingBy와 마찬가지로 downstream 수집기, map 설정이 가능하다
    
- 기타 사용할만한 Collector 수집기
    - minBy
    - maxBy
    - counting
    - joining (charSequence 인스턴스의 스트림에만 적용 가능)
        - paramSize 0 = 단순히 원소들을 연결한다.
        - paramSize 1 = 구분자를 받는다
        - paramSize 3 = prefix, suffix 붙이고 구분자도 받는다. ex) . [a,b,c]