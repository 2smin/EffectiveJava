package Chapter04.Item18.smlee;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class ForwardedHashSet<T> extends HashSet<T> {

    private HashSet hashSet;

    public ForwardedHashSet(HashSet<T> hashSet){
        this.hashSet = hashSet;
    }

    public boolean add(T e){
        return hashSet.add(e);
    }

    public boolean addAll(Collection<? extends T> list){
        return hashSet.addAll(list);
    }
}
