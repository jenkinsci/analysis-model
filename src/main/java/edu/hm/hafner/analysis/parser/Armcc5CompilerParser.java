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
 * A parser for armcc5 compiler warnings.
 *
 * @author Dmytro Kutianskyi
 */
public class Armcc5CompilerParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = -2677728927938443701L;

    private static final String ARMCC5_WARNING_PATTERN = "^(?:\"(.+)\", line (\\d+): (warning|error) #(.+): (.+)|(.+)\\((\\d+)\\): (warning|error):\\s+#(.+): (.+))$";

    /**
     * Creates a new instance of {@link Armcc5CompilerParser}.
     */
    public Armcc5CompilerParser() {
        super(ARMCC5_WARNING_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("#");
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
                                          final IssueBuilder builder) {
        boolean isNewFormat = matcher.group(1) != null;

        String fileName = isNewFormat ? matcher.group(1) : matcher.group(6);
        String lineStr  = isNewFormat ? matcher.group(2) : matcher.group(7);
        String type     = isNewFormat ? matcher.group(3) : matcher.group(8);
        Severity priority = equalsIgnoreCase(type, "error")
                ? Severity.WARNING_HIGH
                : Severity.WARNING_NORMAL;
        String errorCode = isNewFormat ? matcher.group(4) : matcher.group(9);
        String message   = isNewFormat ? matcher.group(5) : matcher.group(10);

        return builder.setFileName(fileName)
                .setLineStart(lineStr)
                .setMessage(errorCode + " - " + message)
                .setSeverity(priority)
                .buildOptional();
    }
}
