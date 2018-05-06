package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractIssueParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link BuckminsterParser}.
 */
class BuckminsterParserTest extends AbstractIssueParserTest {
    private static final String CATEGORY = DEFAULT_CATEGORY;

    protected BuckminsterParserTest() {
        super("buckminster.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(3);

        softly.assertThat(report.get(0)).hasPriority(Priority.NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(43)
                .hasLineEnd(43)
                .hasMessage("ArrayList is a raw type. References to generic type ArrayList<E> should be parameterized")
                .hasFileName("/var/lib/hudson/jobs/MailApp/workspace/plugins/org.eclipse.buckminster.tutorial.mailapp/src/org/eclipse/buckminster/tutorial/mailapp/NavigationView.java");
        softly.assertThat(report.get(1)).hasPriority(Priority.HIGH)
                .hasCategory(CATEGORY)
                .hasLineStart(57)
                .hasLineEnd(57)
                .hasMessage("Type safety: The method toArray(Object[]) belongs to the raw type ArrayList. References to generic type ArrayList<E> should be parameterized")
                .hasFileName(
                        "/var/lib/hudson/jobs/MailApp/workspace/plugins/org.eclipse.buckminster.tutorial.mailapp/src/org/eclipse/buckminster/tutorial/mailapp/NavigationView.java");
        softly.assertThat(report.get(2)).hasPriority(Priority.NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("Build path specifies execution environment J2SE-1.5. There are no JREs installed in the workspace that are strictly compatible with this environment.")
                .hasFileName("/var/lib/hudson/jobs/MailApp/workspace/plugins/org.eclipse.buckminster.tutorial.mailapp");
    }

    @Override
    protected BuckminsterParser createParser() {
        return new BuckminsterParser();
    }
}

