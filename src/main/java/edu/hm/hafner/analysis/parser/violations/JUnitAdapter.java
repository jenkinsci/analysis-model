package edu.hm.hafner.analysis.parser.violations;

import edu.hm.hafner.analysis.ParsingCanceledException;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

import java.io.Serial;
import java.util.stream.Stream;
import se.bjurr.violations.lib.model.SEVERITY;
import se.bjurr.violations.lib.model.Violation;
import se.bjurr.violations.lib.parsers.JUnitParser;

import static se.bjurr.violations.lib.model.SEVERITY.*;

/**
 * Parses JUnit files.
 *
 * @author Gyanesha Prajjwal
 */
public class JUnitAdapter extends AbstractViolationAdapter {
    @Serial
    private static final long serialVersionUID = -1595503635554896281L;

    /** Report property key to obtain the total number of tests. */
    public static final String TOTAL_TESTS = "totalTests";
    /** Report property key to obtain the number of passed tests. */
    public static final String PASSED_TESTS = "passedTests";
    /** Report property key to obtain the number of failed tests. */
    public static final String FAILED_TESTS = "failedTests";
    /** Report property key to obtain the number of skipped tests. */
    public static final String SKIPPED_TESTS = "skippedTests";

    @Override
    JUnitParser createParser() {
        return new JUnitParser();
    }

    @Override
    protected Report parseReport(final ReaderFactory readerFactory) throws ParsingCanceledException, ParsingException {
        var report = super.parseReport(readerFactory);

        countFailedAndSkippedTests(readerFactory, report);

        return report;
    }

    @Override
    Severity convertSeverity(final SEVERITY severity, final Violation violation) {
        if (severity == ERROR) {
            return Severity.ERROR;
        }
        return Severity.WARNING_NORMAL;
    }

    private void countFailedAndSkippedTests(final ReaderFactory readerFactory, final Report report) {
        int total = count(readerFactory, "<testcase");
        report.setCounter(TOTAL_TESTS, total);

        int skipped = count(readerFactory, "<skipped");
        report.setCounter(SKIPPED_TESTS, skipped);

        var failed = report.size() - skipped;
        report.setCounter(FAILED_TESTS, failed);

        report.setCounter(PASSED_TESTS, total - skipped - failed);
    }

    private int count(final ReaderFactory readerFactory, final String text) {
        try (Stream<String> lines = readerFactory.readStream()) {
            return Math.toIntExact(lines
                    .filter(line -> line.contains(text))
                    .count());
        }
    }
}
