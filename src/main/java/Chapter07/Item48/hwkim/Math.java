package Chapter07.Item48.hwkim;

import java.math.BigInteger;
import java.util.stream.LongStream;

public class Math {
    static long noneParallelPi(long n){
        return LongStream.rangeClosed(2, n)
                .mapToObj(BigInteger::valueOf)
                .filter(i -> i.isProbablePrime(50))
                .count();
    }

    static long parallelPi(long n){
        return LongStream.rangeClosed(2, n)
                .parallel()
                .mapToObj(BigInteger::valueOf)
                .filter(i -> i.isProbablePrime(50))
                .count();
    }
}
