package Chapter07.Item44.hwkim;

import java.util.Map;

public class Client {
    public static void main(String[] args){
        Map<Integer, Integer> cache = new LinkedHashMap<>();
        for(int i = 0; i < 12; i++){
            cache.put(i, i);
        }
        for(Map.Entry entry : cache.entrySet()) {
            System.out.println(entry);
        }
    }
}
