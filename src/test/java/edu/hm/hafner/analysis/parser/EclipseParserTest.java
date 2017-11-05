package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.assertj.IssueAssert;


import edu.hm.hafner.analysis.ConsolePostProcessor;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.IssuesAssert;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

import org.junit.jupiter.api.Test;

import static java.time.Duration.*;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the class {@link EclipseParser}.
 */
public class EclipseParserTest extends AbstractEclipseParserTest {
    private static final String CATEGORY = DEFAULT_CATEGORY;

    /**
     * Parses a warning log with previously undetected warnings.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-21377">Issue 21377</a>
     */
    @Test
    public void issue21377() {
        Issues warnings = createParser().parse(openFile("issue21377.txt"));
        IssuesAssert.assertThat(warnings).hasSize(1);

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
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-13969">Issue 13969</a>
     */
    @Test
    public void issue13969() {
        Issues warnings = createParser().parse(openFile("issue13969.txt"));

        assertThat(warnings.size()).isEqualTo(3);

        Iterator<Issue> iterator = warnings.iterator();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(iterator.next()).hasPriority(Priority.NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(369)
                .hasLineEnd(369)
                .hasMessage("The method compare(List<String>, List<String>) from the type PmModelImporter is never used locally")
                .hasFileName("/media/ssd/multi-x-processor/workspace/msgPM_Access/com.faktorzehn.pa2msgpm.core/src/com/faktorzehn/pa2msgpm/core/loader/PmModelImporter.java")
                .hasType(TYPE);
        softly.assertAll();
        softly.assertThat(iterator.next()).hasPriority(Priority.NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(391)
                .hasLineEnd(391)
                .hasMessage("The method getTableValues(PropertyRestrictionType) from the type PmModelImporter is never used locally")
                .hasFileName("/media/ssd/multi-x-processor/workspace/msgPM_Access/com.faktorzehn.pa2msgpm.core/src/com/faktorzehn/pa2msgpm/core/loader/PmModelImporter.java")
                .hasType(TYPE);
        softly.assertAll();
        softly.assertThat(iterator.next()).hasPriority(Priority.NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(56)
                .hasLineEnd(56)
                .hasMessage("The value of the field PropertyImporterTest.ERROR_RESPONSE is not used")
                .hasFileName("/media/ssd/multi-x-processor/workspace/msgPM_Access/com.faktorzehn.pa2msgpm.core.test/src/com/faktorzehn/pa2msgpm/core/importer/PropertyImporterTest.java")
                .hasType(TYPE);
        softly.assertAll();
    }

    /**
     * Parses a warning log with 15 warnings.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-12822">Issue 12822</a>
     */
    @Test
    public void issue12822() {
        Issues warnings = createParser().parse(openFile("issue12822.txt"));

        IssuesAssert.assertThat(warnings).hasSize(15);
    }

    /**
     * Parses a warning log with console annotations which are removed.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-11675">Issue 11675</a>
     */
    @Test
    public void issue11675() {
        EclipseParser parser = new EclipseParser();
        parser.setTransformer(new ConsolePostProcessor());
        Issues warnings = parser.parse(openFile("issue11675.txt"));

        IssuesAssert.assertThat(warnings).hasSize(8);
        
        for (Issue annotation : warnings) {
            checkWithAnnotation(annotation);
        }
    }

    private void checkWithAnnotation(final Issue annotation) {
        IssueAssert.assertThat(annotation).messageMatchTo("[a-zA-Z].*");
    }

    /**
     * Parses a warning log with a ClearCase command line that should not be parsed as a warning.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-6427">Issue 6427</a>
     */
    @Test
    public void issue6427() {
        Issues warnings = createParser().parse(openFile("issue6427.txt"));

        assertThat(warnings.size()).isEqualTo(18);

        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(annotation).hasPriority(Priority.NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(10)
                .hasLineEnd(10)
                .hasMessage("The import com.bombardier.oldinfra.export.dataAccess.InfrastructureDiagramAPI is never used")
                .hasFileName("/srv/hudson/workspace/Ebitool Trunk/build/plugins/com.bombardier.oldInfra.export.jet/jet2java/org/eclipse/jet/compiled/_jet_infraSoe.java")
                .hasType(TYPE);
        softly.assertAll();

    }

    /**
     * Parses a warning log with 2 eclipse messages, the affected source text spans one and two lines.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-7077">Issue 7077</a>
     */
    @Test
    public void issue7077() {
        Issues warnings = createParser().parse(openFile("issue7077.txt"));

        assertThat(warnings.size()).isEqualTo(2);

        IssueAssert.assertThat(warnings.get(0)).hasPriority(Priority.NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(90)
                .hasLineEnd(90)
                .hasMessage("Type safety: The method setBoHandler(BoHandler) belongs to the raw type BoQuickSearchControl.Builder. References to generic type BoQuickSearchControl<S>.Builder<T> should be parameterized")
                .hasFileName("/ige/hudson/work/jobs/esvclient__development/workspace/target/rcp-build/plugins/ch.ipi.esv.client.customer/src/main/java/ch/ipi/esv/client/customer/search/CustomerQuickSearch.java")
                .hasType(TYPE);


        IssueAssert.assertThat(warnings.get(1)).hasPriority(Priority.NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(90)
                .hasLineEnd(90)
                .hasMessage("Type safety: The expression of type BoQuickSearchControl needs unchecked conversion to conform to BoQuickSearchControl<CustomerBO>")
                .hasFileName("/ige/hudson/work/jobs/esvclient__development/workspace/target/rcp-build/plugins/ch.ipi.esv.client.customer/src/main/java/ch/ipi/esv/client/customer/search/CustomerQuickSearch.java")
                .hasType(TYPE);

    }

    /**
     * Parses a warning log with several eclipse messages.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-7077">Issue 7077</a>
     */
    @Test
    public void issue7077all() {
        Issues sorted = createParser().parse(openFile("issue7077-all.txt"));

        assertThat(sorted.size()).isEqualTo(45);

        int number = 0;
        for (Issue fileAnnotation : sorted) {
            boolean containsHat = fileAnnotation.getMessage().contains("^");
            assertThat(containsHat).isFalse().withFailMessage("Message " + number + " contains ^");
            number++;
        }
    }

    /**
     * Parses a warning log which doesn't contain any Eclipse warnings, but shows some pretty bad performance when
     * matching the regular expression.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-27664">Issue 27664</a>
     */
    @Test
    public void issue27664() {

        assertTimeout(ofSeconds(10), () -> {
            Issues warnings = createParser().parse(openFile("issue27664.txt"));

            assertThat(warnings.size()).isEqualTo(0);
        }, "Parsing took more than 5 seconds");
    }
}

