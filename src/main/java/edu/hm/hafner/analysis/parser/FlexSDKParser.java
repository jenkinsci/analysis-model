package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import edu.hm.hafner.analysis.FastRegexpLineParser;
import edu.hm.hafner.analysis.Issue;

/**
 * A parser for Flex SDK compiler warnings.
 *
 * @author Vivien Tintillier
 */
public class FlexSDKParser extends FastRegexpLineParser {
    private static final long serialVersionUID = -185055018399324311L;
    private static final String FLEX_SDK_WARNING_PATTERN = "^\\s*(?:\\[.*\\])?\\s*(.*\\.as|.*\\.mxml)\\((\\d*)\\)" +
            ":\\s*(?:col:\\s*\\d*\\s*)?(?:Warning)\\s*:\\s*(.*)$";

    /**
     * Creates a new instance of {@link FlexSDKParser}.
     */
    public FlexSDKParser() {
        super("flex", FLEX_SDK_WARNING_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("Warning");
    }

    @Override
    protected Issue createWarning(final Matcher matcher) {
        return issueBuilder().setFileName(matcher.group(1)).setLineStart(parseInt(matcher.group(2)))
                             .setMessage(matcher.group(3)).build();
    }
}

