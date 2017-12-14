package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.IssuesAssert.*;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link AnsibleLintParser}.
 */
public class AnsibleLintTest extends AbstractParserTest {
    AnsibleLintTest() {
        super("ansibleLint.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {
        assertThat(issues).hasSize(4);

        Iterator<Issue> iterator = issues.iterator();
        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory("ANSIBLE0002")
                .hasLineStart(2)
                .hasLineEnd(2)
                .hasMessage("Trailing whitespace")
                .hasFileName("/workspace/roles/backup/tasks/main.yml");
        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory("ANSIBLE0012")
                .hasLineStart(1)
                .hasLineEnd(1)
                .hasMessage("Commands should not change things if nothing needs doing")
                .hasFileName("/workspace/roles/upgrade/tasks/main.yml");
        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory("ANSIBLE0011")
                .hasLineStart(12)
                .hasLineEnd(12)
                .hasMessage("All tasks should be named")
                .hasFileName("/workspace/roles/upgrade/tasks/main.yml");
        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory("ANSIBLE0013")
                .hasLineStart(12)
                .hasLineEnd(12)
                .hasMessage("Use shell only when shell functionality is required")
                .hasFileName("/workspace/roles/roll_forward_target/tasks/main.yml");
    }

    @Override
    protected AbstractParser createParser() {
        return new AnsibleLintParser();
    }
}
