package edu.hm.hafner.analysis;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import edu.hm.hafner.analysis.Report.IssuePrinter;
import edu.hm.hafner.analysis.Report.JULAdapter;
import edu.hm.hafner.analysis.Report.SLF4JAdapter;
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
    void shouldPrintAllIssues() {
        Report report = readCheckStyleReport();

        IssuePrinter printer = mock(IssuePrinter.class);
        report.print(printer);

        for (Issue issue : report) {
            verify(printer).print(issue);
        }
    }

    @Test
    void shouldPrintAllIssuesToPrintStream() {
        Report report = readCheckStyleReport();

        PrintStream printStream = mock(PrintStream.class);
        report.print(new StandardOutputPrinter(printStream));

        for (Issue issue : report) {
            verify(printStream).println(issue.toString());
        }
    }

    /* Simple Logging Facade for Java Tests */

    @Test
    void shouldLogOneErrorSLF4J() {
        Report report = new Report();
        report.add(new IssueBuilder().setSeverity(Severity.ERROR).setMessage("Severity Error").build());
        Logger logger = mock(LoggerFactory.getLogger(SLF4JAdapter.class).getClass());
        report.print(new SLF4JAdapter(logger));

        for(Issue issue : report) {
            verify(logger).error(issue.toString());
            verify(logger, never()).trace(issue.toString());
            verify(logger, never()).info(issue.toString());
            verify(logger, never()).warn(issue.toString());
        }
    }

    @Test
    void shouldLogOneWarningHighSLF4J() {
        Report report = new Report();
        report.add(new IssueBuilder().setSeverity(Severity.WARNING_HIGH).setMessage("Severity Warning High").build());
        Logger logger = mock(LoggerFactory.getLogger(SLF4JAdapter.class).getClass());
        report.print(new SLF4JAdapter(logger));

        for(Issue issue : report) {
            verify(logger).warn(issue.toString());
            verify(logger, never()).error(issue.toString());
            verify(logger, never()).info(issue.toString());
            verify(logger, never()).trace(issue.toString());
        }
    }

    @Test
    void shouldLogOneWarningNormalSF4J() {
        Report report = new Report();
        report.add(new IssueBuilder().setSeverity(Severity.WARNING_NORMAL).setMessage("Severity Warning Normal").build());
        Logger logger = mock(LoggerFactory.getLogger(SLF4JAdapter.class).getClass());
        report.print(new SLF4JAdapter(logger));

        for(Issue issue : report) {
            verify(logger).info(issue.toString());
            verify(logger, never()).error(issue.toString());
            verify(logger, never()).trace(issue.toString());
            verify(logger, never()).warn(issue.toString());
        }
    }

    @Test
    void shouldLogOneWarningLowSLF4J() {
        Report report = new Report();
        report.add(new IssueBuilder().setSeverity(Severity.WARNING_LOW).setMessage("Severity Warning Low").build());
        Logger logger = mock(LoggerFactory.getLogger(SLF4JAdapter.class).getClass());
        report.print(new SLF4JAdapter(logger));

        for(Issue issue : report) {
            verify(logger).trace(issue.toString());
            verify(logger, never()).error(issue.toString());
            verify(logger, never()).info(issue.toString());
            verify(logger, never()).warn(issue.toString());
        }
    }

    @Test
    void shouldLogAllIssuesSLF4J() {
        Report report = readCheckStyleReport();
        Logger logger = mock(LoggerFactory.getLogger(SLF4JAdapter.class).getClass());
        report.print(new SLF4JAdapter(logger));

        verify(logger).error(anyString());
        verify(logger).warn(anyString());
        verify(logger).info(anyString());
        verify(logger).trace(anyString());
    }

    @Test
    void shouldLogTwoIssuesSLF4J() {
        Report report = new Report();
        Issue s1 = new IssueBuilder().setSeverity(Severity.WARNING_LOW).setMessage("Severity Warning Low").build();
        Issue s2 = new IssueBuilder().setSeverity(Severity.ERROR).setMessage("Severity Error").build();
        report.add(s1);
        report.add(s2);
        Logger logger = mock(LoggerFactory.getLogger(SLF4JAdapter.class).getClass());
        report.print(new SLF4JAdapter(logger));

        verify(logger).error(s2.toString());
        verify(logger).trace(s1.toString());
        verify(logger, never()).warn(anyString());
        verify(logger, never()).info(anyString());
    }

    /* Java Util Logging Tests */
    @Test
    void shouldLogOneErrorJUL() {
        Report report = new Report();
        report.add(new IssueBuilder().setSeverity(Severity.ERROR).setMessage("Error").build());
        java.util.logging.Logger logger = mock(java.util.logging.Logger.getLogger("edu.hm.hafner.analysis.JULAdapter").getClass());
        report.print(new JULAdapter(logger));

        for(Issue issue : report) {
            verify(logger).log(Level.SEVERE, issue.toString());
            verify(logger, never()).log(Level.WARNING, issue.toString());
            verify(logger, never()).log(Level.INFO, issue.toString());
            verify(logger, never()).log(Level.FINE, issue.toString());
            verify(logger, never()).log(Level.SEVERE, issue.toString() + "test");
        }
    }

    @Test
    void shouldLogOneWarningHighJUL() {
        Report report = new Report();
        report.add(new IssueBuilder().setSeverity(Severity.WARNING_HIGH).setMessage("Severity Warning High").build());
        java.util.logging.Logger logger = mock(java.util.logging.Logger.getLogger("edu.hm.hafner.analysis.JULAdapter").getClass());
        report.print(new JULAdapter(logger));

        for(Issue issue : report) {
            verify(logger).log(Level.WARNING, issue.toString());
            verify(logger, never()).log(Level.SEVERE, issue.toString());
            verify(logger, never()).log(Level.INFO, issue.toString());
            verify(logger, never()).log(Level.FINE, issue.toString());
            verify(logger, never()).log(Level.WARNING, issue.toString() + "test");
        }
    }

    @Test
    void shouldLogOneWarningNormalJUL() {
        Report report = new Report();
        report.add(new IssueBuilder().setSeverity(Severity.WARNING_NORMAL).setMessage("Severity Warning Normal").build());
        java.util.logging.Logger logger = mock(java.util.logging.Logger.getLogger("edu.hm.hafner.analysis.JULAdapter").getClass());
        report.print(new JULAdapter(logger));

        for(Issue issue : report) {
            verify(logger).log(Level.INFO, issue.toString());
            verify(logger, never()).log(Level.WARNING, issue.toString());
            verify(logger, never()).log(Level.SEVERE, issue.toString());
            verify(logger, never()).log(Level.FINE, issue.toString());
            verify(logger, never()).log(Level.INFO, issue.toString() + "test");
        }
    }

    @Test
    void shouldLogOneWarningLowJUL() {
        Report report = new Report();
        report.add(new IssueBuilder().setSeverity(Severity.WARNING_LOW).setMessage("Severity Warning Low").build());
        java.util.logging.Logger logger = mock(java.util.logging.Logger.getLogger("edu.hm.hafner.analysis.JULAdapter").getClass());
        report.print(new JULAdapter(logger));

        for(Issue issue : report) {
            verify(logger).log(Level.FINE, issue.toString());
            verify(logger, never()).log(Level.WARNING, issue.toString());
            verify(logger, never()).log(Level.INFO, issue.toString());
            verify(logger, never()).log(Level.SEVERE, issue.toString());
            verify(logger, never()).log(Level.FINE, issue.toString() + "test");
        }
    }

    @Test
    void shouldLogAllIssuesJavaUtilLogging() {
        Report report = readCheckStyleReport();
        java.util.logging.Logger logger = mock(java.util.logging.Logger.getLogger("edu.hm.hafner.analysis.JULAdapter").getClass());
        report.print(new JULAdapter(logger));

        verify(logger).log(eq(Level.SEVERE), anyString());
        verify(logger).log(eq(Level.WARNING), anyString());
        verify(logger).log(eq(Level.INFO), anyString());
        verify(logger).log(eq(Level.FINE), anyString());
    }

    @Test
    void shouldLogTwoIssuesJUL() {
        Report report = new Report();
        Issue s1 = new IssueBuilder().setSeverity(Severity.WARNING_LOW).setMessage("Severity Warning Low").build();
        Issue s2 = new IssueBuilder().setSeverity(Severity.ERROR).setMessage("Severity Error").build();
        report.add(s1);
        report.add(s2);
        java.util.logging.Logger logger = mock(java.util.logging.Logger.getLogger("edu.hm.hafner.analysis.JULAdapter").getClass());
        report.print(new JULAdapter(logger));

        verify(logger).log(Level.SEVERE, s2.toString());
        verify(logger).log(Level.FINE, s1.toString());
        verify(logger, never()).log(eq(Level.INFO), anyString());
        verify(logger, never()).log(eq(Level.WARNING), anyString());
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