package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

/**
 * Tests the class {@link QACSourceCodeAnalyserParser}.
 */
public class QACSourceCodeAnalyserParserTest extends ParserTester {
    private static final String WARNING_CATEGORY = "Warning";
    private static final String ERROR_CATEGORY = "ERROR";
    private static final String TYPE = new QACSourceCodeAnalyserParser().getId();

    /**
     * Parses a file with QAC warnings.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void testWarningsParser() throws IOException {
        Issues warnings = new QACSourceCodeAnalyserParser().parse(openFile());

        assertEquals(9, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();
        checkWarning(annotation,
                34,
                "[I] Source file 'C:/PATH/PATH/PATH/PATH/Test1.c' has comments containing characters which are not members of the basic source character set.",
                "C:/PATH/PATH/PATH/PATH/Test1.c",
                TYPE, WARNING_CATEGORY, Priority.NORMAL);
        annotation = iterator.next();
        checkWarning(annotation,
                185,
                "A function-like macro is being defined.",
                "C:/PATH/PATH/PATH/PATH/Test2.h",
                TYPE, WARNING_CATEGORY, Priority.NORMAL);
        annotation = iterator.next();
        checkWarning(annotation,
                20233,
                "A function could probably be used instead of this function-like macro.",
                "C:/PATH/PATH/Test3.h",
                TYPE, WARNING_CATEGORY, Priority.NORMAL);
        annotation = iterator.next();
        checkWarning(annotation,
                213,
                "Macro defines an unrecognised code-fragment.",
                "C:/PATH/Test4.h",
                TYPE, WARNING_CATEGORY, Priority.NORMAL);
        annotation = iterator.next();
        checkWarning(annotation,
                75,
                "[L] External identifier matches other identifier(s) (e.g. 'Test') in first 6 characters - program is non-conforming.",
                "C:/PATH/PATH/Test5.h",
                TYPE, WARNING_CATEGORY, Priority.NORMAL);
        annotation = iterator.next();
        checkWarning(annotation,
                46,
                "[E] This in-line assembler construct is a language extension. The code has been ignored",
                "C:/PATH/PATH/PATH/PATH/Test6.h",
                TYPE, WARNING_CATEGORY, Priority.NORMAL);
        annotation = iterator.next();
        checkWarning(annotation,
                94,
                "[C] Redefinition of 'P2FUNC' with a different body.",
                "C:/PATH/PATH/PATH/PATH/Test7.h",
                TYPE, WARNING_CATEGORY, Priority.NORMAL);
        annotation = iterator.next();
        checkWarning(annotation,
                724,
                "Cannot find test.h - Perhaps the appropriate search path was not given?",
                "C:/PATH/PATH/Test8.h",
                TYPE, ERROR_CATEGORY, Priority.HIGH);
        annotation = iterator.next();
        checkWarning(annotation,
                178,
                "Macro parameter not enclosed in ().",
                "C:/PATH/Test9.h",
                TYPE, WARNING_CATEGORY, Priority.NORMAL);
    }


    /** {@inheritDoc} */
    @Override
    protected String getWarningsFile() {
        return "QACSourceCodeAnalyser.txt";
    }
}

