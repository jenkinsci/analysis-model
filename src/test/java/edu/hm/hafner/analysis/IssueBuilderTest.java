package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.IssueAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class IssueBuilderTest {

    @Test
    void buildEmptyIssue() {
        final Issue sut = new IssueBuilder().build();
        assertThat(sut)
                .hasFileName("-")
                .hasCategory("")
                .hasType("-")
                .hasPriority(Priority.NORMAL)
                .hasMessage("")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasColumnStart(0)
                .hasColumnEnd(0);
    }
}