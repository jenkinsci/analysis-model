package edu.hm.hafner.analysis;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
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
    void testJavaUtilLoggingAdapterPrintsAllNormalIssues() {
        // arrange
        final Report report = readCheckStyleReport();
        final Logger javaUtilLogger = mock(Logger.class);
        final JavaUtilLoggingAdapter adapter = new JavaUtilLoggingAdapter(javaUtilLogger);
        final Map<Severity, Level> severityToLevel = new HashMap<>();

        severityToLevel.put(Severity.ERROR, Level.SEVERE);
        severityToLevel.put(Severity.WARNING_HIGH, Level.WARNING);
        severityToLevel.put(Severity.WARNING_NORMAL, Level.INFO);
        severityToLevel.put(Severity.WARNING_LOW, Level.FINE);

        // act
        report.print(adapter);

        // assert
        for (Issue eachIssue : report) {
            verify(javaUtilLogger).log(severityToLevel.get(eachIssue.getSeverity()), eachIssue.getMessage());
        }
    }

    @Test
    void testJavaUtilLoggingAdapterPrintsUnsupportedSeverityBombs() {
        // arrange
        final Report report = readCheckStyleReport();
        report.add(new IssueBuilder().setSeverity(Severity.valueOf("SEVERE_SEVERE"))
                .setMessage("TOO SEVERE! SYSTEM FAILURE!")
                .build());

        final Logger javaUtilLogger = mock(Logger.class);
        final JavaUtilLoggingAdapter adapter = new JavaUtilLoggingAdapter(javaUtilLogger);

        // act and assert
        assertThatIllegalArgumentException().as("Check if the JavaUtilLoggingAdapter throws an IllegalArgumentException"
                + " when given an issue with an unsupported severity.")
                .isThrownBy(() -> report.print(adapter));
    }

    @Test
    void testSimpleLoggingFacadeAdapterPrintsAllNormalIssues() {
        // arrange
        final Report report = readCheckStyleReport();
        final org.slf4j.Logger loggingFacadeLogger = mock(org.slf4j.Logger.class);
        final SimpleLoggingFacadeAdapter adapter = new SimpleLoggingFacadeAdapter(loggingFacadeLogger);

        // act
        report.print(adapter);

        // assert
        for (Issue eachIssue : report) {
            final Severity severity = eachIssue.getSeverity();
            final String issueMessage = eachIssue.getMessage();

            if (Severity.ERROR.equals(severity)) {
                verify(loggingFacadeLogger).error(issueMessage);
            }
            else if (Severity.WARNING_HIGH.equals(severity)) {
                verify(loggingFacadeLogger).warn(issueMessage);
            }
            else if (Severity.WARNING_NORMAL.equals(severity)) {
                verify(loggingFacadeLogger).info(issueMessage);
            }
            else {
                verify(loggingFacadeLogger).trace(issueMessage);
            }
        }
    }

    @Test
    void testSimpleLoggingFacadeAdapterPrintsUnsupportedSeverityBombs() {
        // arrange
        final Report report = readCheckStyleReport();
        report.add(new IssueBuilder().setSeverity(Severity.valueOf("SEVERE_SEVERE"))
                .setMessage("TOO SEVERE! SYSTEM FAILURE!")
                .build());

        final org.slf4j.Logger loggingFacadeLogger = mock(org.slf4j.Logger.class);
        final SimpleLoggingFacadeAdapter adapter = new SimpleLoggingFacadeAdapter(loggingFacadeLogger);

        // act and assert
        assertThatIllegalArgumentException().as("Check if the SimpleLoggingFacadeAdapter throws an IllegalArgumentException"
                + " when given an issue with an unsupported severity.")
                .isThrownBy(() -> report.print(adapter));
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
