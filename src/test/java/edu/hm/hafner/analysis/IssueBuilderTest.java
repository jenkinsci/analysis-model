package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;



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

        IssueSoftAssertion softy = new IssueSoftAssertion();
        softy.assertThat(basicIssue)
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
                .hasDescription("basic issue");

        softy.assertAll();



    }

}