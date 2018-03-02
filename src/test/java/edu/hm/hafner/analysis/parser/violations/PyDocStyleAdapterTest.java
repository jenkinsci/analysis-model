package edu.hm.hafner.analysis.parser.violations;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link PyDocStyleAdapter}.
 *
 * @author Ullrich Hafner
 */
class PyDocStyleAdapterTest extends AbstractParserTest<Issue> {
    PyDocStyleAdapterTest() {
        super("pydocstyle.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {
        softly.assertThat(issues).hasSize(33);
        softly.assertThat(issues.get(0))
                .hasMessage("Missing docstring in public module")
                .hasFileName("fs/csm/admin_api/ui_api.py")
                .hasLineStart(1)
                .hasType("D100")
                .hasPriority(Priority.HIGH);
    }

    @Override
    protected AbstractParser<Issue> createParser() {
        return new PyDocStyleAdapter();
    }
}