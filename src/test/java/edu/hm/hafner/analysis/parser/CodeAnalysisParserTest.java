package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

/**
 * Tests the class {@link CodeAnalysisParser}.
 */
class CodeAnalysisParserTest extends AbstractParserTest {
    CodeAnalysisParserTest() {
        super("codeanalysis.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(3);

        softly.assertThat(report.get(0)).hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage(
                        "It appears that field 'Program.a' is never used or is only ever assigned to. Use this field or remove it.")
                .hasFileName("C:/Src/Parser/CSharp/Test.csproj")
                .hasType("CA1823")
                .hasCategory("Performance")
                .hasSeverity(Severity.WARNING_NORMAL);
        softly.assertThat(report.get(1)).hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage(
                        "'CanvasHandler.Canvas.CreateImage(out bool, out ImageFormat)' has a cyclomatic complexity of 53. Rewrite or refactor the method to reduce complexity to 25.")
                .hasFileName("D:/somefolder/someproject.csproj")
                .hasType("CA1502")
                .hasCategory("Maintainability")
                .hasSeverity(Severity.WARNING_NORMAL);
        softly.assertThat(report.get(2)).hasLineStart(140)
                .hasLineEnd(140)
                .hasMessage(
                        "Modify 'AccountController.ChangePassword(ChangePasswordModel)' to catch a more specific exception than 'Exception' or rethrow the exception.")
                .hasFileName("C:/Src/Parser/CSharp/test.cs")
                .hasType("CA1031")
                .hasCategory("Design")
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Override
    protected CodeAnalysisParser createParser() {
        return new CodeAnalysisParser();
    }
}

