package Chapter05.Item30.ghlim;

public class Circle implements Comparable{

    private final Integer r;

    public Circle(Integer r) {
        this.r = r;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
