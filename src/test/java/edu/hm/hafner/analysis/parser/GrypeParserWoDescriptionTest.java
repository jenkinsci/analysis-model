package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

import static j2html.TagCreator.a;
import static j2html.TagCreator.p;

class GrypeParserWoDescriptionTest extends AbstractParserTest {
    protected GrypeParserWoDescriptionTest() {
        super("grype-report-wo-description.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(20).hasDuplicatesSize(13);
        softly.assertThat(report.get(0))
                .hasFileName("/usr/local/bin/environment-to-ini")
                .hasSeverity(Severity.ERROR)
                .hasCategory("Critical")
                .hasType("GHSA-pg38-r834-g45j")
                .hasMessage("Improper Privilege Management in Gitea")
                .hasDescription(p().with(a()
                        .withHref("https://github.com/advisories/GHSA-pg38-r834-g45j")
                        .withText("https://github.com/advisories/GHSA-pg38-r834-g45j")).render());

        softly.assertThat(report.get(13))
                .hasFileName("/lib/apk/db/installed")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("High")
                .hasType("CVE-2023-38039")
                .hasMessage("Unknown")

                .hasDescription(p().with(a()
                        .withHref("http://cve.mitre.org/cgi-bin/cvename.cgi?name=CVE-2023-38039")
                        .withText("http://cve.mitre.org/cgi-bin/cvename.cgi?name=CVE-2023-38039")).render());
    }

    @Override
    protected IssueParser createParser() {
        return new GrypeParser();
    }
}
