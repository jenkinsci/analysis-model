package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link InvalidsParser}.
 */
class InvalidsParserTest extends AbstractParserTest {
    InvalidsParserTest() {
        super("invalids.txt");
    }

    @Override
    protected InvalidsParser createParser() {
        return new InvalidsParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(3);

        String type = "Oracle Invalid";
        softly.assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("PLW-05004")
                .hasLineStart(45)
                .hasLineEnd(45)
                .hasMessage("Encountered the symbol \"END\" when expecting one of the following:")
                .hasFileName("ENV_UTIL#.PACKAGE BODY")
                .hasType(type)
                .hasPackageName("E");

        softly.assertThat(report.get(1))
                .hasSeverity(Severity.WARNING_LOW)
                .hasCategory("PLW-07202")
                .hasLineStart(5)
                .hasLineEnd(5)
                .hasMessage("Encountered the symbol \"END\" when expecting one of the following:")
                .hasFileName("ENV_ABBR#B.TRIGGER")
                .hasType(type)
                .hasPackageName("E");

        softly.assertThat(report.get(2))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("ORA-29521")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("referenced name javax/management/MBeanConstructorInfo could not be found")
                .hasFileName("/b77ce675_LoggerDynamicMBean.JAVA CLASS")
                .hasType(type)
                .hasPackageName("E");
    }
}
