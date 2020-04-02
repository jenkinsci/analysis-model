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
        Severity newSeverity = new Severity("NotExisting");
        Report report = new Report().add(new IssueBuilder().setSeverity(newSeverity).build());

        Logger logger = mock(Logger.class);

        assertThatThrownBy(() -> {
            report.print(new JavaUtilLoggingPrinter(logger));
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
