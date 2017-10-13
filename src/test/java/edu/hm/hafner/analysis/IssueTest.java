package edu.hm.hafner.analysis;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
class IssueTest {

    @Test void issue1(){
        Issue i = new IssueBuilder().setFileName("test1").build();
        IssueAssert.assertThat(i).hasFileName("test1");

    }


}