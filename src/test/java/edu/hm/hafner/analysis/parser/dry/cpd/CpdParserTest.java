package edu.hm.hafner.analysis.parser.dry.cpd;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.Report;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import edu.hm.hafner.analysis.parser.dry.DuplicationGroup;

/**
 * Tests the extraction of PMD's CPD analysis results.
 */
class CpdParserTest extends AbstractParserTest {
    private static final String FILE_NAME_REPORTER = "/home/ulli/Hudson/jobs/M-Single-Freestyle/workspace/src/main/java/hudson/plugins/warnings/util/HealthAwareMavenReporter.java";
    private static final String FILE_NAME_PUBLISHER = "/home/ulli/Hudson/jobs/M-Single-Freestyle/workspace/src/main/java/hudson/plugins/warnings/util/HealthAwarePublisher.java";
    private static final String CODE_FRAGMENT = "#\n"
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
            + "done";

    CpdParserTest() {
        super("cpd.xml");
    }

    @Override
    protected CpdParser createParser() {
        return new CpdParser(50, 25);
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(4);

        assertThatReporterAndPublisherDuplicationsAreCorrectlyLinked(report.get(0), report.get(1));

        Issue reporterSecond = report.get(2);
        Issue publisherSecond = report.get(3);
        softly.assertThat(reporterSecond)
                .hasLineStart(274).hasLineEnd(274 + 95 - 1)
                .hasFileName(FILE_NAME_REPORTER)
                .hasPriority(Priority.HIGH);
        softly.assertThat(publisherSecond)
                .hasLineStart(202).hasLineEnd(202 + 95 - 1)
                .hasFileName(FILE_NAME_PUBLISHER)
                .hasPriority(Priority.HIGH);

        Serializable additionalProperties = publisherSecond.getAdditionalProperties();
        softly.assertThat(additionalProperties).isEqualTo(reporterSecond.getAdditionalProperties());
        softly.assertThat(additionalProperties).isInstanceOf(DuplicationGroup.class);
        softly.assertThat(((DuplicationGroup)additionalProperties).getCodeFragment()).isNotEmpty();
    }

    @Test
    void shouldAssignPriority() {
        Report report;

        report = parse(68, 25);
        assertThat(report).hasSize(2);
        assertThat(report.get(0)).hasPriority(Priority.HIGH);

        report = parse(69, 25);
        assertThat(report).hasSize(2);
        assertThat(report.get(0)).hasPriority(Priority.NORMAL);

        report = parse(100, 68);
        assertThat(report).hasSize(2);
        assertThat(report.get(0)).hasPriority(Priority.NORMAL);

        report = parse(100, 69);
        assertThat(report).hasSize(2);
        assertThat(report.get(0)).hasPriority(Priority.LOW);
    }

    private Report parse(final int highThreshold, final int normalThreshold) {
        return new CpdParser(highThreshold, normalThreshold)
                .parse(getResourceAsFile("issue12516.xml"), StandardCharsets.UTF_8);
    }

    /**
     * Verifies the parser on a report that contains one duplication in two files. The report contains a code fragment
     * in a complex CDATA element.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-12516">Issue 12516</a>
     */
    @Test
    void issue12516() {
        Report report = parse("issue12516.xml");

        assertThat(report).hasSize(2);
        Issue first = report.get(0);
        assertThat(first)
                .hasLineStart(19).hasLineEnd(19 + 68 - 1)
                .hasFileName("csci07/csc60/remote_copy.sh")
                .hasPriority(Priority.HIGH);
        Issue second = report.get(1);
        assertThat(second)
                .hasLineStart(19).hasLineEnd(19 + 68 - 1)
                .hasFileName("csci08/csc90/remote_copy.sh")
                .hasPriority(Priority.HIGH);

        Serializable additionalProperties = first.getAdditionalProperties();
        assertThat(additionalProperties).isEqualTo(second.getAdditionalProperties());
        assertThat(additionalProperties).isInstanceOf(DuplicationGroup.class);
        assertThat(((DuplicationGroup)additionalProperties).getCodeFragment()).isEqualTo(CODE_FRAGMENT);
    }

    /**
     * Verifies the parser on a report that contains four duplication (in two files each). The report is using
     * ISO-8859-1 encoding.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-22356">Issue 22356</a>
     */
    @Test
    void issue22356() {
        String fileName = "issue22356.xml";
        Report report = parse(fileName);

        assertThat(report).hasSize(8);
    }

    /**
     * Verifies the parser on a report that contains 1 duplication (i.e., 2 warnings).
     */
    @Test
    void scanFileWithOneDuplication() {
        String fileName = "one-cpd.xml";
        Report report = parse(fileName);

        assertThat(report).hasSize(2);

        assertThatReporterAndPublisherDuplicationsAreCorrectlyLinked(report.get(0), report.get(1));
    }

    private void assertThatReporterAndPublisherDuplicationsAreCorrectlyLinked(final Issue reporterFirst,
            final Issue publisherFirst) {
        assertThat(reporterFirst)
                .hasLineStart(76).hasLineEnd(76 + 36 - 1)
                .hasFileName(FILE_NAME_REPORTER)
                .hasPriority(Priority.NORMAL);
        assertThat(publisherFirst)
                .hasLineStart(69).hasLineEnd(69 + 36 - 1)
                .hasFileName(FILE_NAME_PUBLISHER)
                .hasPriority(Priority.NORMAL);
        Serializable additionalProperties = reporterFirst.getAdditionalProperties();
        assertThat(additionalProperties).isEqualTo(publisherFirst.getAdditionalProperties());

        assertThat(additionalProperties).isInstanceOf(DuplicationGroup.class);
        assertThat(((DuplicationGroup)additionalProperties).getCodeFragment()).isNotEmpty();
    }

    @Test
    void shouldIgnoreOtherFile() {
        Report report = parse("otherfile.xml");

        assertThat(report).hasSize(0);
    }

    @Test
    void shouldReadFileWithWindowsEncoding() {
        Report report = parse("pmd-cpd.xml");

        assertThat(report).hasSize(29);
    }
}
