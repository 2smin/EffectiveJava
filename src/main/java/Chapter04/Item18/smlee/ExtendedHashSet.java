package Chapter04.Item18.smlee;

import java.util.Collection;
import java.util.HashSet;

public class ExtendedHashSet<T> extends HashSet<T> {

    public int addCount = 0;

    public ExtendedHashSet(){}

    public ExtendedHashSet(int initialCapacity, float loadFactor){
        super(initialCapacity,loadFactor);
    }

    @Override
    public boolean add(T e){
        addCount++;
        return super.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends T> list){
        addCount += list.size();
        return super.addAll(list);
    }

    public int getAddCount(){
        return addCount;
    }
}
