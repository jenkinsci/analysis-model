package edu.hm.hafner.analysis;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.IssueAssert.*;

/**
 * Tests the class {@link IssueBuilder}.
 *
 * @author Joscha Behrmann
 */
class IssueBuilderTest {

    @Test
    void setFileName() {
        // Normal case
        Issue issueUnderTest = new IssueBuilder()
                .setFileName("test.xml")
                .build();

        assertThat(issueUnderTest)
                .hasFileName("test.xml");
    }

    @Test
    void setFileNameEmpty() {
        // Empty string, expected Issue.UNDEFINED
        Issue issueUnderTest = new IssueBuilder()
                .setFileName("")
                .build();

        assertThat(issueUnderTest)
                .hasFileName("-");
    }

    @Test
    void setFileNameLeadingTrailingWhitespace() {
        // Leading/Trailing whitespace
        Issue issueUnderTest = new IssueBuilder()
                .setFileName("   test.xml   ")
                .build();

        assertThat(issueUnderTest)
                .hasFileName("test.xml");
    }

    @Test
    void setFileNameReplacePathSeperators() {
        // Replace '\\'-Characters with '/'
        Issue issueUnderTest = new IssueBuilder()
                .setFileName("C:\\User\\test.xml")
                .build();

        assertThat(issueUnderTest)
                .hasFileName("C:/User/test.xml");
    }

    @Test
    void setFileNameCombination() {
        // Combination of error-cases
        Issue issueUnderTest = new IssueBuilder()
                .setFileName("   C:\\User/test.xml")
                .build();

        assertThat(issueUnderTest)
                .hasFileName("C:/User/test.xml");
    }

    @Test
    void setLineStart() {
        // Normal case
        Issue issueUnderTest = new IssueBuilder()
                .setLineStart(5)
                .build();

        assertThat(issueUnderTest)
                .hasLineStart(5);
    }

    @Test
    void setLineStartToZero() {
        // Zero should be zero
        Issue issueUnderTest = new IssueBuilder()
                .setLineStart(0)
                .build();

        assertThat(issueUnderTest)
                .hasLineStart(0);
    }

    @Test
    void setLineStartMinValue() {
        // Negative values should default to zero
        Issue issueUnderTest = new IssueBuilder()
                .setLineStart(Integer.MIN_VALUE)
                .build();

        assertThat(issueUnderTest)
                .hasLineStart(0);
    }

    @Test
    void setLineStartMaxValue() {
        // Maximum value should be a normal case
        Issue issueUnderTest = new IssueBuilder()
                .setLineStart(Integer.MAX_VALUE)
                .build();

        assertThat(issueUnderTest)
                .hasLineStart(Integer.MAX_VALUE);
    }

    @Test
    void setLineEnd() {
        // Normal case
        Issue issueUnderTest = new IssueBuilder()
                .setLineEnd(15)
                .build();

        assertThat(issueUnderTest)
                .hasLineEnd(15);
    }

    @Test
    void setLineEndToZero() {
        // Normal case, should default to lineStart
        Issue issueUnderTest = new IssueBuilder()
                .setLineStart(15)
                .setLineEnd(0)
                .build();

        assertThat(issueUnderTest)
                .hasLineEnd(15);
    }

    @Test
    void setLineEndToMinValue() {
        // Negative values should default to zero
        Issue issueUnderTest = new IssueBuilder()
                .setLineEnd(Integer.MIN_VALUE)
                .build();

        assertThat(issueUnderTest)
                .hasLineEnd(0);
    }

    @Test
    void setLineEndToMaxValue() {
        // MAX_VALUE is a normal case
        Issue issueUnderTest = new IssueBuilder()
                .setLineEnd(Integer.MAX_VALUE)
                .build();

        assertThat(issueUnderTest)
                .hasLineEnd(Integer.MAX_VALUE);
    }

    @Test
    void lineEndCantBeSmallerThanLineStart() {
        // If lineStart < lineEnd, lineEnd = lineStart
        Issue issueUnderTest = new IssueBuilder()
                .setLineStart(5)
                .setLineEnd(10)
                .build();

        assertThat(issueUnderTest)
                .hasLineEnd(10);
    }

