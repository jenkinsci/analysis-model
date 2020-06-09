package edu.hm.hafner.analysis;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.bridge.SLF4JBridgeHandler;

import edu.hm.hafner.analysis.Report.IssuePrinter;
import edu.hm.hafner.analysis.Report.JavaUtilLoggingPrinter;
import edu.hm.hafner.analysis.Report.Slf4jLoggingPrinter;
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
    void shouldPrintAllIssuesToSlf4jLogger() {
        Report report = readCheckStyleReport();

        org.slf4j.Logger logger = mock(org.slf4j.Logger.class);
        report.print(new Slf4jLoggingPrinter(logger));

        for(Issue issue : report) {
            if (issue.getSeverity() == Severity.ERROR) {
                verify(logger).error(issue.toString());
            } else if (issue.getSeverity() == Severity.WARNING_HIGH) {
                verify(logger).warn(issue.toString());
            } else if (issue.getSeverity() == Severity.WARNING_LOW) {
                verify(logger).info(issue.toString());
            } else {
                verify(logger).trace(issue.toString());
            }
        }
    }

    @Test
    void shouldPrintAllIssuesToJavaUtilLogger() {
        Report report = readCheckStyleReport();

        java.util.logging.Logger logger = mock(java.util.logging.Logger.class);
        report.print(new JavaUtilLoggingPrinter(logger));

        for(Issue issue : report) {
            if (issue.getSeverity() == Severity.ERROR) {
                verify(logger).log(Level.SEVERE, issue.toString());
            } else if (issue.getSeverity() == Severity.WARNING_HIGH) {
                verify(logger).log(Level.WARNING, issue.toString());
            } else if (issue.getSeverity() == Severity.WARNING_LOW) {
                verify(logger).log(Level.INFO, issue.toString());
            } else {
                verify(logger).log(Level.FINE, issue.toString());
            }
        }
    }

    @Test
    void shouldPrintAllIssuesToPrintStream() {
        Report report = readCheckStyleReport();

        try (PrintStream printStream = mock(PrintStream.class)) {
            report.print(new StandardOutputPrinter(printStream));

            for (Issue issue : report) {
                verify(printStream).println(issue.toString());
            }
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
