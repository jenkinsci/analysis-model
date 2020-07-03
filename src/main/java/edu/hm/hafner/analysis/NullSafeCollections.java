package edu.hm.hafner.analysis;

import java.util.List;

public class NullSafeCollections {

    public static <T> NullSafeList<T> nullSafeList(){
        return new NullSafeList<T>();
    }

    public static <T> NullSafeList<T> nullSafeList(int capacity){
        return new NullSafeList<T>(capacity);
    }

    public  static  <T> NullSafeList<T> nullSafeList(List<T> list){
        return new NullSafeList<T>(list);
    }

//    public static void main(String[] args) {
//        List<Integer> list = new ArrayList<>();
//        list.add(5);
//        list.add(7);
//        list.add(11);
//        Collection collection = Collections.checkedCollection(
//                Collections.synchronizedList(
//                        NullSafeCollections.nullSafeList(list)), Integer.class);
//        System.out.println(collection.size());
//    }
}
