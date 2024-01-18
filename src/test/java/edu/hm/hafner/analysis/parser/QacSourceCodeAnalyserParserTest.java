package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link QacSourceCodeAnalyserParser}.
 */
class QacSourceCodeAnalyserParserTest extends AbstractParserTest {
    private static final String WARNING_CATEGORY = "Warning";
    private static final String ERROR_CATEGORY = "ERROR";
    private static final String ISSUES_FILE = "QACSourceCodeAnalyser.txt";

    /**
     * Creates a new instance of {@link QacSourceCodeAnalyserParserTest}.
     */
    QacSourceCodeAnalyserParserTest() {
        super(ISSUES_FILE);
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(9);

        Iterator<Issue> iterator = report.iterator();

        softly.assertThat(iterator.next())
                .hasLineStart(34)
                .hasLineEnd(34)
                .hasMessage(
                        "[I] Source file 'C:/PATH/PATH/PATH/PATH/Test1.c' has comments containing characters which are not members of the basic source character set.")
                .hasFileName("C:/PATH/PATH/PATH/PATH/Test1.c")
                .hasCategory(WARNING_CATEGORY)
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(185)
                .hasLineEnd(185)
                .hasMessage("A function-like macro is being defined.")
                .hasFileName("C:/PATH/PATH/PATH/PATH/Test2.h")
                .hasCategory(WARNING_CATEGORY)
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(20_233)
                .hasLineEnd(20_233)
                .hasMessage("A function could probably be used instead of this function-like macro.")
                .hasFileName("C:/PATH/PATH/Test3.h")
                .hasCategory(WARNING_CATEGORY)
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(213)
                .hasLineEnd(213)
                .hasMessage("Macro defines an unrecognised code-fragment.")
                .hasFileName("C:/PATH/Test4.h")
                .hasCategory(WARNING_CATEGORY)
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(75)
                .hasLineEnd(75)
                .hasMessage(
                        "[L] External identifier matches other identifier(s) (e.g. 'Test') in first 6 characters - program is non-conforming.")
                .hasFileName("C:/PATH/PATH/Test5.h")
                .hasCategory(WARNING_CATEGORY)
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(46)
                .hasLineEnd(46)
                .hasMessage("[E] This in-line assembler construct is a language extension. The code has been ignored")
                .hasFileName("C:/PATH/PATH/PATH/PATH/Test6.h")
                .hasCategory(WARNING_CATEGORY)
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(94)
                .hasLineEnd(94)
                .hasMessage("[C] Redefinition of 'P2FUNC' with a different body.")
                .hasFileName("C:/PATH/PATH/PATH/PATH/Test7.h")
                .hasCategory(WARNING_CATEGORY)
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(724)
                .hasLineEnd(724)
                .hasMessage("Cannot find test.h - Perhaps the appropriate search path was not given?")
                .hasFileName("C:/PATH/PATH/Test8.h")
                .hasCategory(ERROR_CATEGORY)
                .hasSeverity(Severity.WARNING_HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(178)
                .hasLineEnd(178)
                .hasMessage("Macro parameter not enclosed in ().")
                .hasFileName("C:/PATH/Test9.h")
                .hasCategory(WARNING_CATEGORY)
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Override
    protected QacSourceCodeAnalyserParser createParser() {
        return new QacSourceCodeAnalyserParser();
    }
}

