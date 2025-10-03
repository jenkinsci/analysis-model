package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the extraction of PMD analysis results.
 */
class PmdParserTest extends AbstractParserTest {
    private static final String PREFIX = "pmd/";

    PmdParserTest() {
        super(PREFIX + "4-pmd-warnings.xml");
    }

    @Override
    protected PmdParser createParser() {
        return new PmdParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(4);

        var actionIssues = report.filter(Issue.byPackageName("com.avaloq.adt.env.internal.ui.actions"));
        softly.assertThat(actionIssues).hasSize(1);
        softly.assertThat(report.filter(Issue.byPackageName("com.avaloq.adt.env.internal.ui.actions"))).hasSize(1);
        softly.assertThat(report.filter(Issue.byPackageName("com.avaloq.adt.env.internal.ui.dialogs"))).hasSize(2);

        assertThatReportHasSeverities(report, 0, 1, 2, 1);

        softly.assertThat(actionIssues.get(0))
                .hasMessage("These nested if statements could be combined.")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("Basic")
                .hasType("CollapsibleIfStatements")
                .hasLineStart(54)
                .hasLineEnd(61)
                .hasPackageName("com.avaloq.adt.env.internal.ui.actions")
                .hasFileName(
                        "C:/Build/Results/jobs/ADT-Base/workspace/com.avaloq.adt.ui/src/main/java/com/avaloq/adt/env/internal/ui/actions/CopyToClipboard.java");
    }

    @Test
    void showFileNameWhenReportIsBroken() {
        assertThatExceptionOfType(ParsingException.class).isThrownBy(
                        () -> parseInPmdFolder("otherfile.xml"))
                .withMessageContaining("target/test-classes/edu/hm/hafner/analysis/parser/pmd/otherfile.xml");
    }

    @Test
    void shouldCorrectlyMapLinesAndColumns() {
        var report = parseInPmdFolder("lines-columns.xml");

        assertThat(report).hasSize(1);

        assertThat(report.get(0))
                .hasFileName(
                        "/Users/hafner/Development/jenkins/workspace/Pipeline/src/main/java/edu/hm/hafner/analysis/parser/AjcParser.java")
                .hasLineStart(30).hasLineEnd(74)
                .hasColumnStart(12).hasColumnEnd(5)
                .hasType("CyclomaticComplexity")
                .hasCategory("Code Size")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("The method 'parse' has a Cyclomatic Complexity of 10.");
    }

    /**
     * Parses a warning log with errors.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-54736">Issue 54736</a>
     */
    @Test
    void issue54736() {
        var report = parseInPmdFolder("issue54736.xml");

        assertThat(report).hasSize(4 + 21);
        assertThatReportHasSeverities(report, 21, 0, 4, 0);
        assertThat(report.get(4)).hasSeverity(Severity.ERROR)
                .hasFileName(
                        "/Users/jordillach/DemoTenants/Tenants/vhosts/pre.elperiodico.com/themes/default/articleTemplates/forceOpinion.s.jsp")
                .hasMessage(
                        "Error while parsing /Users/jordillach/DemoTenants/Tenants/vhosts/pre.elperiodico.com/themes/default/articleTemplates/forceOpinion.s.jsp");
        assertThat(report.get(4).getDescription()).isEqualToIgnoringWhitespace(
                """
                        "<!--" ...net.sourceforge.pmd.PMDException: Error while parsing /Users/jordillach/DemoTenants/Tenants/vhosts/pre.elperiodico.com/themes/default/articleTemplates/forceOpinion.s.jsp
                            at net.sourceforge.pmd.SourceCodeProcessor.processSourceCode(SourceCodeProcessor.java:99)
                            at net.sourceforge.pmd.SourceCodeProcessor.processSourceCode(SourceCodeProcessor.java:51)
                            at net.sourceforge.pmd.processor.PmdRunnable.call(PmdRunnable.java:78)
                            at net.sourceforge.pmd.processor.PmdRunnable.call(PmdRunnable.java:24)
                            at java.util.concurrent.FutureTask.run(FutureTask.java:266)
                            at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:511)
                            at java.util.concurrent.FutureTask.run(FutureTask.java:266)
                            at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
                            at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
                            at java.lang.Thread.run(Thread.java:748)
                        Caused by: net.sourceforge.pmd.lang.jsp.ast.ParseException: Encountered " "</" "</ "" at line 22, column 1.
                        Was expecting one of:
                            <EOF>
                            "<" ...
                            "<![CDATA[" ...
                            "<%--" ...
                            "<%!" ...
                            "<%=" ...
                            "<%" ...
                            "<%@" ...
                            "<script" ...
                            <EL_EXPRESSION> ...
                            <UNPARSED_TEXT> ...
                           \s
                            at net.sourceforge.pmd.lang.jsp.ast.JspParser.generateParseException(JspParser.java:1846)
                            at net.sourceforge.pmd.lang.jsp.ast.JspParser.jj_consume_token(JspParser.java:1725)
                            at net.sourceforge.pmd.lang.jsp.ast.JspParser.CompilationUnit(JspParser.java:55)
                            at net.sourceforge.pmd.lang.jsp.JspParser.parse(JspParser.java:41)
                            at net.sourceforge.pmd.SourceCodeProcessor.parse(SourceCodeProcessor.java:111)
                            at net.sourceforge.pmd.SourceCodeProcessor.processSource(SourceCodeProcessor.java:175)
                            at net.sourceforge.pmd.SourceCodeProcessor.processSourceCode(SourceCodeProcessor.java:96)
                            ... 9 more""");
    }

    /**
     * Parses a warning log with 15 warnings.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-12801">Issue 12801</a>
     */
    @Test
    void issue12801() {
        var report = parseInPmdFolder("issue12801.xml");

        assertThat(report).hasSize(2);
    }

    /**
     * Checks whether we correctly detect all 669 warnings.
     */
    @Test
    void scanFileWithSeveralWarnings() {
        var report = parseInPmdFolder("pmd-report.xml");

        assertThat(report).hasSize(669);
    }

    /**
     * Checks whether we create messages with a single dot.
     */
    @Test
    void verifySingleDot() {
        var fileName = "warning-message-with-dot.xml";
        var report = parseInPmdFolder(fileName);

        assertThat(report).hasSize(2);
        assertThat(report.get(0)).hasMessage("Avoid really long parameter lists.");
    }

    /**
     * Checks whether we correctly detect an empty file.
     */
    @Test
    void scanFileWithNoBugs() {
        var report = parseInPmdFolder("empty.xml");

        assertThat(report).isEmpty();
    }

    /**
     * Checks whether we correctly parse a file with 4 warnings.
     */
    @Test
    void testEquals() {
        var report = parseInPmdFolder("equals-test.xml");

        int expectedSize = 4;
        assertThat(report).hasSize(expectedSize);
        assertThat(report.filter(Issue.byPackageName("com.avaloq.adt.env.core.db.plsqlCompletion"))).hasSize(
                expectedSize);
        assertThatReportHasSeverities(report, 0, 0, 4, 0);
    }

    private Report parseInPmdFolder(final String fileName) {
        return parse(PREFIX + fileName);
    }
}
