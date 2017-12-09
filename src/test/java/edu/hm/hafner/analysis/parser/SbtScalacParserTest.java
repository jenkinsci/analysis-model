package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link SbtScalacParser}.
 *
 * @author: <a href="mailto:hochak@gmail.com">Hochak Hung</a>
 */
public class SbtScalacParserTest extends ParserTester {
    @Test
    public void basicFunctionality() {
        Issues<Issue> warnings = new SbtScalacParser().parse(openFile());

        assertThat(warnings).hasSize(2);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(DEFAULT_CATEGORY)
                    .hasLineStart(111)
                    .hasLineEnd(111)
                    .hasMessage("method stop in class Thread is deprecated: see corresponding Javadoc for more information.")
                    .hasFileName("/home/user/.jenkins/jobs/job/workspace/path/SomeFile.scala");
            softly.assertThat(warnings.get(1))
                    .hasPriority(Priority.HIGH)
                    .hasCategory(DEFAULT_CATEGORY)
                    .hasLineStart(9)
                    .hasLineEnd(9)
                    .hasMessage("';' expected but identifier found.")
                    .hasFileName("/home/user/.jenkins/jobs/job/workspace/another/path/SomeFile.scala");
        });
    }

    @Override
    protected String getWarningsFile() {
        return "sbtScalac.txt";
    }
}
