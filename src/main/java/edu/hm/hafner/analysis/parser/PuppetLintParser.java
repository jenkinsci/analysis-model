package edu.hm.hafner.analysis.parser;

import java.io.Serial;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

/**
 * A parser for puppet-lint checks warnings.
 *
 * @author Jan Vansteenkiste
 */
public class PuppetLintParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = 7492869677427430346L;

    private static final String SEPARATOR = "::";
    private static final String PUPPET_LINT_PATTERN_WARNING = "^\\s*((?:[A-Za-z]:)?[^:]+):([0-9]+):([^:]+):("
            + "(?:WARNING)|(?:ERROR)):\\s*(.*)$";
    private static final String PUPPET_LINT_PATTERN_PACKAGE = "^(.*/?modules/)?([^/]*)/manifests(.*)?(/([^/]*)\\.pp)$";
    private static final Pattern PACKAGE_PATTERN = Pattern.compile(PUPPET_LINT_PATTERN_PACKAGE);

    /**
     * Creates a new instance of {@link PuppetLintParser}.
     */
    public PuppetLintParser() {
        super(PUPPET_LINT_PATTERN_WARNING);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        return builder.setFileName(matcher.group(1))
                .setLineStart(matcher.group(2))
                .setCategory(matcher.group(3))
                .setPackageName(detectModuleName(matcher.group(1)))
                .setMessage(matcher.group(5))
                .setSeverity(mapPriority(matcher.group(4)))
                .buildOptional();
    }

    private Severity mapPriority(final String level) {
        var priority = Severity.WARNING_NORMAL;
        if (level.contains("error") || level.contains("ERROR")) {
            priority = Severity.WARNING_HIGH;
        }
        return priority;
    }

    private String detectModuleName(final String fileName) {
        if (StringUtils.isNotBlank(fileName)) {
            return splitFileName(fileName);
        }
        return StringUtils.EMPTY;
    }

    private String splitFileName(final String fileName) {
        var matcher = PACKAGE_PATTERN.matcher(fileName);
        if (matcher.find()) {
            var main = matcher.group(2);
            var subclassed = matcher.group(3);
            var module = SEPARATOR + main;
            if (StringUtils.isNotBlank(subclassed)) {
                module += StringUtils.replace(subclassed, "/", SEPARATOR);
            }
            return module;
        }
        return StringUtils.EMPTY;
    }
}
