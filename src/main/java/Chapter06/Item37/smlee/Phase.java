package Chapter06.Item37.smlee;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
}
