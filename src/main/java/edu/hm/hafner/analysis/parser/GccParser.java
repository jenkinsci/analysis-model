package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.RegexpLineParser;

/**
 * A parser for the gcc compiler warnings.
 *
 * @author Greg Roth
 */
public class GccParser extends RegexpLineParser {
    private static final long serialVersionUID = 2020182274225690532L;

    static final String GCC_ERROR = "GCC error";
    static final String LINKER_ERROR = "Linker error";
    private static final String GCC_WARNING_PATTERN = "^(?:\\s*(?:\\[.*\\]\\s*)?([^ ]*\\.[chpimxsola0-9]+):(?:(\\d*):"
            + "(?:\\d*:)*\\s*(?:(warning|error|note)\\s*:|\\s*(.*))|\\s*(undefined reference to.*))(.*)|.*ld:\\s*(.*-l("
            + ".*)))$";

    /**
     * Creates a new instance of {@link GccParser}.
     */
    public GccParser() {
        super(GCC_WARNING_PATTERN);
    }

    @Override
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        if (StringUtils.isNotBlank(matcher.group(7))) {
            return builder.setFileName(matcher.group(8))
                    .setLineStart(0)
                    .setCategory(LINKER_ERROR)
                    .setMessage(matcher.group(7))
                    .setPriority(Priority.HIGH)
                    .build();
        }

        String fileName = matcher.group(1);
        if (StringUtils.contains(fileName, "cleartool")) {
            return FALSE_POSITIVE;
        }

        Priority priority;
        if ("warning".equalsIgnoreCase(matcher.group(3))) {
            priority = Priority.NORMAL;
        }
        else if ("error".equalsIgnoreCase(matcher.group(3))) {
            priority = Priority.HIGH;
        }
        else if ("note".equalsIgnoreCase(matcher.group(3))) {
            priority = Priority.LOW;
        }
        else if (StringUtils.isNotBlank(matcher.group(4))) {
            if (matcher.group(4).contains("instantiated from here")) {
                return FALSE_POSITIVE;
            }
            return builder.setFileName(fileName)
                    .setLineStart(parseInt(matcher.group(2)))
                    .setCategory(GCC_ERROR)
                    .setMessage(StringEscapeUtils.escapeXml10(matcher.group(4)))
                    .setPriority(Priority.HIGH)
                    .build();
        }
        else {
            return builder.setFileName(fileName)
                    .setLineStart(0)
                    .setCategory(GCC_ERROR)
                    .setMessage(StringEscapeUtils.escapeXml10(matcher.group(5)))
                    .setPriority(Priority.HIGH)
                    .build();
        }
        return builder.setFileName(fileName)
                .setLineStart(parseInt(matcher.group(2)))
                .setCategory("GCC " + matcher.group(3))
                .setMessage(StringEscapeUtils.escapeXml10(matcher.group(6)))
                .setPriority(priority)
                .build();
    }

}

