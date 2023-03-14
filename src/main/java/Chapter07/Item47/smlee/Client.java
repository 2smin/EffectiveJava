package Chapter07.Item47.smlee;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Client {

    public static void main(String[] args) {

    }


    public static void run1(){
        for(ProcessHandle ph : (Iterable<? extends ProcessHandle>) ProcessHandle.allProcesses()::iterator){
            // do ph
        }
    }

    public static <E> Iterable<E> adapter(Stream<E> stream) {
        return stream::iterator;
    }

    public static <E> Stream<E> streamOf(Iterable<E> iterable){
        return StreamSupport.stream(iterable.spliterator(), false);
    }
}
