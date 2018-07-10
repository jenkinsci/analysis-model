package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractIssueParserTest;
import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link RuboCopParser}.
 *
 * @author David van Laatum
 */
class RuboCopParserTest extends AbstractIssueParserTest {
    RuboCopParserTest() {
        super("rubocop.log");
    }

    @Override
    protected AbstractParser createParser() {
        return new RuboCopParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(2);
        softly.assertThat(report.get(0))
                .hasFileName("config.ru")
                .hasLineStart(1)
                .hasCategory("Style/FrozenStringLiteralComment")
                .hasMessage("Missing magic comment # frozen_string_literal: true.")
                .hasPriority(Priority.NORMAL)
                .hasColumnStart(1);
        softly.assertThat(report.get(1))
                .hasFileName("spec/rails_helper.rb")
                .hasLineStart(6)
                .hasCategory("Style/StringLiterals")
                .hasMessage("Prefer single-quoted strings when you don't need string interpolation or special symbols.")
                .hasPriority(Priority.HIGH)
                .hasColumnStart(7);
    }
}
