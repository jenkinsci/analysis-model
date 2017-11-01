package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;
import edu.hm.hafner.analysis.assertions.IssueSoftAssertions;


/**
 * Tests for {@link Issue}
 * @author Raphael Furch
 */
public class IssueTest {
    private static final String UNDEFINED = "-";


    /**
     * Check if Constructor sets all values and use default values if input is bad.
     */
    @SuppressWarnings("ConstantConditions")
    @Test
    public void convertedToDefaultValue(){
        // Create a object which forces all default values
        Issue issue = new IssueBuilder()
                .setFileName("")
                .setLineStart(-1)
                .setLineEnd(-1)
                .setColumnStart(-1)
                .setColumnEnd(-1)
                .setCategory(null)
                .setType("")
                .setPackageName("")
                .setPriority(null)
                .setMessage(null)
                .setDescription(null)
                .build();

        // Check
       IssueSoftAssertions softly = new IssueSoftAssertions();
                softly.assertThat(issue)
                .hasFileName(UNDEFINED)
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasColumnStart(0)
                .hasColumnEnd(0)
                .hasCategory("")
                .hasType(UNDEFINED)
                .hasPackagename(UNDEFINED)
                .hasPriority(Priority.NORMAL)
                .hasMessage("")
                .hasDescription("")
                        .hasId();
                softly.assertAll();
    }

    /**
     * Check if path separator in file is replaced correctly. No \ only /.
     */
    @Test
    public void replacePathSeparator(){
        // Create issue with windows path separator
        Issue issue = new IssueBuilder()
                .setFileName("c:\\test\\")
                .build();

        // Check
        IssueSoftAssertions softly = new IssueSoftAssertions();
        softly.assertThat(issue)
                .hasFileName("c:/test/");
        softly.assertAll();
    }

    /**
     * Check if line end is set to line start when end is 0. --> Single line
     */
    @Test
    public void lineEndBe0UseLineStartAsValue(){
        // Create issue with windows path separator
        Issue issue = new IssueBuilder()
                .setLineStart(42)
                .setLineEnd(0)
                .build();

        // Check
        IssueSoftAssertions softly = new IssueSoftAssertions();
        softly.assertThat(issue)
                .hasLineStart(42)
                .hasLineEnd(42);
        softly.assertAll();
    }

    /**
     * Check if C. removes all unnecessary whitespaces in message and description.
     */
    @Test
    public void removeSuperfluousWhitespacesByFreeText(){
        // Create issue with windows path separator
        Issue issue = new IssueBuilder()
                .setMessage("     ABCD     ")
                .setDescription("    EFGH    ")
                .build();

        // Check
        IssueSoftAssertions softly = new IssueSoftAssertions();
        softly.assertThat(issue)
                .hasMessage("ABCD")
                .hasDescription("EFGH");
        softly.assertAll();
    }

    /**
     * Check if all getter deliver the correct values from constructor.
     */
    @Test
    public void getAllSetValues(){
        // Create a object which forces all default values
        Issue issue = getGoodIssueBuilder().build();
        issue.setFingerprint("testFingerPrint");

        // Check
        IssueSoftAssertions softly = new IssueSoftAssertions();
        softly.assertThat(issue)
                .hasFingerprint("testFingerPrint")
                .hasLineStart(10)
                .hasLineEnd(42)
                .hasColumnStart(5)
                .hasColumnEnd(10)
                .hasCategory("testCategory")
                .hasType("testType")
                .hasPackagename("testPackageName")
                .hasPriority(Priority.HIGH)
                .hasMessage("Test Message")
                .hasDescription("Test Description")
                .hasFileName("/test/");
        softly.assertAll();
    }

    /**
     * Check if to toString creates correct string.
     */
    @Test
    public void valuesConvertToString(){
        // Create a object which forces all default values
        Issue issue = getGoodIssueBuilder().build();
        issue.setFingerprint("testFingerPrint");
        // Check
        IssueSoftAssertions softly = new IssueSoftAssertions();
        softly.assertThat(issue)
                .isString("/test/(10,5): testType: testCategory: Test Message");
        softly.assertAll();
    }

