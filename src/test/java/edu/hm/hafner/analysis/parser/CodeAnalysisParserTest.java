package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

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

        assertEquals(3, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();
        checkWarning(annotation,
                0,
                "It appears that field 'Program.a' is never used or is only ever assigned to. Use this field or remove it.",
                "C:/Src/Parser/CSharp/Test.csproj",
                "CA1823",
                "Performance",
                Priority.NORMAL);
        annotation = iterator.next();
        checkWarning(annotation,
                0,
                "'CanvasHandler.Canvas.CreateImage(out bool, out ImageFormat)' has a cyclomatic complexity of 53. Rewrite or refactor the method to reduce complexity to 25.",
                "D:/somefolder/someproject.csproj",
                "CA1502",
                "Maintainability",
                Priority.NORMAL);
        annotation = iterator.next();
        checkWarning(annotation,
                140,
                "Modify 'AccountController.ChangePassword(ChangePasswordModel)' to catch a more specific exception than 'Exception' or rethrow the exception.",
                "C:/Src/Parser/CSharp/test.cs",
                "CA1031",
                "Design",
                Priority.NORMAL);
    }

    @Override
    protected String getWarningsFile() {
        return "codeanalysis.txt";
    }
}

