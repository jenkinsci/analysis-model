package edu.hm.hafner.analysis.parser.violations;

import java.io.Serial;
import java.util.stream.Stream;

import edu.hm.hafner.analysis.ParsingCanceledException;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;

import se.bjurr.violations.lib.parsers.JUnitParser;

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
    public Report parse(final ReaderFactory readerFactory) throws ParsingCanceledException, ParsingException {
        Report report = super.parse(readerFactory);
        int total = count(readerFactory, "<testcase");
        report.setCounter(TOTAL_TESTS, total);
        int skipped = count(readerFactory, "<skipped");
        report.setCounter(SKIPPED_TESTS, skipped);
        report.setCounter(FAILED_TESTS, report.size());
        report.setCounter(PASSED_TESTS, total - skipped - report.size());
        return report;
    }

    private int count(final ReaderFactory readerFactory, final String text) {
        try (Stream<String> lines = readerFactory.readStream()) {
            return Math.toIntExact(lines
                    .filter(line -> line.contains(text))
                    .count());
        }
    }
}
