package edu.hm.hafner;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class IssueBuilderTest {
    /**
     * Test default Builder Method.
     */
    @Test
    void defaultBuilder() {
        Issue i = new IssueBuilder().build();
        IssueSoftAssert soft = new IssueSoftAssert();
        soft.assertThat(i).hasFileName("-").hasCategory("").hasType("-").hasPriority(Priority.NORMAL).hasMessage("").
                hasDescription("").hasPackageName("-").haslineStart(0).hasLineEnd(0).hasColumnStart(0).hasColumnEnd(0);
        soft.assertAll();
    }

    /**
     * Test customBuilder Method.
     */
    @Test
    void customtBuilder() {
        Issue i = new IssueBuilder().setFileName("1").setCategory("2").setType("3").setPriority(Priority.LOW).setMessage("4")
                .setDescription("5").setPackageName("6").setLineStart(1).setLineEnd(7).setColumnStart(3).setColumnEnd(4).build();
        IssueSoftAssert soft = new IssueSoftAssert();

        soft.assertThat(i).hasFileName("1").hasCategory("2").hasType("3").hasPriority(Priority.LOW).hasMessage("4").
                hasDescription("5").hasPackageName("6").haslineStart(1).hasLineEnd(7).hasColumnStart(3).hasColumnEnd(4);
        soft.assertAll();
    }

    /**
     * Test toString Method.
     */
    @Test
    void checkToString() {
        Issue i = new IssueBuilder().setFileName("1").setCategory("2").setType("3").setPriority(Priority.LOW).setMessage("4")
                .setDescription("5").setPackageName("6").setLineStart(1).setLineEnd(7).setColumnStart(3).setColumnEnd(4).build();
        assertThat(i.toString()).isEqualTo("1(1,3): 3: 2: 4");
    }

    /**
     * Test copy Method.
     */
    @Test
    void CopyIssue() {
        Issue i = new IssueBuilder().setFileName("1").setCategory("2").setType("3").setPriority(Priority.LOW).setMessage("4")
                .setDescription("5").setPackageName("6").setLineStart(1).setLineEnd(7).setColumnStart(3).setColumnEnd(4).build();

        Issue coppy = new IssueBuilder().copy(i).build();
        IssueAssert.assertThat(coppy).isEqualTo(i);
    }

    /**
     * Test copy Method and change one.
     */
    @Test
    void CopyIssueAndChange() {
        Issue i = new IssueBuilder().setFileName("1").setCategory("2").setType("3").setPriority(Priority.LOW).setMessage("4")
                .setDescription("5").setPackageName("6").setLineStart(1).setLineEnd(7).setColumnStart(3).setColumnEnd(4).build();

        Issue coppy = new IssueBuilder().copy(i).setMessage("otherMessage").build();
        IssueAssert.assertThat(coppy).isNotEqualTo(i);
    }
}