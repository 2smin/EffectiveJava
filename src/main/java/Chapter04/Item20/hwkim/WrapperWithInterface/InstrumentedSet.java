package Chapter04.Item20.hwkim.WrapperWithInterface;

import java.util.Collection;
import java.util.Set;

public class InstrumentedSet<E> extends ForwardingSet{
    private int addCount = 0;

    public InstrumentedSet(Set<E> s) {
        super(s);
    }

    @Override
    public boolean add(Object e) {
        addCount++;
        return super.add(e);
    }

    @Override
    public boolean addAll(Collection c) {
        addCount += c.size();
        return super.addAll(c);
    }

    public int getAddCount() {
        return addCount;
    }
}
