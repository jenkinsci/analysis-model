package edu.hm.hafner.analysis.parser.dry.dupfinder;

import java.util.function.Function;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import edu.hm.hafner.analysis.parser.dry.CodeDuplication;

/**
 * Tests the extraction of Resharper DupFinder analysis results.
 *
 * @author Rafal Jasica
 */
class DupFinderParserTest extends AbstractParserTest<CodeDuplication> {
    /** First line in publisher. */
    private static final int PUBLISHER_LINE = 12;
    /** First line in reporter. */
    private static final int REPORTER_LINE = 26;
    /** File name of reporter. */
    private static final String REPORTER = "test/Reporter.cs";
    /** File name of publisher. */
    private static final String PUBLISHER = "test/Publisher.cs";
    /** Source code. */
    private static final String CODE_FRAGMENT = "<pre>if (items == null) throw new ArgumentNullException(\"items\");</pre>";

    DupFinderParserTest() {
        super("with-sourcecode.xml");
    }

    @Override
    protected DupFinderParser createParser() {
        return new DupFinderParser(50, 25);
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues<CodeDuplication> issues,
            final SoftAssertions softly) {
        softly.assertThat(issues).hasSize(2);

        CodeDuplication publisher = issues.get(0);
        CodeDuplication reporter = issues.get(1);

        assertThatReporterAndPublisherDuplicationsAreCorrectlyLinked(publisher, reporter);

        softly.assertThat(reporter).hasDescription(CODE_FRAGMENT);
        softly.assertThat(publisher).hasDescription(CODE_FRAGMENT);
    }

    private void assertThatReporterAndPublisherDuplicationsAreCorrectlyLinked(
            final CodeDuplication publisher, final CodeDuplication reporter) {
        assertThat(publisher)
                .hasLineStart(PUBLISHER_LINE).hasLineEnd(PUBLISHER_LINE + 11)
                .hasFileName(PUBLISHER)
                .hasPriority(Priority.LOW);
        assertThat(reporter)
                .hasLineStart(REPORTER_LINE).hasLineEnd(REPORTER_LINE + 11)
                .hasFileName(REPORTER)
                .hasPriority(Priority.LOW);

        assertThat(publisher.getDuplications()).containsExactly(reporter);
        assertThat(reporter.getDuplications()).containsExactly(publisher);
    }

    /**
     * Verifies the parser on a report that contains one duplication in two files. The report does not contain a code
     * fragment.
     */
    @Test
    void scanFileWithoutSourceCode() {
        Issues<? extends CodeDuplication> issues = parse("without-sourcecode.xml");

        assertThat(issues).hasSize(2);

        CodeDuplication publisher = issues.get(0);
        CodeDuplication reporter = issues.get(1);

        assertThatReporterAndPublisherDuplicationsAreCorrectlyLinked(publisher, reporter);

        assertThat(reporter.getDescription()).isEmpty();
        assertThat(publisher.getDescription()).isEmpty();
    }

    @Test
    void shouldIgnoreOtherFile() {
        Issues<? extends CodeDuplication> issues = parse("otherfile.xml");

        assertThat(issues).hasSize(0);
    }

    @Test
    void shouldAssignPriority() {
        Issues<? extends CodeDuplication> issues;

        issues = parse(12, 5);
        assertThat(issues).hasSize(2);
        assertThat(issues.get(0)).hasPriority(Priority.HIGH);

        issues = parse(13, 5);
        assertThat(issues).hasSize(2);
        assertThat(issues.get(0)).hasPriority(Priority.NORMAL);

        issues = parse(100, 12);
        assertThat(issues).hasSize(2);
        assertThat(issues.get(0)).hasPriority(Priority.NORMAL);

        issues = parse(100, 13);
        assertThat(issues).hasSize(2);
        assertThat(issues.get(0)).hasPriority(Priority.LOW);
    }

    private Issues<? extends CodeDuplication> parse(final int highThreshold, final int normalThreshold) {
        return new DupFinderParser(highThreshold, normalThreshold)
                .parse(openFile("without-sourcecode.xml"), Function.identity());
    }
}

