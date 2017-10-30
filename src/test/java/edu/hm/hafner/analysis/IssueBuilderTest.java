package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * Test cases for Issue abd IssueBuilder.
 *
 * @author Sebastian Balz
 */
class IssueBuilderTest {
    /**
     * use the default Builder Method to create a new Issue.
     */
    @Test
    void newIssueWithDefaultValues() {
        Issue i = new IssueBuilder().build();
        IssueSoftAssert soft = new IssueSoftAssert();
        soft.assertThat(i).hasFileName("-").hasCategory("").hasType("-").hasPriority(Priority.NORMAL).hasMessage("").
                hasDescription("").hasPackageName("-").hasLineStart(0).hasLineEnd(0).hasColumnStart(0).hasColumnEnd(0)
                .hasOrdinal(Priority.NORMAL.ordinal());
        soft.assertAll();
    }

    /**
     * use the Builder wth custom values to create a new Issue.
     */
    @Test
    void newIssueWithCustomtValues() {
        Issue i = new IssueBuilder().setFileName("1").setCategory("2").setType("3").setPriority(Priority.LOW).setMessage("4")
                .setDescription("5").setPackageName("6").setLineStart(1).setLineEnd(7).setColumnStart(3).setColumnEnd(4).build();
        IssueSoftAssert soft = new IssueSoftAssert();

        soft.assertThat(i).hasFileName("1").hasCategory("2").hasType("3").hasPriority(Priority.LOW).hasMessage("4").
                hasDescription("5").hasPackageName("6").hasLineStart(1).hasLineEnd(7).hasColumnStart(3).hasColumnEnd(4)
                .hasOrdinal(Priority.LOW.ordinal());
        soft.assertAll();
    }

    /**
     * Test toString Method.
     */
    @Test
    void checkToString() {
        Issue i = new IssueBuilder().setFileName("a").setCategory("b").setType("c").setPriority(Priority.LOW).setMessage("d")
                .setDescription("e").setPackageName("f").setLineStart(1).setLineEnd(7).setColumnStart(3).setColumnEnd(4).build();
        assertThat(i.toString()).isEqualTo("a(1,3): c: b: d");
    }

    /**
     * copy a Issue and compare it to its origin.
     */
    @Test
    void copyIssueAndCompareUnchangedToOrigin() {
        Issue i = new IssueBuilder().setFileName("1").setCategory("2").setType("3").setPriority(Priority.LOW).setMessage("4")
                .setDescription("5").setPackageName("6").setLineStart(1).setLineEnd(7).setColumnStart(3).setColumnEnd(4).build();

        Issue coppy = new IssueBuilder().copy(i).build();
        IssueAssert.assertThat(coppy).isEqualTo(i);
        Issue cp2 = new IssueBuilder().copy(i).setFileName("XXX").build();
        IssueAssert.assertThat(i).hasFileName("1");
        IssueAssert.assertThat(cp2).hasFileName("XXX");
    }

    /**
     * Copy Method and change one Param.
     */
    @Test
    void copyIssueAndChangeBeforComapare() {
        Issue i = new IssueBuilder().setFileName("1").setCategory("2").setType("3").setPriority(Priority.LOW).setMessage("4")
                .setDescription("5").setPackageName("6").setLineStart(1).setLineEnd(7).setColumnStart(3).setColumnEnd(4).build();

        Issue coppy = new IssueBuilder().copy(i).setMessage("otherMessage").build();
        IssueAssert.assertThat(coppy).isNotEqualTo(i);
    }
}