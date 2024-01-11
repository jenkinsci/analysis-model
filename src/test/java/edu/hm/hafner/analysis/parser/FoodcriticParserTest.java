package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

/**
 * Tests the class {@link FoodcriticParser}.
 *
 * @author Rich Schumacher
 */
class FoodcriticParserTest extends AbstractParserTest {
    FoodcriticParserTest() {
        super("foodcritic.log");
    }

    @Override
    protected FoodcriticParser createParser() {
        return new FoodcriticParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(5);
        softly.assertThat(report.get(0))
                .hasFileName("./foo/attributes/default.rb")
                .hasLineStart(8)
                .hasCategory("FC001")
                .hasMessage("Use strings in preference to symbols to access node attributes")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(1))
                .hasFileName("./foo/recipes/default.rb")
                .hasLineStart(80)
                .hasCategory("FC002")
                .hasMessage("Avoid string interpolation where not required")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(2))
                .hasFileName("foo/definitions/foo.rb")
                .hasLineStart(1)
                .hasCategory("FC015")
                .hasMessage("Consider converting definition to a Custom Resource")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(3))
                .hasFileName("./foo/attributes/default.rb")
                .hasLineStart(30)
                .hasCategory("FC019")
                .hasMessage("Access node attributes in a consistent manner")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(4))
                .hasFileName("./foo/metadata.rb")
                .hasLineStart(1)
                .hasCategory("FC064")
                .hasMessage("Ensure issues_url is set in metadata")
                .hasSeverity(Severity.WARNING_NORMAL);
    }
}
