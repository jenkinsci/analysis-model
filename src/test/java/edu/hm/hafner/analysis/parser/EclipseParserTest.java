package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.ConsolePostProcessor;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

/**
 * Tests the class {@link EclipseParser}.
 */
public class EclipseParserTest extends AbstractEclipseParserTest {
    private static final String CATEGORY = DEFAULT_CATEGORY;

    /**
     * Parses a warning log with previously undetected warnings.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-21377">Issue 21377</a>
     */
    @Test
    public void issue21377() throws IOException {
        Issues warnings = createParser().parse(openFile("issue21377.txt"));

        assertEquals(1, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        checkWarning(iterator.next(),
                13,
                "The method getOldValue() from the type SomeType is deprecated",
                "/path/to/job/job-name/module/src/main/java/com/example/Example.java",
                TYPE, CATEGORY, Priority.NORMAL);
    }

    /**
     * Parses a warning log with 2 previously undetected warnings.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-13969">Issue 13969</a>
     */
    @Test
    public void issue13969() throws IOException {
        Issues warnings = createParser().parse(openFile("issue13969.txt"));

        assertEquals(3, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        checkWarning(iterator.next(),
                369,
                "The method compare(List<String>, List<String>) from the type PmModelImporter is never used locally",
                "/media/ssd/multi-x-processor/workspace/msgPM_Access/com.faktorzehn.pa2msgpm.core/src/com/faktorzehn/pa2msgpm/core/loader/PmModelImporter.java",
                TYPE, CATEGORY, Priority.NORMAL);
        checkWarning(iterator.next(),
                391,
                "The method getTableValues(PropertyRestrictionType) from the type PmModelImporter is never used locally",
                "/media/ssd/multi-x-processor/workspace/msgPM_Access/com.faktorzehn.pa2msgpm.core/src/com/faktorzehn/pa2msgpm/core/loader/PmModelImporter.java",
                TYPE, CATEGORY, Priority.NORMAL);
        checkWarning(iterator.next(),
                56,
                "The value of the field PropertyImporterTest.ERROR_RESPONSE is not used",
                "/media/ssd/multi-x-processor/workspace/msgPM_Access/com.faktorzehn.pa2msgpm.core.test/src/com/faktorzehn/pa2msgpm/core/importer/PropertyImporterTest.java",
                TYPE, CATEGORY, Priority.NORMAL);
    }

    /**
     * Parses a warning log with 15 warnings.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-12822">Issue 12822</a>
     */
    @Test
    public void issue12822() throws IOException {
        Issues warnings = createParser().parse(openFile("issue12822.txt"));

        assertEquals(15, warnings.size());
    }

    /**
     * Parses a warning log with console annotations which are removed.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-11675">Issue 11675</a>
     */
    @Test
    public void issue11675() throws IOException {
        EclipseParser parser = new EclipseParser();
        parser.setTransformer(new ConsolePostProcessor());
        Issues warnings = parser.parse(openFile("issue11675.txt"));

        assertEquals(8, warnings.size());

        for (Issue annotation : warnings) {
            checkWithAnnotation(annotation);
        }
    }

    private void checkWithAnnotation(final Issue annotation) {
        assertTrue("Wrong first character in message", annotation.getMessage().matches("[a-zA-Z].*"));
    }

    /**
     * Parses a warning log with a ClearCase command line that should not be parsed as a warning.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-6427">Issue 6427</a>
     */
    @Test
    public void issue6427() throws IOException {
        Issues warnings = createParser().parse(openFile("issue6427.txt"));

        assertEquals(18, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();
        checkWarning(annotation,
                10,
                "The import com.bombardier.oldinfra.export.dataAccess.InfrastructureDiagramAPI is never used",
                "/srv/hudson/workspace/Ebitool Trunk/build/plugins/com.bombardier.oldInfra.export.jet/jet2java/org/eclipse/jet/compiled/_jet_infraSoe.java",
                TYPE, CATEGORY, Priority.NORMAL);
    }

    /**
     * Parses a warning log with 2 eclipse messages, the affected source text spans one and two lines.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-7077">Issue 7077</a>
     */
    @Test
    public void issue7077() throws IOException {
        Issues warnings = createParser().parse(openFile("issue7077.txt"));

        assertEquals(2, warnings.size());

        checkWarning(warnings.get(0),
                90,
                "Type safety: The method setBoHandler(BoHandler) belongs to the raw type BoQuickSearchControl.Builder. References to generic type BoQuickSearchControl<S>.Builder<T> should be parameterized",
                "/ige/hudson/work/jobs/esvclient__development/workspace/target/rcp-build/plugins/ch.ipi.esv.client.customer/src/main/java/ch/ipi/esv/client/customer/search/CustomerQuickSearch.java",
                TYPE, CATEGORY, Priority.NORMAL);
        checkWarning(warnings.get(1),
                90,
                "Type safety: The expression of type BoQuickSearchControl needs unchecked conversion to conform to BoQuickSearchControl<CustomerBO>",
                "/ige/hudson/work/jobs/esvclient__development/workspace/target/rcp-build/plugins/ch.ipi.esv.client.customer/src/main/java/ch/ipi/esv/client/customer/search/CustomerQuickSearch.java",
                TYPE, CATEGORY, Priority.NORMAL);
    }

    /**
     * Parses a warning log with several eclipse messages.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-7077">Issue 7077</a>
     */
    @Test
    public void issue7077all() throws IOException {
        Issues sorted = createParser().parse(openFile("issue7077-all.txt"));

        assertEquals(45, sorted.size());

        int number = 0;
        for (Issue fileAnnotation : sorted) {
            boolean containsHat = fileAnnotation.getMessage().contains("^");
            assertFalse("Message " + " contains ^" + number, containsHat);
            number++;
        }
    }
}

