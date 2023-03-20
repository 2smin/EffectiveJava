package Chapter08.Item52.ghlim;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    public static void main(String[] args){
        new Thread(System.out::println).start();
        ExecutorService exec = Executors.newCachedThreadPool();
        // 참조 메소드(println)와 호출 메소드(submit) 둘다 overload 되어 컴파일 에러
//        exec.submit(System.out::println);


    }
}
