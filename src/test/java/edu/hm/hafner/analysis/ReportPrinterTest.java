package edu.hm.hafner.analysis;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.bridge.SLF4JBridgeHandler;

import edu.hm.hafner.analysis.Report.IssuePrinter;
import edu.hm.hafner.analysis.Report.JavaUtilLogginAdaptor;
import edu.hm.hafner.analysis.Report.SLF4JAdaptor;
import edu.hm.hafner.analysis.Report.StandardOutputPrinter;
import edu.hm.hafner.analysis.parser.checkstyle.CheckStyleParser;
import edu.hm.hafner.util.ResourceTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;
import org.slf4j.LoggerFactory;
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
    void testWarningLogJavaUtilLogginAdaptor() {
        Report report = new Report();
        Issue issue = new IssueBuilder().setSeverity(Severity.WARNING_HIGH).setMessage("Warning with high importance").build();
        report.add((issue));
        Logger logger = mock(Logger.getLogger(JavaUtilLogginAdaptor.class.getName()).getClass());
        report.print(new JavaUtilLogginAdaptor(logger));
        verify(logger).log(Level.WARNING, issue.toString());
        verify(logger,never()).log(Level.FINE,issue.toString());
        verify(logger,never()).log(Level.INFO,issue.toString());
        verify(logger,never()).log(Level.FINEST,issue.toString());
    }

    @Test
    void testErrorLogJavaUtilLogginAdaptor() {
        Report report = new Report();
        Issue issue = new IssueBuilder().setSeverity(Severity.ERROR).setMessage("Error. Immediate verification is necessary!").build();
        report.add((issue));
        Logger logger = mock(Logger.getLogger(JavaUtilLogginAdaptor.class.getName()).getClass());
        report.print(new JavaUtilLogginAdaptor(logger));
        verify(logger).log(Level.SEVERE, issue.toString());
        verify(logger,never()).log(Level.FINE,issue.toString());
        verify(logger,never()).log(Level.INFO,issue.toString());
        verify(logger,never()).log(Level.WARNING,issue.toString());
    }
    @Test
    void testWarningNormalLogJavaUtilLogginAdaptor() {
        Report report = new Report();
        Issue issue = new IssueBuilder().setSeverity(Severity.WARNING_NORMAL).setMessage("Warning with very low importance, with informative purpose.").build();
        report.add((issue));
        Logger logger = mock(Logger.getLogger(JavaUtilLogginAdaptor.class.getName()).getClass());
        report.print(new JavaUtilLogginAdaptor(logger));
        verify(logger).log(Level.INFO, issue.toString());
        verify(logger,never()).log(Level.FINE,issue.toString());
        verify(logger,never()).log(Level.SEVERE,issue.toString());
        verify(logger,never()).log(Level.WARNING,issue.toString());
    }
    @Test
    void testWarningLowLogJavaUtilLogginAdaptor() {
        Report report = new Report();
        Issue issue = new IssueBuilder().setSeverity(Severity.WARNING_LOW).setMessage("Everything is fine!").build();
        report.add((issue));
        Logger logger = mock(Logger.getLogger(JavaUtilLogginAdaptor.class.getName()).getClass());
        report.print(new JavaUtilLogginAdaptor(logger));
        verify(logger).log(Level.FINE, issue.toString());
        verify(logger,never()).log(Level.INFO,issue.toString());
        verify(logger,never()).log(Level.SEVERE,issue.toString());
        verify(logger,never()).log(Level.WARNING,issue.toString());
    }

    @Test
    void testWarningLogginSLF4JAdaptor() {
        Report report = new Report();
        Issue issue = new IssueBuilder().setSeverity(Severity.WARNING_HIGH).setMessage("Severity High warning").build();
        report.add((issue));
        org.slf4j.Logger logger = mock(LoggerFactory.getLogger(SLF4JAdaptor.class).getClass());
        report.print(new SLF4JAdaptor(logger));
        verify(logger).warn(issue.toString());
        verify(logger,never()).error(issue.toString());
        verify(logger,never()).info(issue.toString());
        verify(logger,never()).trace(issue.toString());

    }
    @Test
    void testErrorLogginSLF4JAdaptor() {
        Report report = new Report();
        Issue issue = new IssueBuilder().setSeverity(Severity.ERROR).setMessage("Severity High warning").build();
        report.add((issue));
        org.slf4j.Logger logger = mock(LoggerFactory.getLogger(SLF4JAdaptor.class).getClass());
        report.print(new SLF4JAdaptor(logger));
        verify(logger).error(issue.toString());
        verify(logger,never()).warn(issue.toString());
        verify(logger,never()).info(issue.toString());
        verify(logger,never()).trace(issue.toString());
    }
    @Test
    void testWarningNormalLogginSLF4JAdaptor() {
        Report report = new Report();
        Issue issue = new IssueBuilder().setSeverity(Severity.WARNING_NORMAL).setMessage("Severity High warning").build();
        report.add((issue));
        org.slf4j.Logger logger = mock(LoggerFactory.getLogger(SLF4JAdaptor.class).getClass());
        report.print(new SLF4JAdaptor(logger));
        verify(logger).info(issue.toString());
        verify(logger,never()).warn(issue.toString());
        verify(logger,never()).error(issue.toString());
        verify(logger,never()).trace(issue.toString());
    }
    @Test
    void testWarningLowLogginSLF4JAdaptor() {
        Report report = new Report();
        Issue issue = new IssueBuilder().setSeverity(Severity.WARNING_LOW).setMessage("Severity High warning").build();
        report.add((issue));
        org.slf4j.Logger logger = mock(LoggerFactory.getLogger(SLF4JAdaptor.class).getClass());
        report.print(new SLF4JAdaptor(logger));
        verify(logger).trace(issue.toString());
        verify(logger,never()).warn(issue.toString());
        verify(logger,never()).error(issue.toString());
        verify(logger,never()).info(issue.toString());
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





