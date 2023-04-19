package Chapter10.Item78.smlee;

import java.util.concurrent.TimeUnit;

public class Client {

    private static boolean stepRequested;
    private static synchronized void stop(){stepRequested = true;}
    private static synchronized boolean getRequested(){return stepRequested;}

    public static void main(String[] args) throws InterruptedException{

        Thread backgroundThread = new Thread(() -> {
            int i = 0;
            while(!getRequested()){
                i++;
            }
        });

        backgroundThread.start();

        TimeUnit.SECONDS.sleep(1);

        stop();
    }

}
