package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.QacSourceCodeAnalyserParser;

/**
 * A descriptor for the PRQA QA-C Sourcecode Analyser.
 *
 * @author Lorenz Munsch
 */
class QacSourceCodeAnalyserDescriptor extends ParserDescriptor {
    private static final String ID = "qac";
    private static final String NAME = "QA-C Sourcecode Analyser";

    QacSourceCodeAnalyserDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new QacSourceCodeAnalyserParser();
    }
}
