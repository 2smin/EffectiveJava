package Chapter06.Item37.smlee;

import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

public class Client {

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
        useEnumMap(garden);
    }

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

    public static void enumMapStream(Plant[] garden){

        //enumMap이 아닌 자체 내부 map 구현체를 사용하게 되므로 성능상 이점을 잃는다
        System.out.println(Arrays.stream(garden).collect(groupingBy(p -> p.lifeCycle)));

        //원하는 map 구현체를 등록하여 사용할 수 있다.
        System.out.println(Arrays.stream(garden).collect(
                groupingBy(p -> p.lifeCycle, () -> new EnumMap<>(Plant.LifeCycle.class), toSet()))
        );
    }
}
