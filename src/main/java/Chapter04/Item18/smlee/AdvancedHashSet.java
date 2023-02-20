package Chapter04.Item18.smlee;

import java.util.ArrayList;
import java.util.Collection;

public class AdvancedHashSet<T> extends ExtendedHashSet<T> {

    @Override
    public boolean add(T e){
        if(e instanceof Integer){
            return super.add(e);
        }
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> list){
        if(list instanceof ArrayList){
            return super.addAll(list);
        }
        return false;
    }
}
