package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static org.assertj.core.api.InstanceOfAssertFactories.iterable;

class NpmAuditParserTest extends AbstractParserTest {
    protected NpmAuditParserTest() {
        super("npm-audit.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report)
                .hasSize(21)
                .asInstanceOf(iterable(Issue.class))
                .anySatisfy(issue -> softly.assertThat(issue)
                        .hasPackageName("serialize-javascript")
                        .hasMessage("Cross-Site Scripting in serialize-javascript")
                        .hasSeverity(Severity.WARNING_NORMAL)
                        .hasCategory("CWE-79")
                        .hasDescription("<p><b>serialize-javascript</b> <code>&lt;2.1.1</code></p> <p>Direct, Fix Available</p> <p><b>Score:</b> 4.2<br><b>Reference:</b> <a href=\"https://github.com/advisories/GHSA-h9rv-jmmf-4pgx\">https://github.com/advisories/GHSA-h9rv-jmmf-4pgx</a><br><b>Weakness:</b><ul><li>CWE-79</li></ul></p>"))
                .anySatisfy(issue -> softly.assertThat(issue)
                        .hasPackageName("serialize-javascript")
                        .hasMessage("Insecure serialization leading to RCE in serialize-javascript")
                        .hasSeverity(Severity.WARNING_HIGH)
                        .hasCategory("CWE-502")
                        .hasDescription("<p><b>serialize-javascript</b> <code>&lt;3.1.0</code></p> <p>Direct, Fix Available</p> <p><b>Score:</b> 8.1<br><b>Reference:</b> <a href=\"https://github.com/advisories/GHSA-hxcc-f52p-wc94\">https://github.com/advisories/GHSA-hxcc-f52p-wc94</a><br><b>Weakness:</b><ul><li>CWE-502</li></ul></p>"))
                .anySatisfy(issue -> softly.assertThat(issue)
                        .hasPackageName("send")
                        .hasMessage("Directory Traversal in send")
                        .hasSeverity(Severity.WARNING_LOW)
                        .hasCategory("CWE-22")
                        .hasDescription("<p><b>send</b> <code>&lt;0.8.4</code></p> <p>Direct, Fix Available</p> <p><b>Score:</b> 0<br><b>Reference:</b> <a href=\"https://github.com/advisories/GHSA-xwg4-93c6-3h42\">https://github.com/advisories/GHSA-xwg4-93c6-3h42</a><br><b>Weakness:</b><ul><li>CWE-22</li></ul></p>"))
                .anySatisfy(issue -> softly.assertThat(issue)
                        .hasPackageName("fresh")
                        .hasMessage("Regular Expression Denial of Service in fresh")
                        .hasSeverity(Severity.WARNING_LOW)
                        .hasCategory("Uncategorized")
                        .hasDescription("<p><b>fresh</b> <code>&lt;0.5.2</code></p> <p>Indirect, No Fix</p> <p><b>Score:</b> -</p>"))
                .anySatisfy(issue -> softly.assertThat(issue)
                        .hasPackageName("ssri")
                        .hasMessage("Regular Expression Denial of Service (ReDoS)")
                        .hasSeverity(Severity.ERROR)
                        .hasCategory("CWE-243")
                        .hasDescription("<p><b>ssri</b> <code>&gt;=5.2.2 &lt;6.0.2</code></p> <p>Direct, Fix Available</p> <p><b>Score:</b> -<br><b>Reference:</b> <a href=\"https://github.com/advisories/GHSA-vx3p-948g-6vhq\">https://github.com/advisories/GHSA-vx3p-948g-6vhq</a><br><b>Weakness:</b><ul><li>CWE-243</li><li>CWE-400</li></ul></p>"));
    }

    @Override
    protected IssueParser createParser() {
        return new NpmAuditParser();
    }
}
