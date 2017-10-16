package edu.hm.balz;


import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import org.junit.jupiter.api.Test;


class IssueTest {

    @Test void issue1(){
        Issue i = new IssueBuilder().setFileName("test1").build();
        IssueAssert.assertThat(i).hasFileName("test1");

    }


}