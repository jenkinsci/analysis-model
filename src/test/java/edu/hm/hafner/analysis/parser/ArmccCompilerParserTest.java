package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link ArmccCompilerParser}.
 */
public class ArmccCompilerParserTest extends ParserTester {
    private static final String WARNING_CATEGORY = DEFAULT_CATEGORY;

    /**
     * Detects three ARMCC warnings.
     */
    @Test
    public void testWarningsParser() {
        Issues<Issue> warnings = new ArmccCompilerParser().parse(openFile());

        assertThat(warnings).hasSize(3);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0)).hasPriority(Priority.HIGH)
                    .hasCategory(WARNING_CATEGORY)
                    .hasLineStart(21)
                    .hasLineEnd(21)
                    .hasMessage("5 - cannot open source input file \"somefile.h\": No such file or directory")
                    .hasFileName("/home/test/main.cpp");
            softly.assertThat(warnings.get(1)).hasPriority(Priority.HIGH)
                    .hasCategory(WARNING_CATEGORY)
                    .hasLineStart(23)
                    .hasLineEnd(23)
                    .hasMessage("5 - cannot open source input file \"somefile.h\": No such file or directory")
                    .hasFileName("C:/home/test/main.cpp");
            softly.assertThat(warnings.get(2)).hasPriority(Priority.NORMAL)
                    .hasCategory(WARNING_CATEGORY)
                    .hasLineStart(25)
                    .hasLineEnd(25)
                    .hasMessage("550 - something bad happened here")
                    .hasFileName("/home/test/main.cpp");
        });
    }

    @Override
    protected String getWarningsFile() {
        return "armcc.txt";
    }
}
