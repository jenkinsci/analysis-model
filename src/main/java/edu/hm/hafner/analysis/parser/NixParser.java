package edu.hm.hafner.analysis.parser;

import java.io.Serial;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.util.LookaheadStream;

/**
 * A parser for Nix build and flake check output.
 *
 * <p>Parses error and warning messages from {@code nix build} and {@code nix flake check} commands.
 * The parser handles Nix's multi-line error format that includes file locations, line numbers,
 * and contextual information.</p>
 *
 * @author Akash Manna
 */
public class NixParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final String NIX_ERROR_PATTERN = "^(error|warning):\\s*(.*)$";
    private static final Pattern LOCATION_PATTERN = Pattern.compile("^\\s+at\\s+(?<file>.+?):(?<line>\\d+):(?<column>\\d+)?:?\\s*$");

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
        String severityStr = matcher.group(1);
        String message = extractMessage(matcher, lookahead);
        
        LocationInfo location = findLocationInfo(lookahead);
        if (location == null) {
            return builder.guessSeverity(severityStr)
                    .setMessage(message.isEmpty() ? "Nix build " + severityStr : message)
                    .buildOptional();
        }
        
        builder.setFileName(location.fileName)
                .setLineStart(location.lineNumber)
                .guessSeverity(severityStr)
                .setMessage(message.isEmpty() ? "Nix build " + severityStr : message);
        
        if (StringUtils.isNotEmpty(location.columnNumber)) {
            builder.setColumnStart(location.columnNumber);
        }
        
        return builder.buildOptional();
    }

    /**
     * Extracts the error message from the matcher and lookahead stream.
     *
     * @param matcher the regex matcher
     * @param lookahead the lookahead stream
     * @return the error message
     */
    private String extractMessage(final Matcher matcher, final LookaheadStream lookahead) {
        String message = matcher.group(2).trim();
        
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
     * @param line the line to check
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
     * @param lookahead the lookahead stream
     * @return the location info, or null if not found
     */
    @CheckForNull
    private LocationInfo findLocationInfo(final LookaheadStream lookahead) {
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
                return location;
            }
            
            if (isLineInteresting(line.trim())) {
                break;
            }
            
            lookahead.next();
        }
        
        return null;
    }

    /**
     * Consumes source context lines that show the code and error location.
     * These lines typically have line numbers followed by | character.
     *
     * @param lookahead the lookahead stream
     */
    private void consumeSourceContext(final LookaheadStream lookahead) {
        while (lookahead.hasNext()) {
            String line = lookahead.peekNext();
            
            if (!line.matches("^\\s+\\d+\\|.*$") && !line.matches("^\\s+\\|.*$")) {
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
