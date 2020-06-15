package edu.hm.hafner.analysis;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.bridge.SLF4JBridgeHandler;

import edu.hm.hafner.analysis.Report.IssuePrinter;
import edu.hm.hafner.analysis.Report.JavaLoggingOutputPrinter;
import edu.hm.hafner.analysis.Report.SLF4JOutputPrinter;
import edu.hm.hafner.analysis.Report.StandardOutputPrinter;
import edu.hm.hafner.analysis.parser.checkstyle.CheckStyleParser;
import edu.hm.hafner.util.ResourceTest;
import edu.hm.hafner.util.VisibleForTesting;

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
    void shouldPrintAllIssuesToJavaLogging() {
        Report report = readCheckStyleReport();
        java.util.logging.Logger logger = mock(java.util.logging.Logger.class);

        final Map<Severity, Level> severityLogLevelMap = new HashMap<>();
        severityLogLevelMap.put(Severity.ERROR, Level.SEVERE);
        severityLogLevelMap.put(Severity.WARNING_HIGH, Level.WARNING);
        severityLogLevelMap.put(Severity.WARNING_NORMAL, Level.INFO);
        severityLogLevelMap.put(Severity.WARNING_LOW, Level.FINE);

        report.print(new JavaLoggingOutputPrinter(logger));

        for (Issue issue : report) {
            verify(logger).log(severityLogLevelMap.get(issue.getSeverity()), issue.getMessage());
        }
    }

    @Test
    void shouldPrintAllIssuesToSLF4J() {
        Report report = readCheckStyleReport();

        org.slf4j.Logger logger = mock(org.slf4j.Logger.class);

        report.print(new SLF4JOutputPrinter(logger));

        for (Issue issue : report) {
            if(issue.getSeverity() == Severity.ERROR) {
                verify(logger).error(issue.getMessage());
            } else if(issue.getSeverity() == Severity.WARNING_HIGH) {
                verify(logger).warn(issue.getMessage());
            }   else if(issue.getSeverity() == Severity.WARNING_NORMAL) {
                verify(logger).info(issue.getMessage());
            }   else if(issue.getSeverity() == Severity.WARNING_LOW) {
                verify(logger).trace(issue.getMessage());
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
