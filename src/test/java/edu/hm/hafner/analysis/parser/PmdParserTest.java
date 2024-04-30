package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.parser.pmd.PmdParser;
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

        Report actionIssues = report.filter(Issue.byPackageName("com.avaloq.adt.env.internal.ui.actions"));
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
    void shouldCorrectlyMapLinesAndColumns() {
        Report report = parseInPmdFolder("lines-columns.xml");

        assertThat(report).hasSize(1);

        assertThat(report.get(0))
                .hasFileName(
                        "/Users/hafner/Development/jenkins/workspace/Pipeline/src/main/java/edu/hm/hafner/analysis/parser/AjcParser.java")
                .hasLineStart(30).hasLineEnd(74)
                .hasColumnStart(5).hasColumnEnd(12)
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
        Report report = parseInPmdFolder("issue54736.xml");

        assertThat(report).hasSize(4 + 21);
        assertThatReportHasSeverities(report, 21, 0, 4, 0);
        assertThat(report.get(4)).hasSeverity(Severity.ERROR)
                .hasFileName(
                        "/Users/jordillach/DemoTenants/Tenants/vhosts/pre.elperiodico.com/themes/default/articleTemplates/forceOpinion.s.jsp")
                .hasMessage(
                        "Error while parsing /Users/jordillach/DemoTenants/Tenants/vhosts/pre.elperiodico.com/themes/default/articleTemplates/forceOpinion.s.jsp")
                .hasDescription(
                        "\"<!--\" ...net.sourceforge.pmd.PMDException: Error while parsing /Users/jordillach/DemoTenants/Tenants/vhosts/pre.elperiodico.com/themes/default/articleTemplates/forceOpinion.s.jsp\n"
                                + "\tat net.sourceforge.pmd.SourceCodeProcessor.processSourceCode(SourceCodeProcessor.java:99)\n"
                                + "\tat net.sourceforge.pmd.SourceCodeProcessor.processSourceCode(SourceCodeProcessor.java:51)\n"
                                + "\tat net.sourceforge.pmd.processor.PmdRunnable.call(PmdRunnable.java:78)\n"
                                + "\tat net.sourceforge.pmd.processor.PmdRunnable.call(PmdRunnable.java:24)\n"
                                + "\tat java.util.concurrent.FutureTask.run(FutureTask.java:266)\n"
                                + "\tat java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:511)\n"
                                + "\tat java.util.concurrent.FutureTask.run(FutureTask.java:266)\n"
                                + "\tat java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)\n"
                                + "\tat java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)\n"
                                + "\tat java.lang.Thread.run(Thread.java:748)\n"
                                + "Caused by: net.sourceforge.pmd.lang.jsp.ast.ParseException: Encountered \" \"</\" \"</ \"\" at line 22, column 1.\n"
                                + "Was expecting one of:\n"
                                + "    <EOF>\n"
                                + "    \"<\" ...\n"
                                + "    \"<![CDATA[\" ...\n"
                                + "    \"<%--\" ...\n"
                                + "    \"<%!\" ...\n"
                                + "    \"<%=\" ...\n"
                                + "    \"<%\" ...\n"
                                + "    \"<%@\" ...\n"
                                + "    \"<script\" ...\n"
                                + "    <EL_EXPRESSION> ...\n"
                                + "    <UNPARSED_TEXT> ...\n"
                                + "    \n"
                                + "\tat net.sourceforge.pmd.lang.jsp.ast.JspParser.generateParseException(JspParser.java:1846)\n"
                                + "\tat net.sourceforge.pmd.lang.jsp.ast.JspParser.jj_consume_token(JspParser.java:1725)\n"
                                + "\tat net.sourceforge.pmd.lang.jsp.ast.JspParser.CompilationUnit(JspParser.java:55)\n"
                                + "\tat net.sourceforge.pmd.lang.jsp.JspParser.parse(JspParser.java:41)\n"
                                + "\tat net.sourceforge.pmd.SourceCodeProcessor.parse(SourceCodeProcessor.java:111)\n"
                                + "\tat net.sourceforge.pmd.SourceCodeProcessor.processSource(SourceCodeProcessor.java:175)\n"
                                + "\tat net.sourceforge.pmd.SourceCodeProcessor.processSourceCode(SourceCodeProcessor.java:96)\n"
                                + "\t... 9 more");
    }

    /**
     * Parses a warning log with 15 warnings.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-12801">Issue 12801</a>
     */
    @Test
    void issue12801() {
        Report report = parseInPmdFolder("issue12801.xml");

        assertThat(report).hasSize(2);
    }

    /**
     * Checks whether we correctly detect all 669 warnings.
     */
    @Test
    void scanFileWithSeveralWarnings() {
        Report report = parseInPmdFolder("pmd-report.xml");

        assertThat(report).hasSize(669);
    }

    /**
     * Checks whether we create messages with a single dot.
     */
    @Test
    void verifySingleDot() {
        String fileName = "warning-message-with-dot.xml";
        Report report = parseInPmdFolder(fileName);

        assertThat(report).hasSize(2);
        assertThat(report.get(0)).hasMessage("Avoid really long parameter lists.");
    }

    /**
     * Checks whether we correctly detect an empty file.
     */
    @Test
    void scanFileWithNoBugs() {
        Report report = parseInPmdFolder("empty.xml");

        assertThat(report).isEmpty();
    }

    /**
     * Checks whether we correctly parse a file with 4 warnings.
     */
    @Test
    void testEquals() {
        Report report = parseInPmdFolder("equals-test.xml");

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
