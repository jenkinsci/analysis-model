package edu.hm.hafner.analysis;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
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
        report.add(issue);
        IssuePrinter issuePrinter = mock(JavaUtilLoggingAdapter.class);
    }

    @Test
    void JavaUtilWarning_High(){
        Issue issue = new IssueBuilder().setSeverity(Severity.WARNING_HIGH).setMessage("Severity Error").build();
        IssuePrinter issuePrinter = mock(JavaUtilLoggingAdapter.class);
        issuePrinter.print(issue);
    }

    @Test
    void JavaUtilWarning_Normal(){
        Issue issue = new IssueBuilder().setSeverity(Severity.WARNING_NORMAL).setMessage("Severity Error").build();
        IssuePrinter issuePrinter = mock(JavaUtilLoggingAdapter.class);
        issuePrinter.print(issue);
    }

    @Test
    void JavaUtilWarning_Lock(){
        Issue issue = new IssueBuilder().setSeverity(Severity.WARNING_LOW).setMessage("Severity Error").build();
        IssuePrinter issuePrinter = mock(JavaUtilLoggingAdapter.class);
        issuePrinter.print(issue);
    }


}
