package edu.hm.hafner.analysis;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
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

        PrintStream printStream = mock(PrintStream.class);
        report.print(new StandardOutputPrinter(printStream));

        for (Issue issue : report) {
            verify(printStream).println(issue.toString());
        }
    }

    @Test
    void jULCtorCallWithNullBooms() {
        // act
        assertThatIllegalArgumentException().isThrownBy(
                () -> new JULAdapter(null)
        );
    }

    @Test
    void printNullViaJULBooms() {
        // arrange
        final java.util.logging.Logger logger = mock(
                java.util.logging.Logger.getLogger("edu.hm.hafner.analysis.JULAdapter")
                        .getClass()
        );
        // act
        assertThatIllegalArgumentException().isThrownBy(
                () -> new JULAdapter(logger).print(null)
        );
    }

    @Test
    void shouldJULLogError() {
        // arrange
        final Report report = new Report();
        report.add(
                new IssueBuilder()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Error")
                        .build()
        );
        final java.util.logging.Logger logger = mock(
                java.util.logging.Logger.getLogger("edu.hm.hafner.analysis.JULAdapter")
                        .getClass()
        );
        // act
        report.print(new JULAdapter(logger));
        // assert
        for (final Issue issue : report) {
            verify(logger).log(Level.SEVERE, issue.toString());
            verify(logger, never()).log(Level.WARNING, issue.toString());
            verify(logger, never()).log(Level.INFO, issue.toString());
            verify(logger, never()).log(Level.FINE, issue.toString());
        }
    }

    @Test
    void shouldJULLogWarningHigh() {
        // arrange
        final Report report = new Report();
        report.add(
                new IssueBuilder()
                        .setSeverity(Severity.WARNING_HIGH)
                        .setMessage("Warning High")
                        .build()
        );
        final java.util.logging.Logger logger = mock(
                java.util.logging.Logger.getLogger("edu.hm.hafner.analysis.JULAdapter")
                        .getClass()
        );
        // act
        report.print(new JULAdapter(logger));
        // assert
        for (final Issue issue : report) {
            verify(logger, never()).log(Level.SEVERE, issue.toString());
            verify(logger).log(Level.WARNING, issue.toString());
            verify(logger, never()).log(Level.INFO, issue.toString());
            verify(logger, never()).log(Level.FINE, issue.toString());
        }
    }

    @Test
    void shouldJULLogWarningNormal() {
        // arrange
        final Report report = new Report();
        report.add(
                new IssueBuilder()
                        .setSeverity(Severity.WARNING_NORMAL)
                        .setMessage("Warning Normal")
                        .build()
        );
        final java.util.logging.Logger logger = mock(
                java.util.logging.Logger.getLogger("edu.hm.hafner.analysis.JULAdapter")
                        .getClass()
        );
        // act
        report.print(new JULAdapter(logger));
        // assert
        for (final Issue issue : report) {
            verify(logger, never()).log(Level.SEVERE, issue.toString());
            verify(logger, never()).log(Level.WARNING, issue.toString());
            verify(logger).log(Level.INFO, issue.toString());
            verify(logger, never()).log(Level.FINE, issue.toString());
        }
    }

    @Test
    void shouldJULLogWarningLow() {
        // arrange
        final Report report = new Report();
        report.add(
                new IssueBuilder()
                        .setSeverity(Severity.WARNING_LOW)
                        .setMessage("Warning Low")
                        .build()
        );
        final java.util.logging.Logger logger = mock(
                java.util.logging.Logger.getLogger("edu.hm.hafner.analysis.JULAdapter")
                        .getClass()
        );
        // act
        report.print(new JULAdapter(logger));
        // assert
        for (final Issue issue : report) {
            verify(logger, never()).log(Level.SEVERE, issue.toString());
            verify(logger, never()).log(Level.WARNING, issue.toString());
            verify(logger, never()).log(Level.INFO, issue.toString());
            verify(logger).log(Level.FINE, issue.toString());
        }
    }

    @Test
    void sLF4JCtorCallWithNullBooms() {
        // act
        assertThatIllegalArgumentException().isThrownBy(
                () -> new SLF4JAdapter(null)
        );
    }

    @Test
    void printNullViaSLF4JBooms() {
        // arrange
        final org.slf4j.Logger logger = mock(
                org.slf4j.LoggerFactory.getLogger(SLF4JAdapter.class)
                        .getClass()
        );
        // act
        assertThatIllegalArgumentException().isThrownBy(
                () -> new SLF4JAdapter(logger).print(null)
        );
    }

    @Test
    void shouldSLF4JLogError() {
        // arrange
        final Report report = new Report();
        report.add(
                new IssueBuilder()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Error")
                        .build()
        );
        final org.slf4j.Logger logger = mock(
                org.slf4j.LoggerFactory.getLogger(SLF4JAdapter.class)
                        .getClass()
        );
        // act
        report.print(new SLF4JAdapter(logger));
        // assert
        for (final Issue issue : report) {
            verify(logger).error(issue.toString());
            verify(logger, never()).warn(issue.toString());
            verify(logger, never()).info(issue.toString());
            verify(logger, never()).trace(issue.toString());
        }
    }

    @Test
    void shouldSLF4JLogWarningHigh() {
        // arrange
        final Report report = new Report();
        report.add(
                new IssueBuilder()
                        .setSeverity(Severity.WARNING_HIGH)
                        .setMessage("Warning High")
                        .build()
        );
        final org.slf4j.Logger logger = mock(
                org.slf4j.LoggerFactory.getLogger(SLF4JAdapter.class)
                        .getClass()
        );
        // act
        report.print(new SLF4JAdapter(logger));
        // assert
        for (final Issue issue : report) {
            verify(logger, never()).error(issue.toString());
            verify(logger).warn(issue.toString());
            verify(logger, never()).info(issue.toString());
            verify(logger, never()).trace(issue.toString());
        }
    }

    @Test
    void shouldSLF4JLogWarningNormal() {
        // arrange
        final Report report = new Report();
        report.add(
                new IssueBuilder()
                        .setSeverity(Severity.WARNING_NORMAL)
                        .setMessage("Warning Normal")
                        .build()
        );
        final org.slf4j.Logger logger = mock(
                org.slf4j.LoggerFactory.getLogger(SLF4JAdapter.class)
                        .getClass()
        );
        // act
        report.print(new SLF4JAdapter(logger));
        // assert
        for (final Issue issue : report) {
            verify(logger, never()).error(issue.toString());
            verify(logger, never()).warn(issue.toString());
            verify(logger).info(issue.toString());
            verify(logger, never()).trace(issue.toString());
        }
    }

    @Test
    void shouldSLF4JLogWarningLow() {
        // arrange
        final Report report = new Report();
        report.add(
                new IssueBuilder()
                        .setSeverity(Severity.WARNING_LOW)
                        .setMessage("Warning Low")
                        .build()
        );
        final org.slf4j.Logger logger = mock(
                org.slf4j.LoggerFactory.getLogger(SLF4JAdapter.class)
                        .getClass()
        );
        // act
        report.print(new SLF4JAdapter(logger));
        // assert
        for (final Issue issue : report) {
            verify(logger, never()).error(issue.toString());
            verify(logger, never()).warn(issue.toString());
            verify(logger, never()).info(issue.toString());
            verify(logger).trace(issue.toString());
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
