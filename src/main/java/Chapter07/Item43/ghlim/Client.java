package Chapter07.Item43.ghlim;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Client {

    public static void main(String[] args){
        Map<String, Integer> mymap = new HashMap<>();

        mymap.put("key1", 2);

        mymap.merge("key1",1,Integer::sum);
    }
}
