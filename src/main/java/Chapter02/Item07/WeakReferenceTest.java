package Chapter02.Item07;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class WeakReferenceTest {

    public Map<Integer,String> hashMap = new HashMap<>();
    public Map<Integer,Object> weakHashMap = new WeakHashMap<Integer,Object>();

    public static void main(String[] args) {
    //============WeakHashMap=============
        Map<Key,String> hashMap = new HashMap<>();
        WeakHashMap<Key,Object> weakedHashMap = new WeakHashMap<Key,Object>();

        Key key1 = new Key("testKey");
        Key key2 = new Key("testKey2");

        weakedHashMap.put(key1,"value1");
        weakedHashMap.put(key2,"value2");

        Key key3 = new Key("testKey");
        Key key4 = new Key("testKey2");

        hashMap.put(key3,"value3");
        hashMap.put(key4,"value4");

        key2 = null;
        key4 = null;

        System.gc();

        weakedHashMap.entrySet().stream().forEach(value -> System.out.println(value));
        hashMap.entrySet().stream().forEach(value -> System.out.println(value));


        //============WeakReference=============
        Key key5 = new Key("Weak");
        Key key6 = new Key("Strong");
        Key strong = key6;
        WeakReference<Key> weak = new WeakReference<>(key5);

        key5 = null;
        key6 = null;

        System.gc();

        System.out.println(strong == null ? "null" : strong.name);
        System.out.println(weak == null ? "null" : weak.get().name);
    }



}
