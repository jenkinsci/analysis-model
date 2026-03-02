package edu.hm.hafner.analysis.parser;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.util.LookaheadStream;

import java.io.Serial;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A parser for Nix build and flake check output.
 *
 * <p>Parses error and warning messages from {@code nix build} and {@code nix flake check} commands.
 * The parser handles Nix's multi-line error format that includes file locations, line numbers, and contextual
 * information.</p>
 *
 * @author Akash Manna
 */
public class NixParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final String NIX_ERROR_PATTERN = "^(?<severity>error|warning):\\s*(?<message>.*)$";
    private static final Pattern LOCATION_PATTERN = Pattern.compile(
            "^\\s+at\\s+(?<file>.+?):(?<line>\\d+):(?<column>\\d+)?:?\\s*$");
    private static final Pattern LINE_NUMBER_PREFIX = Pattern.compile("^\\s+\\d+\\|.*$");
    private static final Pattern LINE_PREFIX = Pattern.compile("^\\s+\\|.*$");

    /**
     * Creates a new instance of {@link NixParser}.
     */
    public NixParser() {
        super(NIX_ERROR_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.startsWith("error:") || line.startsWith("warning:");
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        var severityStr = matcher.group("severity");
        var message = extractMessage(lookahead, matcher.group("message"));
        builder.guessSeverity(severityStr).setMessage(message.isEmpty() ? "Nix build " + severityStr : message);
        var location = findLocationInfo(lookahead);
        if (location.isEmpty()) {
            return builder.buildOptional();
        }

        builder.setFileName(location.get().fileName).setLineStart(location.get().lineNumber);
        if (StringUtils.isNotEmpty(location.get().columnNumber)) {
            builder.setColumnStart(location.get().columnNumber);
        }

        return builder.buildOptional();
    }

    private String extractMessage(final LookaheadStream lookahead, final String title) {
        String message = title.trim();

        if (message.isEmpty() && lookahead.hasNext()) {
            String nextLine = lookahead.peekNext();
            if (isMessageLine(nextLine)) {
                message = lookahead.next().trim();
            }
        }

        return message;
    }

    /**
     * Checks if a line contains the error message.
     *
     * @param line
     *         the line to check
     *
     * @return true if the line is a message line
     */
    private boolean isMessageLine(final String line) {
        return StringUtils.isNotBlank(line)
                && !line.trim().startsWith("at ")
                && !line.trim().startsWith("…");
    }

    /**
     * Finds location information (file, line, column) from the lookahead stream.
     *
     * @param lookahead
     *         the lookahead stream
     *
     * @return the location info, or null if not found
     */
    private Optional<LocationInfo> findLocationInfo(final LookaheadStream lookahead) {
        while (lookahead.hasNext()) {
            String line = lookahead.peekNext();

            Matcher locationMatcher = LOCATION_PATTERN.matcher(line);
            if (locationMatcher.matches()) {
                lookahead.next();
                var location = new LocationInfo(
                        locationMatcher.group("file").trim(),
                        locationMatcher.group("line"),
                        locationMatcher.group("column")
                );
                consumeSourceContext(lookahead);
                return Optional.of(location);
            }

            if (isLineInteresting(line.trim())) {
                break;
            }

            lookahead.next();
        }

        return Optional.empty();
    }

    /**
     * Consumes source context lines that show the code and error location. These lines typically have line numbers
     * followed by | character.
     *
     * @param lookahead
     *         the lookahead stream
     */
    private void consumeSourceContext(final LookaheadStream lookahead) {
        while (lookahead.hasNext()) {
            String line = lookahead.peekNext();

            if (!LINE_NUMBER_PREFIX.matcher(line).matches()
                    && !LINE_PREFIX.matcher(line).matches()) {
                return;
            }

            lookahead.next();
        }
    }

    /**
     * Helper class to store location information.
     */
    private static final class LocationInfo {
        private final String fileName;
        private final String lineNumber;
        private final String columnNumber;

        LocationInfo(final String fileName, final String lineNumber, final String columnNumber) {
            this.fileName = fileName;
            this.lineNumber = lineNumber;
            this.columnNumber = columnNumber;
        }
    }
}
