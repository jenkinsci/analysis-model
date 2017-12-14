package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link ScalacParser}.
 *
 * @author <a href="mailto:alexey.kislin@gmail.com">Alexey Kislin</a>
 */
public class ScalacParserTest extends ParserTester {
    @Test
    public void basicFunctionality() {
        Issues<Issue> warnings = parse("scalac.txt");

        assertThat(warnings).hasSize(3);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory("warning")
                    .hasLineStart(29)
                    .hasLineEnd(29)
                    .hasMessage("implicit conversion method toLab2OI should be enabled")
                    .hasFileName("/home/user/.jenkins/jobs/job/workspace/some/path/SomeFile.scala");
            softly.assertThat(warnings.get(1))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory("warning")
                    .hasLineStart(408)
                    .hasLineEnd(408)
                    .hasMessage("method asJavaMap in object JavaConversions is deprecated: use mapAsJavaMap instead")
                    .hasFileName("/home/user/.jenkins/jobs/job/workspace/another/path/SomeFile.scala");
            softly.assertThat(warnings.get(2))
                    .hasPriority(Priority.HIGH)
                    .hasCategory("warning")
                    .hasLineStart(59)
                    .hasLineEnd(59)
                    .hasMessage("method error in object Predef is deprecated: Use `sys.error(message)` instead")
                    .hasFileName("/home/user/.jenkins/jobs/job/workspace/yet/another/path/SomeFile.scala");
        });
    }

    private Issues<Issue> parse(final String fileName) {
        return new ScalacParser().parse(openFile(fileName));
    }

    @Override
    protected String getWarningsFile() {
        return "scalac.txt";
    }
}
