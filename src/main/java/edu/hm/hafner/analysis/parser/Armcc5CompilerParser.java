package edu.hm.hafner.analysis.parser;

import java.io.Serial;

/**
 * A parser for armcc5 compiler warnings.
 *
 * @author Dmytro Kutianskyi
 */
public final class Armcc5CompilerParser extends ArmccAbstractParser {
    @Serial
    private static final long serialVersionUID = -2677728927938443701L;

    private static final String ARMCC5_WARNING_PATTERN =
            "^(?<file>.+)\\((?<line>\\d+)\\): (?<severity>warning|error):\\s+#(?<code>.+): (?<message>.+)$";

    /** Creates a new instance of {@link Armcc5CompilerParser}. */
    public Armcc5CompilerParser() {
        super(ARMCC5_WARNING_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("#");
    }
}
