package edu.hm.hafner.analysis.parser.violations;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

/**
 * Tests the class {@link XmlLintAdapter}.
 *
 * @author Ullrich Hafner
 */
class SemgrepAdapterTest extends AbstractParserTest {
    SemgrepAdapterTest() {
        super("semgrep-report.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(1);
        softly.assertThat(report.get(0))
                .hasFileName("src/main/java/se/bjurr/violations/lib/parsers/JUnitParser.java")
                .hasLineStart(33)
                .hasColumnStart(24)
                .hasColumnEnd(86)
                .hasType("java.lang.security.audit.formatted-sql-string.formatted-sql-string")
                .hasSeverity(Severity.WARNING_HIGH);

        softly.assertThat(report.get(0).getMessage()).contains("Detected a formatted string in a SQL statement. This could lead to SQL"
                + " injection if variables in the SQL statement are not properly sanitized."
                + " Use a prepared statements (java.sql.PreparedStatement) instead. You can"
                + " obtain a PreparedStatement using 'connection.prepareStatement'.");
    }

    @Override
    protected SemgrepAdapter createParser() {
        return new SemgrepAdapter();
    }
}
