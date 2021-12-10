package edu.hm.hafner.analysis.parser;

import java.nio.file.FileSystems;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.FileReaderFactory;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link BrakemanParser}.
 *
 * @author Justin Collins
 */
class BrakemanParserTest extends AbstractParserTest {
    BrakemanParserTest() {
        super("brakeman.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(32);

        softly.assertThat(report.get(0))
                .hasMessage("Unescaped parameter value: params[:y]")
                .hasCategory("Cross-Site Scripting")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasType("CrossSiteScripting")
                .hasFileName("app/views/widget/haml_test.html.haml")
                .hasFingerprint("01ff71dc776c03921089d8559dabd1a75480411ec7f1de7f2886659085c26045")
                .hasLineStart(6);

        softly.assertThat(report.get(20)).hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(10))
                .hasMessage(
                    "Directory traversal vulnerability in "
                    + "actionpack-page_caching 1.2.0 (CVE-2020-8159). Upgrade "
                    + "to actionpack-page_caching 1.2.2")
                .hasCategory("Directory Traversal")
                .hasSeverity(Severity.WARNING_LOW)
                .hasType("PageCachingCVE")
                .hasFileName("Gemfile")
                .hasLineStart(24);
    }

    @Override
    protected IssueParser createParser() {
        return new BrakemanParser();
    }

    @Test
    void accepts() {
        assertThat(new BrakemanParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("brakeman.json")))).isTrue();
        assertThat(new BrakemanParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("brakeman.xml")))).isFalse();
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt"))
                .isInstanceOf(ParsingException.class);
    }
}