    @Test
    void setColumnStart() {
        // Normal case
        Issue issueUnderTest = new IssueBuilder()
                .setColumnStart(15)
                .build();

        assertThat(issueUnderTest)
                .hasColumnStart(15);
    }

    @Test
    void setColumnStartToZero() {
        // Normal case
        Issue issueUnderTest = new IssueBuilder()
                .setColumnStart(0)
                .build();

        assertThat(issueUnderTest)
                .hasColumnStart(0);
    }

    @Test
    void setColumnStartMinValue() {
        // Normal case
        Issue issueUnderTest = new IssueBuilder()
                .setColumnStart(Integer.MIN_VALUE)
                .build();

        assertThat(issueUnderTest)
                .hasColumnStart(0);
    }

    @Test
    void setColumnStartMaxValue() {
        // Normal case
        Issue issueUnderTest = new IssueBuilder()
                .setColumnStart(Integer.MAX_VALUE)
                .build();

        assertThat(issueUnderTest)
                .hasColumnStart(Integer.MAX_VALUE);
    }

    @Test
    void setColumnEnd() {
        // Normal case
        Issue issueUnderTest = new IssueBuilder()
                .setColumnEnd(15)
                .build();

        assertThat(issueUnderTest)
                .hasColumnEnd(15);
    }

    @Test
    void setColumnEndToZero() {
        // Normal case, should default to columnStart
        Issue issueUnderTest = new IssueBuilder()
                .setColumnStart(15)
                .setColumnEnd(0)
                .build();

        assertThat(issueUnderTest)
                .hasColumnEnd(15);
    }

    @Test
    void setColumnEndToMinValue() {
        // Negative values should default to zero
        Issue issueUnderTest = new IssueBuilder()
                .setColumnEnd(Integer.MIN_VALUE)
                .build();

        assertThat(issueUnderTest)
                .hasColumnEnd(0);
    }

    @Test
    void setColumnEndToMaxValue() {
        // Integer.MAX_VALUE is a normal case
        Issue issueUnderTest = new IssueBuilder()
                .setColumnEnd(Integer.MAX_VALUE)
                .build();

        assertThat(issueUnderTest)
                .hasColumnEnd(Integer.MAX_VALUE);
    }


    @Test
    void setCategory() {
        // Normal case
        Issue issueUnderTest = new IssueBuilder()
                .setCategory("Testcategory")
                .build();

        assertThat(issueUnderTest)
                .hasCategory("Testcategory");

        // Normal case
        issueUnderTest = new IssueBuilder()
                .setCategory("   C:\\User/test.xml")
                .build();

        assertThat(issueUnderTest)
                .hasCategory("   C:\\User/test.xml");
    }

    @Test
    void setCategoryToNull() {
        // Normal case
        Issue issueUnderTest = new IssueBuilder()
                .setCategory(null)
                .build();

        assertThat(issueUnderTest)
                .hasCategory(StringUtils.EMPTY);
    }

    @Test
    void setCategoryToWhitespace() {
        // Whitespace is a normal case
        Issue issueUnderTest = new IssueBuilder()
                .setCategory("   ")
                .build();

        assertThat(issueUnderTest)
                .hasCategory("   ");
    }

    @Test
    void setType() {
        // Normal case
        Issue issueUnderTest = new IssueBuilder()
                .setType("test-type")
                .build();

        assertThat(issueUnderTest)
                .hasType("test-type");
    }

    @Test
    void setTypeToWhitespace() {
        // Whitespace is a normal case
        Issue issueUnderTest = new IssueBuilder()
                .setType("   ")
                .build();

        assertThat(issueUnderTest)
                .hasType("   ");
    }

    @Test
    void setTypeToEmptyString() {
        // Whitespace should default to Issue.UNDEFINED
        Issue issueUnderTest = new IssueBuilder()
                .setType("")
                .build();

        assertThat(issueUnderTest)
                .hasType("-");
    }

