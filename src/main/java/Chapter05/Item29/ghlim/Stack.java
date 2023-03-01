package Chapter05.Item29.ghlim;

import java.util.Arrays;
import java.util.EmptyStackException;

public class Stack<E> {
//    private E[] elements;
    // 해결 2 - Object로 바꾸기 - line 27의 SurpressWarnings로 경고 제거
    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    @SuppressWarnings("unchecked")
    public Stack() {
//        elements = new E[DEFAULT_INITIAL_CAPACITY]; // 컴파일 에러 발생 지점

        // 해결 1 - 비검사 형변환 (casting) - line 11의 SurpressWarnings로 경고 제거
        elements = (E[]) new Object[DEFAULT_INITIAL_CAPACITY];
    }
    public void push(E e) {
        ensureCapacity();
        elements[size++] = e;
    }
    public E pop() {
        if (size == 0)
            throw new EmptyStackException();
//        E result = elements[--size];
        @SuppressWarnings("unchecked") E result = (E) elements[--size];
        elements[size] = null; // 다 쓴 참조 해제
        return result;
    }

    private void ensureCapacity() {
        if(elements.length == size)
            elements = Arrays.copyOf(elements, 2 * size - 1);
    }
}