package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;


/**
 * Tests the class {@link Issue} by building it with {@link IssueBuilder}.
 */
class IssueTest {

    /**
     * Verifies correctly build issue with default values.
     */
    @Test
    void testDefaultValues() {
        IssueBuilder builder = new IssueBuilder();
        Issue issue = builder.build();
        Issue issueToCompare = builder.build();
        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        softly.assertThat(issue).as("Has default lineStart").hasLineStart(0);
        softly.assertThat(issue).as("Has default lineEnd").hasLineEnd(0);
        softly.assertThat(issue).as("Has default message").hasMessage("");
        softly.assertThat(issue).as("Has default filename").hasFileName("-");
        softly.assertThat(issue).as("Has default category").hasCategory("");
        softly.assertThat(issue).as("Has default priority").hasPriority(Priority.NORMAL);
        softly.assertThat(issue).as("Has default columnStart").hasColumnStart(0);
        softly.assertThat(issue).as("Has default columnEnd").hasColumnEnd(0);
        softly.assertThat(issue).as("Has default description").hasDescription("");
        softly.assertThat(issue).as("Has default type").hasType("-");
        softly.assertThat(issue)
                .as("Default issue is equal to other default issue")
                .isEqualTo(issueToCompare);
        softly.assertThat(issue)
                .as("Copied issue is equal")
                .isEqualTo(builder.copy(issue).build());
        softly.assertThat(issue.toString())
                .as("Has default string representation")
                .isEqualTo("-(0,0): -: : ");
        softly.assertAll();
    }

    /**
     * Verifies correctly build issue with custom values.
     */
    @Test
    void testCustomValues() {
        Issue issue = new IssueBuilder()
                .setLineStart(1)
                .setLineEnd(2)
                .setMessage(" message  ")
                .setFileName("\\foldername/filename")
                .setCategory("category")
                .setPriority(Priority.LOW)
                .setColumnStart(1)
                .setColumnEnd(2)
                .setDescription("\ndescription with spaces  \t")
                .setPackageName("packageName")
                .setType("type")
                .build();
        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        softly.assertThat(issue).hasLineStart(1);
        softly.assertThat(issue).hasLineEnd(2);
        softly.assertThat(issue).hasMessage("message");
        softly.assertThat(issue).hasFileName("/foldername/filename");
        softly.assertThat(issue).hasCategory("category");
        softly.assertThat(issue).hasPriority(Priority.LOW);
        softly.assertThat(issue).hasColumnStart(1);
        softly.assertThat(issue).hasColumnEnd(2);
        softly.assertThat(issue).hasDescription("description with spaces");
        softly.assertThat(issue).hasPackageName("packageName");
        softly.assertThat(issue).hasType("type");
        softly.assertAll();

    }

}