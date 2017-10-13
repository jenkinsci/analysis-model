package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.IssueAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class IssueBuilderTest {

    @Test
    void buildEmptyIssue() {
        final Issue sut = new IssueBuilder().build();
        assertThat(sut)
                .hasFileName("-");
    }
}