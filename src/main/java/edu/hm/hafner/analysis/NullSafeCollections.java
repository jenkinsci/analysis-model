package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

/**
 * Eine Simple Factory fuer die NullSafeList.
 * @author Michael Schober
 */
class NullSafeCollections {

    NullSafeList nullSafeList(){
        return new NullSafeList(new ArrayList());
    }

    NullSafeList nullSafeList(int size){
        return new NullSafeList(new ArrayList(size));
    }

    NullSafeList nullSafeList(List<Integer> integerList){
        return new NullSafeList(integerList);
    }

}
