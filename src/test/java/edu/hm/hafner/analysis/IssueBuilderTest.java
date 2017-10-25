package edu.hm.hafner.analysis;


import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.assertions.IssueSoftAssertions;

/**
 * Testing Class for IssueBuilder and Issue.
 *
 * @author Mark Tripolt
 */
class IssueBuilderTest {

    @Test
    void createDefaultIssue() {
        Issue issue = new IssueBuilder().build();
        IssueSoftAssertions.assertIssueSoftly(softly -> {
            softly.assertThat(issue)
                    .hasFileName("-")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasColumnStart(0)
                    .hasColumnEnd(0)
                    .hasCategory("")
                    .hasType("-")
                    .hasPackageName("-")
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("")
                    .hasDescription("")
                    .hasToString("-(0,0): -: : ");
        });

    }

    @Test
    void createIssue() {
        Issue issue = new IssueBuilder()
                .setFileName("anotherIssue")
                .setLineStart(1)
                .setLineEnd(3)
                .setColumnStart(0)
                .setColumnEnd(123)
                .setCategory("really bad")
                .setType("worst")
                .setPackageName("issue")
                .setPriority(Priority.HIGH)
                .setMessage("Just another Issue")
                .setDescription("just another bad issue")
                .build();

        IssueSoftAssertions.assertIssueSoftly(softly -> {
            softly.assertThat(issue)
                    .hasFileName("anotherIssue")
                    .hasLineStart(1)
                    .hasLineEnd(3)
                    .hasColumnStart(0)
                    .hasColumnEnd(123)
                    .hasCategory("really bad")
                    .hasType("worst")
                    .hasPackageName("issue")
                    .hasPriority(Priority.HIGH)
                    .hasMessage("Just another Issue")
                    .hasDescription("just another bad issue")
                    .hasToString("anotherIssue(1,0): worst: really bad: Just another Issue");
        });
    }

    @Test
    void testCopyIssue() {
        Issue issue = new IssueBuilder()
                .setFileName("anotherIssue")
                .setLineStart(1)
                .setLineEnd(3)
                .setColumnStart(0)
                .setColumnEnd(123)
                .setCategory("really bad")
                .setType("worst")
                .setPackageName("issue")
                .setPriority(Priority.HIGH)
                .setMessage("Just another Issue")
                .setDescription("just another bad issue")
                .build();

        Issue copy = new IssueBuilder()
                .copy(issue)
                .build();

        IssueSoftAssertions.assertIssueSoftly(softly -> {
            softly.assertThat(copy)
                    .hasFileName("anotherIssue")
                    .hasLineStart(1)
                    .hasLineEnd(3)
                    .hasColumnStart(0)
                    .hasColumnEnd(123)
                    .hasCategory("really bad")
                    .hasType("worst")
                    .hasPackageName("issue")
                    .hasPriority(Priority.HIGH)
                    .hasMessage("Just another Issue")
                    .hasDescription("just another bad issue")
                    .hasToString("anotherIssue(1,0): worst: really bad: Just another Issue");
        });
    }


    @Test
    void createNullIssue() {
        Issue issue = new IssueBuilder()
                .setFileName(null)
                .setLineStart(0)
                .setLineEnd(0)
                .setColumnStart(0)
                .setColumnEnd(0)
                .setCategory(null)
                .setType(null)
                .setPackageName(null)
                .setPriority(null)
                .setMessage(null)
                .setDescription(null)
                .build();

        IssueSoftAssertions.assertIssueSoftly(softly -> {
            softly.assertThat(issue)
                    .hasFileName("-")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasColumnStart(0)
                    .hasColumnEnd(0)
                    .hasCategory("")
                    .hasType("-")
                    .hasPackageName("-")
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("")
                    .hasDescription("")
                    .hasToString("-(0,0): -: : ");
        });
    }

    @Test
    void createNegativeIssue() {
        Issue issue = new IssueBuilder()
                .setLineStart(-1)
                .setLineEnd(-1)
                .setColumnStart(-1)
                .setColumnEnd(-1)
                .build();

        IssueSoftAssertions.assertIssueSoftly(softly -> {
            softly.assertThat(issue)
                    .hasFileName("-")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasColumnStart(0)
                    .hasColumnEnd(0)
                    .hasCategory("")
                    .hasType("-")
                    .hasPackageName("-")
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("")
                    .hasDescription("")
                    .hasToString("-(0,0): -: : ");
        });

    }

    @Test
    void createIssueWithMaxValues() {
        Issue issue = new IssueBuilder()
                .setLineStart(Integer.MAX_VALUE)
                .setLineEnd(Integer.MAX_VALUE)
                .setColumnStart(Integer.MAX_VALUE)
                .setColumnEnd(Integer.MAX_VALUE)
                .build();

        IssueSoftAssertions.assertIssueSoftly(softly -> {
            softly.assertThat(issue)
                    .hasFileName("-")
                    .hasLineStart(Integer.MAX_VALUE)
                    .hasLineEnd(Integer.MAX_VALUE)
                    .hasColumnStart(Integer.MAX_VALUE)
                    .hasColumnEnd(Integer.MAX_VALUE)
                    .hasCategory("")
                    .hasType("-")
                    .hasPackageName("-")
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("")
                    .hasDescription("")
                    .hasToString("-(2147483647,2147483647): -: : ");
        });
    }
}
