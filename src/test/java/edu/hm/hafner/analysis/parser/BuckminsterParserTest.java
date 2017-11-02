//Sarah Hofstätter
package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link BuckminsterParser}.
 */
public class BuckminsterParserTest extends ParserTester {
    private static final String TYPE = new BuckminsterParser().getId();
    private static final String CATEGORY = DEFAULT_CATEGORY;

    /**
     * Parses a file with three Buckminster warnings.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void testWarningsParser() throws IOException {
        SoftAssertions softly = new SoftAssertions();
        Issues warnings = new BuckminsterParser().parse(openFile());
        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation2 = iterator.next();
        Issue annotation1 = iterator.next();
        Issue annotation = iterator.next();

        softly.assertThat(warnings).hasSize(3);
        softly.assertThat(annotation2).hasPriority(Priority.NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(43)
                .hasLineEnd(43)
                .hasMessage("ArrayList is a raw type. References to generic type ArrayList<E> should be parameterized")
                .hasFileName("/var/lib/hudson/jobs/MailApp/workspace/plugins/org.eclipse.buckminster.tutorial.mailapp/src/org/eclipse/buckminster/tutorial/mailapp/NavigationView.java")
                .hasType(TYPE);
        softly.assertThat(annotation1).hasPriority(Priority.HIGH)
                .hasCategory(CATEGORY)
                .hasLineStart(57)
                .hasLineEnd(57)
                .hasMessage("Type safety: The method toArray(Object[]) belongs to the raw type ArrayList. References to generic type ArrayList<E> should be parameterized")
                .hasFileName("/var/lib/hudson/jobs/MailApp/workspace/plugins/org.eclipse.buckminster.tutorial.mailapp/src/org/eclipse/buckminster/tutorial/mailapp/NavigationView.java")
                .hasType(TYPE);
        softly.assertThat(annotation).hasPriority(Priority.NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("Build path specifies execution environment J2SE-1.5. There are no JREs installed in the workspace that are strictly compatible with this environment.")
                .hasFileName("/var/lib/hudson/jobs/MailApp/workspace/plugins/org.eclipse.buckminster.tutorial.mailapp")
                .hasType(TYPE);
    }

    @Override
    protected String getWarningsFile() {
        return "buckminster.txt";
    }
}

