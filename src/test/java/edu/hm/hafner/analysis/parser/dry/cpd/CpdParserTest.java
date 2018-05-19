package edu.hm.hafner.analysis.parser.dry.cpd;

import java.util.function.Function;

import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.assertj.Assertions.*;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import edu.hm.hafner.analysis.parser.dry.CodeDuplication;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Tests the extraction of PMD's CPD analysis results.
 */
@SuppressFBWarnings("BC_UNCONFIRMED_CAST_OF_RETURN_VALUE")
class CpdParserTest extends AbstractParserTest<CodeDuplication> {
    private static final String FILE_NAME_REPORTER = "/home/ulli/Hudson/jobs/M-Single-Freestyle/workspace/src/main/java/hudson/plugins/warnings/util/HealthAwareMavenReporter.java";
    private static final String FILE_NAME_PUBLISHER = "/home/ulli/Hudson/jobs/M-Single-Freestyle/workspace/src/main/java/hudson/plugins/warnings/util/HealthAwarePublisher.java";
    private static final String CODE_FRAGMENT = "<pre><code>#\n"
            + "\n"
            + "    ERROR HANDLING: N/A\n"
            + "    #\n"
            + "    REMARKS: N/A\n"
            + "    #\n"
            + "    ****************************** END HEADER *************************************\n"
            + "    #\n"
            + "\n"
            + "    ***************************** BEGIN PDL ***************************************\n"
            + "    #\n"
            + "    ****************************** END PDL ****************************************\n"
            + "    #\n"
            + "\n"
            + "    ***************************** BEGIN CODE **************************************\n"
            + "    **\n"
            + "    *******************************************************************************\n"
            + "\n"
            + "    *******************************************************************************\n"
            + "    *******************************************************************************\n"
            + "\n"
            + "if [ $# -lt 3 ]\n"
            + "then\n"
            + "exit 1\n"
            + "fi\n"
            + "\n"
            + "    *******************************************************************************\n"
            + "    initialize local variables\n"
            + "    shift input parameter (twice) to leave only files to copy\n"
            + "    *******************************************************************************\n"
            + "\n"
            + "files=\"\"\n"
            + "shift\n"
            + "shift\n"
            + "\n"
            + "    *******************************************************************************\n"
            + "    *******************************************************************************\n"
            + "\n"
            + "for i in $*\n"
            + "do\n"
            + "files=\"$files $directory/$i\"\n"
            + "done</code></pre>";

    CpdParserTest() {
        super("cpd.xml");
    }

    @Override
    protected CpdParser createParser() {
        return new CpdParser(50, 25);
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues<CodeDuplication> issues,
            final SoftAssertions softly) {
        softly.assertThat(issues).hasSize(4);

        assertThatReporterAndPublisherDuplicationsAreCorrectlyLinked(issues.get(0), issues.get(1));

        CodeDuplication reporterSecond = issues.get(2);
        CodeDuplication publisherSecond = issues.get(3);
        softly.assertThat(reporterSecond)
                .hasLineStart(274).hasLineEnd(274 + 94)
                .hasFileName(FILE_NAME_REPORTER)
                .hasPriority(Priority.HIGH);
        softly.assertThat(publisherSecond)
                .hasLineStart(202).hasLineEnd(202 + 94)
                .hasFileName(FILE_NAME_PUBLISHER)
                .hasPriority(Priority.HIGH);
        softly.assertThat(publisherSecond.getDescription()).isNotEmpty();
        softly.assertThat(reporterSecond.getDescription()).isNotEmpty();
        softly.assertThat(publisherSecond.getDuplications()).containsExactly(reporterSecond);
        softly.assertThat(reporterSecond.getDuplications()).containsExactly(publisherSecond);
    }

