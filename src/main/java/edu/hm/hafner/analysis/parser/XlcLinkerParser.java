package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.RegexpLineParser;

/**
 * A parser for IBM xlC linker warnings.
 *
 * @author Andrew Gvozdev
 */
public class XlcLinkerParser extends RegexpLineParser {
    private static final long serialVersionUID = 211259620936831096L;
    private static final String XLC_LINKER_WARNING_PATTERN = ANT_TASK + "ld: ([0-9]+-[0-9]+)*\\s*(.*)$";

    private static final String XLC_LINKER_WARNING_PATTERN_ERROR_1 = ANT_TASK + "ld: ([0-9]+-[0-9]+).*ERROR:\\s*(.*)$";
    private static final String XLC_LINKER_WARNING_PATTERN_ERROR_2 = ANT_TASK + "ld: ([0-9]+-[0-9]+)\\s*(Error .*)$";
    private static final String XLC_LINKER_WARNING_PATTERN_WARNING = ANT_TASK + "ld: ([0-9]+-[0-9]+)\\s*WARNING:\\s*(" +
            ".*)$";
    private static final String XLC_LINKER_WARNING_PATTERN_INFO = ANT_TASK + "ld: ([0-9]+-[0-9]+)\\s*(.*)$";
    private static final Pattern PATTERN_ERROR_1 = Pattern.compile(XLC_LINKER_WARNING_PATTERN_ERROR_1);
    private static final Pattern PATTERN_ERROR_2 = Pattern.compile(XLC_LINKER_WARNING_PATTERN_ERROR_2);
    private static final Pattern PATTERN_WARNING = Pattern.compile(XLC_LINKER_WARNING_PATTERN_WARNING);
    private static final Pattern PATTERN_INFO = Pattern.compile(XLC_LINKER_WARNING_PATTERN_INFO);

    /**
     * Creates a new instance of {@link XlcLinkerParser}. Note that the name matches {@link XlcCompilerParser} to unite
     * them as one parser in UI.
     */
    public XlcLinkerParser() {
        super(XLC_LINKER_WARNING_PATTERN);
    }

    @Override
    protected Issue createWarning(final Matcher matcher0, final IssueBuilder builder) {
        String line = matcher0.group(0);
        Matcher matcher = PATTERN_ERROR_1.matcher(line);
        if (matcher.find()) {
            String category = matcher.group(1);
            String message = matcher.group(2);
            return builder.setFileName("").setLineStart(0).setCategory(category).setMessage(message)
                          .setPriority(Priority.HIGH).build();
        }
        matcher = PATTERN_ERROR_2.matcher(line);
        if (matcher.find()) {
            String category = matcher.group(1);
            String message = matcher.group(2);
            return builder.setFileName("").setLineStart(0).setCategory(category).setMessage(message)
                          .setPriority(Priority.HIGH).build();
        }
        matcher = PATTERN_WARNING.matcher(line);
        if (matcher.find()) {
            String category = matcher.group(1);
            String message = matcher.group(2);
            return builder.setFileName("").setLineStart(0).setCategory(category).setMessage(message)
                          .setPriority(Priority.NORMAL).build();
        }
        matcher = PATTERN_INFO.matcher(line);
        if (matcher.find()) {
            String category = matcher.group(1);
            String message = matcher.group(2);
            return builder.setFileName("").setLineStart(0).setCategory(category).setMessage(message)
                          .setPriority(Priority.LOW).build();
        }
        return FALSE_POSITIVE;
    }
}

