package edu.hm.hafner.analysis.parser;

import java.nio.file.FileSystems;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.FileReaderFactory;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link TfsecParser}.
 *
 * @author Akash Manna
 */
class TfsecParserTest extends AbstractParserTest {
    TfsecParserTest() {
        super("tfsec-report.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(3);

        softly.assertThat(report.get(0))
                .hasFileName("main.tf")
                .hasLineStart(5)
                .hasLineEnd(12)
                .hasColumnStart(1)
                .hasColumnEnd(1)
                .hasType("AVD-AWS-0001")
                .hasMessage("S3 bucket should have been encrypted")
                .hasSeverity(Severity.WARNING_HIGH);

        softly.assertThat(report.get(1))
                .hasFileName("storage.tf")
                .hasLineStart(15)
                .hasLineEnd(22)
                .hasColumnStart(1)
                .hasColumnEnd(1)
                .hasType("AVD-AWS-0002")
                .hasMessage("S3 bucket does not have logging enabled")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(2))
                .hasFileName("security.tf")
                .hasLineStart(8)
                .hasLineEnd(20)
                .hasColumnStart(1)
                .hasColumnEnd(1)
                .hasType("AVD-AWS-0003")
                .hasMessage("Security group should not allow unrestricted ingress")
                .hasSeverity(Severity.ERROR);
    }

    @Override
    protected IssueParser createParser() {
        return new TfsecParser();
    }

    @Test
    void accepts() {
        var parser = new TfsecParser();

        assertThat(parser.accepts(new FileReaderFactory(FileSystems.getDefault().getPath("tfsec-report.json"))))
                .isTrue();
        assertThat(parser.accepts(new FileReaderFactory(FileSystems.getDefault().getPath("tfsec-report.txt"))))
                .isFalse();
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt")).isInstanceOf(ParsingException.class);
    }
}
