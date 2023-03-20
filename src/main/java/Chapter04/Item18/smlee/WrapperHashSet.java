package Chapter04.Item18.smlee;

import java.util.Collection;
import java.util.HashSet;

public class WrapperHashSet<T> extends ForwardedHashSet<T>{

    private int addCount = 0;

    public WrapperHashSet(HashSet<T> hashSet){
        super(hashSet);
    }

    @Override
    public boolean add(T e) {
        addCount++;
        return super.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends T> list) {
        addCount++;
        return super.addAll(list);
    }
}
