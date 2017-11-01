package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link CodeAnalysisParser}.
 */
public class CodeAnalysisParserTest extends ParserTester {
    /**
     * Parses a file with warnings of the CodeAnalysis tools.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void parseWarnings() throws IOException {
        Issues warnings = new CodeAnalysisParser().parse(openFile());
        Iterator<Issue> iterator = warnings.iterator();
        int expectedSize = 3;

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(warnings).hasSize(expectedSize);
        softly.assertThat(iterator.next()).hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("It appears that field 'Program.a' is never used or is only ever assigned to. Use this field or remove it.")
                .hasFileName("C:/Src/Parser/CSharp/Test.csproj")
                .hasType("CA1823")
                .hasCategory("Performance")
                .hasPriority(Priority.NORMAL);
        softly.assertThat(iterator.next()).hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("'CanvasHandler.Canvas.CreateImage(out bool, out ImageFormat)' has a cyclomatic complexity of 53. Rewrite or refactor the method to reduce complexity to 25.")
                .hasFileName("D:/somefolder/someproject.csproj")
                .hasType("CA1502")
                .hasCategory("Maintainability")
                .hasPriority(Priority.NORMAL);
        softly.assertThat(iterator.next()).hasLineStart(140)
                .hasLineEnd(140)
                .hasMessage("Modify 'AccountController.ChangePassword(ChangePasswordModel)' to catch a more specific exception than 'Exception' or rethrow the exception.")
                .hasFileName("C:/Src/Parser/CSharp/test.cs")
                .hasType("CA1031")
                .hasCategory("Design")
                .hasPriority(Priority.NORMAL);
        softly.assertAll();
    }

    @Override
    protected String getWarningsFile() {
        return "codeanalysis.txt";
    }
}

