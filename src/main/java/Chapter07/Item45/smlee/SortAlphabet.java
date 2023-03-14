package Chapter07.Item45.smlee;

import java.util.Arrays;
import java.util.stream.IntStream;

public class SortAlphabet {

    private int[] number;
    private int limit;

    public SortAlphabet(int[] number, int limit) {
        this.number = number;
        this.limit = limit;
    }

    public void sort(){
        IntStream stream = Arrays.stream(number);
        stream
                .filter(number -> number <= limit)
                .sorted()
                .forEach(System.out::println);

    }

    public static void main(String[] args) {
        int[] array = new int[]{4,2,7,9,6};
        SortAlphabet sortAlphabet = new SortAlphabet(array,8);
        sortAlphabet.sort();
    }
}
