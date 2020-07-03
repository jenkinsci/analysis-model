package edu.hm.hafner.analysis;

import edu.hm.hafner.util.SerializableTest;

class NullSafeListSerializableTest extends SerializableTest<NullSafeList<Integer>> {

    @Override
    protected NullSafeList<Integer> createSerializable() {
        return new NullSafeList<Integer>(10);
    }
}
