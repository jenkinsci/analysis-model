package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.RegexpLineParser;

/**
 * A parser for the SUN Studio C++ compiler warnings.
 *
 * @author Ulli Hafner
 */
public class SunCParser extends RegexpLineParser {
    private static final long serialVersionUID = -1251248150596418456L;

    private static final String SUN_CPP_WARNING_PATTERN = "^\\s*\"(.*)\"\\s*,\\s*line\\s*(\\d+)\\s*:\\s*" +
            "(Warning|Error)(?:| .Anachronism.)\\s*(?:, \\s*([^:]*))?\\s*:\\s*(.*)$";

    /**
     * Creates a new instance of <code>HpiCompileParser</code>.
     */
    public SunCParser() {
        super("sunc", SUN_CPP_WARNING_PATTERN);
    }

    @Override
    protected Issue createWarning(final Matcher matcher) {
        Priority priority;
        if ("warning".equalsIgnoreCase(matcher.group(3))) {
            priority = Priority.NORMAL;
        }
        else {
            priority = Priority.HIGH;
        }
        return issueBuilder().setFileName(matcher.group(1)).setLineStart(parseInt(matcher.group(2)))
                             .setCategory(matcher.group(4)).setMessage(matcher.group(5)).setPriority(priority).build();
    }
}

