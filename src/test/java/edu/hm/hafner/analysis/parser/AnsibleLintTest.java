package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.IssuesAssert.*;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link AnsibleLintParser}
 */
public class AnsibleLintTest extends ParserTester {
    private static final String WARNING_TYPE = new AnsibleLintParser().getId();

    /**
     * Parses a file with 4 ansible-lint warnings
     */
    @Test
    public void testWarningParserError() {
        Issues<Issue> warnings = new AnsibleLintParser().parse(openFile());
        assertThat(warnings).hasSize(4);

        Iterator<Issue> iterator = warnings.iterator();
        assertSoftly(softly -> {
            softly.assertThat(iterator.next())
                    .hasPriority(Priority.NORMAL)
                    .hasCategory("ANSIBLE0002")
                    .hasLineStart(2)
                    .hasLineEnd(2)
                    .hasMessage("Trailing whitespace")
                    .hasFileName("/workspace/roles/backup/tasks/main.yml")
                    .hasType(WARNING_TYPE);
            softly.assertThat(iterator.next())
                    .hasPriority(Priority.NORMAL)
                    .hasCategory("ANSIBLE0012")
                    .hasLineStart(1)
                    .hasLineEnd(1)
                    .hasMessage("Commands should not change things if nothing needs doing")
                    .hasFileName("/workspace/roles/upgrade/tasks/main.yml")
                    .hasType(WARNING_TYPE);
            softly.assertThat(iterator.next())
                    .hasPriority(Priority.NORMAL)
                    .hasCategory("ANSIBLE0011")
                    .hasLineStart(12)
                    .hasLineEnd(12)
                    .hasMessage("All tasks should be named")
                    .hasFileName("/workspace/roles/upgrade/tasks/main.yml")
                    .hasType(WARNING_TYPE);
            softly.assertThat(iterator.next())
                    .hasPriority(Priority.NORMAL)
                    .hasCategory("ANSIBLE0013")
                    .hasLineStart(12)
                    .hasLineEnd(12)
                    .hasMessage("Use shell only when shell functionality is required")
                    .hasFileName("/workspace/roles/roll_forward_target/tasks/main.yml")
                    .hasType(WARNING_TYPE);
        });

    }

    @Override
    protected String getWarningsFile() {
        return "ansibleLint.txt";
    }

}
