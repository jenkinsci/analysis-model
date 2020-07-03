package edu.hm.hafner.analysis;

import edu.hm.hafner.util.SerializableTest;

public class NullSafeListSerializableTest extends SerializableTest<NullSafeList> {

    @Override
    protected NullSafeList createSerializable() {
        return new NullSafeList(10);
    }
}
