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

    /**
     * SLF4J Tests
     *
     * @author Viet Phuoc Ho (v.ho@hm.edu)
     */
    @Test
    public void SLF4JNull(){
        assertThatThrownBy(() -> {
            SLF4JAdapter slf4j = new SLF4JAdapter(null);
        }).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void SLF4JnoIssue(){
        org.slf4j.Logger log = mock(org.slf4j.Logger.class);
        SLF4JAdapter slf4j = new SLF4JAdapter(log);

        assertThatThrownBy(() ->{
           slf4j.print(null);
        }).isInstanceOf(NullPointerException.class);
    }

    @Test
    void SLF4JLoggerTest(){
        Report report = new Report();
        report.add(new IssueBuilder().setSeverity(Severity.WARNING_LOW).setMessage("Low").build());
        report.add(new IssueBuilder().setSeverity(Severity.WARNING_NORMAL).setMessage("Normal").build());
        report.add(new IssueBuilder().setSeverity(Severity.WARNING_HIGH).setMessage("High").build());
        report.add(new IssueBuilder().setSeverity(Severity.ERROR).setMessage("Error").build());
        org.slf4j.Logger slf4j = mock(LoggerFactory.getLogger(SLF4JAdapter.class).getClass());

        report.print(new SLF4JAdapter(slf4j));

        verify(slf4j).trace(new IssueBuilder().setSeverity(Severity.WARNING_LOW).setMessage("Low").build().toString());
        verify(slf4j).info(new IssueBuilder().setSeverity(Severity.WARNING_NORMAL).setMessage("Normal").build().toString());
        verify(slf4j).warn(new IssueBuilder().setSeverity(Severity.WARNING_HIGH).setMessage("High").build().toString());
        verify(slf4j).error(new IssueBuilder().setSeverity(Severity.ERROR).setMessage("Error").build().toString());
    }
    /**
     * JUL Tests
     *
     * @author Viet Phuoc Ho (v.ho@hm.edu)
     */
    @Test
    public void JULNull(){
        assertThatThrownBy(() -> {
            JULAdapter jul = new JULAdapter(null);
        }).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void JULnoIssue(){
        Logger log = mock(Logger.class);
        JULAdapter jul = new JULAdapter(log);

        assertThatThrownBy(() ->{
            jul.print(null);
        }).isInstanceOf(NullPointerException.class);
    }


    @Test
    public void JULLoggerTest() {
        Report report = new Report();
        report.add( new IssueBuilder().setSeverity(Severity.WARNING_LOW).setMessage("Low").build());
        report.add( new IssueBuilder().setSeverity(Severity.WARNING_NORMAL).setMessage("Normal").build());
        report.add( new IssueBuilder().setSeverity(Severity.WARNING_HIGH).setMessage("High").build());
        report.add( new IssueBuilder().setSeverity(Severity.ERROR).setMessage("Error").build());

        java.util.logging.Logger JUL = mock(java.util.logging.Logger.class);

        report.print(new JULAdapter(JUL));
        verify(JUL).log(Level.FINE, new IssueBuilder().setSeverity(Severity.WARNING_LOW).setMessage("Low").build().toString());
        verify(JUL).log(Level.INFO, new IssueBuilder().setSeverity(Severity.WARNING_NORMAL).setMessage("Normal").build().toString());
        verify(JUL).log(Level.WARNING, new IssueBuilder().setSeverity(Severity.WARNING_HIGH).setMessage("High").build().toString());
        verify(JUL).log(Level.SEVERE, new IssueBuilder().setSeverity(Severity.ERROR).setMessage("Error").build().toString());
}



}
