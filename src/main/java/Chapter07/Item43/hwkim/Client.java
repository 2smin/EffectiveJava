package Chapter07.Item43.hwkim;

import java.util.HashMap;
import java.util.Map;

public class Client {
    public static void main(String[] args){
        Map<String, Integer> map = new HashMap<>();
        map.put("hwkim", 1);
        map.merge("hwkim", 1, Integer::sum);
        System.out.println(map.get("hwkim"));
    }
}
