package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

/**
 * Tests the class {@link DiabCParser}.
 */
public class DiabCParserTest extends ParserTester {
    private static final String TYPE = new DiabCParser().getId();

    /**
     * Parses a file with 13 warnings.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void parseDiabCpp() throws IOException {
        Issues warnings = new DiabCParser().parse(openFile());

        assertEquals(13, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();
        checkWarning(annotation,
                7,
                "missing return expression",
                "lint.c",
                TYPE, "1521", Priority.NORMAL);
        annotation = iterator.next();
        checkWarning(annotation,
                22,
                "narrowing or signed-to-unsigned type conversion found: int to unsigned char",
                "lint.c",
                TYPE, "1643", Priority.NORMAL);
        annotation = iterator.next();
        checkWarning(annotation,
                28,
                "constant out of range",
                "lint.c",
                TYPE, "1243", Priority.NORMAL);
        annotation = iterator.next();
        checkWarning(annotation,
                4,
                "function f4 is never used",
                "lint.c",
                TYPE, "1517", Priority.NORMAL);
        annotation = iterator.next();
        checkWarning(annotation,
                11,
                "function f5 is not found",
                "lint.c",
                TYPE, "1378", Priority.HIGH);
        annotation = iterator.next();
        checkWarning(annotation,
                5,
                "division by zero",
                "main.c",
                TYPE, "1025", Priority.NORMAL);
        annotation = iterator.next();
        checkWarning(annotation,
                5,
                "division by zero",
                "main.c",
                TYPE, "1025", Priority.HIGH);
        annotation = iterator.next();
        checkWarning(annotation,
                5,
                "division by zero",
                "main.c",
                TYPE, "1025", Priority.HIGH);
        annotation = iterator.next();
        checkWarning(annotation,
                976,
                "function \"testing\" was declared but never referenced",
                "test.cpp",
                TYPE, "4177", Priority.NORMAL);
        annotation = iterator.next();
        checkWarning(annotation,
                427,
                "pointless comparison of unsigned integer with zero",
                "test.cpp",
                TYPE, "4186", Priority.NORMAL);
        annotation = iterator.next();
        checkWarning(annotation,
                38,
                "expected a \";\"",
                "test.cpp",
                TYPE, "4065", Priority.HIGH);
        annotation = iterator.next();
        checkWarning(annotation,
                443,
                "external/internal linkage conflict with previous declaration",
                "test.cpp",
                TYPE, "4172", Priority.LOW);
        annotation = iterator.next();
        checkWarning(annotation,
                293,
                "access control not specified (\"private\" by default)",
                "test.h",
                TYPE, "4261", Priority.LOW);
    }

    @Override
    protected String getWarningsFile() {
        return "diabc.txt";
    }
}

