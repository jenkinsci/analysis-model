package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Assertions.IssueSoftAssertion;
import static edu.hm.hafner.analysis.Assertions.IssueAssert.*;


class IssueBuilderTest {


    @Test
    void createBasicIssueTest(){
        IssueBuilder builder = new IssueBuilder();
        Issue basicIssue = builder
                .setLineStart(1)
                .setLineEnd(1)
                .setPriority(Priority.NORMAL)
                .setMessage("Hello I am a basic issue")
                .setFileName("basic.txt")
                .setCategory("basic")
                .setType("basic")
                .setColumnStart(1)
                .setColumnEnd(1)
                .setPackageName("basic")
                .setDescription("basic issue")
                .build();


     IssueSoftAssertion.assertIssueSoftly((softly) ->{
         softly.assertThat(basicIssue)
                 .hasLineStart(1)
                 .hasLineEnd(1)
                 .hasPriority(Priority.NORMAL)
                 .hasMessage("Hello I am a basic issue")
                 .hasCategory("basic")
                 .hasFileName("basic.txt")
                 .hasType("basic")
                 .hasColumnStart(1)
                 .hasColumnEnd(1)
                 .hasPackageName("basic")
                 .hasDescription("basic issue")
                 .hasToString(String.format("%s(%d,%d): %s: %s: %s", "basic.txt", 1, 1, "basic", "basic", "Hello I am a basic issue"));
     } );

    }

    @Test
    void createEmptyIssueTest(){
        IssueBuilder builder = new IssueBuilder();
        Issue basicIssue = builder
                .build();

        IssueSoftAssertion.assertIssueSoftly((softly) -> {
            softly.assertThat(basicIssue)
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
    void CopyEmptyIssueTest(){
        IssueBuilder builder = new IssueBuilder();
        Issue basicIssue = builder
                .build();
        IssueBuilder copiedBuilder =builder.copy(basicIssue);
        Issue copy = copiedBuilder.build();
        assertThat(copy).isEqualTo(basicIssue);

    }

    @Test
    void CopyBasicIssueTest(){
        IssueBuilder builder = new IssueBuilder();
        Issue basicIssue = builder
                .setLineStart(1)
                .setLineEnd(1)
                .setPriority(Priority.NORMAL)
                .setMessage("Hello I am a basic issue")
                .setFileName("basic.txt")
                .setCategory("basic")
                .setType("basic")
                .setColumnStart(1)
                .setColumnEnd(1)
                .setPackageName("basic")
                .setDescription("basic issue")
                .build();

        IssueBuilder copiedBuilder = builder.copy(basicIssue);
        Issue copy = copiedBuilder.build();
        assertThat(copy).isEqualTo(basicIssue);
    }

}