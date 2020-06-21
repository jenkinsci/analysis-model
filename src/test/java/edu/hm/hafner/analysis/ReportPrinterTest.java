package edu.hm.hafner.analysis;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import edu.hm.hafner.analysis.Report.IssuePrinter;
import edu.hm.hafner.analysis.Report.StandardOutputPrinter;
import edu.hm.hafner.analysis.parser.checkstyle.CheckStyleParser;
import edu.hm.hafner.util.ResourceTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests printing of reports using the {@link Report#print(IssuePrinter)} method.
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
    void JavaUtilError(){
        Report report = new Report();
        Issue issue = new IssueBuilder().setSeverity(Severity.ERROR).setMessage("Severity Error").build();
        Logger logger = mock(Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).getClass());
        JavaUtilLoggingAdapter javaUtilLoggingAdapter = new JavaUtilLoggingAdapter(logger);

        report.add(issue);
        report.print(javaUtilLoggingAdapter);

        verify(logger).log(Level.SEVERE, issue.toString());
    }

    @Test
    void JavaUtilWarningHigh(){
        Report report = new Report();
        Issue issue = new IssueBuilder().setSeverity(Severity.WARNING_HIGH).setMessage("Severity Warning_High").build();
        Logger logger = mock(Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).getClass());
        JavaUtilLoggingAdapter javaUtilLoggingAdapter = new JavaUtilLoggingAdapter(logger);

        report.add(issue);
        report.print(javaUtilLoggingAdapter);

        verify(logger).log(Level.WARNING, issue.toString());
    }

    @Test
    void JavaUtilWarningNormal(){
        Report report = new Report();
        Issue issue = new IssueBuilder().setSeverity(Severity.WARNING_NORMAL).setMessage("Severity Warning_Normal").build();
        Logger logger = mock(Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).getClass());
        JavaUtilLoggingAdapter javaUtilLoggingAdapter = new JavaUtilLoggingAdapter(logger);

        report.add(issue);
        report.print(javaUtilLoggingAdapter);

        verify(logger).log(Level.INFO, issue.toString());
    }

    @Test
    void JavaUtilWarningLow(){
        Report report = new Report();
        Issue issue = new IssueBuilder().setSeverity(Severity.WARNING_LOW).setMessage("Severity Warning_Low").build();
        Logger logger = mock(Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).getClass());
        JavaUtilLoggingAdapter javaUtilLoggingAdapter = new JavaUtilLoggingAdapter(logger);

        report.add(issue);
        report.print(javaUtilLoggingAdapter);

        verify(logger).log(Level.FINE, issue.toString());
    }

    @Test
    void slf4Error(){
        Report report = new Report();
        Issue issue = new IssueBuilder().setSeverity(Severity.ERROR).setMessage("Severity Error").build();
        org.slf4j.Logger logger = mock(LoggerFactory.getLogger("slf4j").getClass());
        SLF4JAdapter slf4JAdapter = new SLF4JAdapter(logger);

        report.add(issue);
        report.print(slf4JAdapter);

        verify(logger).error(issue.toString());
    }

    @Test
    void slf4WarningHigh(){
        Report report = new Report();
        Issue issue = new IssueBuilder().setSeverity(Severity.WARNING_HIGH).setMessage("Severity Warning_High").build();
        org.slf4j.Logger logger = mock(LoggerFactory.getLogger("slf4j").getClass());
        SLF4JAdapter slf4JAdapter = new SLF4JAdapter(logger);

        report.add(issue);
        report.print(slf4JAdapter);

        verify(logger).warn(issue.toString());
    }

    @Test
    void slf4WarningNormal(){
        Report report = new Report();
        Issue issue = new IssueBuilder().setSeverity(Severity.WARNING_NORMAL).setMessage("Severity Warning_Normal").build();
        org.slf4j.Logger logger = mock(LoggerFactory.getLogger("slf4j").getClass());
        SLF4JAdapter slf4JAdapter = new SLF4JAdapter(logger);

        report.add(issue);
        report.print(slf4JAdapter);

        verify(logger).info(issue.toString());
    }

    @Test
    void slf4WarningLow(){
        Report report = new Report();
        Issue issue = new IssueBuilder().setSeverity(Severity.WARNING_LOW).setMessage("Severity Warning_Low").build();
        org.slf4j.Logger logger = mock(LoggerFactory.getLogger("slf4j").getClass());
        SLF4JAdapter slf4JAdapter = new SLF4JAdapter(logger);

        report.add(issue);
        report.print(slf4JAdapter);

        verify(logger).trace(issue.toString());
    }




}
