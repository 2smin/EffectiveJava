package Chapter03.Item15.smlee;

import java.util.Arrays;

public class Client {
    public static void main(String[] args) {
        Arrays.stream(Sample.ARRAY1).iterator().forEachRemaining(str -> System.out.println(str));
        Sample.ARRAY1[2] = "Q";
        Arrays.stream(Sample.ARRAY1).iterator().forEachRemaining(str -> System.out.println(str));

        String[] clonedArray = Sample.returnArray();
        Arrays.stream(clonedArray).iterator().forEachRemaining(str -> System.out.println(str));
        clonedArray[2] = "Q";
        Arrays.stream(clonedArray).iterator().forEachRemaining(str -> System.out.println(str));

        System.out.println(Sample.ARRAY_AS_LIST);
        Sample.ARRAY_AS_LIST.add("Q");
        System.out.println(Sample.ARRAY_AS_LIST);
    }
}
