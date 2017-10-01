package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.RegexpLineParser;

/**
 * A parser for puppet-lint checks warnings.
 *
 * @author Jan Vansteenkiste <jan@vstone.eu>
 */
public class PuppetLintParser extends RegexpLineParser {
    private static final long serialVersionUID = 7492869677427430346L;
    private static final String SEPARATOR = "::";

    /** Pattern of puppet-lint compiler warnings. */
    private static final String PUPPET_LINT_PATTERN_WARNING = "^\\s*((?:[A-Za-z]:)?[^:]+):([0-9]+):([^:]+):(" +
            "(?:WARNING)|(?:ERROR)):\\s*(.*)$";

    private static final String PUPPET_LINT_PATTERN_PACKAGE = "^(.*/?modules/)?([^/]*)/manifests(.*)?(/([^/]*)\\.pp)$";

    private final Pattern packagePattern;

    /**
     * Creates a new instance of {@link PuppetLintParser}.
     */
    public PuppetLintParser() {
        super("puppet-lint", PUPPET_LINT_PATTERN_WARNING);

        packagePattern = Pattern.compile(PUPPET_LINT_PATTERN_PACKAGE);
    }

    @Override
    protected Issue createWarning(final Matcher matcher) {
        final String fileName = matcher.group(1);
        final String start = matcher.group(2);
        final String category = matcher.group(3);
        final String level = matcher.group(4);
        final String message = matcher.group(5);

        Priority priority = Priority.NORMAL;
        if (level.contains("error") || level.contains("ERROR")) {
            priority = Priority.HIGH;
        }

        return issueBuilder().setFileName(fileName).setLineStart(Integer.parseInt(start)).setType(getId())
                             .setCategory(category).setPackageName(detectModuleName(fileName)).setMessage(message)
                             .setPriority(priority).build();
    }

    private String detectModuleName(final String fileName) {
        if (StringUtils.isNotBlank(fileName)) {
            return splitFileName(fileName);
        }
        return StringUtils.EMPTY;
    }

    private String splitFileName(final String fileName) {
        Matcher matcher = packagePattern.matcher(fileName);
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
