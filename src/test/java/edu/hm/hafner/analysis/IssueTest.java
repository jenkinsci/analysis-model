package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.IssueAssert.assertThat;

class IssueTest {
    @Test
    public void createDefaultIssue() {
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
                .hasColumnEnd(0)
                .hasFingerPrint("-");
    }

    @Test
    public void negativeLineAndColumnParams() {
        final Issue sut = new IssueBuilder()
                .setLineStart(-1)
                .setLineEnd(-1)
                .setColumnStart(-1)
                .setLineEnd(-1)
                .build();

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
                .hasColumnEnd(0)
                .hasFingerPrint("-");
    }

    @Test
    public void issueWithWindowsFileName() {
        final Issue sut = new IssueBuilder()
                .setFileName("\\random\\folder\\filename")
                .build();

        assertThat(sut).hasFileName("/random/folder/filename");
    }

    @Test
    public void setFingerPrintToNullAndEmptyAndReplaceWithDefault() {
        final Issue sut = new IssueBuilder().build();
        sut.setFingerprint(null);
        assertThat(sut).hasFingerPrint("-");
        sut.setFingerprint("");
        assertThat(sut).hasFingerPrint("-");
    }

    @Test
    public void stringRepresentationOfDefaultIssue() {
        final Issue sut = new IssueBuilder().build();

        assertThat(sut).hasToString("-(0,0): -: : ");
    }

}