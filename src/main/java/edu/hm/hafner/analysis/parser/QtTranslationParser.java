package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;

/**
 * Parser for translation files of Qt.
 * 
 * @author Heiko Thiel
 */
public class QtTranslationParser extends IssueParser {
    private static final long serialVersionUID = 1L;

    @Override
    public Report parse(final ReaderFactory readerFactory) throws ParsingException {
        Report report = new Report();
        readerFactory.parse(new QtTranslationSaxParser(report, readerFactory.getFileName()));
        return report;
    }
}