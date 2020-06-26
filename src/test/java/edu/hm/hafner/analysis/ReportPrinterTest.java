package edu.hm.hafner.analysis;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.bridge.SLF4JBridgeHandler;

import edu.hm.hafner.analysis.Report.IssuePrinter;
import edu.hm.hafner.analysis.Report.JavaUtilLoggingPrinter;
import edu.hm.hafner.analysis.Report.SimpleLoggingFacadeForJavaPrinter;
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
    void shouldPrintAllIssuesToJavaUtilLoggingPrinter() {
        Report report = readCheckStyleReport();
        Logger logger = mock(Logger.class);
        report.print(new JavaUtilLoggingPrinter(logger));
        final Map<Severity, Level> severityLevelMap = new HashMap<>();
        severityLevelMap.put(Severity.ERROR, Level.SEVERE);
        severityLevelMap.put(Severity.WARNING_HIGH, Level.WARNING);
        severityLevelMap.put(Severity.WARNING_NORMAL, Level.INFO);
        severityLevelMap.put(Severity.WARNING_LOW, Level.FINE);

        for (Issue issue : report){
            Severity severity = issue.getSeverity();
            String verifyString = issue.toString();
            if (severityLevelMap.containsKey(severity)){
                verify(logger).log(severityLevelMap.get(severity), verifyString);
            }
            else {
                assertThatExceptionOfType(IllegalArgumentException.class);
            }
        }
    }

    @Test
    void shouldPrintAllIssuesToSLF4JPrinter() {
        Report report = readCheckStyleReport();
        org.slf4j.Logger logger = mock(org.slf4j.Logger.class);
        report.print(new SimpleLoggingFacadeForJavaPrinter(logger));
        final Map<Severity, Consumer<String>> severityFunctionMap = new HashMap<>();
        severityFunctionMap.put(Severity.ERROR, msg -> verify(logger).error(msg));
        severityFunctionMap.put(Severity.WARNING_HIGH, msg -> verify(logger).warn(msg));
        severityFunctionMap.put(Severity.WARNING_NORMAL, msg -> verify(logger).info(msg));
        severityFunctionMap.put(Severity.WARNING_LOW, msg -> verify(logger).trace(msg));

        for (Issue issue : report){
            Severity severity = issue.getSeverity();
            String verifyMessage = issue.getMessage();
            if (severityFunctionMap.containsKey(severity)){
                severityFunctionMap.get(severity).accept(verifyMessage);
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
