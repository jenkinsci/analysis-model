package edu.hm.hafner.analysis;

import java.util.List;

class NullSafeList<E extends List> extends BaseListDecorator {
    private List list;

    NullSafeList(final List list) {
        super(list);
        this.list = list;
    }

    @Override
    public boolean add(Object o) {
        if (o != null) {
            this.list.add(o);
            return true;
        }
        else {
            throw new NullPointerException();
        }
    }
}