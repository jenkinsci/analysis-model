package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

class OwaspDependencyCheckParserTest extends AbstractParserTest {
    protected OwaspDependencyCheckParserTest() {
        super("dependency-check-report.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(2).hasDuplicatesSize(0);
        softly.assertThat(report.get(0))
                .hasFileName("commons-beanutils-1.8.3.jar")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("NETWORK")
                .hasType("CVE-2014-0114")
                .hasMessage("Apache Commons BeanUtils, as distributed in lib/commons-beanutils-1.8.0.jar in Apache Struts 1.x through 1.3.10 and in other products requiring commons-beanutils through 1.9.2, does not suppress the class property, which allows remote attackers to \"manipulate\" the ClassLoader and execute arbitrary code via the class parameter, as demonstrated by the passing of this parameter to the getClass method of the ActionForm object in Struts 1.")
                .hasDescription("<a href=\"https://nvd.nist.gov/vuln/detail/CVE-2014-0114\">https://nvd.nist.gov/vuln/detail/CVE-2014-0114</a>");
        softly.assertThat(report.get(1))
                .hasFileName("commons-beanutils-1.8.3.jar")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("NETWORK")
                .hasType("CVE-2019-10086")
                .hasMessage("In Apache Commons Beanutils 1.9.2, a special BeanIntrospector class was added which allows suppressing the ability for an attacker to access the classloader via the class property available on all Java objects. We, however were not using this by default characteristic of the PropertyUtilsBean.")
                .hasDescription("<a href=\"https://nvd.nist.gov/vuln/detail/CVE-2019-10086\">https://nvd.nist.gov/vuln/detail/CVE-2019-10086</a>");
    }

    @Override
    protected IssueParser createParser() {
        return new OwaspDependencyCheckParser();
    }
}
