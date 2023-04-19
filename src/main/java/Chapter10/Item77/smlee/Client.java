package Chapter10.Item77.smlee;

import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.Queue;

public class Client {

    public static void main(String[] args) {

        if(args.length == 0){
            throw new InvalidParameterException();
        }

        Queue<String> testQueue = new LinkedList<>();
        try{
            testQueue.offer(args[0]);
        }catch (ArrayIndexOutOfBoundsException ignored){
            // unreachable code
        }


    }
}




