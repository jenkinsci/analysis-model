package edu.hm.hafner.analysis.parser.dry.cpd;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.DuplicationGroup;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the extraction of PMD's CPD analysis results.
 */
@SuppressWarnings("NullAway")
class CpdParserTest extends AbstractParserTest {
    private static final String FILE_NAME_REPORTER = "/home/ulli/Hudson/jobs/M-Single-Freestyle/workspace/src/main/java/hudson/plugins/warnings/util/HealthAwareMavenReporter.java";
    private static final String FILE_NAME_PUBLISHER = "/home/ulli/Hudson/jobs/M-Single-Freestyle/workspace/src/main/java/hudson/plugins/warnings/util/HealthAwarePublisher.java";
    private static final String CODE_FRAGMENT = """
            #
            
                ERROR HANDLING: N/A
                #
                REMARKS: N/A
                #
                ****************************** END HEADER *************************************
                #
            
                ***************************** BEGIN PDL ***************************************
                #
                ****************************** END PDL ****************************************
                #
            
                ***************************** BEGIN CODE **************************************
                **
                *******************************************************************************
            
                *******************************************************************************
                *******************************************************************************
            
            if [ $# -lt 3 ]
            then
            exit 1
            fi
            
                *******************************************************************************
                initialize local variables
                shift input parameter (twice) to leave only files to copy
                *******************************************************************************
            
            files=""
            shift
            shift
            
                *******************************************************************************
                *******************************************************************************
            
            for i in $*
            do
            files="$files $directory/$i"
            done""";

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

        var reporterSecond = report.get(2);
        var publisherSecond = report.get(3);
        softly.assertThat(reporterSecond)
                .hasLineStart(274).hasLineEnd(274 + 95 - 1)
                .hasFileName(FILE_NAME_REPORTER)
                .hasSeverity(Severity.WARNING_HIGH)
                .hasMessage("Found duplicated code.")
                .hasCategory("Code Duplication")
                .hasType("CPD");
        softly.assertThat(publisherSecond)
                .hasLineStart(202).hasLineEnd(202 + 95 - 1)
                .hasFileName(FILE_NAME_PUBLISHER)
                .hasSeverity(Severity.WARNING_HIGH)
                .hasMessage("Found duplicated code.")
                .hasCategory("Code Duplication")
                .hasType("CPD");

        var additionalProperties = publisherSecond.getAdditionalProperties();
        softly.assertThat(additionalProperties).isEqualTo(reporterSecond.getAdditionalProperties());
        softly.assertThat(additionalProperties).isInstanceOfSatisfying(DuplicationGroup.class,
                duplicationGroup -> assertThat(duplicationGroup.getCodeFragment()).isNotEmpty());
    }

    @Test
    void shouldAssignPriority() {
        Report report;

        report = parse(68, 25);
        assertThat(report).hasSize(2);
        assertThat(report.get(0)).hasSeverity(Severity.WARNING_HIGH);

        report = parse(69, 25);
        assertThat(report).hasSize(2);
        assertThat(report.get(0)).hasSeverity(Severity.WARNING_NORMAL);

        report = parse(100, 68);
        assertThat(report).hasSize(2);
        assertThat(report.get(0)).hasSeverity(Severity.WARNING_NORMAL);

        report = parse(100, 69);
        assertThat(report).hasSize(2);
        assertThat(report.get(0)).hasSeverity(Severity.WARNING_LOW);
    }

    private Report parse(final int highThreshold, final int normalThreshold) {
        var parser = new CpdParser(highThreshold, normalThreshold);
        return parser.parse(createReaderFactory("issue12516.xml"));
    }

    /**
     * Verifies the parser on a report that contains one duplication in two files. The report contains a code fragment
     * in a complex CDATA element.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-12516">Issue 12516</a>
     */
    @Test
    void issue12516() {
        var report = parse("issue12516.xml");

        assertThat(report).hasSize(2);
        var first = report.get(0);
        assertThat(first)
                .hasLineStart(19).hasLineEnd(19 + 68 - 1)
                .hasFileName("csci07/csc60/remote_copy.sh")
                .hasSeverity(Severity.WARNING_HIGH);
        var second = report.get(1);
        assertThat(second)
                .hasLineStart(19).hasLineEnd(19 + 68 - 1)
                .hasFileName("csci08/csc90/remote_copy.sh")
                .hasSeverity(Severity.WARNING_HIGH);

        var additionalProperties = first.getAdditionalProperties();
        assertThat(additionalProperties).isEqualTo(second.getAdditionalProperties());
        assertThat(additionalProperties).isInstanceOf(DuplicationGroup.class);
        assertThat(((DuplicationGroup) additionalProperties).getCodeFragment()).isEqualTo(CODE_FRAGMENT);
    }

    /**
     * Verifies the parser on a report that contains four duplication (in two files each). The report is using
     * ISO-8859-1 encoding.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-22356">Issue 22356</a>
     */
    @Test
    void issue22356() {
        var fileName = "issue22356.xml";
        var report = parse(fileName);

        assertThat(report).hasSize(8);
    }

    /**
     * Verifies the parser on a report that contains 1 duplication (i.e., 2 warnings).
     */
    @Test
    void scanFileWithOneDuplication() {
        var fileName = "one-cpd.xml";
        var report = parse(fileName);

        assertThat(report).hasSize(2);

        assertThatReporterAndPublisherDuplicationsAreCorrectlyLinked(report.get(0), report.get(1));
    }

    private void assertThatReporterAndPublisherDuplicationsAreCorrectlyLinked(final Issue reporterFirst,
            final Issue publisherFirst) {
        assertThat(reporterFirst)
                .hasLineStart(76).hasLineEnd(76 + 36 - 1)
                .hasFileName(FILE_NAME_REPORTER)
                .hasSeverity(Severity.WARNING_NORMAL);
        assertThat(publisherFirst)
                .hasLineStart(69).hasLineEnd(69 + 36 - 1)
                .hasFileName(FILE_NAME_PUBLISHER)
                .hasSeverity(Severity.WARNING_NORMAL);
        var additionalProperties = reporterFirst.getAdditionalProperties();
        assertThat(additionalProperties).isEqualTo(publisherFirst.getAdditionalProperties());

        assertThat(additionalProperties).isInstanceOf(DuplicationGroup.class);
        assertThat(((DuplicationGroup) additionalProperties).getCodeFragment()).isNotEmpty();
    }

    @Test
    void shouldIgnoreOtherFile() {
        var report = parse("otherfile.xml");

        assertThat(report).hasSize(0);
    }

    @Test
    void shouldReadFileWithWindowsEncoding() {
        var report = parse("pmd-cpd.xml");

        assertThat(report).hasSize(29);
    }
}
