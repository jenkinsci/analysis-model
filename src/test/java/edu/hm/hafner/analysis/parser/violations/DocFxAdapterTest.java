package edu.hm.hafner.analysis.parser.violations;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link DocFxAdapter}.
 *
 * @author Ullrich Hafner
 */
class DocFxAdapterTest extends AbstractParserTest<Issue> {
    DocFxAdapterTest() {
        super("docfx.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {
        softly.assertThat(issues).hasSize(3);
        softly.assertThat(issues.get(0))
                .hasMessage("Invalid file link:(~/missing.md#mobiilisovellus).")
                .hasFileName("sanasto.md")
                .hasType("InvalidFileLink")
                .hasLineStart(63)
                .hasPriority(Priority.NORMAL);
        softly.assertThat(issues.get(1))
                .hasMessage("Invalid file link:(~/mobiilirajapinta/puuttuu.md).")
                .hasFileName("mobiilirajapinta/json-dateandtime.md")
                .hasType("InvalidFileLink")
                .hasLineStart(18)
                .hasPriority(Priority.NORMAL);
    }

    @Override
    protected AbstractParser<Issue> createParser() {
        return new DocFxAdapter();
    }
}