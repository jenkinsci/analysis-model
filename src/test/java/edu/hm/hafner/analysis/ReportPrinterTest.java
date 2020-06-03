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
    public void JULAdapterTest() {
        Report sut = new Report();
        sut.add( new IssueBuilder().setSeverity(Severity.ERROR).setMessage("ERROR").build());
        sut.add( new IssueBuilder().setSeverity(Severity.WARNING_HIGH).setMessage("WARNING_HIGH").build());
        sut.add( new IssueBuilder().setSeverity(Severity.WARNING_NORMAL).setMessage("WARNING_NORMAL").build());
        sut.add( new IssueBuilder().setSeverity(Severity.WARNING_LOW).setMessage("WARNING_LOW").build());
        java.util.logging.Logger log = mock(java.util.logging.Logger.class);
        sut.print(new JULAdapter(log));
        verify(log).log(Level.SEVERE, new IssueBuilder().setSeverity(Severity.ERROR).setMessage("ERROR").build().toString());
        verify(log).log(Level.WARNING, new IssueBuilder().setSeverity(Severity.WARNING_HIGH).setMessage("WARNING_HIGH").build().toString());
        verify(log).log(Level.INFO, new IssueBuilder().setSeverity(Severity.WARNING_NORMAL).setMessage("WARNING_NORMAL").build().toString());
        verify(log).log(Level.FINE, new IssueBuilder().setSeverity(Severity.WARNING_LOW).setMessage("WARNING_LOW").build().toString());
    }
    @Test
    public void SLF4JAdapter() {
        Report sut = new Report();
        sut.add( new IssueBuilder().setSeverity(Severity.ERROR).setMessage("ERROR").build());
        sut.add( new IssueBuilder().setSeverity(Severity.WARNING_HIGH).setMessage("WARNING_HIGH").build());
        sut.add( new IssueBuilder().setSeverity(Severity.WARNING_NORMAL).setMessage("WARNING_NORMAL").build());
        sut.add( new IssueBuilder().setSeverity(Severity.WARNING_LOW).setMessage("WARNING_LOW").build());
        org.slf4j.Logger logger = mock(LoggerFactory.getLogger(SLF4JAdapter.class).getClass());
        sut.print(new SLF4JAdapter(logger));
        verify(logger).error(new IssueBuilder().setSeverity(Severity.ERROR).setMessage("ERROR").build().toString());
        verify(logger).warn(new IssueBuilder().setSeverity(Severity.WARNING_HIGH).setMessage("WARNING_HIGH").build().toString());
        verify(logger).trace(new IssueBuilder().setSeverity(Severity.WARNING_LOW).setMessage("WARNING_LOW").build().toString());
        verify(logger).info(new IssueBuilder().setSeverity(Severity.WARNING_NORMAL).setMessage("WARNING_NORMAL").build().toString());
    }
}
