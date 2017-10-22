package edu.hm.hafner.analysis;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the class {@link Issue}.
 */
class IssueTest {

    private final Issue issueUnderTest = new Issue("fileName",
            0, 10, 0, 10,
            "category", "type", "packageName",
            Priority.NORMAL,
            "message", "description");

    @Test
    void testAllPropertyGetters() {
        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(issueUnderTest.getFileName())
                    .as("fileNames should match")
                    .isEqualTo("fileName");

            softly.assertThat(issueUnderTest.getLineStart())
                    .as("lineStart should match")
                    .isEqualTo(0);

            softly.assertThat(issueUnderTest.getLineEnd())
                    .as("lineEnd should match")
                    .isEqualTo(10);

            softly.assertThat(issueUnderTest.getColumnStart())
                    .as("columnStart should match")
                    .isEqualTo(0);

            softly.assertThat(issueUnderTest.getColumnEnd())
                    .as("columnEnd should match")
                    .isEqualTo(10);

            softly.assertThat(issueUnderTest.getCategory())
                    .as("category should match")
                    .isEqualTo("category");

            softly.assertThat(issueUnderTest.getType())
                    .as("type should match")
                    .isEqualTo("type");

            softly.assertThat(issueUnderTest.getPackageName())
                    .as("packageName should match")
                    .isEqualTo("packageName");

            softly.assertThat(issueUnderTest.getPriority())
                    .as("priority should match")
                    .isEqualTo(Priority.NORMAL);

            softly.assertThat(issueUnderTest.getMessage())
                    .as("message should match")
                    .isEqualTo("message");

            softly.assertThat(issueUnderTest.getDescription())
                    .as("description should match")
                    .isEqualTo("description");
        });
    }

    @Test
    void testFingerprint() {
        SoftAssertions.assertSoftly((softly) -> {
            // Normal case
            issueUnderTest.setFingerprint("fingerPrint");
            softly.assertThat(issueUnderTest.getFingerprint())
                    .as("fingerPrint should be \"fingerPrint\"")
                    .isEqualTo("fingerPrint");

            // Leading/Trailing whitespace should be untouched
            issueUnderTest.setFingerprint("   fingerPrint   ");
            softly.assertThat(issueUnderTest.getFingerprint())
                    .as("fingerPrint should be \"   fingerPrint   \"")
                    .isEqualTo("   fingerPrint   ");

            // Whitespace is a normal case
            issueUnderTest.setFingerprint("   ");
            softly.assertThat(issueUnderTest.getFingerprint())
                    .as("fingerPrint should be \"   \"")
                    .isEqualTo("   ");

            // null should default to Issue.UNDEFINED
            issueUnderTest.setFingerprint(null);
            softly.assertThat(issueUnderTest.getFingerprint())
                    .as("fingerPrint should be \"   \"")
                    .isEqualTo("-");
        });
    }

    @Test
    void testToString() {
        assertThat(issueUnderTest.toString())
                .as("Strings should match")
                .isEqualTo(String.format("%s(%d,%d): %s: %s: %s",
                        issueUnderTest.getFileName(),
                        issueUnderTest.getLineStart(),
                        issueUnderTest.getColumnStart(),
                        issueUnderTest.getType(),
                        issueUnderTest.getCategory(),
                        issueUnderTest.getMessage()));
    }
}