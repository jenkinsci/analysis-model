package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

/**
 * Tests the class {@link BuckminsterParser}.
 */
class BuckminsterParserTest extends AbstractParserTest {
    private static final String CATEGORY = DEFAULT_CATEGORY;

    protected BuckminsterParserTest() {
        super("buckminster.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(3);

        softly.assertThat(report.get(0)).hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(43)
                .hasLineEnd(43)
                .hasMessage("ArrayList is a raw type. References to generic type ArrayList<E> should be parameterized")
                .hasFileName(
                        "/var/lib/hudson/jobs/MailApp/workspace/plugins/org.eclipse.buckminster.tutorial.mailapp/src/org/eclipse/buckminster/tutorial/mailapp/NavigationView.java");
        softly.assertThat(report.get(1)).hasSeverity(Severity.WARNING_HIGH)
                .hasCategory(CATEGORY)
                .hasLineStart(57)
                .hasLineEnd(57)
                .hasMessage(
                        "IssueType safety: The method toArray(Object[]) belongs to the raw type ArrayList. References to generic type ArrayList<E> should be parameterized")
                .hasFileName(
                        "/var/lib/hudson/jobs/MailApp/workspace/plugins/org.eclipse.buckminster.tutorial.mailapp/src/org/eclipse/buckminster/tutorial/mailapp/NavigationView.java");
        softly.assertThat(report.get(2)).hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage(
                        "Build path specifies execution environment J2SE-1.5. There are no JREs installed in the workspace that are strictly compatible with this environment.")
                .hasFileName("/var/lib/hudson/jobs/MailApp/workspace/plugins/org.eclipse.buckminster.tutorial.mailapp");
    }

    @Override
    protected BuckminsterParser createParser() {
        return new BuckminsterParser();
    }
}
