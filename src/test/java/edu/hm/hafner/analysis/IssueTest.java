package edu.hm.hafner.analysis;


import org.junit.jupiter.api.Test;


class IssueTest {

    @Test void issue1(){
        Issue i = new IssueBuilder().setFileName("test1").build();
        IssueAssert.assertThat(i).hasFileName("test1");

    }


}