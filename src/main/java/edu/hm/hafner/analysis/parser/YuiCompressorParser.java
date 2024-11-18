package edu.hm.hafner.analysis.parser;

import java.io.Serial;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

/**
 * A parser for the YUI Compressor warnings.
 */
public class YuiCompressorParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = -4807932429496693096L;
    private static final String YUI_COMPRESSOR_WARNING_PATTERN = "\\[WARNING\\] (.*)";

    private static final Pattern UNUSED_SYMBOL_PATTERN = Pattern
            .compile("The symbol [^ ]+ is declared but is apparently never used.*");
    private static final Pattern UNUSED_VARIABLE_PATTERN = Pattern
            .compile("The variable [^ ]+ has already been declared in the same scope.*");
    private static final Pattern UNUSED_FUNCTION_PATTERN = Pattern
            .compile("The function [^ ]+ has already been declared in the same scope.*");
    private static final Pattern INVALID_HINT_PATTERN = Pattern.compile("Invalid hint syntax: [^ ]+");
    private static final Pattern UNSUPPORTED_HINT_PATTERN = Pattern.compile("Unsupported hint value: [^ ]+");
    private static final Pattern UNKNOWN_HINT_PATTERN = Pattern.compile("Hint refers to an unknown identifier: [^ ]+");
    private static final Pattern PRINT_SYMBOL_PATTERN = Pattern.compile("This symbol cannot be printed: [^ ]+");

    /**
     * Creates a new instance of {@code YuiCompressorParser}.
     */
    public YuiCompressorParser() {
        super(YUI_COMPRESSOR_WARNING_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        String messageHeader = matcher.group(1);
        String messageDetails = lookahead.hasNext() ? lookahead.next() : "";

        CategoryAndPriority categoryAndPriority = getCategoryAndPriority(messageHeader);

        String message = messageHeader + " [" + messageDetails + "]";

        return builder.setFileName("unknown.file").setLineStart(0).setCategory(categoryAndPriority.getCategory())
                .setMessage(message).setSeverity(categoryAndPriority.getPriority()).buildOptional();
    }

    @SuppressWarnings("npathcomplexity")
    private CategoryAndPriority getCategoryAndPriority(final String message) { // NOPMD
        if (message.startsWith("Found an undeclared symbol")) {
            return CategoryAndPriority.UNDECLARED_SYMBOL;
        }
        if (message.startsWith("Try to use a single 'var' statement per scope")) {
            return CategoryAndPriority.USE_SINGLE_VAR;
        }
        if (message.startsWith("Using JScript conditional comments is not recommended")) {
            return CategoryAndPriority.USE_JSCRIPT;
        }
        if (message.startsWith("Using 'eval' is not recommended")) {
            return CategoryAndPriority.USE_EVAL;
        }
        if (message.startsWith("Using 'with' is not recommended")) {
            return CategoryAndPriority.USE_WITH;
        }
        if (UNUSED_SYMBOL_PATTERN.matcher(message).matches()) {
            return CategoryAndPriority.UNUSED_SYMBOL;
        }
        if (UNUSED_VARIABLE_PATTERN.matcher(message).matches()) {
            return CategoryAndPriority.DUPLICATE_VAR;
        }
        if (UNUSED_FUNCTION_PATTERN.matcher(message).matches()) {
            return CategoryAndPriority.DUPLICATE_FUN;
        }
        if (INVALID_HINT_PATTERN.matcher(message).matches()) {
            return CategoryAndPriority.INVALID_HINT;
        }
        if (UNSUPPORTED_HINT_PATTERN.matcher(message).matches()) {
            return CategoryAndPriority.UNSUPPORTED_HINT;
        }
        if (UNKNOWN_HINT_PATTERN.matcher(message).matches()) {
            return CategoryAndPriority.UNKNOWN_HINT;
        }
        if (PRINT_SYMBOL_PATTERN.matcher(message).matches()) {
            return CategoryAndPriority.PRINT_SYMBOL;
        }
        return CategoryAndPriority.UNKNOWN;
    }

    /**
     * Handles category and priority of the warning.
     */
    private enum CategoryAndPriority {
        UNDECLARED_SYMBOL("Undeclared symbol"),
        USE_SINGLE_VAR("Use single 'var' per scope", Severity.WARNING_LOW),
        UNUSED_SYMBOL("Unused symbol"),
        DUPLICATE_VAR("Duplicate variable", Severity.WARNING_HIGH),
        UNKNOWN(""),
        DUPLICATE_FUN("Duplicate function", Severity.WARNING_HIGH),
        INVALID_HINT("Invalid hint"),
        UNSUPPORTED_HINT("Unsupported hint", Severity.WARNING_LOW),
        UNKNOWN_HINT("Unknown hint", Severity.WARNING_LOW),
        USE_JSCRIPT("Use Jscript", Severity.WARNING_HIGH),
        USE_EVAL("Use eval", Severity.WARNING_HIGH),
        USE_WITH("Use with", Severity.WARNING_HIGH),
        PRINT_SYMBOL("Cannot print symbol", Severity.WARNING_LOW);

        private final String category;
        private final Severity priority;

        CategoryAndPriority(final String category) {
            this(category, Severity.WARNING_NORMAL);
        }

        CategoryAndPriority(final String category, final Severity priority) {
            this.category = category;
            this.priority = priority;
        }

        public String getCategory() {
            return category;
        }

        public Severity getPriority() {
            return priority;
        }
    }
}
