package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.RegexpLineParser;

/**
 * A parser for the scalac compiler warnings. You should use -feature & -deprecation compiler opts.
 *
 * @author <a href="mailto:alexey.kislin@gmail.com">Alexey Kislin</a>
 */
public class ScalacParser extends RegexpLineParser {
    private static final String SCALAC_WARNING_PATTERN = "^(\\[WARNING\\]|\\[ERROR\\])\\s*(.*):(\\d+):\\s*([a-z]*)"
            + ":\\s*(.*)$";

    /**
     * Creates a new instance of {@link ScalacParser}.
     */
    public ScalacParser() {
        super("scalac", SCALAC_WARNING_PATTERN);
    }

    @Override
    protected Issue createWarning(Matcher matcher) {
        Priority p = matcher.group(1).equals("[ERROR]") ? Priority.HIGH : Priority.NORMAL;
        String fileName = matcher.group(2);
        String lineNumber = matcher.group(3);
        String category = matcher.group(4);
        String message = matcher.group(5);
        return issueBuilder().setFileName(fileName).setLineStart(parseInt(lineNumber)).setCategory(category)
                             .setMessage(message).setPriority(p).build();
    }
}
