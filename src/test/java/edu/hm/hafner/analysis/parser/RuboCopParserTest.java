package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

/**
 * Tests the class {@link RuboCopParser}.
 *
 * @author David van Laatum
 */
class RuboCopParserTest extends AbstractParserTest {
    RuboCopParserTest() {
        super("rubocop.log");
    }

    @Override
    protected RuboCopParser createParser() {
        return new RuboCopParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(3);
        softly.assertThat(report.get(0))
                .hasFileName("config.ru")
                .hasLineStart(1)
                .hasCategory("Style/FrozenStringLiteralComment")
                .hasMessage("Missing magic comment # frozen_string_literal: true.")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasColumnStart(1);
        softly.assertThat(report.get(1))
                .hasFileName("spec/rails_helper.rb")
                .hasLineStart(6)
                .hasCategory("Style/StringLiterals")
                .hasMessage("Prefer single-quoted strings when you don't need string interpolation or special symbols.")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasColumnStart(7);
        softly.assertThat(report.get(2))
                .hasFileName("lib/tasks/generate_version.rake")
                .hasLineStart(21)
                .hasCategory("Layout/SpaceBeforeBlockBraces")
                .hasMessage("Space missing to the left of {.")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasColumnStart(24);
    }
}
