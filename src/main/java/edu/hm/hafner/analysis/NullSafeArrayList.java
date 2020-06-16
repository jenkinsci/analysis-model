package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

public class NullSafeArrayList<E> extends ArrayList<E> {

    public NullSafeArrayList(){
        super();
    }

    public NullSafeArrayList(final List<E> list){
        super(list);
        for(E e: list){
            if(e.equals(null)) throw new IllegalArgumentException();
        }
    }

    public NullSafeArrayList(int i){
        super(i);
    }

    @Override
    public E set(final int i, final E e) {
        if(e.equals(null)) throw new IllegalArgumentException();
        return super.set(i, e);
    }

    @Override
    public boolean add(final E e) {
        if(e.equals(null)) throw new IllegalArgumentException();
        return super.add(e);
    }

    @Override
    public void add(final int i, final E e) {
        if(e.equals(null)) throw new IllegalArgumentException();
        super.add(i, e);
    }

    @Override
    public boolean addAll(final Collection<? extends E> collection) {
        Objects.requireNonNull(collection);
        for(E e :collection){
            if(e.equals(null)) throw new IllegalArgumentException();
        }
        return super.addAll(collection);
    }

    @Override
    public boolean addAll(final int i, final Collection<? extends E> collection) {
        Objects.requireNonNull(collection);
        for(E e :collection){
            if(e.equals(null)) throw new IllegalArgumentException();
        }
        return super.addAll(i, collection);
    }

    @Override
    public void replaceAll(final UnaryOperator<E> unaryOperator) {
        forEach((o) -> Objects.requireNonNull(unaryOperator.apply(o)));
        super.replaceAll(unaryOperator);
    }
}
