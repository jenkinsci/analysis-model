package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.RegexpLineParser;

/**
 * A parser for puppet-lint checks warnings.
 *
 * @author Jan Vansteenkiste
 */
public class PuppetLintParser extends RegexpLineParser {
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
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        return builder.setFileName(matcher.group(1))
                .setLineStart(parseInt(matcher.group(2)))
                .setCategory(matcher.group(3))
                .setPackageName(detectModuleName(matcher.group(1)))
                .setMessage(matcher.group(5))
                .setPriority(mapPriority(matcher.group(4)))
                .build();
    }

    private Priority mapPriority(final String level) {
        Priority priority = Priority.NORMAL;
        if (level.contains("error") || level.contains("ERROR")) {
            priority = Priority.HIGH;
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
        Matcher matcher = PACKAGE_PATTERN.matcher(fileName);
        if (matcher.find()) {
            String main = matcher.group(2);
            String subclassed = matcher.group(3);
            String module = SEPARATOR + main;
            if (StringUtils.isNotBlank(subclassed)) {
                module += StringUtils.replace(subclassed, "/", SEPARATOR);
            }
            return module;
        }
        return StringUtils.EMPTY;
    }
}
