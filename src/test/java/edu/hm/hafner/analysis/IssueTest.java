package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IssueTest {
    @Test
    void buildEmptyIssue() {
        Issue sut = new IssueBuilder().build();
        IssueAssertSoft softly = new IssueAssertSoft();
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
                .hasFingerprint("-")
                .hasToString("-(0,0): -: : ");
        softly.assertAll();
    }

    @Test
    void issueSameStartAndEndLineAndColumn() {
        Issue sut = new IssueBuilder()
                .setDescription("Test same LineStart, LineEnd, ColumnStart and ColumnEnd")
                .setPackageName("edu.hm.hafner.analysis")
                .setPriority(Priority.LOW)
                .setMessage("Message - Test same LineStart, LineEnd, ColumnStart and ColumnEnd")
                .setFileName("IssuesTest.java")
                .setType("Type")
                .setCategory("Category")
                .setLineStart(1)
                .setLineEnd(1)
                .setColumnStart(2)
                .setColumnEnd(2)
                .build();
        IssueAssertSoft softly = new IssueAssertSoft();
        softly.assertThat(sut)
                .hasFileName("IssuesTest.java")
                .hasCategory("Category")
                .hasType("Type")
                .hasPriority(Priority.LOW)
                .hasMessage("Message - Test same LineStart, LineEnd, ColumnStart and ColumnEnd")
                .hasDescription("Test same LineStart, LineEnd, ColumnStart and ColumnEnd")
                .hasPackageName("edu.hm.hafner.analysis")
                .hasLineStart(1)
                .hasLineEnd(1)
                .hasColumnStart(2)
                .hasColumnEnd(2)
                .hasFingerprint("-")
                .hasToString("IssuesTest.java(1,2): Type: Category: Message - Test same LineStart, LineEnd, ColumnStart and ColumnEnd");
        softly.assertAll();
    }

    @Test
    void issueDifferentStartAndEndLineAndColumn() {
        Issue sut = new IssueBuilder()
                .setDescription("Test different LineStart, LineEnd, ColumnStart and ColumnEnd")
                .setPackageName("edu.hm.hafner.analysis")
                .setPriority(Priority.HIGH)
                .setMessage("Message - Test different LineStart, LineEnd, ColumnStart and ColumnEnd")
                .setFileName("IssuesTest.java")
                .setType("Type")
                .setCategory("Category")
                .setLineStart(1)
                .setLineEnd(4)
                .setColumnStart(2)
                .setColumnEnd(5)
                .build();
        IssueAssertSoft softly = new IssueAssertSoft();
        softly.assertThat(sut)
                .hasFileName("IssuesTest.java")
                .hasCategory("Category")
                .hasType("Type")
                .hasPriority(Priority.HIGH)
                .hasMessage("Message - Test different LineStart, LineEnd, ColumnStart and ColumnEnd")
                .hasDescription("Test different LineStart, LineEnd, ColumnStart and ColumnEnd")
                .hasPackageName("edu.hm.hafner.analysis")
                .hasLineStart(1)
                .hasLineEnd(4)
                .hasColumnStart(2)
                .hasColumnEnd(5)
                .hasFingerprint("-")
                .hasToString("IssuesTest.java(1,2): Type: Category: Message - Test different LineStart, LineEnd, ColumnStart and ColumnEnd");
        softly.assertAll();
    }

    @Test
    void testSetFingerprint() {
        Issue sut = new IssueBuilder().build();
        IssueAssertSoft softly = new IssueAssertSoft();

        // Check issue object before modification
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
                .hasFingerprint("-")
                .hasToString("-(0,0): -: : ");
        softly.assertAll();

        sut.setFingerprint("new fingerprint");

        // Check that only the fingerprint has changed
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
                .hasFingerprint("new fingerprint")
                .hasToString("-(0,0): -: : ");
        softly.assertAll();
    }

    @Test
    void uuidIsUnique() {
        Issue sut1 = new IssueBuilder().build();
        Issue sut2 = new IssueBuilder().build();
        assertThat(sut1.getId()).as("The uuid of a issue is not equal to it's own uuid").isEqualTo(sut1.getId());
        assertThat(sut2.getId()).as("The uuid of a issue is not equal to it's own uuid").isEqualTo(sut2.getId());
        assertThat(sut1.getId()).as("The uuid of equal issues is the same").isNotEqualTo(sut2.getId());
        assertThat(sut2.getId()).as("The uuid of equal issues is the same").isNotEqualTo(sut1.getId());
    }
}