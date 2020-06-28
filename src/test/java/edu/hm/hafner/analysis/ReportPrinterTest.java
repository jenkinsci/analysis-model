package edu.hm.hafner.analysis;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
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
    void shouldPrintAllIssuesByJavaUtilLogger(){
        Report report = readCheckStyleReport();
        java.util.logging.Logger printer = mock(java.util.logging.Logger.class);
        report.print(new JavaUtilLogger(printer));
        for (Issue issue : report) {
            final String logMessageOfIssue = issue.toString();
            if(issue.getSeverity().equals(Severity.WARNING_LOW))
                verify(printer).log(Level.FINE, logMessageOfIssue);
            else if(issue.getSeverity().equals(Severity.WARNING_NORMAL))
                verify(printer).log(Level.INFO, logMessageOfIssue);
            else if(issue.getSeverity().equals(Severity.WARNING_HIGH))
                verify(printer).log(Level.WARNING, logMessageOfIssue);
            else if(issue.getSeverity().equals(Severity.ERROR))
                verify(printer).log(Level.SEVERE, logMessageOfIssue);
//                verify(printer).log(Level.SEVERE, logMessageOfIssue + " hallo"); -> Test failed => wird richtig getestet
        }
    }

    // todo: mit warn() anstatt atWarn() würde test gehen. jedoch soll man ja atWarn() benutzen.
    @Test
    void shouldPrintAllIssuesBySLF4JLogger(){
        Report report = readCheckStyleReport();
        org.slf4j.Logger printer = mock(org.slf4j.Logger.class);
        // todo: atError() funktioniert leider nur ohne mock().
        report.print(new SLF4JLogger("Report.class"));
        // Test, ob geht ews mict mock() geht am bsp info() (da in SLF4JLogger nur info anstelle von atInfo()
        for (Issue issue : report) {
            if(issue.getSeverity().equals(Severity.WARNING_NORMAL)) {
                Report infoReport = new Report();
                infoReport.add(issue);
                infoReport.print(new SLF4JLogger(printer));
                verify(printer).info(issue.toString());
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
