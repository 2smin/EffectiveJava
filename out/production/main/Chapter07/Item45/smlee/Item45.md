# Item45

# 스트림은 주의해서 사용하라

### Stream의 핵심 추상 개념

- Stream은 데이터 원소의 유한/무한 시퀀스 이다.
- Stream pipeline은 원소들로 수행 할 수 있는 연산 단계이다.

Stream은 어느곳이서든 만들 수 있다. 예를 들어 collection, 배열, 파일, 난수생성기 등

### Stream에 적용 가능한 type

객체 참조나 기본 타입 값을 지정할 수 있는데, 이 중 int, long, double은 타입 특화된 stream을 따로 제공한다.

```java
IntStream intStream = Arrays.stream(new int[]{1,2,3,4});
DoubleStream doubleStream = Arrays.stream(new double[]{1.1,2.1,3.1,4.1});
LongStream longStream = Arrays.stream(new long[]{1L,2L,3L,4L});
```

### 스트림의 연산

스트림 파이프라인은 소스 스트림에서 시작해서 중간연산 n개를 거친 후 종단연산에서 끝난다.

중간연산 : 스트림을 다른 스트림으로 변환시킨다. 변환된 스트림의 원소타입은 바뀔 수 있다.

종단연산 : 마지막 중간연산이 내놓은 스트림에 최종 연산을 추가한다.

### 스트림의 지연평가

스트림은 최종 연산이 호출되는 시점에 이전 중간연산을 실시한다. 최종연산을 수행하는 시점에 평가를 해서 쓰이지 않을 원소는 계산에 쓰지 않는다. 

[[Java] 스트림: 지연 연산과 최적화](https://bugoverdose.github.io/development/stream-lazy-evaluation/)

### 스트림을 언제 사용해야할까?

스트림을 사용하면 코드의 수를 줄일 수 있지만 읽기 힘들고 유지보수가 어려워 질 수 있다.

- Stream에 적합한 작업
    - 원소들의 sequence를 일관되게 변환
    - sequence filtering
    - sequence 를 하나의 연산을 사용해 결합
    - collection에 모으기
    - 특정 조건을 만족하는 원소 찾기
- Stream에 적합하지 않은 작업 (람다로 표현하기 어려운 작업)
    - 반복문을 빠져나가거나 건너뛰는 작업 (continue, break, return 등)
    - 지역 변수를 읽고 수정 (람다 내부에서는 지역변수 수정 불가능)
        - 저장용 객체를 만들어서 가지고 다니면 되긴 하지만 영 만족스럽진 않다.

### 스트림을 과하게 사용하지 말자

스트림으로는 코드의 수가 줄어드는 효과가 있지만 너무 과하게 사용하면 읽기도 어렵고 유지보수하기도 힘들다.

```java
public static void main(String[] args) throws IOException {
    List<String> readline = Files.readAllLines(Paths.get(
            "C:\\Users\\tmax\\git\\EffectiveJava\\src\\main\\java\\Chapter07\\Item45\\word.txt"));
    int minGroupSize = 3;

    Map<String, Set<String>> groups = new HashMap<>();

    for(String s : readline){
        groups.computeIfAbsent(alphabetize(s), (unused) -> new TreeSet<>()).add(s);
    }

    for(Set<String> group : groups.values()){
        if(group.size() >= minGroupSize){
            System.out.println(group.size() + ": " + group);
        }
    }
}

public static String alphabetize(String s){
    char[] a = s.toCharArray();
    Arrays.sort(a);
    return new String(a);
}
```

```dart
public static void stream1() throws IOException{
    List<String> readline = Files.readAllLines(Paths.get(
            "C:\\Users\\tmax\\git\\EffectiveJava\\src\\main\\java\\Chapter07\\Item45\\word.txt"));
    int minGroupSize = 3;

    Stream<String> words = readline.stream();
    words.collect(
                    Collectors.groupingBy(word -> word.chars().sorted()
                            .collect(StringBuilder::new,(sb,c) -> sb.append((char)c),
                                    StringBuilder::append).toString()))
            .values().stream()
            .filter(group -> group.size() >= minGroupSize
            );
}
```

```java
public static void stream2() throws IOException{
    List<String> readline = Files.readAllLines(Paths.get(
            "C:\\Users\\tmax\\git\\EffectiveJava\\src\\main\\java\\Chapter07\\Item45\\word.txt"));
    int minGroupSize = 3;

    Stream<String> words = readline.stream();
    words.collect(
                    Collectors.groupingBy(word -> Anagrams.alphabetize(word)))
            .values().stream()
            .filter(group -> group.size() >= minGroupSize)
            .forEach(System.out::println);
}
```