    @Test
    void shouldAssignPriority() {
        Issues<? extends CodeDuplication> issues;

        issues = parse(68, 25);
        assertThat(issues).hasSize(2);
        assertThat(issues.get(0)).hasPriority(Priority.HIGH);

        issues = parse(69, 25);
        assertThat(issues).hasSize(2);
        assertThat(issues.get(0)).hasPriority(Priority.NORMAL);

        issues = parse(100, 68);
        assertThat(issues).hasSize(2);
        assertThat(issues.get(0)).hasPriority(Priority.NORMAL);

        issues = parse(100, 69);
        assertThat(issues).hasSize(2);
        assertThat(issues.get(0)).hasPriority(Priority.LOW);
    }

    private Issues<? extends CodeDuplication> parse(final int highThreshold, final int normalThreshold) {
        return new CpdParser(highThreshold, normalThreshold)
                .parse(openFile("issue12516.xml"), Function.identity());
    }

    /**
     * Verifies the parser on a report that contains one duplication in two files. The report contains a code fragment
     * in a complex CDATA element.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-12516">Issue 12516</a>
     */
    @Test
    void issue12516() {
        Issues<? extends CodeDuplication> issues = parse("issue12516.xml");

        assertThat(issues).hasSize(2);
        CodeDuplication first = issues.get(0);
        assertThat(first)
                .hasLineStart(19).hasLineEnd(19 + 67)
                .hasFileName("csci07/csc60/remote_copy.sh")
                .hasDescription(CODE_FRAGMENT)
                .hasPriority(Priority.HIGH);
        CodeDuplication second = issues.get(1);
        assertThat(second)
                .hasLineStart(19).hasLineEnd(19 + 67)
                .hasFileName("csci08/csc90/remote_copy.sh")
                .hasDescription(CODE_FRAGMENT)
                .hasPriority(Priority.HIGH);

        assertThat(first.getDuplications()).containsExactly(second);
        assertThat(second.getDuplications()).containsExactly(first);
    }

    /**
     * Verifies the parser on a report that contains four duplication (in two files each). The report is using
     * ISO-8859-1 encoding.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-22356">Issue 22356</a>
     */
    @Test
    public void issue22356() {
        String fileName = "issue22356.xml";
        Issues<? extends CodeDuplication> issues = parse(fileName);

        assertThat(issues).hasSize(8);
    }

    /**
     * Verifies the parser on a report that contains 1 duplication (i.e., 2 warnings).
     */
    @Test
    void scanFileWithOneDuplication() {
        String fileName = "one-cpd.xml";
        Issues<? extends CodeDuplication> issues = parse(fileName);

        assertThat(issues).hasSize(2);

        assertThatReporterAndPublisherDuplicationsAreCorrectlyLinked(issues.get(0), issues.get(1));
    }

    private void assertThatReporterAndPublisherDuplicationsAreCorrectlyLinked(final CodeDuplication reporterFirst,
            final CodeDuplication publisherFirst) {
        assertThat(reporterFirst)
                .hasLineStart(76).hasLineEnd(76 + 35)
                .hasFileName(FILE_NAME_REPORTER)
                .hasPriority(Priority.NORMAL);
        assertThat(publisherFirst)
                .hasLineStart(69).hasLineEnd(69 + 35)
                .hasFileName(FILE_NAME_PUBLISHER)
                .hasPriority(Priority.NORMAL);
        assertThat(reporterFirst.getDescription()).isNotEmpty();
        assertThat(publisherFirst.getDescription()).isNotEmpty();
        assertThat(reporterFirst.getDuplications()).containsExactly(publisherFirst);
        assertThat(publisherFirst.getDuplications()).containsExactly(reporterFirst);
    }

    @Test
    void shouldIgnoreOtherFile() {
        Issues<? extends CodeDuplication> issues = parse("otherfile.xml");

        assertThat(issues).hasSize(0);
    }

    @Test
    void shouldReadFileWithWindowsEncoding() {
        Issues<? extends CodeDuplication> issues = parse("pmd-cpd.xml");

        assertThat(issues).hasSize(29);
    }
}
