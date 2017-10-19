package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.IssueAssert.assertThat;

class IssueTest {

    @Test
    public void createDefaultIssue() {
        final Issue sut = new IssueBuilder().build();

        final IssueSoftAssertions softly = new IssueSoftAssertions();
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
                .hasFingerPrint("-");

        softly.assertAll();

        /*
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
         */
    }

    @Test
    public void createSimpleIssue() {
        final Issue sut = new IssueBuilder()
                .setFileName("/random/folder/fileName")
                .setLineStart(0)
                .setLineEnd(10)
                .setPriority(Priority.NORMAL)
                .setColumnStart(0)
                .setColumnEnd(10)
                .setMessage("Error in File \"fileName\"")
                .build();

        assertThat(sut)
                .hasFileName("/random/folder/fileName")
                .hasLineStart(0)
                .hasLineEnd(10)
                .hasColumnStart(0)
                .hasColumnEnd(10)
                .hasMessage("Error in File \"fileName\"");
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