package edu.hm.hafner.analysis.parser.violations;

import java.io.Serial;

import se.bjurr.violations.lib.parsers.KlocworkParser;

/**
 * Parses Klocwork files.
 *
 * @author Ullrich Hafner
 */
public class KlocWorkAdapter extends AbstractViolationAdapter {
    @Serial
    private static final long serialVersionUID = 5676554459268768313L;

    @Override
    KlocworkParser createParser() {
        return new KlocworkParser();
    }
}
