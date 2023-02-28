package Chapter05.Item28.smlee;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Chooser<T> {

    private T[] choiceArray; //형변환 직접 해야하니까 경고가 뜬다
    private List<T> choiceList; //경고 제거 (컴파일러가 알아서 형변환 해주기 때문)

    public Chooser(Collection<T> choices){
        choiceArray = (T[])choices.toArray();
    }

    public static void main(String[] args) {
        Chooser chooser = new Chooser(new ArrayList<String >());
    }
}