    @Test
    void setPackageName() {
        // Normal case
        Issue issueUnderTest = new IssueBuilder()
                .setPackageName("test-package")
                .build();

        assertThat(issueUnderTest)
                .hasPackageName("test-package");
    }

    @Test
    void setPackageNameToEmptyString() {
        // Empty string should default to Issue.UNDEFINED
        Issue issueUnderTest = new IssueBuilder()
                .setPackageName("")
                .build();

        assertThat(issueUnderTest)
                .hasPackageName("-");
    }

    @Test
    void setPackageNameToWhitespace() {
        // Whitespace is a normal case
        Issue issueUnderTest = new IssueBuilder()
                .setPackageName("   ")
                .build();

        assertThat(issueUnderTest)
                .hasPackageName("   ");
    }

    @Test
    void setPriority() {
        // Normal case
        Issue issueUnderTest = new IssueBuilder()
                .setPriority(Priority.NORMAL)
                .build();

        assertThat(issueUnderTest)
                .hasPriority(Priority.NORMAL);
    }

    @Test
    void setPriorityToNull() {
        // Normal case
        Issue issueUnderTest = new IssueBuilder()
                .setPriority(null)
                .build();

        assertThat(issueUnderTest)
                .hasPriority(Priority.NORMAL);
    }

    @Test
    void setMessage() {
        // Normal case
        Issue issueUnderTest = new IssueBuilder()
                .setMessage("This is a test-message.")
                .build();

        assertThat(issueUnderTest)
                .hasMessage("This is a test-message.");
    }

    @Test
    void setMessageWithLeadingTrailingWhitespace() {
        // Whitespace should be removed
        Issue issueUnderTest = new IssueBuilder()
                .setMessage("   This is a test-message.   ")
                .build();

        assertThat(issueUnderTest)
                .hasMessage("This is a test-message.");
    }

    @Test
    void setMessageToNull() {
        // Null should default to empty string
        Issue issueUnderTest = new IssueBuilder()
                .setMessage(null)
                .build();

        assertThat(issueUnderTest)
                .hasMessage("");
    }

    @Test
    void setMessageToWhitespace() {
        // Whitespace should be removed
        Issue issueUnderTest = new IssueBuilder()
                .setMessage("   ")
                .build();

        assertThat(issueUnderTest)
                .hasMessage("");
    }

    @Test
    void setDescription() {
        // Normal case
        Issue issueUnderTest = new IssueBuilder()
                .setDescription("This is a test-description.")
                .build();

        assertThat(issueUnderTest)
                .hasDescription("This is a test-description.");
    }

    @Test
    void setDescriptionWithLeadingTrailingWhitespace() {
        // Whitespace should be removed
        Issue issueUnderTest = new IssueBuilder()
                .setDescription("   This is a test-description.   ")
                .build();

        assertThat(issueUnderTest)
                .hasDescription("This is a test-description.");
    }

    @Test
    void setDescriptionToNull() {
        // Null should default to empty string
        Issue issueUnderTest = new IssueBuilder()
                .setDescription(null)
                .build();

        assertThat(issueUnderTest)
                .hasDescription("");
    }

    @Test
    void setDescriptionToWhitespace() {
        // Whitespace should be removed
        Issue issueUnderTest = new IssueBuilder()
                .setDescription("   ")
                .build();

        assertThat(issueUnderTest)
                .hasDescription("");
    }

    @Test
    void copy() {
        // Normal case
        Issue issueUnderTest = new IssueBuilder()
                .setFileName("testFile.xml")
                .setLineStart(10)
                .setLineEnd(15)
                .setColumnStart(10)
                .setColumnEnd(15)
                .setCategory("testCategory")
                .setType("testType")
                .setPackageName("testPackage")
                .setPriority(Priority.NORMAL)
                .setMessage("this is a test-message.")
                .setDescription("this is a test-description.")
                .build();

        Issue copyOfIssueUnderTest = new IssueBuilder().copy(issueUnderTest).build();
        assertThat(issueUnderTest)
                .as("Original issue and copy issue should be equal")
                .isEqualTo(copyOfIssueUnderTest);
    }

}