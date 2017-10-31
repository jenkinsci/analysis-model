package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Assertions.IssueSoftAssertion;
import static edu.hm.hafner.analysis.Assertions.IssueAssert.*;

/**
 * Test class for the issue builder.
 *
 * @author Tom Maier
 * @author Johannes Arzt
 */
class IssueBuilderTest {

    @Test
    void createIssueTest() {
        Issue issue = getTestIssueBuilder().build();
        IssueSoftAssertion.assertIssueSoftly(softly -> {
            softly.assertThat(issue)
                    .hasLineStart(1)
                    .hasLineEnd(8)
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("Hello I am a issue")
                    .hasCategory("standard")
                    .hasFileName("/root/issues/issue.log")
                    .hasType("warning")
                    .hasColumnStart(4)
                    .hasColumnEnd(6)
                    .hasPackageName("edu.hm.hafner")
                    .hasDescription("no big problems")
                    .hasToString(String.format("%s(%d,%d): %s: %s: %s", "/root/issues/issue.log", 1, 4, "warning", "standard", "Hello I am a issue"));
        });

    }

    @Test
    void createIssueDefaultValuesTest() {
        IssueBuilder builder = new IssueBuilder();
        Issue defaultIssue = builder.build();

        IssueSoftAssertion.assertIssueSoftly(softly -> {
            softly.assertThat(defaultIssue)
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("")
                    .hasCategory("")
                    .hasFileName("-")
                    .hasType("-")
                    .hasColumnStart(0)
                    .hasColumnEnd(0)
                    .hasPackageName("-")
                    .hasDescription("")
                    .hasToString(String.format("%s(%d,%d): %s: %s: %s", "-", 0, 0, "-", "", ""));
        });


    }

    @Test
    void copyIssueTest() {
        IssueBuilder builder = getTestIssueBuilder();
        Issue issue = getTestIssue().build();
        IssueBuilder copiedBuilder = builder.copy(issue);
        Issue copy = copiedBuilder.build();
        assertThat(copy).isEqualTo(issue);
    }

    private IssueBuilder getTestIssueBuilder() {
        return new IssueBuilder()
                .setLineStart(1)
                .setLineEnd(1)
                .setPriority(Priority.NORMAL)
                .setMessage("Hello I am a issue")
                .setFileName("issue.txt")
                .setCategory("standard")
                .setType("warning")
                .setColumnStart(4)
                .setColumnEnd(1)
                .setPackageName("edu.hm.hafner")
                .setDescription("no big problems");
    }



    @Test
    void createIssueWithNegativeValuesTest() {
        IssueBuilder builder = new IssueBuilder();
        Issue basicIssue = builder
                .setLineStart(-1)
                .setLineEnd(1)
                .setPriority(Priority.NORMAL)
                .setMessage("Hello I am a basic issue")
                .setFileName("basic.txt")
                .setCategory("basic")
                .setType("basic")
                .setColumnStart(-1)
                .setColumnEnd(1)
                .setPackageName("basic")
                .setDescription("basic issue")
                .build();


        IssueSoftAssertion.assertIssueSoftly(softly -> {
            softly.assertThat(basicIssue)
                    .hasLineStart(0)
                    .hasLineEnd(1)
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("Hello I am a basic issue")
                    .hasCategory("basic")
                    .hasFileName("basic.txt")
                    .hasType("basic")
                    .hasColumnStart(0)
                    .hasColumnEnd(1)
                    .hasPackageName("basic")
                    .hasDescription("basic issue")
                    .hasToString(String.format("%s(%d,%d): %s: %s: %s", "basic.txt", 0, 0, "basic", "basic", "Hello I am a basic issue"));
        });
    }

    @Test
    void createIssueWithMaxValuesTest() {
        IssueBuilder builder = new IssueBuilder();
        Issue basicIssue = builder
                .setLineStart(1)
                .setLineEnd(Integer.MAX_VALUE)
                .setPriority(Priority.NORMAL)
                .setMessage("Hello I am a basic issue")
                .setFileName("basic.txt")
                .setCategory("basic")
                .setType("basic")
                .setColumnStart(-1)
                .setColumnEnd(1)
                .setPackageName("basic")
                .setDescription("basic issue")
                .build();


        IssueSoftAssertion.assertIssueSoftly(softly -> {
            softly.assertThat(basicIssue)
                    .hasLineStart(1)
                    .hasLineEnd(Integer.MAX_VALUE)
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("Hello I am a basic issue")
                    .hasCategory("basic")
                    .hasFileName("basic.txt")
                    .hasType("basic")
                    .hasColumnStart(0)
                    .hasColumnEnd(1)
                    .hasPackageName("basic")
                    .hasDescription("basic issue")
                    .hasToString(String.format("%s(%d,%d): %s: %s: %s", "basic.txt", 1, 0, "basic", "basic", "Hello I am a basic issue"));
        });
    }

    private IssueBuilder getTestIssue() {
        IssueBuilder builder = new IssueBuilder();
        return builder
                .setLineStart(1)
                .setLineEnd(8)
                .setPriority(Priority.NORMAL)
                .setMessage("Hello I am a issue")
                .setFileName("/root/issues/issue.log")
                .setCategory("standard")
                .setType("warning")
                .setColumnStart(4)
                .setColumnEnd(6)
                .setPackageName("edu.hm.hafner")
                .setDescription("no big problems");
    }
}