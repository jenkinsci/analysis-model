package edu.hm.hafner.analysis.parser.dry.dupfinder;

import java.io.Serializable;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.Assertions.assertThat;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import edu.hm.hafner.analysis.parser.dry.DuplicationGroup;

/**
 * Tests the extraction of Resharper DupFinder analysis results.
 *
 * @author Rafal Jasica
 */
class DupFinderParserTest extends AbstractParserTest {
    /** First line in publisher. */
    private static final int PUBLISHER_LINE = 12;
    /** First line in reporter. */
    private static final int REPORTER_LINE = 26;
    /** File name of reporter. */
    private static final String REPORTER = "test/Reporter.cs";
    /** File name of publisher. */
    private static final String PUBLISHER = "test/Publisher.cs";
    /** Source code. */
    private static final String CODE_FRAGMENT = "if (items == null) throw new ArgumentNullException(\"items\");";

    DupFinderParserTest() {
        super("with-sourcecode.xml");
    }

    @Override
    protected DupFinderParser createParser() {
        return new DupFinderParser(50, 25);
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues issues,
            final SoftAssertions softly) {
        softly.assertThat(issues).hasSize(2);

        Issue publisher = issues.get(0);
        Issue reporter = issues.get(1);

        assertThatReporterAndPublisherDuplicationsAreCorrectlyLinked(publisher, reporter);

        Serializable additionalProperties = publisher.getAdditionalProperties();
        softly.assertThat(additionalProperties).isEqualTo(reporter.getAdditionalProperties());
        softly.assertThat(additionalProperties).isInstanceOf(DuplicationGroup.class);
        softly.assertThat(((DuplicationGroup)additionalProperties).getCodeFragment()).isEqualTo(CODE_FRAGMENT);
    }

    private void assertThatReporterAndPublisherDuplicationsAreCorrectlyLinked(
            final Issue publisher, final Issue reporter) {
        assertThat(publisher)
                .hasLineStart(PUBLISHER_LINE).hasLineEnd(PUBLISHER_LINE + 11)
                .hasFileName(PUBLISHER)
                .hasPriority(Priority.LOW);
        assertThat(reporter)
                .hasLineStart(REPORTER_LINE).hasLineEnd(REPORTER_LINE + 11)
                .hasFileName(REPORTER)
                .hasPriority(Priority.LOW);

        assertThat(publisher.getAdditionalProperties()).isEqualTo(reporter.getAdditionalProperties());
    }

    /**
     * Verifies the parser on a report that contains one duplication in two files. The report does not contain a code
     * fragment.
     */
    @Test
    void scanFileWithoutSourceCode() {
        Issues issues = parse("without-sourcecode.xml");

        assertThat(issues).hasSize(2);

        Issue publisher = issues.get(0);
        Issue reporter = issues.get(1);

        assertThatReporterAndPublisherDuplicationsAreCorrectlyLinked(publisher, reporter);

        assertThat(reporter.getDescription()).isEmpty();
        assertThat(publisher.getDescription()).isEmpty();
    }

    @Test
    void shouldIgnoreOtherFile() {
        Issues issues = parse("otherfile.xml");

        assertThat(issues).hasSize(0);
    }

    @Test
    void shouldAssignPriority() {
        Issues issues;

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

    private Issues parse(final int highThreshold, final int normalThreshold) {
        return new DupFinderParser(highThreshold, normalThreshold)
                .parse(openFile("without-sourcecode.xml"), Function.identity());
    }
}

