package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;

public class NullSafeArrayList<T> extends ArrayList<T> {

    public NullSafeArrayList(final int i) {
        super(i);
    }

    public NullSafeArrayList(){
        super();
    }

    @Override
    public T set(final int i, final T t) {
        if(t == null)
            throw new NullPointerException();
        return super.set(i, t);
    }

    @Override
    public boolean add(final T t) {
        if(t == null)
            throw new NullPointerException();
        return super.add(t);
    }

    @Override
    public void add(final int i, final T t) {
        if(t == null)
            throw new NullPointerException();
        super.add(i, t);
    }

    @Override
    public boolean addAll(final Collection<? extends T> collection) {
        for (T t : collection){
            if(t == null)
                throw new NullPointerException();
        }
        return super.addAll(collection);
    }

    @Override
    public boolean addAll(final int i, final Collection<? extends T> collection) {
        for (T t : collection){
            if(t == null)
                throw new NullPointerException();
        }
        return super.addAll(i, collection);
    }
}
