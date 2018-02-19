package edu.hm.hafner.analysis.parser;

import java.util.regex.Matcher;

import edu.hm.hafner.analysis.FastRegexpLineParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;

/**
 * A parser for Perforce execution.
 *
 * @author Adrian Deccico
 */
public class P4Parser extends FastRegexpLineParser {
    private static final long serialVersionUID = -8106854254745366432L;

    private static final String ALREADY_OPENED = "already opened for edit";
    private static final String CANT_ADD = "can't add existing file";
    private static final String WARNING_ADD_OF = "warning: add of existing file";
    private static final String OPENED_FOR_EDIT = "can't add \\(" + ALREADY_OPENED + "\\)";
    private static final String NOTHING_CHANGED = "nothing changed";
    private static final String OR = "|";

    /** Pattern of perforce compiler warnings. */
    private static final String PERFORCE_WARNING_PATTERN = "^(.*) - " + "(" + CANT_ADD + OR + WARNING_ADD_OF + OR
            + OPENED_FOR_EDIT + OR + NOTHING_CHANGED + ")" + "(.*)$";

    /**
     * Creates a new instance of {@link P4Parser}.
     */
    public P4Parser() {
        super(PERFORCE_WARNING_PATTERN);
    }

    @Override
    protected Issue createIssue(final Matcher matcher, final IssueBuilder builder) {
        String category = matcher.group(2).trim();
        Priority p = mapPriority(category);
        return builder.setFileName(matcher.group(1).trim()).setLineStart(0).setCategory(category).setMessage(
                matcher.group(1).trim())
                .setPriority(p).build();
    }

    private Priority mapPriority(final String category) {
        if (category.contains(ALREADY_OPENED) || category.equals(NOTHING_CHANGED)) {
            return Priority.LOW;
        }
        return Priority.NORMAL;
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains(" - ");
    }
}

