package edu.hm.ahager.listtest;

import java.util.List;

import edu.hm.ahager.list.NullSafeSuperList;

public class NullSafeSuperListTest extends BaseNullSafeListTest {

    @Override
    List<Integer> create(final int numberOfInitialElements) {
        if (numberOfInitialElements < 0) {
            throw new IllegalArgumentException();
        }
        final List<Integer> nullSaveList = new NullSafeSuperList<>();
        for (int index = 0; index < numberOfInitialElements; index++) {
            nullSaveList.add(0);
        }
        return nullSaveList;
    }
}
