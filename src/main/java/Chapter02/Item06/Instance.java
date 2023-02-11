package Chapter02.Item06;

import java.util.HashMap;
import java.util.Map;

public class Instance {

    private int value;
    private Instance(int value){
        this.value = value;
    }
    //자주 쓰이는 값을 갖거나, 비용이 높은 객체는 미리 만들어두거나 만들었을떄 캐싱해둔다.
    private static final Instance valueZero = new Instance(0);
    private static final Instance valueOne = new Instance(1);

    private static Map<Integer, Instance> cacheMap = new HashMap<>();

    //만든 객체는 map에 저장해두고 여기서 가져다 쓴다
    //매개변수로 int 인데 autoboxing으로 Integer쓰면 성능만 떨어진다 걍 int 쓰세욤
    public static Instance caching(int value){
        if(cacheMap.containsKey(value)){
            return cacheMap.get(value);
        }

        Instance instance = newInstance(value);
        cacheMap.put(value,instance);
        return instance;
    }
    public static Instance newInstance(int value){
        return new Instance(value);
    }

    /*
    정적 팩토리 메서드를 사용해서 불필요한 객체 생성을 막는다
     */
    public static Instance getInstance(int value){

        if(value == 0){
            return valueZero;
        }else if(value == 1){
            return valueOne;
        }
        Instance instance = new Instance(value);
        return instance;
    }
}
