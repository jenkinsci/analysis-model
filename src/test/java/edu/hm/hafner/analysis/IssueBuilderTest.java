package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.assertj.AnalysisSoftAssertions;
import static edu.hm.hafner.analysis.assertj.IssueAssert.*;

/**
 * Tests the class {@link IssueBuilder}.
 *
 * @author Andreas Moser
 */
class IssueBuilderTest {

    /** Verifies IssueBuilder build correct issues. */
    @Test
    void testIssueBuilderBuildsCorrectIssue() {
        String fileName = "testFile";
        int lineStart = 2;
        int lineEnd = 10;
        int columnStart = 3;
        int columnEnd = 5;
        String category = "testCategory";
        String type = "testType";
        String packageName = "testPackage";
        Priority priority = Priority.HIGH;
        String message = "testMessage";
        String description = "testDescription";
        IssueBuilder builder = new IssueBuilder().setFileName(fileName)
                .setLineStart(lineStart)
                .setLineEnd(lineEnd)
                .setColumnStart(columnStart)
                .setColumnEnd(columnEnd)
                .setCategory(category)
                .setType(type)
                .setPackageName(packageName)
                .setPriority(priority)
                .setMessage(message)
                .setDescription(description);

        Issue issue = builder.build();


        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        softly.assertThat(issue).as("Issue created by IssueBuilder").hasFileName(fileName)
                .hasLineStart(lineStart)
                .hasLineEnd(lineEnd)
                .hasColumnStart(columnStart)
                .hasColumnEnd(columnEnd)
                .hasCategory(category)
                .hasType(type)
                .hasPackageName(packageName)
                .hasPriority(priority)
                .hasMessage(message)
                .hasDescription(description);
        softly.assertAll();
    }

    /** Verifies IssueBuilder can copy issues. */
    @Test
    void testCopyMethodCopiesValuesCorrect() {
        String fileName = "testFile";
        int lineStart = 4;
        int lineEnd = 16;
        int columnStart = 2;
        int columnEnd = 7;
        String category = "testCategory";
        String type = "testType";
        String packageName = "testPackage";
        Priority priority = Priority.HIGH;
        String message = "testMessage";
        String description = "testDescription";
        IssueBuilder builder = new IssueBuilder().setFileName(fileName)
                .setLineStart(lineStart)
                .setLineEnd(lineEnd)
                .setColumnStart(columnStart)
                .setColumnEnd(columnEnd)
                .setCategory(category)
                .setType(type)
                .setPackageName(packageName)
                .setPriority(priority)
                .setMessage(message)
                .setDescription(description);
        Issue buildedIssue = builder.build();

        IssueBuilder copiedBuilder = builder.copy(buildedIssue);
        Issue issueOfCopiedBuilder = copiedBuilder.build();

        assertThat(issueOfCopiedBuilder).as("Issue of copied builder").isEqualTo(buildedIssue);
    }

}