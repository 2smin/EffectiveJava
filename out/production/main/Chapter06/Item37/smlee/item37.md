# Item37

# ordinal 인덱싱 대신 EnumMap을 사용하라

배열이나 리스트에서 원소를 꺼낼 때 ordinal 메서드를 사용해서 인덱스를 얻는 경우가 있다.

```java
public class Plant {

    enum LifeCycle { ANNUAL, PRENNIAL, BIENNIAL }

    final String name;
    final LifeCycle lifeCycle;

    public Plant(String name, LifeCycle lifeCycle) {
        this.name = name;
        this.lifeCycle = lifeCycle;
    }

    @Override
    public String toString() {
        return name;
    }

    public static void main(String[] args) {

       List<Plant> garden = new ArrayList<>();

        garden.add(new Plant("민들레", Plant.LifeCycle.ANNUAL));
        garden.add(new Plant("무궁화", Plant.LifeCycle.BIENNIAL));
        garden.add(new Plant("잡초", Plant.LifeCycle.PRENNIAL));

        //배열은 제네릭과 형변환 불가, 비검사 형변환 수행해야함.
        Set<Plant>[] plantsByLifeCycle = (Set<Plant>[])new Set[Plant.LifeCycle.values().length];

        for(Plant p : garden){
            plantsByLifeCycle[p.lifeCycle.ordinal()].add(p);
        }
        
        //배열인덱스에서 오류가 날 수 있다.
        //출력용 문자열 직접 설정해야함
        for(int i=0; i < plantsByLifeCycle.length; i++){
            System.out.printf("%s: %s%n", Plant.LifeCycle.values()[i], plantsByLifeCycle[i]);
        }
    }
}
```

배열은 실질적으로 열거 타입 상수를 값으로 매핑하는 일을 한다. 그러므로 map을 사용하면 편하다.

### EnumMap

enumMap은 열거타입을 키로 사용하여 아주 빠른 성능을 보장한다.

```java
public static void useEnumMap(List<Plant> garden){

    Map<Plant.LifeCycle, Set<Plant>> plantByLifeCycle = new EnumMap<Plant.LifeCycle, Set<Plant>>(Plant.LifeCycle.class);

    for(Plant.LifeCycle lc : Plant.LifeCycle.values()){
        plantByLifeCycle.put(lc,new HashSet<>());
    }

    //enum 자체적으로 출력용 문자열 제공
    //배열 인덱스에서 오류날 일도 없다.
    //형변환도 필요 없음
    for(Plant p : garden){
        plantByLifeCycle.get(p.lifeCycle).add(p);
        System.out.println(plantByLifeCycle);
    }
}
```

enumMap은 내부적으로 배열을 사용한다. 내부적으로 코드를 숨겨서 배열의 성능과 map의 안정성을 모두 얻어냈다.

### Stream을 사용한 enumMap

```java
public static void enumMapStream(Plant[] garden){

    //enumMap이 아닌 자체 내부 map 구현체를 사용하게 되므로 성능상 이점을 잃는다
    System.out.println(Arrays.stream(garden).collect(groupingBy(p -> p.lifeCycle)));

    //원하는 map 구현체를 등록하여 사용할 수 있다.
    System.out.println(Arrays.stream(garden).collect(
groupingBy(p -> p.lifeCycle, () -> new EnumMap<>(Plant.LifeCycle.class),toSet()))
    );
}
```

Stream을 사용하면 코드 수를 줄일 수 있으며, 원하는 map 구현체를 등록하여 사용 가능하다.

steram에서는 일반 enumMap과 살짝 다르게 동작하는데, 기존 코드는 모든 group에 대해 map을 만들지만, stream의 경우 원소가 존재하는 group에 대해서만 map을 만든다 (중첩map이 어디있지?)

[[Java] EnumMap에 대한 관찰](https://velog.io/@kasania/Java-EnumMap)

### 다차원 관계에서의 EnumMap

이중 배열 같은 다차원 관계에서도 EnumMap을 사용할 수 있다. 만약 enum의 oridinal()을 사용하여 다차원 index로 사용한다면 문제가 발생할 수 있다.

```java
public enum Phase {
    
    SOLID, LIQUID, GAS;
    
    public enum Transition{
        MELT, FREEZE, BOIL;
    
        private static final Transition[][] TRANSITIONS = {
                {null, MELT, null}, {FREEZE, null, BOIL}
        };
        
        public static Transition from(Phase from, Phase to){
            return TRANSITIONS[from.ordinal()][to.ordinal()];
        }
    }
}
```

from 과 to 에 상태 값을 넣어서 어떤 전이인지 알아내는 방법이다. ordinal을 사용했으므로 enum field의 값이 변경되면 TRANSITIONS 표도 같이 수정되어야 한다.

field값이 늘어날 수록 표의 크기도 제곱으로 커지며 관리하기가 어려워진다.

```java
public enum TransitionWithEnumMap{
        MELT(SOLID,LIQUID), FREEZE(LIQUID,SOLID), BOIL(LIQUID,GAS);

        private final Phase from;
        private final Phase to;

        TransitionWithEnumMap(Phase from, Phase to) {
            this.from = from;
            this.to = to;
        }

        private static final Map<Phase, Map<Phase,TransitionWithEnumMap>> transitionMap
                = Stream.of(values()).collect(
                        Collectors.groupingBy(
                                t -> t.from, //from 상태 기준으로 그룹화
                                () -> new EnumMap<>(Phase.class),
                                Collectors.toMap(
                                        t -> t.to, //to 상태를 전이에 대응
                                        t -> t,
                                        (x,y) -> y,
                                        () -> new EnumMap<>(Phase.class)
                                )
                        )
        );
        //뭔소리인지는 모르겠는데 하여튼 원소가 추가되어도 map 코드는 변경되지 않는다
    }
```

stream이 좀 어렵긴 한데 중요한것은 phase, transition의 field 위치가 변경되어도 map 관련 로직은 수정 될 일이 없다는것이다. enum에 원소를 추가해도 나머지 코드는 그대로 쓸 수 있다.