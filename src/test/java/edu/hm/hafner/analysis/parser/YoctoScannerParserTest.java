package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link YoctoScannerParser}.
 */
class YoctoScannerParserTest extends AbstractParserTest {
    YoctoScannerParserTest() {
        super("yocto_scanner_result.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(25);

        softly.assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_LOW)
                .hasFileName("acl")
                .hasType("CVE-2009-4411")
                .hasDescription("<div><b>Package: </b>acl</div> <div><b>Version: </b>2.3.2</div>"
                                + " <div><b>Link: </b><a href=\"https://nvd.nist.gov/vuln/detail/CVE-2009-4411\""
                                + ">https://nvd.nist.gov/vuln/detail/CVE-2009-4411</a></div>"
                                + " <div><b>Yocto Layer: </b>meta</div> <div><b>Vector: </b>LOCAL</div> <p>"
                                + "The (1) setfacl and (2) getfacl commands in XFS acl 2.2.47, when running"
                                + " in recursive (-R) mode, follow symbolic links even when the --physical"
                                + " (aka -P) or -L option is specified, which might allow local users to modify"
                                + " the ACL for arbitrary files or directories via a symlink attack.</p>");
        softly.assertThat(report.get(3))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasFileName("automake-native")
                .hasType("CVE-2012-3386");
        softly.assertThat(report.get(12))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasFileName("avahi")
                .hasType("CVE-2017-6519");
    }

    @Test
    void shouldHandleEmptyResultsJenkins67296() {
        Report report = parse("issue67296.json");

        assertThat(report).isEmpty();
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt")).isInstanceOf(ParsingException.class);
    }

    @Override
    protected IssueParser createParser() {
        return new YoctoScannerParser();
    }
}
