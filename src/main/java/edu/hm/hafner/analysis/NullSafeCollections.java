package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NullSafeCollections<T> {

	
	public static <T> List<T> emptyNullSafeList(){
		return new NullSafeList<>(new ArrayList<>());
	}

	public static <T> List<T> NullSafeList(final int initialCapacity){
		return new NullSafeList<>(new ArrayList<>(initialCapacity));	
	}

	public static <T> List<T> copyNullSafeList(final Collection<? extends T> that){
		final List<T> resultList = emptyNullSafeList();
		resultList.addAll(that);		
		return resultList;	
	}


}
