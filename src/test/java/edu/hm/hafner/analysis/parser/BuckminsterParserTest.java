package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

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
        Issues warnings = new BuckminsterParser().parse(openFile());

        assertEquals(3, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();
        checkWarning(annotation,
                43,
                "ArrayList is a raw type. References to generic type ArrayList<E> should be parameterized",
                "/var/lib/hudson/jobs/MailApp/workspace/plugins/org.eclipse.buckminster.tutorial.mailapp/src/org/eclipse/buckminster/tutorial/mailapp/NavigationView.java",
                TYPE, CATEGORY, Priority.NORMAL);
        annotation = iterator.next();
        checkWarning(annotation,
                57,
                "Type safety: The method toArray(Object[]) belongs to the raw type ArrayList. References to generic type ArrayList<E> should be parameterized",
                "/var/lib/hudson/jobs/MailApp/workspace/plugins/org.eclipse.buckminster.tutorial.mailapp/src/org/eclipse/buckminster/tutorial/mailapp/NavigationView.java",
                TYPE, CATEGORY, Priority.HIGH);
        annotation = iterator.next();
        checkWarning(annotation,
                0,
                "Build path specifies execution environment J2SE-1.5. There are no JREs installed in the workspace that are strictly compatible with this environment.",
                "/var/lib/hudson/jobs/MailApp/workspace/plugins/org.eclipse.buckminster.tutorial.mailapp",
                TYPE, CATEGORY, Priority.NORMAL);
    }

    @Override
    protected String getWarningsFile() {
        return "buckminster.txt";
    }
}

