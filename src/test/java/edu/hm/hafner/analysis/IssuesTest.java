package edu.hm.hafner.analysis;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

class IssuesTest {

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

}