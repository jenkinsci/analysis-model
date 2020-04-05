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
import edu.hm.hafner.analysis.Report.JavaLoggerAdapter;
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

    @Test
    void shouldLogErrorSLF4J() {
        Logger logger = mock(LoggerFactory.getLogger(SLF4JAdapter.class).getClass());
        Report report = new Report()
                .add(new IssueBuilder().setMessage("Error found").setSeverity(Severity.ERROR).build());
        report.print(new SLF4JAdapter(logger));

        for (Issue issue : report) {
            verify(logger).error(issue.toString());
            verify(logger, never()).trace(issue.toString());
            verify(logger, never()).info(issue.toString());
            verify(logger, never()).warn(issue.toString());
        }
    }

    @Test
    void shouldLogWarningLowSLF4J() {
        Logger logger = mock(LoggerFactory.getLogger(SLF4JAdapter.class).getClass());
        Report report = new Report()
                .add(new IssueBuilder().setMessage("Warning low found").setSeverity(Severity.WARNING_LOW).build());
        report.print(new SLF4JAdapter(logger));

        for (Issue issue : report) {
            verify(logger, never()).error(issue.toString());
            verify(logger).trace(issue.toString());
            verify(logger, never()).info(issue.toString());
            verify(logger, never()).warn(issue.toString());
        }
    }

    @Test
    void shouldLogWarningHighSLF4J() {
        Logger logger = mock(LoggerFactory.getLogger(SLF4JAdapter.class).getClass());
        Report report = new Report()
                .add(new IssueBuilder().setMessage("Warning high found").setSeverity(Severity.WARNING_HIGH).build());
        report.print(new SLF4JAdapter(logger));

        for (Issue issue : report) {
            verify(logger, never()).error(issue.toString());
            verify(logger, never()).trace(issue.toString());
            verify(logger, never()).info(issue.toString());
            verify(logger).warn(issue.toString());
        }
    }

    @Test
    void shouldLogInfoSLF4J() {
        Logger logger = mock(LoggerFactory.getLogger(SLF4JAdapter.class).getClass());
        Report report = new Report()
                .add(new IssueBuilder().setMessage("Warning normal found").setSeverity(Severity.WARNING_NORMAL).build());
        report.print(new SLF4JAdapter(logger));

        for (Issue issue : report) {
            verify(logger, never()).error(issue.toString());
            verify(logger, never()).trace(issue.toString());
            verify(logger).info(issue.toString());
            verify(logger, never()).warn(issue.toString());
        }
    }

    /* Java Util Logging Tests */
    @Test
    void shouldLogErrorJavaLogger() {
        Report report = new Report()
                .add(new IssueBuilder().setSeverity(Severity.ERROR).setMessage("Error found").build());
        java.util.logging.Logger logger = mock(java.util.logging.Logger.getLogger("edu.hm.hafner.analysis.JavaLoggerAdapter").getClass());
        report.print(new JavaLoggerAdapter(logger));

        for (Issue issue : report) {
            verify(logger).log(Level.SEVERE, issue.toString());
            verify(logger, never()).log(Level.WARNING, issue.toString());
            verify(logger, never()).log(Level.INFO, issue.toString());
            verify(logger, never()).log(Level.FINE, issue.toString());
            verify(logger, never()).log(Level.SEVERE, issue.toString() + "test");
        }
    }
    @Test
    void shouldLogWarningHighJavaLogger() {
        Report report = new Report()
                .add(new IssueBuilder().setSeverity(Severity.WARNING_HIGH).setMessage("High Warning found").build());
        java.util.logging.Logger logger = mock(java.util.logging.Logger.getLogger("edu.hm.hafner.analysis.JavaLoggerAdapter").getClass());
        report.print(new JavaLoggerAdapter(logger));

        for (Issue issue : report) {
            verify(logger).log(Level.WARNING, issue.toString());
            verify(logger, never()).log(Level.WARNING, issue.toString() + "test");
            verify(logger, never()).log(Level.INFO, issue.toString());
            verify(logger, never()).log(Level.FINE, issue.toString());
            verify(logger, never()).log(Level.SEVERE, issue.toString());
        }
    }
    @Test
    void shouldLogWarningLowJavaLogger() {
        Report report = new Report()
                .add(new IssueBuilder().setSeverity(Severity.WARNING_LOW).setMessage("Low Warning found").build());
        java.util.logging.Logger logger = mock(java.util.logging.Logger.getLogger("edu.hm.hafner.analysis.JavaLoggerAdapter").getClass());
        report.print(new JavaLoggerAdapter(logger));

        for (Issue issue : report) {
            verify(logger).log(Level.FINE, issue.toString());
            verify(logger, never()).log(Level.WARNING, issue.toString());
            verify(logger, never()).log(Level.INFO, issue.toString());
            verify(logger, never()).log(Level.FINE, issue.toString() + "test");
            verify(logger, never()).log(Level.SEVERE, issue.toString());
        }
    }

    @Test
    void shouldLogWarningNormalJavaLogger() {
        Report report = new Report()
                .add(new IssueBuilder().setSeverity(Severity.WARNING_NORMAL).setMessage("Normal Warning found").build());
        java.util.logging.Logger logger = mock(java.util.logging.Logger.getLogger("edu.hm.hafner.analysis.JavaLoggerAdapter").getClass());
        report.print(new JavaLoggerAdapter(logger));

        for (Issue issue : report) {
            verify(logger).log(Level.INFO, issue.toString());
            verify(logger, never()).log(Level.WARNING, issue.toString());
            verify(logger, never()).log(Level.INFO, issue.toString() + "test");
            verify(logger, never()).log(Level.FINE, issue.toString());
            verify(logger, never()).log(Level.SEVERE, issue.toString());
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
