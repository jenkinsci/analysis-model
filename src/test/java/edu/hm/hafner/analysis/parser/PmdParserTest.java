package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractIssueParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import edu.hm.hafner.analysis.parser.pmd.PmdParser;

/**
 * Tests the extraction of PMD analysis results.
 */
class PmdParserTest extends AbstractIssueParserTest {
    private static final String PREFIX = "pmd/";

    PmdParserTest() {
        super(PREFIX + "4-pmd-warnings.xml");
    }

    @Override
    protected PmdParser createParser() {
        return new PmdParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(4);

        Report actionIssues = report.filter(Issue.byPackageName("com.avaloq.adt.env.internal.ui.actions"));
        softly.assertThat(actionIssues).hasSize(1);
        softly.assertThat(report.filter(Issue.byPackageName("com.avaloq.adt.env.internal.ui.actions"))).hasSize(1);
        softly.assertThat(report.filter(Issue.byPackageName("com.avaloq.adt.env.internal.ui.dialogs"))).hasSize(2);

        softly.assertThat(report).hasPriorities(1, 2, 1);

        softly.assertThat(actionIssues.get(0))
                .hasMessage("These nested if statements could be combined.")
                .hasPriority(Priority.NORMAL)
                .hasCategory("Basic")
                .hasType("CollapsibleIfStatements")
                .hasLineStart(54)
                .hasLineEnd(61)
                .hasPackageName("com.avaloq.adt.env.internal.ui.actions")
                .hasFileName("C:/Build/Results/jobs/ADT-Base/workspace/com.avaloq.adt.ui/src/main/java/com/avaloq/adt/env/internal/ui/actions/CopyToClipboard.java");
    }

    @Test
    void shouldCorrectlyMapLinesAndColumns() {
        Report report = parseInPmdFolder("lines-columns.xml");

        assertThat(report).hasSize(1);

        assertThat(report.get(0))
                .hasFileName(
                        "/Users/hafner/Development/jenkins/workspace/Pipeline/src/main/java/edu/hm/hafner/analysis/parser/AjcParser.java")
                .hasLineStart(30).hasLineEnd(74)
                .hasColumnStart(5).hasColumnEnd(12)
                .hasType("CyclomaticComplexity")
                .hasCategory("Code Size")
                .hasPriority(Priority.NORMAL)
                .hasMessage("The method 'parse' has a Cyclomatic Complexity of 10.");
    }

    /**
     * Parses a warning log with 15 warnings.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-12801">Issue 12801</a>
     */
    @Test
    void issue12801() {
        Report report = parseInPmdFolder("issue12801.xml");

        assertThat(report).hasSize(2);
    }

    /**
     * Checks whether we correctly detect all 669 warnings.
     */
    @Test
    void scanFileWithSeveralWarnings() {
        Report report = parseInPmdFolder("pmd.xml");

        assertThat(report).hasSize(669);
    }

    /**
     * Checks whether we create messages with a single dot.
     */
    @Test
    void verifySingleDot() {
        String fileName = "warning-message-with-dot.xml";
        Report report = parseInPmdFolder(fileName);

        assertThat(report).hasSize(2);
        assertThat(report.get(0)).hasMessage("Avoid really long parameter lists.");
    }

    /**
     * Checks whether we correctly detect an empty file.
     */
    @Test
    void scanFileWithNoBugs() {
        Report report = parseInPmdFolder("empty.xml");

        assertThat(report).isEmpty();
    }

    /**
     * Checks whether we correctly parse a file with 4 warnings.
     */
    @Test
    void testEquals() {
        Report report = parseInPmdFolder("equals-test.xml");

        int expectedSize = 4;
        assertThat(report).hasSize(expectedSize);
        assertThat(report.filter(Issue.byPackageName("com.avaloq.adt.env.core.db.plsqlCompletion"))).hasSize(expectedSize);
        assertThat(report).hasPriorities(0, 4, 0);
    }

    private Report parseInPmdFolder(final String fileName) {
        return parse(PREFIX + fileName);
    }
}
