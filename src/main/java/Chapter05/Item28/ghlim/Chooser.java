package Chapter05.Item28.ghlim;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Chooser <T> {
//    private final T[] choiceArray;
    private final List<T> choiceList;

    public Chooser(Collection<T> choices) {
//        choiceArray = choices.toArray(); // 컴파일 안됨
//        choiceArray = (T[])choices.toArray();  // 경고 - 캐스팅 안전하지 않음

        choiceList = new ArrayList<>(choices); // list로 만들기!
    }

    public T choose() {
        Random rnd = ThreadLocalRandom.current();

//        return choiceArray[rnd.nextInt(choiceArray.length)];
        return choiceList.get(rnd.nextInt(choiceList.size()));
    }
}
