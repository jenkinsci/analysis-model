//Sarah Hofstätter
package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.AnalysisSoftAssertions;

/**
 * Tests the class {@link ArmccCompilerParser}.
 */
public class ArmccCompilerParserTest extends ParserTester {
    private static final String WARNING_CATEGORY = DEFAULT_CATEGORY;
    private static final String WARNING_TYPE = new ArmccCompilerParser().getId();

    /**
     * Detects three ARMCC warnings.
     *
     * @throws IOException if file could not be read
     */
    @Test
    public void testWarningsParser() throws IOException {
        AnalysisSoftAssertions softly = new AnalysisSoftAssertions();
        Issues warnings = new ArmccCompilerParser().parse(openFile());
        Iterator<Issue> iterator = warnings.iterator();
        Issue warning2 = iterator.next();
        Issue warning1 = iterator.next();
        Issue warning = iterator.next();

        softly.assertThat(warnings).hasSize(3);
        softly.assertThat(warning2).hasPriority(Priority.HIGH)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(21)
                .hasLineEnd(21)
                .hasMessage("5 - cannot open source input file \"somefile.h\": No such file or directory")
                .hasFileName("/home/test/main.cpp")
                .hasType(WARNING_TYPE);
        softly.assertThat(warning1).hasPriority(Priority.HIGH)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(23)
                .hasLineEnd(23)
                .hasMessage("5 - cannot open source input file \"somefile.h\": No such file or directory")
                .hasFileName("C:/home/test/main.cpp")
                .hasType(WARNING_TYPE);
        softly.assertThat(warning).hasPriority(Priority.NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(25)
                .hasLineEnd(25)
                .hasMessage("550 - something bad happened here")
                .hasFileName("/home/test/main.cpp")
                .hasType(WARNING_TYPE);
    }

    @Override
    protected String getWarningsFile() {
        return "armcc.txt";
    }
}
