package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

class IssueDifferenceBuilderTest {

    @Test
    void buildThrowsIllegalStateOnIncomplete() {
        IssueDifferenceBuilder sut = new IssueDifferenceBuilder();

        assertThatThrownBy(sut::build).isInstanceOf(IllegalStateException.class);
    }
}
