package Chapter08.Item52.smlee;

import java.math.BigInteger;
import java.util.*;

public class CollectionClassifier {

    public static String classify(Set<?> s){
        return "set";
    }

    public static String classify(List<?> list){
        return "list";
    }

    public static String classify(Collection<?> collection){
        return "else";
    }

    public static void main(String[] args) {
        Collection<?>[] collections = {
                new HashSet<String>(),
                new ArrayList<BigInteger>(),
                new HashMap<String ,String >().values()
        };

        for(Collection<?> c : collections){
            System.out.println(classify(c));
        }
    }

    public static String classify2(Collection<?> c){
        return c instanceof Set ? "set" : c instanceof  List ? "list" : "else";
    }
}
