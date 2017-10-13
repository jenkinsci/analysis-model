package edu.hm.hafner.analysis;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IssueBuilderTest {
    @Test
    void defaultBuilder() {
        Issue i = new IssueBuilder().build();
        // IssueSoftAssert soft = new IssueSoftAssert();

        IssueAssert.assertThat(i).hasFileName("-").hasCategory("").hasType("-").hasPriority(Priority.NORMAL).hasMessage("").
                hasDescription("").hasPackageName("-").haslineStart(0).hasLineEnd(0).hasColumnStart(0).hasColumnEnd(0);
        // soft.assertAll();
    }

    @Test
    void customtBuilder() {
        Issue i = new IssueBuilder().setFileName("1").setCategory("2").setType("3").setPriority(Priority.LOW).setMessage("4")
                .setDescription("5").setPackageName("6").setLineStart(1).setLineEnd(7).setColumnStart(3).setColumnEnd(4).build();
        //  IssueSoftAssert soft = new IssueSoftAssert();

        IssueAssert.assertThat(i).hasFileName("1").hasCategory("2").hasType("3").hasPriority(Priority.LOW).hasMessage("4").
                hasDescription("5").hasPackageName("6").haslineStart(1).hasLineEnd(7).hasColumnStart(3).hasColumnEnd(4);
        // soft.assertAll();
    }
}