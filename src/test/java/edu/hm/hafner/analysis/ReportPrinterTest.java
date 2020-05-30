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
import edu.hm.hafner.analysis.Report.JULAdapter;
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
    public void exceptionAdapterSLF4J() {
        assertThatThrownBy(()-> {
            SLF4JAdapter adapter = new SLF4JAdapter(null);
        }).isInstanceOf(NullPointerException.class);
    }
    @Test
    public void exceptionAdapterJavaUtil() {
        assertThatThrownBy(()-> {
            JULAdapter adapter = new JULAdapter(null);
        }).isInstanceOf(NullPointerException.class);
    }
    @Test
    public void noIssueJUL() {
        Logger loggerJul = mock(Logger.class);
        JULAdapter adapter = new JULAdapter(loggerJul);

        assertThatThrownBy(()-> {
            adapter.print(null);
        }).isInstanceOf(NullPointerException.class);
    }
    @Test
    public void noIssueSF() {
        org.slf4j.Logger loggerSL = mock(org.slf4j.Logger.class);
        SLF4JAdapter adapter = new SLF4JAdapter(loggerSL);

        assertThatThrownBy(() -> {
            adapter.print(null);
        }).isInstanceOf(NullPointerException.class);
    }
    @Test
    public void illegalSeveritySLF() {
        org.slf4j.Logger loggerSL = mock(org.slf4j.Logger.class);
        SLF4JAdapter adapter = new SLF4JAdapter(loggerSL);

        assertThatThrownBy(() -> {
            adapter.print(new IssueBuilder().setSeverity(new Severity("illegal")).build());
        }).isInstanceOf(IllegalArgumentException.class);
    }
    @Test
    public void illegalSeverityJUL() {
        Logger loggerJul = mock(Logger.class);
        JULAdapter adapter = new JULAdapter(loggerJul);

        assertThatThrownBy(() -> {
            adapter.print(new IssueBuilder().setSeverity(new Severity("illegal")).build());
        }).isInstanceOf(IllegalArgumentException.class);
    }
    @Test
    public void testJULERROR() {
        Report report = new Report();
        Issue issue = new IssueBuilder().setSeverity(Severity.ERROR).setMessage("error").build();

        report.add(issue);
        java.util.logging.Logger logger = mock(Logger.getLogger("edu.hm.hafner.analysis.Report.JULAdapter").getClass());
        report.print(new JULAdapter(logger));
        verify(logger).log(Level.SEVERE, issue.toString());
    }
    @Test
    public void testJULWARNHIGH() {
        Report report = new Report();
        Issue issue = new IssueBuilder().setSeverity(Severity.WARNING_HIGH).setMessage("error").build();

        report.add(issue);
        java.util.logging.Logger logger = mock(Logger.getLogger("edu.hm.hafner.analysis.Report.JULAdapter").getClass());
        report.print(new JULAdapter(logger));
        verify(logger).log(Level.WARNING, issue.toString());
    }

    @Test
    public void testJULWARNNORM() {
        Report report = new Report();
        Issue issue = new IssueBuilder().setSeverity(Severity.WARNING_NORMAL).setMessage("error").build();

        report.add(issue);
        java.util.logging.Logger logger = mock(Logger.getLogger("edu.hm.hafner.analysis.Report.JULAdapter").getClass());
        report.print(new JULAdapter(logger));
        verify(logger).log(Level.INFO, issue.toString());
    }

    @Test
    public void testJULWARNLOW() {
        Report report = new Report();
        Issue issue = new IssueBuilder().setSeverity(Severity.WARNING_LOW).setMessage("error").build();

        report.add(issue);
        java.util.logging.Logger logger = mock(Logger.getLogger("edu.hm.hafner.analysis.Report.JULAdapter").getClass());
        report.print(new JULAdapter(logger));
        verify(logger).log(Level.FINE, issue.toString());
    }

    @Test
    public void testSLF4JERROR() {
        Report report = new Report();
        Issue issue = new IssueBuilder().setSeverity(Severity.ERROR).setMessage("error").build();

        report.add(issue);
        org.slf4j.Logger logger = mock(LoggerFactory.getLogger(SLF4JAdapter.class).getClass());

        report.print(new SLF4JAdapter(logger));
        verify(logger).error(issue.toString());
        verify(logger, never()).warn(issue.toString());
    }
    @Test
    public void testSLF4JWARNHIGH() {
        Report report = new Report();
        Issue issue = new IssueBuilder().setSeverity(Severity.WARNING_HIGH).setMessage("error").build();

        report.add(issue);
        org.slf4j.Logger logger = mock(LoggerFactory.getLogger(SLF4JAdapter.class).getClass());

        report.print(new SLF4JAdapter(logger));
        verify(logger, never()).error(issue.toString());
        verify(logger).warn(issue.toString());
    }

    @Test
    public void testSLF4JWARNNORM() {
        Report report = new Report();
        Issue issue = new IssueBuilder().setSeverity(Severity.WARNING_NORMAL).setMessage("error").build();

        report.add(issue);
        org.slf4j.Logger logger = mock(LoggerFactory.getLogger(SLF4JAdapter.class).getClass());

        report.print(new SLF4JAdapter(logger));
        verify(logger).info(issue.toString());
        verify(logger, never()).warn(issue.toString());
        verify(logger, never()).trace(issue.toString());
        verify(logger, never()).error(issue.toString());
    }

    @Test
    public void testSLF4JWARNLOW() {
        Report report = new Report();
        Issue issue = new IssueBuilder().setSeverity(Severity.WARNING_LOW).setMessage("error").build();

        report.add(issue);
        org.slf4j.Logger logger = mock(LoggerFactory.getLogger(SLF4JAdapter.class).getClass());

        report.print(new SLF4JAdapter(logger));
        verify(logger, never()).info(issue.toString());
        verify(logger, never()).warn(issue.toString());
        verify(logger).trace(issue.toString());
        verify(logger, never()).error(issue.toString());
    }
}
