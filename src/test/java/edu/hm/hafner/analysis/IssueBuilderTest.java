package edu.hm.hafner.analysis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.IssueAssert.*;

class IssueBuilderTest {
    private IssueBuilder builderUnderTest;
    private Issue issueUnderTest;

    @BeforeEach
    void setup() {
        builderUnderTest = new IssueBuilder();
    }

    @Test
    void setFileName() {
        builderUnderTest.setFileName("test");
        issueUnderTest = builderUnderTest.build();
        assertThat(issueUnderTest)
                .as("Filenames should match")
                .hasFileName("test");
    }

    @Test
    void setLineStart() {
        builderUnderTest.setLineStart(1);
        issueUnderTest = builderUnderTest.build();
        assertThat(issueUnderTest)
                .as("LineStart should match")
                .hasLineStart(1);
    }

    @Test
    void lineStartCantBeNegative() {
        builderUnderTest.setLineStart(-1);
        issueUnderTest = builderUnderTest.build();
        assertThat(issueUnderTest)
                .as("Negative LineStart should default to 0")
                .hasLineStart(0);
    }

    @Test
    void setLineEnd() {
        builderUnderTest.setLineEnd(1);
        issueUnderTest = builderUnderTest.build();
        assertThat(issueUnderTest)
                .as("LineEnd should match")
                .hasLineEnd(1);
    }

    @Test
    void lineEndCantBeSmallerThanLineStart() {
        builderUnderTest.setLineStart(2);
        builderUnderTest.setLineEnd(1);
        issueUnderTest = builderUnderTest.build();
        assertThat(issueUnderTest)
                .as("LineEnd can't be below LineStart")
                .hasLineEnd(1);
    }

    @Test
    void setColumnStart() {
    }

    @Test
    void setColumnEnd() {
    }

    @Test
    void setCategory() {
    }

    @Test
    void setType() {
    }

    @Test
    void setPackageName() {
    }

    @Test
    void setPriority() {
    }

    @Test
    void setMessage() {
    }

    @Test
    void setDescription() {
    }

    @Test
    void copy() {
    }

    @Test
    void build() {
    }

}