package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

/**
 * Tests the class {@link Gcc4Cc1Parser}.
 *
 * @author Akash Manna
 * @see <a href="https://issues.jenkins.io/browse/JENKINS-73509">Issue 73509</a>
 */
class Gcc4Cc1ParserTest extends AbstractParserTest {
    Gcc4Cc1ParserTest() {
        super("issue73509.txt");
    }

    @Override
    protected Gcc4Cc1Parser createParser() {
        return new Gcc4Cc1Parser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(2);

        softly.assertThat(report.get(0))
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage(
                        "&apos;void* _builtin_memset(void*, int, long unsigned int)&apos;: specified size 18446744073709551612 exceeds maximum object size 9223372036854775807 [-Wstringop-overflow=]")
                .hasFileName("-")
                .hasCategory("GCC warning")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(1))
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage(
                        "&apos;void* _builtin_memcpy(void*, const void*, long unsigned int)&apos;: specified size 18446744073709551612 exceeds maximum object size 9223372036854775807 [-Wstringop-overflow=]")
                .hasFileName("-")
                .hasCategory("GCC warning")
                .hasSeverity(Severity.WARNING_NORMAL);
    }
}
