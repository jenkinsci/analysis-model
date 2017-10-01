package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.RegexpLineParser;

/**
 * A parser for the CodeAnalysis compiler warnings.
 *
 * @author Rafal Jasica
 */
public class CodeAnalysisParser extends RegexpLineParser {
    private static final long serialVersionUID = -125874563249851L;
    private static final String WARNING_PATTERN = ANT_TASK + "((MSBUILD)|((.+)\\((\\d+)\\)))" +
            "\\s*:\\s*[Ww]arning\\s*:?\\s*(\\w*)\\s*:\\s*(Microsoft\\.|)" + "(\\w*(\\.\\w*)*)\\s*:\\s*(.*)\\[(.*)" +
            "\\]\\s*$";

    /**
     * Creates a new instance of {@link CodeAnalysisParser}.
     */
    public CodeAnalysisParser() {
        super("code-analysis", WARNING_PATTERN);
    }

    @Override
    protected Issue createWarning(final Matcher matcher) {
        if (StringUtils.isNotBlank(matcher.group(2))) {
            return issueBuilder().setFileName(matcher.group(11)).setLineStart(0).setCategory(matcher.group(8))
                                 .setType(matcher.group(6)).setMessage(matcher.group(10)).setPriority(Priority.NORMAL)
                                 .build();
        }
        else {
            return issueBuilder().setFileName(matcher.group(4)).setLineStart(parseInt(matcher.group(5)))
                                 .setCategory(matcher.group(8)).setType(matcher.group(6)).setMessage(matcher.group(10))
                                 .setPriority(Priority.NORMAL).build();
        }
    }
}
