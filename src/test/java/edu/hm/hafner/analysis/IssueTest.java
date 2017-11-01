package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link Issue}.
 *
 * @author Joscha Behrmann
 */
@SuppressWarnings({"JUnitTestMethodWithNoAssertions", "ConstantConditions"})
class IssueTest {

    /** Instance of Issue which is tested. */
    private final Issue issueUnderTest = new Issue("fileName",
            0, 10, 20, 30,
            "category", "type", "packageName",
            Priority.NORMAL,
            "message", "description");

    /** All getter-methods should return the correct value. */
    @Test
    void testAllPropertyGetters() {
        IssueSoftAssert soft = new IssueSoftAssert();
        soft.assertThat(issueUnderTest).hasFileName("fileName");
        soft.assertThat(issueUnderTest).hasLineStart(0);
        soft.assertThat(issueUnderTest).hasLineEnd(10);
        soft.assertThat(issueUnderTest).hasColumnStart(20);
        soft.assertThat(issueUnderTest).hasColumnEnd(30);
        soft.assertThat(issueUnderTest).hasCategory("category");
        soft.assertThat(issueUnderTest).hasType("type");
        soft.assertThat(issueUnderTest).hasPackageName("packageName");
        soft.assertThat(issueUnderTest).hasPriority(Priority.NORMAL);
        soft.assertThat(issueUnderTest).hasMessage("message");
        soft.assertThat(issueUnderTest).hasDescription("description");
        soft.assertAll();
    }

    /** setFingerprint() and getFingerprint() should work accordingly. */
    @Test
    void testFingerprint() {
        IssueSoftAssert soft = new IssueSoftAssert();

        // Normal case
        issueUnderTest.setFingerprint("fingerPrint");
        soft.assertThat(issueUnderTest).hasFingerprint("fingerPrint");

        // Leading/Trailing whitespace should be untouched
        issueUnderTest.setFingerprint("   fingerPrint   ");
        soft.assertThat(issueUnderTest).hasFingerprint("   fingerPrint   ");

        // Whitespace is a normal case
        issueUnderTest.setFingerprint("   ");
        soft.assertThat(issueUnderTest).hasFingerprint("   ");

        // null should default to Issue.UNDEFINED
        issueUnderTest.setFingerprint(null);
        soft.assertThat(issueUnderTest).hasFingerprint("-");

        soft.assertAll();
    }

    /** toString() should return a nicely formatted string. */
    @Test
    void testToString() {
        assertThat(issueUnderTest.toString())
                .as("Strings should match")
                .contains(String.format("%s(%d,%d): %s: %s: %s",
                        issueUnderTest.getFileName(),
                        issueUnderTest.getLineStart(),
                        issueUnderTest.getColumnStart(),
                        issueUnderTest.getType(),
                        issueUnderTest.getCategory(),
                        issueUnderTest.getMessage()));
    }
}