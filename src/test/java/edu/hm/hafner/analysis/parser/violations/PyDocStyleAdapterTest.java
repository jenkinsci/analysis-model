package edu.hm.hafner.analysis.parser.violations;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

/**
 * Tests the class {@link PyDocStyleAdapter}.
 *
 * @author Ullrich Hafner
 */
class PyDocStyleAdapterTest extends AbstractParserTest {
    PyDocStyleAdapterTest() {
        super("pydocstyle.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(33);
        softly.assertThat(report.get(31))
                .hasMessage("Missing docstring in public module")
                .hasFileName("fs/csm/admin_api/ui_api.py")
                .hasLineStart(1)
                .hasType("D100")
                .hasSeverity(Severity.WARNING_HIGH);
    }

    @Override
    protected PyDocStyleAdapter createParser() {
        return new PyDocStyleAdapter();
    }
}
