package Chapter05.Item32.hwkim;

import java.util.List;

public class Client {
    public static void main(String[] args){
        List<String> attrs = VariableArgumentExample.pickTwo("좋은", "빠른", "저렴한");
        System.out.println(attrs);
    }
}
