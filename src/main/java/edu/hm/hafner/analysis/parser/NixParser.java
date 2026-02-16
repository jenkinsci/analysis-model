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
    private static final Pattern LOCATION_PATTERN = Pattern.compile("^\\s+at\\s+(.+?):(\\d+):(\\d+)?:?\\s*$");

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
        String message = matcher.group(2).trim();
        
        if (message.isEmpty() && lookahead.hasNext()) {
            String nextLine = lookahead.peekNext();
            if (!nextLine.trim().isEmpty() && !nextLine.trim().startsWith("at ") 
                    && !nextLine.trim().startsWith("…")) {
                message = lookahead.next().trim();
            }
        }
        
        Severity severity = "error".equals(severityStr) ? Severity.WARNING_HIGH : Severity.WARNING_NORMAL;
        
        String fileName = null;
        String lineNumber = null;
        String columnNumber = null;
        
        while (lookahead.hasNext()) {
            String line = lookahead.peekNext();
            
            Matcher locationMatcher = LOCATION_PATTERN.matcher(line);
            if (locationMatcher.matches()) {
                lookahead.next();
                fileName = locationMatcher.group(1).trim();
                lineNumber = locationMatcher.group(2);
                columnNumber = locationMatcher.group(3);
                
                consumeSourceContext(lookahead);
                break;
            }
            
            if (line.trim().startsWith("…") || line.trim().isEmpty()) {
                lookahead.next();
                continue;
            }
            
            if (line.trim().startsWith("error:") || line.trim().startsWith("warning:")) {
                break;
            }
            
            lookahead.next();
        }
        
        if (fileName == null) {
            return Optional.empty();
        }
        
        builder.setFileName(fileName)
                .setLineStart(lineNumber)
                .setSeverity(severity)
                .setMessage(message.isEmpty() ? "Nix build " + severityStr : message);
        
        if (columnNumber != null && !columnNumber.isEmpty()) {
            builder.setColumnStart(columnNumber);
        }
        
        return builder.buildOptional();
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
            
            if (line.matches("^\\s+\\d+\\|.*$") || line.matches("^\\s+\\|.*$")) {
                lookahead.next(); 
                continue;
            }
            
            break;
        }
    }
}