    /**
     * Check if equals detect a different in each property.
     */
    @SuppressWarnings("ConstantConditions")
    @Test
    public void notBeEquals(){
        // Create a object which forces all default values
        Issue issue = getGoodIssueBuilder().build();
        // Check
        IssueSoftAssertions softly = new IssueSoftAssertions();
        softly.assertThat(issue)
                .isNotEqualTo(null)
                .isNotEqualTo("")
                .isNotEqualTo(getGoodIssueBuilder().setLineStart(5).build())
                .isNotEqualTo(getGoodIssueBuilder().setLineEnd(100).build())
                .isNotEqualTo(getGoodIssueBuilder().setColumnStart(2).build())
                .isNotEqualTo(getGoodIssueBuilder().setColumnEnd(2).build())
                .isNotEqualTo(getGoodIssueBuilder().setFileName(null).build())
                .isNotEqualTo(getGoodIssueBuilder().setFileName("other name").build())
                .isNotEqualTo(getGoodIssueBuilder().setCategory(null).build())
                .isNotEqualTo(getGoodIssueBuilder().setCategory("other category").build())
                .isNotEqualTo(getGoodIssueBuilder().setType(null).build())
                .isNotEqualTo(getGoodIssueBuilder().setType("other type").build())
                .isNotEqualTo(getGoodIssueBuilder().setPriority(null).build())
                .isNotEqualTo(getGoodIssueBuilder().setPriority(Priority.LOW).build())
                .isNotEqualTo(getGoodIssueBuilder().setMessage(null).build())
                .isNotEqualTo(getGoodIssueBuilder().setMessage("other message").build())
                .isNotEqualTo(getGoodIssueBuilder().setDescription(null).build())
                .isNotEqualTo(getGoodIssueBuilder().setDescription("other description").build())
                .isNotEqualTo(getGoodIssueBuilder().setPackageName(null).build())
                .isNotEqualTo(getGoodIssueBuilder().setPackageName("other package name").build())
        ;
        softly.assertAll();
    }

    /**
     * Check if equals detects are equal object.
     */
    @Test
    public void beEquals(){
        // Create a object which forces all default values
        Issue issue = getGoodIssueBuilder().build();
        // Check
        IssueSoftAssertions softly = new IssueSoftAssertions();
        softly.assertThat(issue)
               .isEqualTo(issue)
                .isEqualTo(getGoodIssueBuilder().build());

        softly.assertAll();
    }

    /**
     * Check if equals objects have a equal hashCode.
     */
    @Test
    public void haveSameHashCode(){
        // Create a object which forces all default values
        Issue issue = getGoodIssueBuilder().build();
        // Check
        IssueSoftAssertions softly = new IssueSoftAssertions();
        softly.assertThat(issue)
                .hasHashCode(issue.hashCode())
                .hasHashCode(getGoodIssueBuilder().build().hashCode());
        softly.assertAll();
    }

    /**
     * Check if different objects have different hasCodes.
     */
    @SuppressWarnings("ConstantConditions")
    @Test
    public void haveNotSameHashCode(){
        // Create a object which forces all default values
        Issue issue = getGoodIssueBuilder().build();
        // Check
        IssueSoftAssertions softly = new IssueSoftAssertions();
        softly.assertThat(issue)
                .hasNotHashCode("".hashCode())
                .hasNotHashCode(getGoodIssueBuilder().setLineStart(5).build().hashCode())
                .hasNotHashCode(getGoodIssueBuilder().setLineEnd(100).build().hashCode())
                .hasNotHashCode(getGoodIssueBuilder().setColumnStart(2).build().hashCode())
                .hasNotHashCode(getGoodIssueBuilder().setColumnEnd(2).build().hashCode())
                .hasNotHashCode(getGoodIssueBuilder().setFileName(null).build().hashCode())
                .hasNotHashCode(getGoodIssueBuilder().setFileName("other name").build().hashCode())
                .hasNotHashCode(getGoodIssueBuilder().setCategory(null).build().hashCode())
                .hasNotHashCode(getGoodIssueBuilder().setCategory("other category").build().hashCode())
                .hasNotHashCode(getGoodIssueBuilder().setType(null).build().hashCode())
                .hasNotHashCode(getGoodIssueBuilder().setType("other type").build().hashCode())
                .hasNotHashCode(getGoodIssueBuilder().setPriority(null).build().hashCode())
                .hasNotHashCode(getGoodIssueBuilder().setPriority(Priority.LOW).build().hashCode())
                .hasNotHashCode(getGoodIssueBuilder().setMessage(null).build().hashCode())
                .hasNotHashCode(getGoodIssueBuilder().setMessage("other message").build().hashCode())
                .hasNotHashCode(getGoodIssueBuilder().setDescription(null).build().hashCode())
                .hasNotHashCode(getGoodIssueBuilder().setDescription("other description").build().hashCode())
                .hasNotHashCode(getGoodIssueBuilder().setPackageName(null).build().hashCode())
                .hasNotHashCode(getGoodIssueBuilder().setPackageName("other package name").build().hashCode())
        ;
        softly.assertAll();
    }


    /**
     * Creats a IssueBuilder with values which won't force a special case.
     * @return a IssueBuilder
     */
    private IssueBuilder getGoodIssueBuilder(){
        return new IssueBuilder()
                .setFileName("\\test\\")
                .setLineStart(10)
                .setLineEnd(42)
                .setColumnStart(5)
                .setColumnEnd(10)
                .setCategory("testCategory")
                .setType("testType")
                .setPackageName("testPackageName")
                .setPriority(Priority.HIGH)
                .setMessage("Test Message")
                .setDescription("Test Description");
    }


}
