package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link AnsibleLintParser}.
 */
class AnsibleLintParserTest extends AbstractParserTest {
    AnsibleLintParserTest() {
        super("ansibleLint.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(5);

        Iterator<Issue> iterator = report.iterator();
        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("EANSIBLE0002")
                .hasLineStart(2)
                .hasLineEnd(2)
                .hasMessage("Trailing whitespace")
                .hasFileName("/workspace/roles/backup/tasks/main.yml");
        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("EANSIBLE0012")
                .hasLineStart(1)
                .hasLineEnd(1)
                .hasMessage("Commands should not change things if nothing needs doing")
                .hasFileName("/workspace/roles/upgrade/tasks/main.yml");
        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("EANSIBLE0011")
                .hasLineStart(12)
                .hasLineEnd(12)
                .hasMessage("All tasks should be named")
                .hasFileName("/workspace/roles/upgrade/tasks/main.yml");
        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("EANSIBLE0013")
                .hasLineStart(12)
                .hasLineEnd(12)
                .hasMessage("Use shell only when shell functionality is required")
                .hasFileName("/workspace/roles/roll_forward_target/tasks/main.yml");
        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("E301")
                .hasLineStart(9)
                .hasLineEnd(9)
                .hasMessage("Commands should not change things if nothing needs doing")
                .hasFileName("/workspace/roles/some_role/tasks/main.yml");
    }

    @Override
    protected AnsibleLintParser createParser() {
        return new AnsibleLintParser();
    }
}
