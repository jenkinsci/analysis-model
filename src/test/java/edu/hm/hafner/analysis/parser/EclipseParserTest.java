package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Categories;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link EclipseParser}.
 */
class EclipseParserTest extends AbstractParserTest {
    EclipseParserTest() {
        super("eclipse.txt");
    }

    @Override
    protected EclipseParser createParser() {
        return new EclipseParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(8);

        var annotation = report.get(0);
        softly.assertThat(annotation)
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(3)
                .hasLineEnd(3)
                .hasMessage(
                        "The serializable class AttributeException does not declare a static final serialVersionUID field of type long")
                .hasFileName("C:/Desenvolvimento/Java/jfg/src/jfg/AttributeException.java")
                .hasCategory(Categories.OTHER);
    }

    /**
     * Parses a warning log with previously undetected warnings.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-55358">Issue 55358</a>
     */
    @Test
    void issue55358() {
        var warnings = parse("java-eclipse-ant.log");

        assertThat(warnings).hasSize(27);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(82)
                    .hasMessage("Class is a raw type. References to generic type Class<T> should be parameterized")
                    .hasFileName(
                            "C:/PCI_Dev/source/pcisvn/CUSTOMER/CUSTOMER_Market/Code/CUSTOMER_Market/src/CUSTOMER/AutomatedProcess/BackOffice/Dataset/CUSTOMERBaseDataset.java")
                    .hasCategory(Categories.OTHER);
        }
    }

    /**
     * Parses a warning log with 15 warnings.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-12822">Issue 12822</a>
     */
    @Test
    void issue12822() {
        var warnings = parse("issue12822.txt");

        assertThat(warnings).hasSize(15);
    }

    /**
     * Parses a warning log with a ClearCase command line that should not be parsed as a warning.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-6427">Issue 6427</a>
     */
    @Test
    void issue6427() {
        var warnings = parse("issue6427.txt");

        assertThat(warnings).hasSize(18);
        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(10)
                    .hasLineEnd(10)
                    .hasMessage(
                            "The import com.bombardier.oldinfra.export.dataAccess.InfrastructureDiagramAPI is never used")
                    .hasFileName(
                            "/srv/hudson/workspace/Ebitool Trunk/build/plugins/com.bombardier.oldInfra.export.jet/jet2java/org/eclipse/jet/compiled/_jet_infraSoe.java")
                    .hasCategory(Categories.OTHER);
        }
    }

    /**
     * Parses a warning log with 2 eclipse messages, the affected source text spans one and two lines.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-7077">Issue 7077</a>
     */
    @Test
    void issue7077() {
        var warnings = parse("issue7077.txt");

        assertThat(warnings).hasSize(2);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0)).hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(90)
                    .hasLineEnd(90)
                    .hasMessage(
                            "IssueType safety: The method setBoHandler(BoHandler) belongs to the raw type BoQuickSearchControl.Builder. References to generic type BoQuickSearchControl<S>.Builder<T> should be parameterized")
                    .hasFileName(
                            "/ige/hudson/work/jobs/esvclient__development/workspace/target/rcp-build/plugins/ch.ipi.esv.client.customer/src/main/java/ch/ipi/esv/client/customer/search/CustomerQuickSearch.java")
                    .hasCategory(Categories.OTHER);
            softly.assertThat(warnings.get(1)).hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(90)
                    .hasLineEnd(90)
                    .hasMessage(
                            "IssueType safety: The expression of type BoQuickSearchControl needs unchecked conversion to conform to BoQuickSearchControl<CustomerBO>")
                    .hasFileName(
                            "/ige/hudson/work/jobs/esvclient__development/workspace/target/rcp-build/plugins/ch.ipi.esv.client.customer/src/main/java/ch/ipi/esv/client/customer/search/CustomerQuickSearch.java")
                    .hasCategory(Categories.OTHER);
        }
    }

    /**
     * Parses a warning log with several eclipse messages.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-7077">Issue 7077</a>
     */
    @Test
    void issue7077all() {
        var sorted = parse("issue7077-all.txt");

        assertThat(sorted).hasSize(45);

        int number = 0;
        for (Issue fileAnnotation : sorted) {
            boolean containsHat = fileAnnotation.getMessage().contains("^");
            assertThat(containsHat).withFailMessage("Message " + number + " contains ^").isFalse();
            number++;
        }
    }

    @Test
    void issue57379() {
        var report = parse("issue57379.txt");

        assertThat(report).hasSize(1);
        try (var softly = new SoftAssertions()) {
            softly.assertThat(report.get(0))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(3)
                    .hasLineEnd(3)
                    .hasMessage(
                            "Javadoc: The method msd(double[], double) in the type File is not applicable for the arguments ()")
                    .hasFileName("C:/File.java")
                    .hasCategory(Categories.JAVADOC);
        }
    }

    /**
     * Test for the info log level for the eclipse compiler.
     */
    @Test
    void infoLogLevel() {
        var report = parse("eclipse-withinfo.txt");

        assertThat(report).hasSize(6);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(report.get(0))
                    .hasSeverity(Severity.ERROR)
                    .hasLineStart(8)
                    .hasLineEnd(8)
                    .hasFileName("C:/devenv/workspace/x/y/src/main/java/y/ECE.java")
                    .hasMessage("IssueType mismatch: cannot convert from float to Integer")
                    .hasCategory(Categories.OTHER);

            softly.assertThat(report.get(1))
                    .hasSeverity(Severity.ERROR)
                    .hasLineStart(16)
                    .hasLineEnd(16)
                    .hasFileName("C:/devenv/workspace/x/y/src/main/java/y/ECE.java")
                    .hasMessage("Dead code")
                    .hasCategory(Categories.OTHER);

            softly.assertThat(report.get(2))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(22)
                    .hasLineEnd(22)
                    .hasFileName("C:/devenv/workspace/x/y/src/main/java/y/ECE.java")
                    .hasMessage("The value of the local variable x is not used")
                    .hasCategory(Categories.OTHER);

            softly.assertThat(report.get(3))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(27)
                    .hasLineEnd(27)
                    .hasFileName("C:/devenv/workspace/x/y/src/main/java/y/ECE.java")
                    .hasMessage(
                            "Statement unnecessarily nested within else clause. The corresponding then clause does not complete normally")
                    .hasCategory(Categories.OTHER);

            softly.assertThat(report.get(4))
                    .hasSeverity(Severity.WARNING_LOW)
                    .hasLineStart(33)
                    .hasLineEnd(33)
                    .hasFileName("C:/devenv/workspace/x/y/src/main/java/y/ECE.java")
                    .hasMessage("Comparing identical expressions")
                    .hasCategory(Categories.OTHER);

            softly.assertThat(report.get(5))
                    .hasSeverity(Severity.WARNING_LOW)
                    .hasLineStart(35)
                    .hasLineEnd(35)
                    .hasFileName("C:/devenv/workspace/x/y/src/main/java/y/ECE.java")
                    .hasMessage("The allocated object is never used")
                    .hasCategory(Categories.OTHER);
        }
    }

    /**
     * Tests that warnings are categorized as {@code Code} or {@code JavaDoc}.
     */
    @Test
    void javadocCategory() {
        var warnings = parse("eclipse-withjavadoc.log");
        EclipseSharedChecks.verifyCategory(warnings);
    }

    @Test
    void shouldOnlyAcceptTextFiles() {
        var parser = createParser();

        assertThat(parser.accepts(createReaderFactory("eclipse-withinfo.txt"))).isTrue();
        assertThat(parser.accepts(createReaderFactory("eclipse-withinfo.xml"))).isFalse();
    }
}
