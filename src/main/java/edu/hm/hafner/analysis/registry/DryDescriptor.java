package edu.hm.hafner.analysis.registry;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.DuplicationGroup;
import edu.hm.hafner.analysis.Issue;
import edu.umd.cs.findbugs.annotations.CheckForNull;

import j2html.tags.UnescapedText;

import static j2html.TagCreator.*;

/**
 * A descriptor for duplicate code parsers.
 *
 * @author Ullrich Hafner
 */
public abstract class DryDescriptor extends ParserDescriptor {
    /** Key to define minimum number of duplicate lines for high priority warnings. */
    public static final String HIGH_OPTION_KEY = "DRY-HIGH-THRESHOLD";
    /** Key to define minimum number of duplicate lines for normal priority warnings. */
    public static final String NORMAL_OPTION_KEY = "DRY-NORMAL-THRESHOLD";

    DryDescriptor(final String id, final String name) {
        super(id, name);
    }

    int getHighThreshold(final Option... options) {
        return convertThreshold(HIGH_OPTION_KEY, 50, options);
    }

    int getNormalThreshold(final Option... options) {
        return convertThreshold(NORMAL_OPTION_KEY, 25, options);
    }

    private int convertThreshold(final String key, final int defaultValue, final Option... options) {
        for (Option option : options) {
            if (key.equals(option.getKey())) {
                try {
                    return Integer.parseInt(option.getValue());
                }
                catch (NumberFormatException exception) {
                    throw new IllegalArgumentException("Parser cannot handle option " + option, exception);
                }
            }
        }
        return defaultValue;
    }

    private String getDuplicateCode(@CheckForNull final Serializable properties) {
        if (properties instanceof DuplicationGroup group) {
            return pre().with(new UnescapedText(getCodeFragment(group)))
                    .renderFormatted();
        }
        else {
            return StringUtils.EMPTY;
        }
    }

    private static String getCodeFragment(final DuplicationGroup duplicationGroup) {
        return code(duplicationGroup.getCodeFragment()).renderFormatted();
    }

    @Override
    public String getDescription(final Issue issue) {
        return getDuplicateCode(issue.getAdditionalProperties());
    }
}
