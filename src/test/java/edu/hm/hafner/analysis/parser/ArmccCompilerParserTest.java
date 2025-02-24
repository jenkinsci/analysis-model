package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

/**
 * Tests the class {@link ArmccCompilerParser}.
 */
class ArmccCompilerParserTest extends AbstractParserTest {
    private static final String WARNING_CATEGORY = DEFAULT_CATEGORY;

    /**
     * Creates a new instance of {@link ArmccCompilerParserTest}.
     */
    protected ArmccCompilerParserTest() {
        super("armcc.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(3);

        softly.assertThat(report.get(0)).hasSeverity(Severity.ERROR)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(21)
                .hasLineEnd(21)
                .hasMessage("5 - cannot open source input file \"somefile.h\": No such file or directory")
                .hasFileName("/home/test/main.cpp");
        softly.assertThat(report.get(1)).hasSeverity(Severity.ERROR)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(23)
                .hasLineEnd(23)
                .hasMessage("5 - cannot open source input file \"somefile.h\": No such file or directory")
                .hasFileName("C:/home/test/main.cpp");
        softly.assertThat(report.get(2)).hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(25)
                .hasLineEnd(25)
                .hasMessage("550 - something bad happened here")
                .hasFileName("/home/test/main.cpp");
    }

    @Override
    protected ArmccCompilerParser createParser() {
        return new ArmccCompilerParser();
    }
}
