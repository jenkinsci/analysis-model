package edu.hm.hafner.analysis.parser;

import java.util.Optional;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.RegexpDocumentParser;

/**
 * A parser for CMake warnings.
 *
 * @author Uwe Brandt
 */
public class CMakeParser extends RegexpDocumentParser {
    private static final long serialVersionUID = 8149238560432255036L;

    private static final String CMAKE_WARNING_PATTERN =
            "CMake\\s+Warning(?:.*?(\\S+)){0,1}(?::(\\d+)\\s+(\\S+)){0,1}\\s*:\\n(.*)\\n";

    /**
     * Creates a new instance of {@link CMakeParser}.
     */
    public CMakeParser() {
        super(CMAKE_WARNING_PATTERN, true);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final IssueBuilder builder) {
        String fileName = matcher.group(1);
        if (fileName == null) {
            fileName = "<none>";
        }
        return builder.setFileName(fileName)
                .setLineStart(matcher.group(2))
                .setCategory(matcher.group(3))
                .setMessage(matcher.group(4))
                .setSeverity(Severity.WARNING_NORMAL)
                .buildOptional();
    }
}

