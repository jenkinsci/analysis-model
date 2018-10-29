package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractIssueParserTest;
import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.Report;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link EclipseParser}.
 */
public class EclipseParserTest extends AbstractIssueParserTest {
    /**
     * Creates a new test case that read.
     */
    protected EclipseParserTest() {
        super("eclipse.txt");
    }

    @Override
    protected AbstractParser createParser() {
        return new EclipseParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(8);

        Issue annotation = report.get(0);
        softly.assertThat(annotation)
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(3)
                .hasLineEnd(3)
                .hasMessage("The serializable class AttributeException does not declare a static final serialVersionUID field of type long")
                .hasFileName("C:/Desenvolvimento/Java/jfg/src/jfg/AttributeException.java");
    }

    /**
     * Parses a warning log with previously undetected warnings.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-21377">Issue 21377</a>
     */
    @Test
    void issue21377() {
        Report warnings = parse("issue21377.txt");

        assertThat(warnings).hasSize(1);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(13)
                    .hasLineEnd(13)
                    .hasColumnStart(11)
                    .hasColumnEnd(11 + 12)
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
        Report warnings = parse("issue13969.txt");

        assertThat(warnings).hasSize(3);

        assertSoftly(softly -> {
            Iterator<? extends Issue> iterator = warnings.iterator();
            softly.assertThat(iterator.next())
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(369)
                    .hasLineEnd(369)
                    .hasMessage("The method compare(List<String>, List<String>) from the type PmModelImporter is never used locally")
                    .hasFileName("/media/ssd/multi-x-processor/workspace/msgPM_Access/com.faktorzehn.pa2msgpm.core/src/com/faktorzehn/pa2msgpm/core/loader/PmModelImporter.java");
            softly.assertThat(iterator.next())
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(391)
                    .hasLineEnd(391)
                    .hasMessage("The method getTableValues(PropertyRestrictionType) from the type PmModelImporter is never used locally")
                    .hasFileName("/media/ssd/multi-x-processor/workspace/msgPM_Access/com.faktorzehn.pa2msgpm.core/src/com/faktorzehn/pa2msgpm/core/loader/PmModelImporter.java");
            softly.assertThat(iterator.next())
                    .hasSeverity(Severity.WARNING_NORMAL)
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
        Report warnings = parse("issue12822.txt");

        assertThat(warnings).hasSize(15);
    }

    /**
     * Parses a warning log with a ClearCase command line that should not be parsed as a warning.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-6427">Issue 6427</a>
     */
    @Test
    void issue6427() {
        Report warnings = parse("issue6427.txt");

        assertThat(warnings).hasSize(18);
        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasSeverity(Severity.WARNING_NORMAL)
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
        Report warnings = parse("issue7077.txt");

        assertThat(warnings).hasSize(2);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0)).hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(90)
                    .hasLineEnd(90)
                    .hasMessage("Type safety: The method setBoHandler(BoHandler) belongs to the raw type BoQuickSearchControl.Builder. References to generic type BoQuickSearchControl<S>.Builder<T> should be parameterized")
                    .hasFileName("/ige/hudson/work/jobs/esvclient__development/workspace/target/rcp-build/plugins/ch.ipi.esv.client.customer/src/main/java/ch/ipi/esv/client/customer/search/CustomerQuickSearch.java");
            softly.assertThat(warnings.get(1)).hasSeverity(Severity.WARNING_NORMAL)
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
        Report sorted = parse("issue7077-all.txt");

        assertThat(sorted).hasSize(45);

        int number = 0;
        for (Issue fileAnnotation : sorted) {
            boolean containsHat = fileAnnotation.getMessage().contains("^");
            assertThat(containsHat).withFailMessage("Message " + number + " contains ^").isFalse();
            number++;
        }
    }

    /**
     * Test for the info log level for the eclipse compiler.
     */
    @Test
    void infoLogLevel() {
        Report report = parse("eclipse-withinfo.txt");

        assertThat(report).hasSize(6);

        assertSoftly(softly -> {
            softly.assertThat(report.get(0))
                    .hasSeverity(Severity.ERROR)
                    .hasLineStart(8)
                    .hasLineEnd(8)
                    .hasColumnStart(13)
                    .hasColumnEnd(16)
                    .hasFileName("C:/devenv/workspace/x/y/src/main/java/y/ECE.java")
                    .hasMessage("Type mismatch: cannot convert from float to Integer");

            softly.assertThat(report.get(1))
                    .hasSeverity(Severity.ERROR)
                    .hasLineStart(16)
                    .hasLineEnd(16)
                    .hasColumnStart(8)
                    .hasColumnEnd(40)
                    .hasFileName("C:/devenv/workspace/x/y/src/main/java/y/ECE.java")
                    .hasMessage("Dead code");

            softly.assertThat(report.get(2))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(22)
                    .hasLineEnd(22)
                    .hasColumnStart(9)
                    .hasColumnEnd(9)
                    .hasFileName("C:/devenv/workspace/x/y/src/main/java/y/ECE.java")
                    .hasMessage("The value of the local variable x is not used");

            softly.assertThat(report.get(3))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(27)
                    .hasLineEnd(27)
                    .hasColumnStart(8)
                    .hasColumnEnd(40)
                    .hasFileName("C:/devenv/workspace/x/y/src/main/java/y/ECE.java")
                    .hasMessage(
                            "Statement unnecessarily nested within else clause. The corresponding then clause does not complete normally");

            softly.assertThat(report.get(4))
                    .hasSeverity(Severity.WARNING_LOW)
                    .hasLineStart(33)
                    .hasLineEnd(33)
                    .hasColumnStart(13)
                    .hasColumnEnd(18)
                    .hasFileName("C:/devenv/workspace/x/y/src/main/java/y/ECE.java")
                    .hasMessage("Comparing identical expressions");

            softly.assertThat(report.get(5))
                    .hasSeverity(Severity.WARNING_LOW)
                    .hasLineStart(35)
                    .hasLineEnd(35)
                    .hasColumnStart(1)
                    .hasColumnEnd(95)
                    .hasFileName("C:/devenv/workspace/x/y/src/main/java/y/ECE.java")
                    .hasMessage("The allocated object is never used");
        });
    }

    /**
     * Test for the info log level for the eclipse compiler.
     */
    @Test
    void columnCounting() {
        Report report = parse("eclipse-columns.txt");

        assertThat(report).hasSize(1);

        assertSoftly(softly -> {
            softly.assertThat(report.get(0))
                    .hasSeverity(Severity.ERROR)
                    .hasLineStart(2)
                    .hasLineEnd(2)
                    .hasColumnStart(1)
                    .hasColumnEnd(5)
                    .hasFileName("C:/TEMP/Column.java")
                    .hasMessage("Syntax error on token \"12345\", delete this token");
        });
    }

}

