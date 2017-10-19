package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.assertj.IssueAssert.assertThat;

import static org.junit.jupiter.api.Assertions.*;

class IssueBuilderTest {

    @Test
    void testIssueBuilderBuildsCorrectIssue(){
        final String fileName = "testFile";
        final int lineStart = 2;
        final int lineEnd = 10;
        final int columnStart = 3;
        final int columnEnd = 5;
        final String category = "testCategory";
        final String type = "testType";
        final String packageName = "testPackage";
        final Priority priority = Priority.HIGH;
        final String message = "testMessage";
        final String description = "testDescription";
        final IssueBuilder builder = new IssueBuilder().setFileName(fileName)
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

        final Issue issue = builder.build();

        assertThat(issue).as("Issue created by IssueBuilder").hasFileName(fileName)
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
    }

    @Test
    @Disabled
    //TODO testCopyMethodCopiesValuesCorrect is not working
    void testCopyMethodCopiesValuesCorrect(){
        final String fileName = "testFile";
        final int lineStart = 4;
        final int lineEnd = 16;
        final int columnStart = 2;
        final int columnEnd = 7;
        final String category = "testCategory";
        final String type = "testType";
        final String packageName = "testPackage";
        final Priority priority = Priority.HIGH;
        final String message = "testMessage";
        final String description = "testDescription";
        final IssueBuilder builder = new IssueBuilder().setFileName(fileName)
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
        final Issue buildedIssue = builder.build();

        final IssueBuilder copiedBuilder = builder.copy(buildedIssue);
        final Issue issueOfCopiedBuilder = copiedBuilder.build();

        assertThat(issueOfCopiedBuilder).as("Issue of copied builder").isEqualTo(buildedIssue);
    }

}