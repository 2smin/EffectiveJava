package Chapter07.Item48.hwkim;

public class Client {
    public static void main(String[] args){
        long noneParallelBeforeTime = System.currentTimeMillis();
        Math.noneParallelPi(10000000);
        long noneParallelAfterTime = System.currentTimeMillis();
        System.out.println(noneParallelAfterTime - noneParallelBeforeTime);

        long parallelBeforeTime = System.currentTimeMillis();
        Math.parallelPi(10000000);
        long parallelAfterTime = System.currentTimeMillis();
        System.out.println(parallelAfterTime - parallelBeforeTime);
    }
}
