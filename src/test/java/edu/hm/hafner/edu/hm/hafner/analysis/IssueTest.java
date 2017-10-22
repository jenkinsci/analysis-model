package edu.hm.hafner.edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.edu.hm.hafner.analysis.edu.hm.hafner.analysis.assertions.IssueSoftAssertions;

@SuppressWarnings({"NonBooleanMethodNameMayNotStartWithQuestion", "JUnitTestMethodWithNoAssertions"})
public class IssueTest {
    private static final String UNDEFINED = "-";

    @SuppressWarnings("ConstantConditions")
    @Test
    public void shouldConvertedToDefaultValue(){
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
       IssueSoftAssertions isa = new IssueSoftAssertions();
                isa.assertThat(issue)
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
                        .hasAId();
                isa.assertAll();
    }
    @Test
    public void shouldReplacePathSeparator(){
        // Create issue with windows path separator
        Issue issue = new IssueBuilder()
                .setFileName("c:\\test\\")
                .build();

        // Check
        IssueSoftAssertions isa = new IssueSoftAssertions();
        isa.assertThat(issue)
                .hasFileName("c:/test/");
        isa.assertAll();
    }
    @Test
    public void shouldLineEndBe0UseLineStartAsValue(){
        // Create issue with windows path separator
        Issue issue = new IssueBuilder()
                .setLineStart(42)
                .setLineEnd(0)
                .build();

        // Check
        IssueSoftAssertions isa = new IssueSoftAssertions();
        isa.assertThat(issue)
                .hasLineStart(42)
                .hasLineEnd(42);
        isa.assertAll();
    }
    @Test
    public void shouldRemoveSuperfluousWhitespacesByFreeText(){
        // Create issue with windows path separator
        Issue issue = new IssueBuilder()
                .setMessage("     ABCD     ")
                .setDescription("    EFGH    ")
                .build();

        // Check
        IssueSoftAssertions isa = new IssueSoftAssertions();
        isa.assertThat(issue)
                .hasMessage("ABCD")
                .hasDescription("EFGH");
        isa.assertAll();
    }
    @Test
    public void shouldGetAllSetValues(){
        // Create a object which forces all default values
        Issue issue = getGoodIssueBuilder().build();
        issue.setFingerprint("testFingerPrint");

        // Check
        IssueSoftAssertions isa = new IssueSoftAssertions();
        isa.assertThat(issue)
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
        isa.assertAll();
    }
    @Test
    public void shouldValuesConvertToString(){
        // Create a object which forces all default values
        Issue issue = getGoodIssueBuilder().build();
        issue.setFingerprint("testFingerPrint");
        // Check
        IssueSoftAssertions isa = new IssueSoftAssertions();
        isa.assertThat(issue)
                .isString("/test/(10,5): testType: testCategory: Test Message");
        isa.assertAll();
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void shouldNotBeEquals(){
        // Create a object which forces all default values
        Issue issue = getGoodIssueBuilder().build();
        // Check
        IssueSoftAssertions isa = new IssueSoftAssertions();
        isa.assertThat(issue)
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
        isa.assertAll();
    }

    @Test
    public void shouldBeEquals(){
        // Create a object which forces all default values
        Issue issue = getGoodIssueBuilder().build();
        // Check
        IssueSoftAssertions isa = new IssueSoftAssertions();
        isa.assertThat(issue)
               .isEqualTo(issue)
                .isEqualTo(getGoodIssueBuilder().build());

        isa.assertAll();
    }

    @Test
    public void shouldHaveSameHashCode(){
        // Create a object which forces all default values
        Issue issue = getGoodIssueBuilder().build();
        // Check
        IssueSoftAssertions isa = new IssueSoftAssertions();
        isa.assertThat(issue)
                .hasHashCode(issue.hashCode())
                .hasHashCode(getGoodIssueBuilder().build().hashCode());
        isa.assertAll();
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void shouldNotHaveSameHashCode(){
        // Create a object which forces all default values
        Issue issue = getGoodIssueBuilder().build();
        // Check
        IssueSoftAssertions isa = new IssueSoftAssertions();
        isa.assertThat(issue)
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
        isa.assertAll();
    }



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
