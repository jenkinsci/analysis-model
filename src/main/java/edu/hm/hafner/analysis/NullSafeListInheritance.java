package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;

public class NullSafeListInheritance<Object> extends ArrayList<Object> {

    public NullSafeListInheritance(final Collection collection) {
        super(collection);
        for(java.lang.Object o: collection){
            if(o == null){
                throw new NullPointerException();
            }
        }
    }

    @Override
    public boolean add(final Object o) {
        if(o == null){
            throw new NullPointerException();
        }
        return super.add((Object) o);
    }

    @Override
    public void add(final int i, final Object o) {
        if(o == null){
            throw new NullPointerException();
        }
        super.add(i, o);
    }

    @Override
    public boolean addAll(final Collection collection) {
        for(java.lang.Object o: collection){
            if(o == null){
                throw new NullPointerException();
            }
        }
        return super.addAll(collection);
    }

    @Override
    public boolean addAll(final int i, final Collection collection) {
        for(java.lang.Object o: collection){
            if(o == null){
                throw new NullPointerException();
            }
        }
        return super.addAll(i, collection);
    }
}
