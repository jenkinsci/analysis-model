package edu.hm.hafner.analysis;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.bridge.SLF4JBridgeHandler;

import edu.hm.hafner.analysis.Report.IssuePrinter;
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
    void shouldPrintAllIssuesToUtilLogger() {

        final Map<Severity, Level> map = new HashMap<Severity, Level>() {{
            put(Severity.ERROR, Level.SEVERE);
            put(Severity.WARNING_HIGH, Level.WARNING);
            put(Severity.WARNING_NORMAL, Level.INFO);
            put(Severity.WARNING_LOW, Level.FINE);
        }};

        for (Map.Entry<Severity, Level> entry : map.entrySet()) {
            utilLoggerEqual(entry.getKey(),entry.getValue());
        }
    }

    private void utilLoggerEqual(Severity severity, Level level){
        Report report = new Report();
        report.add(new IssueBuilder().setSeverity(severity).setMessage("Issue: " + severity.getName()).build());

        Logger logger = mock(Logger.class);
        report.print(new JavaUtilLoggingPrinter(logger));

        for (Issue issue : report) {
            verify(logger).log(level, issue.toString());
        }
    }

    @Test
    void shouldPrintAllIssuesToSLF4() {
        final Severity[] severities = {
                Severity.ERROR,
                Severity.WARNING_HIGH,
                Severity.WARNING_NORMAL,
                Severity.WARNING_LOW
        };

        for(Severity severity : severities){
            slf4PrinterEqual(severity);
        }
    }

    private void slf4PrinterEqual(Severity severity){
        Report report = new Report();
        report.add(new IssueBuilder().setSeverity(severity).setMessage("Issue: " + severity.getName()).build());

        org.slf4j.Logger logger = mock(org.slf4j.Logger.class);
        report.print(new Slf4jPrinter(logger));

        for (Issue issue : report) {
            if (severity == Severity.ERROR) {
                verify(logger).error(issue.toString());
            }
            if (severity == Severity.WARNING_HIGH) {
                verify(logger).warn(issue.toString());
            }
            if (severity == Severity.WARNING_NORMAL) {
                verify(logger).info(issue.toString());
            }
            if (severity == Severity.WARNING_LOW) {
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
