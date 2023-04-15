package Chapter10.Item76.smlee;

import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.Queue;

public class Client {

    private Queue<Object> queue = new LinkedList<>();

    public Object pop(){
        if(queue.size() == 0){
            throw new EmptyStackException();
        }else{
            return queue.poll();
        }
    }
}
