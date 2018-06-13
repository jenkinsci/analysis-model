package edu.hm.hafner.analysis.parser.violations;

import java.io.Reader;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.ParsingCanceledException;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.Report;
import se.bjurr.violations.lib.model.SEVERITY;
import se.bjurr.violations.lib.model.Violation;
import se.bjurr.violations.lib.parsers.ViolationsParser;

/**
 * Adapter for {@link ViolationsParser} instances. Converts the results of a {@link ViolationsParser} into a static
 * analysis {@link Report}.
 *
 * @author Ullrich Hafner
 */
public abstract class AbstractViolationAdapter extends AbstractParser {
    private static final long serialVersionUID = 7203311857999721045L;

    /** Determines whether the Rule property of a {@link Violation} should be used as Category or Type. */
    enum Rule {
        CATEGORY, TYPE
    }

    private final Rule useRuleAs;

    /**
     * Creates a new adapter instance.
     *
     * @param useRuleAs
     *         determines whether the Rule property of a {@link Violation} should be used as Category or Type
     */
    protected AbstractViolationAdapter(final Rule useRuleAs) {
        super();

        this.useRuleAs = useRuleAs;
    }

    @SuppressWarnings({"illegalcatch", "OverlyBroadCatchBlock", "PMD.AvoidCatchingGenericException"})
    @Override
    public Report parse(final Reader reader, final Function<String, String> preProcessor)
            throws ParsingCanceledException, ParsingException {
        try {
            ViolationsParser parser = createParser();
            List<Violation> violations = parser.parseReportOutput(IOUtils.toString(reader));
            return convertToReport(violations);
        }
        catch (Exception exception) {
            throw new ParsingException(exception);
        }
    }

    /**
     * Creates a new parser instance.
     *
     * @return the {@link ViolationsParser} instance
     */
    protected abstract ViolationsParser createParser();

    private Report convertToReport(final List<Violation> violations) {
        Report report = new Report();
        for (Violation violation : violations) {
            if (isValid(violation)) {
                report.add(convertToIssue(violation));
            }
        }
        return report;
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
    protected boolean isValid(final Violation violation) {
        return true;
    }

    private Issue convertToIssue(final Violation violation) {
        IssueBuilder builder = new IssueBuilder();
        builder.setPriority(convertSeverity(violation.getSeverity(), violation))
                .setFileName(violation.getFile())
                .setMessage(violation.getMessage())
                .setLineStart(violation.getStartLine())
                .setLineEnd(violation.getEndLine())
                .setColumnStart(violation.getColumn().or(0));
        String rule = violation.getRule().or(StringUtils.EMPTY);
        if (useRuleAs == Rule.TYPE) {
            builder.setType(rule);
        }
        else {
            builder.setCategory(rule);
        }

        extractAdditionalProperties(builder, violation);

        return builder.build();
    }

    /**
     * Sub-classes may add additional {@link IssueBuilder} properties based on the content of the specified {@link
     * Violation}. This default implementation is empty.
     *
     * @param builder
     *         the issue builder to change
     * @param violation
     *         the violation instance
     */
    protected void extractAdditionalProperties(final IssueBuilder builder, final Violation violation) {
        // default implementation is empty
    }

    /**
     * Computes the {@link Priority} from the specified {@link SEVERITY}. Sub-classes may override and use any of the
     * properties of the provided violation.
     *
     * @param severity
     *         the severity
     * @param violation
     *         the violation instance
     *
     * @return the {@link Priority}
     */
    @SuppressWarnings("unused")
    protected Priority convertSeverity(final SEVERITY severity, final Violation violation) {
        if (severity == SEVERITY.ERROR) {
            return Priority.HIGH;
        }
        if (severity == SEVERITY.WARN) {
            return Priority.NORMAL;
        }
        return Priority.LOW;
    }
}
