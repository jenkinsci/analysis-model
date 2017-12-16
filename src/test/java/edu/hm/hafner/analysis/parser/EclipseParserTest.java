package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link EclipseParser}.
 */
class EclipseParserTest extends AbstractParserTest {
    private static final String CATEGORY = new IssueBuilder().build().getCategory();

    EclipseParserTest() {
        super("eclipse.txt");
    }

    @Override
    protected AbstractParser createParser() {
        return new EclipseParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {
        assertThat(issues).hasSize(8);

        Issue annotation = issues.get(0);
        softly.assertThat(annotation)
                .hasPriority(Priority.NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(3)
                .hasLineEnd(3)
                .hasMessage(
                        "The serializable class AttributeException does not declare a static final serialVersionUID field of type long")
                .hasFileName("C:/Desenvolvimento/Java/jfg/src/jfg/AttributeException.java");
    }

    /**
     * Parses a warning log with previously undetected warnings.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-21377">Issue 21377</a>
     */
    @Test
    void issue21377() {
        Issues<Issue> warnings = parse("issue21377.txt");

        assertThat(warnings).hasSize(1);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(CATEGORY)
                    .hasLineStart(13)
                    .hasLineEnd(13)
                    .hasColumnStart(15)
                    .hasColumnEnd(15 + 13)
                    .hasMessage("The method getOldValue() from the type SomeType is deprecated")
                    .hasFileName("/path/to/job/job-name/module/src/main/java/com/example/Example.java");
        });
    }

    /**
     * Parses a warning log with 2 previously undetected warnings.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-13969">Issue 13969</a>
     */
    @Test
    void issue13969() {
        Issues<Issue> warnings = parse("issue13969.txt");

        assertThat(warnings).hasSize(3);

        assertSoftly(softly -> {
            Iterator<Issue> iterator = warnings.iterator();
            softly.assertThat(iterator.next())
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(CATEGORY)
                    .hasLineStart(369)
                    .hasLineEnd(369)
                    .hasMessage("The method compare(List<String>, List<String>) from the type PmModelImporter is never used locally")
                    .hasFileName("/media/ssd/multi-x-processor/workspace/msgPM_Access/com.faktorzehn.pa2msgpm.core/src/com/faktorzehn/pa2msgpm/core/loader/PmModelImporter.java");
            softly.assertThat(iterator.next())
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(CATEGORY)
                    .hasLineStart(391)
                    .hasLineEnd(391)
                    .hasMessage("The method getTableValues(PropertyRestrictionType) from the type PmModelImporter is never used locally")
                    .hasFileName("/media/ssd/multi-x-processor/workspace/msgPM_Access/com.faktorzehn.pa2msgpm.core/src/com/faktorzehn/pa2msgpm/core/loader/PmModelImporter.java");
            softly.assertThat(iterator.next())
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(CATEGORY)
                    .hasLineStart(56)
                    .hasLineEnd(56)
                    .hasMessage("The value of the field PropertyImporterTest.ERROR_RESPONSE is not used")
                    .hasFileName("/media/ssd/multi-x-processor/workspace/msgPM_Access/com.faktorzehn.pa2msgpm.core.test/src/com/faktorzehn/pa2msgpm/core/importer/PropertyImporterTest.java");
        });
    }

    /**
     * Parses a warning log with 15 warnings.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-12822">Issue 12822</a>
     */
    @Test
    void issue12822() {
        Issues<Issue> warnings = parse("issue12822.txt");

        assertThat(warnings).hasSize(15);
    }

    /**
     * Parses a warning log with a ClearCase command line that should not be parsed as a warning.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-6427">Issue 6427</a>
     */
    @Test
    void issue6427() {
        Issues<Issue> warnings = parse("issue6427.txt");

        assertThat(warnings).hasSize(18);
        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(CATEGORY)
                    .hasLineStart(10)
                    .hasLineEnd(10)
                    .hasMessage("The import com.bombardier.oldinfra.export.dataAccess.InfrastructureDiagramAPI is never used")
                    .hasFileName("/srv/hudson/workspace/Ebitool Trunk/build/plugins/com.bombardier.oldInfra.export.jet/jet2java/org/eclipse/jet/compiled/_jet_infraSoe.java");
        });
    }

    /**
     * Parses a warning log with 2 eclipse messages, the affected source text spans one and two lines.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-7077">Issue 7077</a>
     */
    @Test
    void issue7077() {
        Issues<Issue> warnings = parse("issue7077.txt");

        assertThat(warnings).hasSize(2);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0)).hasPriority(Priority.NORMAL)
                    .hasCategory(CATEGORY)
                    .hasLineStart(90)
                    .hasLineEnd(90)
                    .hasMessage("Type safety: The method setBoHandler(BoHandler) belongs to the raw type BoQuickSearchControl.Builder. References to generic type BoQuickSearchControl<S>.Builder<T> should be parameterized")
                    .hasFileName("/ige/hudson/work/jobs/esvclient__development/workspace/target/rcp-build/plugins/ch.ipi.esv.client.customer/src/main/java/ch/ipi/esv/client/customer/search/CustomerQuickSearch.java");
            softly.assertThat(warnings.get(1)).hasPriority(Priority.NORMAL)
                    .hasCategory(CATEGORY)
                    .hasLineStart(90)
                    .hasLineEnd(90)
                    .hasMessage("Type safety: The expression of type BoQuickSearchControl needs unchecked conversion to conform to BoQuickSearchControl<CustomerBO>")
                    .hasFileName("/ige/hudson/work/jobs/esvclient__development/workspace/target/rcp-build/plugins/ch.ipi.esv.client.customer/src/main/java/ch/ipi/esv/client/customer/search/CustomerQuickSearch.java");
        });
    }

    /**
     * Parses a warning log with several eclipse messages.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-7077">Issue 7077</a>
     */
    @Test
    void issue7077all() {
        Issues<Issue> sorted = parse("issue7077-all.txt");

        assertThat(sorted).hasSize(45);

        int number = 0;
        for (Issue fileAnnotation : sorted) {
            boolean containsHat = fileAnnotation.getMessage().contains("^");
            assertThat(containsHat).isFalse().withFailMessage("Message " + number + " contains ^");
            number++;
        }
    }
}

