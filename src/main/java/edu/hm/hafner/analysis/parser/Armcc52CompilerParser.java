package edu.hm.hafner.analysis.parser;

import java.io.Serial;

/**
 * A parser for armcc5 compiler warnings.
 *
 * @author Dmytro Kutianskyi
 */
public final class Armcc52CompilerParser extends ArmccAbstractParser {
    @Serial
    private static final long serialVersionUID = -2677728927938443701L;

    private static final String ARMCC5_WARNING_PATTERN =
            "^\"(?<file>.+)\", line (?<line>\\d+): (?<severity>warning|error) #(?<code>.+): (?<message>.+)";

    /**
     * Creates a new instance of {@link Armcc52CompilerParser}.
     */
    public Armcc52CompilerParser() {
        super(ARMCC5_WARNING_PATTERN);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("#") && line.contains(", line");
    }
}
