package edu.hm.hafner.analysis;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.bridge.SLF4JBridgeHandler;

import edu.hm.hafner.analysis.Report.IssuePrinter;
import edu.hm.hafner.analysis.Report.JulAdapter;
import edu.hm.hafner.analysis.Report.Slf4jAdapter;
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

        try (PrintStream printStream = mock(PrintStream.class)) {
            report.print(new StandardOutputPrinter(printStream));

            for (Issue issue : report) {
                verify(printStream).println(issue.toString());
            }
        }
    }

    @Test
    void shouldPrintAllIssuesToJulLogger() {
        Report report = readCheckStyleReport();
        java.util.logging.Logger logger = mock(java.util.logging.Logger.class);
        report.print(new JulAdapter(logger));

        for (Issue issue : report) {
            final Severity severity = issue.getSeverity();
            final String issueAsString = issue.toString();

            if (severity.equals(Severity.ERROR)) {
                verify(logger).log(Level.SEVERE, issueAsString);
            }
            else if (severity.equals(Severity.WARNING_HIGH)) {
                verify(logger).log(Level.WARNING, issueAsString);
            }
            else if (severity.equals(Severity.WARNING_NORMAL)) {
                verify(logger).log(Level.INFO, issueAsString);
            }
            else if (severity.equals(Severity.WARNING_LOW)) {
                verify(logger).log(Level.FINE, issueAsString);
            }
            else {
                assertThatExceptionOfType(IllegalArgumentException.class);
            }
        }
    }

    @Test
    void shouldPrintAllIssuesToSlf4jLogger() {
        Report report = readCheckStyleReport();

        org.slf4j.Logger logger = mock(org.slf4j.Logger.class);
        report.print(new Slf4jAdapter(logger));

        for (Issue issue : report) {
            final Severity severity = issue.getSeverity();
            final String issueAsString = issue.toString();

            if (severity.equals(Severity.ERROR)) {
                verify(logger).error(issueAsString);
            }
            else if (severity.equals(Severity.WARNING_HIGH)) {
                verify(logger).warn(issueAsString);
            }
            else if (severity.equals(Severity.WARNING_NORMAL)) {
                verify(logger).info(issueAsString);
            }
            else if (severity.equals(Severity.WARNING_LOW)) {
                verify(logger).trace(issueAsString);
            }
            else {
                assertThatExceptionOfType(IllegalArgumentException.class);
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