package edu.hm.hafner.analysis.parser.violations;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link PitAdapter}.
 *
 * @author Ullrich Hafner
 */
class PitAdapterTest extends AbstractParserTest<Issue> {
    PitAdapterTest() {
        super("pit.xml");
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {
        softly.assertThat(issues).hasSize(25);
        softly.assertThat(issues.get(0))
                .hasMessage("NO_COVERAGE, org.pitest.mutationtest.engine.gregor.mutators.ReturnValsMutator, (Ljava/lang/Object;)Z")
                .hasFileName("se/bjurr/violations/lib/example/CopyOfMyClass.java")
                .hasLineStart(17)
                .hasPriority(Priority.NORMAL);
    }

    @Override
    protected AbstractParser<Issue> createParser() {
        return new PitAdapter();
    }
}