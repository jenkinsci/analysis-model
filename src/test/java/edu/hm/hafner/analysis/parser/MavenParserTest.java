package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.IssueAssert;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link JavacParser} for output log of a maven compile.
 */
public class MavenParserTest extends AbstractParserTest {

    /**
     * Creates a new instance of {@link MavenParserTest}.
     *
     */
    protected MavenParserTest() {
        super("maven.txt");
    }

    protected String getWarningsFile() {
        return "maven.txt";
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {
        softly.assertThat(issues)
                .hasSize(5);

        Iterator<Issue> iterator = issues.iterator();

        assertThatWarningIsAtLine(softly, iterator.next(), 3);
        assertThatWarningIsAtLine(softly, iterator.next(), 36);
        assertThatWarningIsAtLine(softly, iterator.next(), 47);
        assertThatWarningIsAtLine(softly, iterator.next(), 69);
        assertThatWarningIsAtLine(softly, iterator.next(), 105);
    }

    private IssueAssert assertThatWarningIsAtLine(final SoftAssertions softly, final Issue warning, final int lineNumber) {
        return softly.assertThat(warning)
                .hasPriority(Priority.NORMAL)
                .hasCategory(AbstractParser.PROPRIETARY_API)
                .hasLineStart(lineNumber)
                .hasLineEnd(lineNumber)
                .hasMessage("com.sun.org.apache.xerces.internal.impl.dv.util.Base64 is Sun proprietary API and may be removed in a future release")
                .hasFileName("/home/hudson/hudson/data/jobs/Hudson main/workspace/remoting/src/test/java/hudson/remoting/BinarySafeStreamTest.java");
    }

    @Override
    protected AbstractParser createParser() {
        return new JavacParser();
    }
}

