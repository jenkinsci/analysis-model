package edu.hm.hafner.analysis;


import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.IssuesAssert.*;


class SampleTests {

    @Test
    void SampleTest(){

        IssueBuilder builder = new IssueBuilder();
        builder.setMessage("Hello again!");
        builder.setLineStart(123);
        Issue testissue = builder.build();

        MySoftAssertions softly = new MySoftAssertions();
        softly.assertThat(testissue)
                .hasMessage("Hello again!")
                .hasLineStart(123);

        softly.assertAll();






    }
    @Test
    void secondSample(){

        Issues list = new Issues();
        IssueBuilder builder = new IssueBuilder();
        builder.setMessage("Hello again!");
        builder.setLineStart(123);
        Issue firstIssue = builder.build();
        builder.setLineStart(200);
        Issue secondIssue = builder.build();

        list.add(firstIssue);
        list.add(secondIssue);

        assertThat(list).hasIssueonIndex(firstIssue,0);


    }

}