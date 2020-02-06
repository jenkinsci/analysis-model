package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;

/**
 * Parser for Lint.
 *
 * @author Gavin Mogan
 */
public class LintParser extends IssueParser {
    private static final long serialVersionUID = 3341424685245834156L;

    @Override
    public Report parse(final ReaderFactory readerFactory) throws ParsingException {
        Report report = new Report();
        readerFactory.parse(new JSLintXmlSaxParser(report));
        return report;
    }
}
