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

    @Test
    void issueSameStartAndEndLineAndColumn() {
        final Issue sut = new IssueBuilder()
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
        final IssueAssertSoft softly = new IssueAssertSoft();
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
                .hasToString("IssuesTest.java(1,2): Type: Category: Message - Test same LineStart, LineEnd, ColumnStart and ColumnEnd");
        softly.assertAll();
    }

    @Test
    void issueDifferentStartAndEndLineAndColumn() {
        final Issue sut = new IssueBuilder()
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
        final IssueAssertSoft softly = new IssueAssertSoft();
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
                .hasToString("IssuesTest.java(1,2): Type: Category: Message - Test different LineStart, LineEnd, ColumnStart and ColumnEnd");
        softly.assertAll();
    }
}