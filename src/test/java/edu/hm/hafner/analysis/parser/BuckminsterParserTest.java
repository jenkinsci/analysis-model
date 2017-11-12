package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;


/**
 * Tests the class {@link BuckminsterParser}.
 */
public class BuckminsterParserTest extends ParserTester {
    private static final String CATEGORY = DEFAULT_CATEGORY;

    /**
     * Parses a file with three Buckminster warnings.
     */
    @Test
    public void testWarningsParser() {
        Issues<Issue> warnings = new BuckminsterParser().parse(openFile());
        assertThat(warnings).hasSize(3);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0)).hasPriority(Priority.NORMAL)
                    .hasCategory(CATEGORY)
                    .hasLineStart(43)
                    .hasLineEnd(43)
                    .hasMessage("ArrayList is a raw type. References to generic type ArrayList<E> should be parameterized")
                    .hasFileName("/var/lib/hudson/jobs/MailApp/workspace/plugins/org.eclipse.buckminster.tutorial.mailapp/src/org/eclipse/buckminster/tutorial/mailapp/NavigationView.java");
            softly.assertThat(warnings.get(1)).hasPriority(Priority.HIGH)
                    .hasCategory(CATEGORY)
                    .hasLineStart(57)
                    .hasLineEnd(57)
                    .hasMessage("Type safety: The method toArray(Object[]) belongs to the raw type ArrayList. References to generic type ArrayList<E> should be parameterized")
                    .hasFileName("/var/lib/hudson/jobs/MailApp/workspace/plugins/org.eclipse.buckminster.tutorial.mailapp/src/org/eclipse/buckminster/tutorial/mailapp/NavigationView.java");
            softly.assertThat(warnings.get(2)).hasPriority(Priority.NORMAL)
                    .hasCategory(CATEGORY)
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("Build path specifies execution environment J2SE-1.5. There are no JREs installed in the workspace that are strictly compatible with this environment.")
                    .hasFileName("/var/lib/hudson/jobs/MailApp/workspace/plugins/org.eclipse.buckminster.tutorial.mailapp");
        });
    }

    @Override
    protected String getWarningsFile() {
        return "buckminster.txt";
    }
}

