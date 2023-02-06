package Chapter01.Item07;

import java.util.EmptyStackException;

public class ReleaseReferenceTest {

    private Object[] cacheArray;

    private int index = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    public ReleaseReferenceTest(){
        this.cacheArray = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object obj){
        cacheArray[index++] = obj;
    }


    public Object pop(){
        if(index==0){
            throw new EmptyStackException();
        }

        Object obj = cacheArray[--index];
        cacheArray[index] = null;
        return obj;
    }
}
