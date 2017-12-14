package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link CodeAnalysisParser}.
 */
public class CodeAnalysisParserTest extends AbstractParserTest {

    /**
     * Creates a new CodeAnalysisParserTest.
     */
    public CodeAnalysisParserTest() {
        super("codeanalysis.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {
        softly.assertThat(issues).hasSize(3);

        softly.assertThat(issues.get(0)).hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("It appears that field 'Program.a' is never used or is only ever assigned to. Use this field or remove it.")
                .hasFileName("C:/Src/Parser/CSharp/Test.csproj")
                .hasType("CA1823")
                .hasCategory("Performance")
                .hasPriority(Priority.NORMAL);
        softly.assertThat(issues.get(1)).hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("'CanvasHandler.Canvas.CreateImage(out bool, out ImageFormat)' has a cyclomatic complexity of 53. Rewrite or refactor the method to reduce complexity to 25.")
                .hasFileName("D:/somefolder/someproject.csproj")
                .hasType("CA1502")
                .hasCategory("Maintainability")
                .hasPriority(Priority.NORMAL);
        softly.assertThat(issues.get(2)).hasLineStart(140)
                .hasLineEnd(140)
                .hasMessage("Modify 'AccountController.ChangePassword(ChangePasswordModel)' to catch a more specific exception than 'Exception' or rethrow the exception.")
                .hasFileName("C:/Src/Parser/CSharp/test.cs")
                .hasType("CA1031")
                .hasCategory("Design")
                .hasPriority(Priority.NORMAL);
    }

    @Override
    protected AbstractParser createParser() {
        return new CodeAnalysisParser();
    }
}

