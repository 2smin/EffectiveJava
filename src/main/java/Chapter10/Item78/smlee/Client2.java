package Chapter10.Item78.smlee;

import java.util.concurrent.atomic.AtomicInteger;

public class Client2 {

    private static volatile boolean stopRequest;

    public static void main(String[] args) {

        Thread backgroundThread = new Thread(() -> {
            int i = 0;
            while(!stopRequest){
                i++;
            }
        });

        stopRequest = true;
    }

    private static final AtomicInteger nextNumber = new AtomicInteger();

    public static int incrementNumber(){
        return nextNumber.getAndIncrement();
    }
}
