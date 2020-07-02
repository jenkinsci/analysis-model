package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

/**
 * Eine Simple Factory fuer die NullSafeList.
 * @author Michael Schober
 */
public class NullSafeCollections {

    public NullSafeList nullSafeList(){
        return new NullSafeList(new ArrayList());
    }

    public NullSafeList nullSafeList(int size){
        return new NullSafeList(new ArrayList(size));
    }

    public NullSafeList nullSafeList(List<Integer> integerList){
        return new NullSafeList(integerList);
    }

}
