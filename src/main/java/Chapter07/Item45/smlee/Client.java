package Chapter07.Item45.smlee;

import java.util.Arrays;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Client {

    public static void main(String[] args) {

        IntStream intStream = Arrays.stream(new int[]{1,2,3,4});
        DoubleStream doubleStream = Arrays.stream(new double[]{1.1,2.1,3.1,4.1});
        LongStream longStream = Arrays.stream(new long[]{1L,2L,3L,4L});


    }
}
