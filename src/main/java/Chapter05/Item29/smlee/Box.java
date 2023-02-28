package Chapter05.Item29.smlee;

public class Box<E> {

    private Object[] items;
    private int size;

    public Box(){
        items = new Object[size];
    }

    public void push(E e){
        items[size++] = e;
    }

    public E pop(){
        E result = (E) items[--size];
        return result;
    }
}
