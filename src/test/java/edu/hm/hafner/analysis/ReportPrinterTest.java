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
import edu.hm.hafner.analysis.Report.JavaUtilPrinter;
import edu.hm.hafner.analysis.Report.SLF4JPrinter;
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

    @Test
    void logErrorSLF4J() {
        Report report = generateReport(Severity.ERROR);
        Logger logger = mock(LoggerFactory.getLogger(SLF4JPrinter.class).getClass());
        report.print(new SLF4JPrinter(logger));

        assertThat(report).hasSize(1);
        report.forEach(issue -> {
            verify(logger).error(issue.toString());
            verify(logger,  never()).warn(issue.toString());
            verify(logger,  never()).info(issue.toString());
            verify(logger,  never()).trace(issue.toString());
        });
    }

    @Test
    void logWarningHighSLF4J() {
        Report report = generateReport(Severity.WARNING_HIGH);
        Logger logger = mock(LoggerFactory.getLogger(SLF4JPrinter.class).getClass());
        report.print(new SLF4JPrinter(logger));

        assertThat(report).hasSize(1);
        report.forEach(issue -> {
            verify(logger, never()).error(issue.toString());
            verify(logger).warn(issue.toString());
            verify(logger,  never()).info(issue.toString());
            verify(logger,  never()).trace(issue.toString());
        });
    }

    @Test
    void logWarningNormalSLF4J() {
        Report report = generateReport(Severity.WARNING_NORMAL);
        Logger logger = mock(LoggerFactory.getLogger(SLF4JPrinter.class).getClass());
        report.print(new SLF4JPrinter(logger));

        assertThat(report).hasSize(1);
        report.forEach(issue -> {
            verify(logger, never()).error(issue.toString());
            verify(logger,  never()).warn(issue.toString());
            verify(logger).info(issue.toString());
            verify(logger,  never()).trace(issue.toString());
        });
    }

    @Test
    void logWarningLowSLF4J() {
        Report report = generateReport(Severity.WARNING_LOW);
        Logger logger = mock(LoggerFactory.getLogger(SLF4JPrinter.class).getClass());
        report.print(new SLF4JPrinter(logger));

        assertThat(report).hasSize(1);
        report.forEach(issue -> {
            verify(logger, never()).error(issue.toString());
            verify(logger,  never()).warn(issue.toString());
            verify(logger,  never()).info(issue.toString());
            verify(logger).trace(issue.toString());
        });
    }

    @Test
    void logErrorJavaUtil() {
        Report report = generateReport(Severity.ERROR);
        java.util.logging.Logger logger = mock(java.util.logging.Logger.getLogger("edu.hm.hafner.analysis.JavaUtilPrinter").getClass());
        report.print(new JavaUtilPrinter(logger));

        assertThat(report).hasSize(1);
        report.forEach(issue -> {
            verify(logger).log(Level.SEVERE, issue.toString());
            verify(logger,  never()).log(Level.WARNING, issue.toString());
            verify(logger, never()).log(Level.INFO, issue.toString());
            verify(logger, never()).log(Level.FINE, issue.toString());
        });
    }

    @Test
    void logWarningJavaUtil() {
        Report report = generateReport(Severity.WARNING_HIGH);
        java.util.logging.Logger logger = mock(java.util.logging.Logger.getLogger("edu.hm.hafner.analysis.JavaUtilPrinter").getClass());
        report.print(new JavaUtilPrinter(logger));

        assertThat(report).hasSize(1);
        report.forEach(issue -> {
            verify(logger, never()).log(Level.SEVERE, issue.toString());
            verify(logger).log(Level.WARNING, issue.toString());
            verify(logger, never()).log(Level.INFO, issue.toString());
            verify(logger, never()).log(Level.FINE, issue.toString());
        });
    }

    @Test
    void logWarningNormalJavaUtil() {
        Report report = generateReport(Severity.WARNING_NORMAL);
        java.util.logging.Logger logger = mock(java.util.logging.Logger.getLogger("edu.hm.hafner.analysis.JavaUtilPrinter").getClass());
        report.print(new JavaUtilPrinter(logger));

        assertThat(report).hasSize(1);
        report.forEach(issue -> {
            verify(logger, never()).log(Level.SEVERE, issue.toString());
            verify(logger,  never()).log(Level.WARNING, issue.toString());
            verify(logger).log(Level.INFO, issue.toString());
            verify(logger, never()).log(Level.FINE, issue.toString());
        });
    }

    @Test
    void logWarningLowJavaUtil() {
        Report report = generateReport(Severity.WARNING_LOW);
        java.util.logging.Logger logger = mock(java.util.logging.Logger.getLogger("edu.hm.hafner.analysis.JavaUtilPrinter").getClass());
        report.print(new JavaUtilPrinter(logger));

        assertThat(report).hasSize(1);
        report.forEach(issue -> {
            verify(logger, never()).log(Level.SEVERE, issue.toString());
            verify(logger,  never()).log(Level.WARNING, issue.toString());
            verify(logger,  never()).log(Level.INFO, issue.toString());
            verify(logger).log(Level.FINE, issue.toString());
        });
    }

    private Report generateReport(final Severity severity) {
        return new Report()
                .add(new IssueBuilder().setSeverity(severity).setMessage(severity.toString()).build());
    }

}
