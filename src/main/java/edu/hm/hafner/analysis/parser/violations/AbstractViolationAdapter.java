package edu.hm.hafner.analysis.parser.violations;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Location;
import edu.hm.hafner.analysis.ParsingCanceledException;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.TreeString;
import edu.umd.cs.findbugs.annotations.CheckForNull;

import java.io.Serial;
import java.util.Set;
import java.util.logging.Level;
import se.bjurr.violations.lib.ViolationsLogger;
import se.bjurr.violations.lib.model.SEVERITY;
import se.bjurr.violations.lib.model.Violation;
import se.bjurr.violations.lib.parsers.ViolationsParser;

import static se.bjurr.violations.lib.model.SEVERITY.*;

/**
 * Adapter for {@link ViolationsParser} instances. Converts the results of a {@link ViolationsParser} into a static
 * analysis {@link Report}.
 *
 * @author Ullrich Hafner
 */
public abstract class AbstractViolationAdapter extends IssueParser {
    @Serial
    private static final long serialVersionUID = 7203311857999721045L;

    @SuppressWarnings({"illegalcatch", "OverlyBroadCatchBlock"})
    @Override
    public Report parseReport(final ReaderFactory readerFactory)
            throws ParsingCanceledException, ParsingException {
        try {
            var parser = createParser();
            Set<Violation> violations = parser.parseReportOutput(readerFactory.readString(),
                    new NullViolationsLogger());
            return convertToReport(violations);
        }
        catch (Exception exception) {
            throw new ParsingException(exception, readerFactory);
        }
    }

    /**
     * Creates a new parser instance.
     *
     * @return the {@link ViolationsParser} instance
     */
    abstract ViolationsParser createParser();

    /**
     * Converts the list of violations to a corresponding report of issues.
     *
     * @param violations
     *         the violations
     *
     * @return the report
     */
    Report convertToReport(final Set<Violation> violations) {
        try (var builder = new IssueBuilder()) {
            var report = new Report();

            for (Violation violation : violations) {
                if (isValid(violation)) {
                    report.add(convertToIssue(violation, builder));
                }
            }
            postProcess(report, violations);

            return report;
        }
    }

    /**
     * Post processes the report.
     *
     * @param report
     *         the report with all converted and valid issues
     * @param violations
     *         the violations that have been converted
     */
    void postProcess(final Report report, final Set<Violation> violations) {
        // empty default implementation
    }

    /**
     * Returns whether this violation is valid and should be converted to an {@link Issue}. Return {@code false} if the
     * specified violation is a false positive or should not be counted.
     *
     * @param violation
     *         the violation to check
     *
     * @return {@code true} if the violation is valid, {@code false} otherwise
     */
    boolean isValid(final Violation violation) {
        return true;
    }

    /**
     * Converts the specified violation to a corresponding {@link Issue} instance.
     *
     * @param violation
     *         the violation
     * @param builder
     *         the issue builder to use
     *
     * @return corresponding {@link Issue}
     */
    Issue convertToIssue(final Violation violation, final IssueBuilder builder) {
        updateIssueBuilder(violation, builder);
        extractAdditionalProperties(builder, violation);

        return builder.buildAndClean();
    }

    /**
     * Converts the specified violation to a corresponding {@link Issue} instance by setting the properties in the
     * provided {@link IssueBuilder}.
     *
     * @param violation
     *         the violation
     * @param builder
     *         the issue builder to change
     */
    void updateIssueBuilder(final Violation violation, final IssueBuilder builder) {
        var location = new Location(TreeString.valueOf(getFileName(violation)),
                toValidInt(violation.getStartLine()),
                        toValidInt(violation.getEndLine()),
                        toValidInt(violation.getColumn()),
                        toValidInt(violation.getEndColumn()));
        builder.setSeverity(convertSeverity(violation.getSeverity(), violation))
                .setMessage(violation.getMessage())
                .addLocation(location)
                .setType(violation.getRule())
                .setCategory(violation.getCategory());
    }

    protected String getFileName(final Violation violation) {
        return violation.getFile();
    }

    /**
     * Creates a default Integer representation for undefined input parameters.
     *
     * @param integer
     *         the integer to check
     *
     * @return the valid integer value or 0 if the specified {@link Integer} is {@code null} or less than 0
     */
    int toValidInt(@CheckForNull final Integer integer) {
        if (integer == null) {
            return 0;
        }
        return Math.max(integer, 0);
    }

    /**
     * Subclasses may add additional {@link IssueBuilder} properties based on the content of the specified {@link
     * Violation}. This default implementation is empty.
     *
     * @param builder
     *         the issue builder to change
     * @param violation
     *         the violation instance
     */
    void extractAdditionalProperties(final IssueBuilder builder, final Violation violation) {
        // default implementation is empty
    }

    /**
     * Computes the {@link Severity} from the specified {@link SEVERITY}. Subclasses may override and use any of the
     * properties of the provided violation.
     *
     * @param severity
     *         the severity
     * @param violation
     *         the violation instance
     *
     * @return the {@link Severity}
     */
    Severity convertSeverity(final SEVERITY severity, final Violation violation) {
        if (severity == ERROR) {
            return Severity.WARNING_HIGH;
        }
        if (severity == WARN) {
            return Severity.WARNING_NORMAL;
        }
        return Severity.WARNING_LOW;
    }

    /**
     * A logger that does nothing.
     */
    private static class NullViolationsLogger implements ViolationsLogger {
        @Override
        public void log(final Level level, final String s) {
            // do not log anything
        }

        @Override
        public void log(final Level level, final String s, final Throwable throwable) {
            // do not log anything
        }
    }
}
