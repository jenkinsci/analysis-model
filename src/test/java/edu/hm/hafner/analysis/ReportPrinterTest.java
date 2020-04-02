package edu.hm.hafner.analysis;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.bridge.SLF4JBridgeHandler;

import edu.hm.hafner.analysis.Report.IssuePrinter;
import edu.hm.hafner.analysis.Report.JavaUtilLoggingPrinter;
import edu.hm.hafner.analysis.Report.Slf4jPrinter;
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

    private static final Severity NEW_SEVERITY = new Severity("NewSeverityNotExisting");

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
    void shouldPrintIssuesUsingJavaUtilLoggingPrinterWithSeverityError() {
        checkJavaUtilLoggingPrinting(Severity.ERROR, Level.SEVERE);
    }

    @Test
    void shouldPrintIssuesUsingJavaUtilLoggingPrinterWithSeverityWarningHigh() {
        checkJavaUtilLoggingPrinting(Severity.WARNING_HIGH, Level.WARNING);
    }

    @Test
    void shouldPrintIssuesUsingJavaUtilLoggingPrinterWithSeverityWarningNormal() {
        checkJavaUtilLoggingPrinting(Severity.WARNING_NORMAL, Level.INFO);
    }

    @Test
    void shouldPrintIssuesUsingJavaUtilLoggingPrinterWithSeverityWarningLow() {
        checkJavaUtilLoggingPrinting(Severity.WARNING_LOW, Level.FINE);
    }

    @Test
    void throwsUsingJavaUtilLoggingPrinterWithNewSeverity() {
        Report report = new Report().add(new IssueBuilder().setSeverity(NEW_SEVERITY).build());

        Logger logger = mock(Logger.class);

        assertThatThrownBy(() -> {
            report.print(new JavaUtilLoggingPrinter(logger));
        }).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldPrintIssuesUsingSlf4jPrinterWithSeverityError() {
        checkSlf4jPrinting(Severity.ERROR);
    }

    @Test
    void shouldPrintIssuesUsingSlf4jPrinterWithSeverityWarningHigh() {
        checkSlf4jPrinting(Severity.WARNING_HIGH);
    }

    @Test
    void shouldPrintIssuesUsingSlf4jPrinterWithSeverityWarningNormal() {
        checkSlf4jPrinting(Severity.WARNING_NORMAL);
    }

    @Test
    void shouldPrintIssuesUsingSlf4jPrinterWithSeverityWarningLow() {
        checkSlf4jPrinting(Severity.WARNING_LOW);
    }

    @Test
    void throwsUsingSlf4jPrinterWithNewSeverity() {
        Report report = new Report().add(new IssueBuilder().setSeverity(NEW_SEVERITY).build());

        org.slf4j.Logger logger = mock(org.slf4j.Logger.class);

        assertThatThrownBy(() -> {
            report.print(new Slf4jPrinter(logger));
        }).isInstanceOf(UnsupportedOperationException.class);
    }

    private void checkJavaUtilLoggingPrinting(final Severity severity, final Level expectedLevel) {
        Report report = readCheckStyleReport().filter(issue -> issue.getSeverity() == severity);

        Logger logger = mock(Logger.class);
        report.print(new JavaUtilLoggingPrinter(logger));

        for (Issue issue : report) {
            verify(logger).log(expectedLevel, issue.toString());
        }
    }

    private void checkSlf4jPrinting(final Severity severity) {
        Report report = readCheckStyleReport().filter(issue -> issue.getSeverity() == severity);

        org.slf4j.Logger logger = mock(org.slf4j.Logger.class);
        report.print(new Slf4jPrinter(logger));

        for (Issue issue : report) {
            verifySlf4jLoggerCall(logger, severity, issue.toString());
        }
    }

    private void verifySlf4jLoggerCall(final org.slf4j.Logger mock, final Severity severity,
            final String expectedMessage) {
        if (severity == Severity.ERROR) {
            verify(mock).error(expectedMessage);
        }
        else if (severity == Severity.WARNING_HIGH) {
            verify(mock).warn(expectedMessage);
        }
        else if (severity == Severity.WARNING_NORMAL) {
            verify(mock).info(expectedMessage);
        }
        else if (severity == Severity.WARNING_LOW) {
            verify(mock).trace(expectedMessage);
        }
        else {
            fail("Severity not supported!");
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
