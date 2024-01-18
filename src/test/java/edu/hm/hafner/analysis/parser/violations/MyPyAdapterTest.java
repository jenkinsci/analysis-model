package edu.hm.hafner.analysis.parser.violations;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

/**
 * Tests the class {@link MyPyAdapter}.
 *
 * @author Ullrich Hafner
 */
class MyPyAdapterTest extends AbstractParserTest {
    MyPyAdapterTest() {
        super("mypy.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(5);
        softly.assertThat(report.get(3))
                .hasMessage("\"LogRecord\" has no attribute \"user_uuid\"")
                .hasFileName("fs/cs/backend/log.py")
                .hasLineStart(16)
                .hasSeverity(Severity.WARNING_HIGH);
        softly.assertThat(report.get(2))
                .hasMessage("\"LogRecord\" has no attribute \"tenant_id\"")
                .hasFileName("fs/cs/backend/log.py")
                .hasLineStart(17)
                .hasSeverity(Severity.WARNING_HIGH);
    }

    @Override
    protected MyPyAdapter createParser() {
        return new MyPyAdapter();
    }
}
