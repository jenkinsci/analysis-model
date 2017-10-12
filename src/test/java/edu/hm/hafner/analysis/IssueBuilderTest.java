package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IssueBuilderTest {

    @Test
    void buildEmptyIssue() {
        final Issue sut = new IssueBuilder().build();
        IssueAssert.assertThat(sut)
                .hasFileName("-");
    }
}