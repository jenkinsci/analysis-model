package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.Assertions.assertThat;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.*;

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
        Issues<Issue> warnings = new QACSourceCodeAnalyserParser().parse(openFile());

        assertThat(warnings).hasSize(9);

        Iterator<Issue> iterator = warnings.iterator();

        assertSoftly(softly -> {
           softly.assertThat(iterator.next())
                   .hasLineStart(34)
                   .hasLineEnd(34)
                   .hasMessage("[I] Source file 'C:/PATH/PATH/PATH/PATH/Test1.c' has comments containing characters which are not members of the basic source character set.")
                   .hasFileName("C:/PATH/PATH/PATH/PATH/Test1.c")
                   .hasType(TYPE)
                   .hasCategory(WARNING_CATEGORY)
                   .hasPriority(Priority.NORMAL);

            softly.assertThat(iterator.next())
                    .hasLineStart(185)
                    .hasLineEnd(185)
                    .hasMessage("A function-like macro is being defined.")
                    .hasFileName("C:/PATH/PATH/PATH/PATH/Test2.h")
                    .hasType(TYPE)
                    .hasCategory(WARNING_CATEGORY)
                    .hasPriority(Priority.NORMAL);

            softly.assertThat(iterator.next())
                    .hasLineStart(20233)
                    .hasLineEnd(20233)
                    .hasMessage("A function could probably be used instead of this function-like macro.")
                    .hasFileName("C:/PATH/PATH/Test3.h")
                    .hasType(TYPE)
                    .hasCategory(WARNING_CATEGORY)
                    .hasPriority(Priority.NORMAL);

            softly.assertThat(iterator.next())
                    .hasLineStart(213)
                    .hasLineEnd(213)
                    .hasMessage("Macro defines an unrecognised code-fragment.")
                    .hasFileName("C:/PATH/Test4.h")
                    .hasType(TYPE)
                    .hasCategory(WARNING_CATEGORY)
                    .hasPriority(Priority.NORMAL);

            softly.assertThat(iterator.next())
                    .hasLineStart(75)
                    .hasLineEnd(75)
                    .hasMessage("[L] External identifier matches other identifier(s) (e.g. 'Test') in first 6 characters - program is non-conforming.")
                    .hasFileName("C:/PATH/PATH/Test5.h")
                    .hasType(TYPE)
                    .hasCategory(WARNING_CATEGORY)
                    .hasPriority(Priority.NORMAL);

            softly.assertThat(iterator.next())
                    .hasLineStart(46)
                    .hasLineEnd(46)
                    .hasMessage("[E] This in-line assembler construct is a language extension. The code has been ignored")
                    .hasFileName("C:/PATH/PATH/PATH/PATH/Test6.h")
                    .hasType(TYPE)
                    .hasCategory(WARNING_CATEGORY)
                    .hasPriority(Priority.NORMAL);

            softly.assertThat(iterator.next())
                    .hasLineStart(94)
                    .hasLineEnd(94)
                    .hasMessage("[C] Redefinition of 'P2FUNC' with a different body.")
                    .hasFileName("C:/PATH/PATH/PATH/PATH/Test7.h")
                    .hasType(TYPE)
                    .hasCategory(WARNING_CATEGORY)
                    .hasPriority(Priority.NORMAL);

            softly.assertThat(iterator.next())
                    .hasLineStart(724)
                    .hasLineEnd(724)
                    .hasMessage("Cannot find test.h - Perhaps the appropriate search path was not given?")
                    .hasFileName("C:/PATH/PATH/Test8.h")
                    .hasType(TYPE)
                    .hasCategory(ERROR_CATEGORY)
                    .hasPriority(Priority.HIGH);

            softly.assertThat(iterator.next())
                    .hasLineStart(178)
                    .hasLineEnd(178)
                    .hasMessage("Macro parameter not enclosed in ().")
                    .hasFileName("C:/PATH/Test9.h")
                    .hasType(TYPE)
                    .hasCategory(WARNING_CATEGORY)
                    .hasPriority(Priority.NORMAL);
        });
    }


    /** {@inheritDoc} */
    @Override
    protected String getWarningsFile() {
        return "QACSourceCodeAnalyser.txt";
    }
}

