package edu.hm.hafner.analysis.parser;

import java.io.Serial;
import java.util.Optional;
import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

/**
 * A parser for LLVM lld linker warnings and errors.
 *
 * @author Steven Scheffler
 */
public class LlvmLinkerParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = 1L;

    // Named groups: linker, severity, message
    private static final String LLD_LINKER_PATTERN = "^.*[/\\\\]?(?<linker>ld\\.lld(?:-\\d+)?):\\s*(?<severity>error|warning|note):\\s*(?<message>.*)$";

    /**
     * Creates a new instance of {@link LlvmLinkerParser}.
     */
    public LlvmLinkerParser() {
        super(LLD_LINKER_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        String linkerName = matcher.group("linker");    // e.g., ld.lld or ld.lld-15
        String severity   = matcher.group("severity");  // error|warning|note
        String message    = matcher.group("message");   // full message text
        
        return builder.setFileName(linkerName)  // Use captured linker name
                .setLineStart(0)
                .setColumnStart(0)
                .setCategory("Linker")
                .setMessage(message.trim())
                .setSeverity(mapPriority(severity))
                .buildOptional();
    }

    private Severity mapPriority(final String severity) {
        return switch (severity) {
            case "error" -> Severity.WARNING_HIGH;
            case "warning" -> Severity.WARNING_NORMAL;
            case "note" -> Severity.WARNING_LOW;
            default -> Severity.WARNING_NORMAL;
        };
    }
}