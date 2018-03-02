package edu.hm.hafner.analysis.parser.violations;

import java.io.Reader;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import se.bjurr.violations.lib.model.SEVERITY;
import se.bjurr.violations.lib.model.Violation;
import se.bjurr.violations.lib.parsers.ViolationsParser;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.ParsingCanceledException;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Priority;

/**
 * Adapter for {@link ViolationsParser} instances. Converts the results of an {@link ViolationsParser} into the static
 * analysis {@link Issues} model.
 *
 * @author Ullrich Hafner
 */
public abstract class AbstractViolationAdapter extends AbstractParser<Issue> {
    private static final long serialVersionUID = 7203311857999721045L;

    /** Determines whether the Rule property of a {@link Violation} should be used as Category or Type. */
    enum Rule {
        CATEGORY, TYPE
    }

    private final Rule useRuleAs;

    protected AbstractViolationAdapter(final Rule useRuleAs) {
        super();

        this.useRuleAs = useRuleAs;
    }

    @SuppressWarnings({"illegalcatch", "OverlyBroadCatchBlock", "PMD.AvoidCatchingGenericException"})
    @Override
    public Issues<Issue> parse(final Reader reader, final Function<String, String> preProcessor)
            throws ParsingCanceledException, ParsingException {
        try {
            ViolationsParser parser = createParser();
            List<Violation> violations = parser.parseReportOutput(IOUtils.toString(reader));
            return convertToIssues(violations);
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

    private Issues<Issue> convertToIssues(final List<Violation> violations) {
        Issues<Issue> issues = new Issues<>();
        for (Violation violation : violations) {
            issues.add(convertToIssue(violation));
        }
        return issues;
    }

    private Issue convertToIssue(final Violation violation) {
        IssueBuilder builder = new IssueBuilder();
        builder.setPriority(convertSeverity(violation.getSeverity()))
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
        return builder.build();
    }

    private Priority convertSeverity(final SEVERITY severity) {
        if (severity == SEVERITY.ERROR) {
            return Priority.HIGH;
        }
        if (severity == SEVERITY.WARN) {
            return Priority.NORMAL;
        }
        return Priority.LOW;
    }
}
