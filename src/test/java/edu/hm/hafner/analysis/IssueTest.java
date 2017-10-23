package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IssueTest {
    @Test
    void buildEmptyIssue() {
        final Issue sut = new IssueBuilder().build();
        final IssueAssertSoft softly = new IssueAssertSoft();
        softly.assertThat(sut)
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
                .hasColumnEnd(0)
                .hasToString("-(0,0): -: : ");
        softly.assertAll();
    }
}