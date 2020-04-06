package edu.hm.hafner.analysis;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.bridge.SLF4JBridgeHandler;

import edu.hm.hafner.analysis.Report.IssuePrinter;
import edu.hm.hafner.analysis.Report.JULoggingAdapter;
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

    static Severity ILLEGALLOGGINGSEVERITY = new Severity("This can't be logged by common libraries");

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
    void shouldBombConstructor() {
        assertThatThrownBy(() -> {
            JULoggingAdapter adapter = new JULoggingAdapter(null);
        }).isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> {
            SLF4JAdapter adapter = new SLF4JAdapter(null);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldBombNoIssue() {
        Logger loggerJUL = mock(Logger.class);
        JULoggingAdapter adapterJUL = new JULoggingAdapter(loggerJUL);

        assertThatThrownBy(() -> {
            adapterJUL.print(null);
        }).isInstanceOf(IllegalArgumentException.class);

        org.slf4j.Logger loggerSLF4J = mock(org.slf4j.Logger.class);
        SLF4JAdapter adapterSLF4J = new SLF4JAdapter(loggerSLF4J);

        assertThatThrownBy(() -> {
            adapterSLF4J.print(null);
        }).isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    void shouldBombSLF4JWithIllegalSeverity() {
        org.slf4j.Logger logger = mock(org.slf4j.Logger.class);
        SLF4JAdapter adapter = new SLF4JAdapter(logger);

        assertThatThrownBy(() -> {
            adapter.print(new IssueBuilder().setSeverity(ILLEGALLOGGINGSEVERITY).build());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldBombJULWithIllegalSeverity() {
        Logger logger = mock(Logger.class);
        JULoggingAdapter adapter = new JULoggingAdapter(logger);

        assertThatThrownBy(() -> adapter.getLevel(ILLEGALLOGGINGSEVERITY)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldWorkSeverityError() {
        checkJULoggingWithSeverity(Severity.ERROR);
        checkSLF4JWithSeverity(Severity.ERROR);
    }

    @Test
    void shouldWorkSeverityWarningHigh() {
        checkJULoggingWithSeverity(Severity.WARNING_HIGH);
        checkSLF4JWithSeverity(Severity.WARNING_HIGH);
    }

    @Test
    void shouldWorkSeverityWarningNormal() {
        checkJULoggingWithSeverity(Severity.WARNING_NORMAL);
        checkSLF4JWithSeverity(Severity.WARNING_NORMAL);
    }

    @Test
    void shouldWorkSeverityWarningLow() {
        checkJULoggingWithSeverity(Severity.WARNING_LOW);
        checkSLF4JWithSeverity(Severity.WARNING_LOW);
    }

    void checkJULoggingWithSeverity(final Severity severity) {
        Report report = readCheckStyleReport().filter(issue -> issue.getSeverity().equals(severity));

        Logger logger = mock(Logger.class);
        JULoggingAdapter adapter = new JULoggingAdapter(logger);
        report.print(adapter);

        for (Issue issue : report) {
            verify(logger).log(adapter.getLevel(severity), issue.toString());
        }
    }

    void checkSLF4JWithSeverity(final Severity severity) {
        Report report = readCheckStyleReport().filter(issue -> issue.getSeverity().equals(severity));

        org.slf4j.Logger logger = mock(org.slf4j.Logger.class);
        SLF4JAdapter adapter = new SLF4JAdapter(logger);
        report.print(adapter);

        for (Issue issue : report) {
            if (Severity.ERROR.equals(severity)) {
                verify(logger).error(issue.toString());
            }
            if (Severity.WARNING_HIGH.equals(severity)) {
                verify(logger).warn(issue.toString());
            }
            if (Severity.WARNING_NORMAL.equals(severity)) {
                verify(logger).info(issue.toString());
            }
            if (Severity.WARNING_LOW.equals(severity)) {
                verify(logger).trace(issue.toString());
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
