package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

public class NullSaveArrayList<E> extends ArrayList<E> {
    public NullSaveArrayList(){
        super();
    }

    public NullSaveArrayList(final List<E> liste){
        super(liste);
        if(liste.contains(null)){
            throw new NullPointerException();
        }
    }

    public NullSaveArrayList(final int intCap){
        super(intCap);
    }

    @Override
    public E set(final int i, final E e) {
        if(i<0){
            throw new IllegalArgumentException();
        }
        Objects.requireNonNull(e);
        return super.set(i, e);
    }

    @Override
    public boolean add(final E e) {
        Objects.requireNonNull(e);
        return super.add(e);
    }

    @Override
    public void add(final int i, final E e) {
        if(i<0){
            throw new IllegalArgumentException();
        }
        Objects.requireNonNull(e);
        super.add(i, e);
    }

    @Override
    public boolean addAll(final Collection<? extends E> collection) {
        if(collection.contains(null)){
            throw new NullPointerException();
        }
        return super.addAll(collection);
    }

    @Override
    public boolean addAll(final int i, final Collection<? extends E> collection) {
        if(i<0){
            throw new IllegalArgumentException();
        }
        if(collection.contains(null)){
            throw new NullPointerException();
        }
        return super.addAll(i, collection);
    }

    @Override
    public void replaceAll(final UnaryOperator<E> unaryOperator) {
        Objects.requireNonNull(unaryOperator);
        forEach((o) -> Objects.requireNonNull(unaryOperator.apply(o)));
        super.replaceAll(unaryOperator);
    }
}
