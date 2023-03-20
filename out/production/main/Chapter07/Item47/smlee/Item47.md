# Item47

# 반환 타입으로는 스트림보다 컬렉션이 낫다

stream은 기본적으로 반복을 지원하지 않기 때문에 스트림과 반복을 알맞게 조합해야한다.

하지만 stream은 iterable이 정의한 추상메서드를 모두 포함하지만, iterable을 확장하지 않아서
for-each로 반복이 불가능하다.

stream에 iterator를 건네주면 해결이 될 듯 하지만 타입을 지정해줘야 한다.

```java
for(ProcessHandle ph : (Iterable<? extends ProcessHandle>) ProcessHandle.allProcesses()::iterator){
    // do ph
}
```

코드의 직관성이 떨어지고 유지 보수도 어렵기 때문에 다음과 같이 adapter 메서드를 만들어서 사용하는것이 훨씬 낫다.

```java
public static <E> Iterable<E> adapter(Stream<E> stream) {
    return stream::iterator;
}
```

반대로  iterator 로부터 stream을 반환하는 adapter 메서드도 만들 수 있다.

```java
public static <E> Stream<E> streamOf(Iterable<E> iterable){
    return StreamSupport.stream(iterable.spliterator(), false);
}
```

### 언제 어떤 타입을 반환할까?

객체 시퀀스를 반환하는 메서드를 작성하는데, 해당 메서드가 stream pipeline에서만 쓰일걸 안다면 스트림을 반환하면 되고, 반복문에서 쓰일거라면 iterable을 반환해주면 된다.

보통 공개 api에서는 두 방식을 모두 제공하긴 한다.

Collection 인터페이스의 경우 iterable의 하위타입이면서 stream 메서드도 제공한다.

Arrays 역시 Arrays.asList 와 Stream.of로 양쪽을 동시에 지원한다.

반환할 시퀀스가 메모리에 올려도 안전할만큼 작다면 표준 컬렉션 구현체 (ex. ArrayList, HashSet ) 를 반환하는게 낫지만, 컬렉션을 반환한다는 이유로 큰 시퀀스를 메모리에 올려서는 안된다.

### 전용 컬렉션을 구현하자.

반환할 시퀀스가 크지만 표현을 간결하게 할 수 있다면 전용 컬렉션을 구현하는것이 낫다.

전용 컬렉션을 구현하기 위해서는 AbstractCollection 를 이용하면 된다. 해당 구현체를 작성할때는 iterable 용 메서드 외에 contains와 size만 구현해주면 된다. (contains는 현재 없음)

```java
public static final <E> Collection<Set<E>> of(Set<E> s) {
    List<E> src = new ArrayList<>(s);
    if(src.size() > 30){
        throw new IllegalArgumentException("too many elements");
    }

    return new AbstractList<Set<E>>() {
        @Override
        public Set<E> get(int index) {
           Set<E> result = new HashSet<>();
           for(int i=0; index != 0; i++, index >>= 1){
               if((index & 1) == 1){
                   result.add(src.get(i));
               }
           }
           return result;
        };

        @Override
        public int size() { //멱집합 갯수 반환
            return 1 << src.size();
        }
    };
}
```

비슷한 로직을 stream으로 구현하면 이렇다 (…)

```java
public static <E> Stream<List<E>> of(List<E> list){
    return Stream.concat(Stream.of(Collections.emptyList()),prefixes(list).flatMap(SubLists::suffixes));
}

private static <E> Stream<List<E>> prefixes(List<E> list){
    return IntStream.rangeClosed(1, list.size())
            .mapToObj(end -> list.subList(0,end));
}

private static <E> Stream<List<E>> suffixes(List<E> list){
    return IntStream.rangeClosed(0, list.size())
            .mapToObj(start -> list.subList(start,list.size()));
}
```

둘 다 동작은 하지만 반복을 사용하는게 자연스러운 상황에서도 굳이 stream을 사용하면 사용자는 adapter 메서드를 사용하여 itreable로 변환해주어야 한다. 하지만 adapter 메서드는 코드를 어수선하게 만든다.

또한 stream을 사용하면 속도 저하도 발생할 수 있으니 그냥 collection을 쓰는게 낫다.