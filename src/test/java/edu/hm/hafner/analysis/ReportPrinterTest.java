package edu.hm.hafner.analysis;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import edu.hm.hafner.analysis.Report.IssuePrinter;
import edu.hm.hafner.analysis.Report.JavaUtilLoggingAdapter;
import edu.hm.hafner.analysis.Report.SLF4Adapter;

import edu.hm.hafner.analysis.Report.StandardOutputPrinter;
import edu.hm.hafner.analysis.parser.checkstyle.CheckStyleParser;
import edu.hm.hafner.util.ResourceTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests printing of reports using the {@link Report#print(Report.IssuePrinter)} method.
 *
 * @author Ullrich Hafner
 */
// TODO: Move implementation to ReportTest
class ReportPrinterTest extends ResourceTest {
    @BeforeAll
    static void beforeAll() {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    @Test
    void shouldLogNormalWarningJavaUtilLogger() {

        Report report = new Report();
        Issue issue = new IssueBuilder().setSeverity(Severity.WARNING_NORMAL).setMessage("JavaUtilLogger WARNING_NORMAL").build();
        report.add(issue);
        java.util.logging.Logger logger = mock(Logger.getLogger("edu.hm.hafner.analysis.Report.JavaUtilLoggingAdapter").getClass());
        report.print(new JavaUtilLoggingAdapter(logger));
        verify(logger).log(Level.INFO, issue.toString());
    }

    @Test
    void shouldLogLowWarningJavaUtilLogger() {

        Report report = new Report();
        Issue issue = new IssueBuilder().setSeverity(Severity.WARNING_LOW).setMessage("JavaUtilLogger WARNING_LOW").build();
        report.add(issue);
        java.util.logging.Logger logger = mock(Logger.getLogger("edu.hm.hafner.analysis.Report.JavaUtilLoggingAdapter").getClass());
        report.print(new JavaUtilLoggingAdapter(logger));
        verify(logger).log(Level.FINE, issue.toString());
    }


    @Test
    void shouldLogHighWarningJavaUtilLogger() {

        Report report = new Report();
        Issue issue = new IssueBuilder().setSeverity(Severity.WARNING_HIGH).setMessage("JavaUtilLogger WARNING_HIGH").build();
        report.add(issue);
        java.util.logging.Logger logger = mock(Logger.getLogger("edu.hm.hafner.analysis.Report.JavaUtilLoggingAdapter").getClass());
        report.print(new JavaUtilLoggingAdapter(logger));
        verify(logger).log(Level.WARNING, issue.toString());
    }

    @Test
    void shouldLogErrorJavaUtilLogger() {

        Report report = new Report();
        Issue issue = new IssueBuilder().setSeverity(Severity.ERROR).setMessage("JavaUtilLogger ERROR").build();
        report.add(issue);
        java.util.logging.Logger logger = mock(Logger.getLogger("edu.hm.hafner.analysis.Report.JavaUtilLoggingAdapter").getClass());
        report.print(new JavaUtilLoggingAdapter(logger));
        verify(logger).log(Level.SEVERE, issue.toString());
    }

    @Test
    void shouldLogErrorSLF4JLogger() {

        Report report = new Report();
        Issue issue = new IssueBuilder().setSeverity(Severity.ERROR).setMessage("SLF4J ERROR").build();
        report.add(issue);
        org.slf4j.Logger logger = mock(LoggerFactory.getLogger("slf4j").getClass());
        report.print(new SLF4Adapter(logger));
        verify(logger).error(issue.toString());
    }

    @Test
    void shouldLogHighWarningSLF4JLogger() {

        Report report = new Report();
        Issue issue = new IssueBuilder().setSeverity(Severity.WARNING_HIGH).setMessage("SLF4J WARNING_HIGH").build();
        report.add(issue);
        org.slf4j.Logger logger = mock(LoggerFactory.getLogger("slf4j").getClass());
        report.print(new SLF4Adapter(logger));
        verify(logger).warn(issue.toString());
    }

    @Test
    void shouldLogNormalWarningSLF4JLogger() {

        Report report = new Report();
        Issue issue = new IssueBuilder().setSeverity(Severity.WARNING_NORMAL).setMessage("SLF4J ERROR").build();
        report.add(issue);
        org.slf4j.Logger logger = mock(LoggerFactory.getLogger("slf4j").getClass());
        report.print(new SLF4Adapter(logger));
        verify(logger).info(issue.toString());
    }

    @Test
    void shouldLogLowWarningSLF4JLogger() {

        Report report = new Report();
        Issue issue = new IssueBuilder().setSeverity(Severity.WARNING_LOW).setMessage("SLF4J ERROR").build();
        report.add(issue);
        org.slf4j.Logger logger = mock(LoggerFactory.getLogger("slf4j").getClass());
        report.print(new SLF4Adapter(logger));
        verify(logger).trace(issue.toString());
    }


    @Test
    void shouldPrintAllIssues() {
        Report report = readCheckStyleReport();

        IssuePrinter printer = mock(IssuePrinter.class);
        report.print(printer);

        for (Issue issue : report) {
            verify(printer).print(issue);
        }
    }

    private Report readCheckStyleReport() {
        Report report = new CheckStyleParser().parse(read("parser/checkstyle/all-severities.xml"));
        report.add(new IssueBuilder().setSeverity(Severity.WARNING_HIGH).setMessage("Severity High warning").build());
        assertThat(report).hasSize(4);
        assertThat(report.getSeverities()).hasSize(4);
        return report;
    }

    private ReaderFactory read(final String fileName) {
        return new FileReaderFactory(getResourceAsFile(fileName), StandardCharsets.UTF_8);
    }
}
