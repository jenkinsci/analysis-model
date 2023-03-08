package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LookaheadStream;

/**
 * A parser for Simulink Code Generator tool log files.
 *
 * @author Eva Habeeb
 */
public class CodeGeneratorParser extends LookaheadParser {
    private static final long serialVersionUID = -1251248150731418714L;
    private static final String WARNING_PATTERN = "^(Warning:)(.*)";

    /**
     * Creates a new instance of {@link CodeGeneratorParser}.
     */
    public CodeGeneratorParser() {
        super(WARNING_PATTERN);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        builder.setCategory(getCategory(matcher.group(2)));
        builder.setMessage(matcher.group(2));
        builder.setSeverity(Severity.WARNING_NORMAL);

        return builder.buildOptional();
    }

    private String getCategory(final String line) {
        if (line.contains("no longer available in the Configuration Parameters")) {
            return "Configuration Parameters Unavailable";
        }
        else if (line.contains("does not support multiword aliases")) {
            return "Multiword Aliases not Supported by Code Generation";
        }
        else if (line.contains("Unnecessary Data Type Conversion")) {
            return "Unnecessary Data Type Conversion";
        }
        else if (line.contains("Cannot close the model")) {
            return "Model Cannot be Closed";
        }
        else if (line.contains("Cannot find library called")) {
            return "Library Not Found";
        }
        else if (line.contains("File not found or permission denied")) {
            return "File not found or permission denied";
        }
        return "Other";
    }
}
