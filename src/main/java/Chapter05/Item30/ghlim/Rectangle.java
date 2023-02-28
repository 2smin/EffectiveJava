package Chapter05.Item30.ghlim;

import java.util.Collection;

import static java.util.Objects.requireNonNull;


public class Rectangle implements Comparable<Rectangle>{

    private final Integer w;
    private final Integer h;

    public Rectangle(Integer w, Integer h) {
        this.w = w;
        this.h = h;
    }

    public Integer area() {
        return w * h;
    }

    @Override
    public int compareTo(Rectangle that) {
        if(that.area() < this.area() ) return 1;
        if(that.area().equals(this.area()) ) return 0;
        if(that.area() > this.area() ) return -1;

        return 0;
    }

    public static <E extends Comparable<E>> E max(Collection<E> c) {
        if (c.isEmpty())
            throw new IllegalArgumentException("컬렉션이 비어 있습니다.");

        E result = null;
        for (E e : c)
            if (result == null || e.compareTo(result) > 0) {
                result = e;
            }

        return result;
    }
}
