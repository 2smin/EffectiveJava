package Chapter05.Item31.ghlim;

import java.util.Arrays;
import java.util.Collection;
import java.util.EmptyStackException;

public class Stack<E> {
    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;


    public Stack() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }
    public void push(E e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public E pop() {
        if (size == 0)
            throw new EmptyStackException();
        @SuppressWarnings("unchecked") E result = (E) elements[--size];
        elements[size] = null; // 다 쓴 참조 해제
        return result;
    }

    private void ensureCapacity() {
        if(elements.length == size)
            elements = Arrays.copyOf(elements, 2 * size - 1);
    }

    private Boolean isEmpty() {
        return elements.length == 0;
    }

    // 한정적 와일드 카드 사용 - E와 그 하위 타입만 지원 (e : Number, src : Integer)
    public void pushAll(Iterable<? extends E> src) {
        for (E e : src)
            push(e);
    }

    // 한정적 와일드 카드 사용 - E와 그 상위 타입만 지원 (e : Integer, dst : Number)
    public void popAll(Collection<? super E> dst) {
        while (!isEmpty())
            dst.add(pop());
    }
}
