package edu.hm.hafner.analysis.parser;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

import org.xml.sax.SAXException;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingCanceledException;
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
    public Report parse(final ReaderFactory readerFactory)
            throws ParsingException, ParsingCanceledException {
        try {
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();

            SAXParser parser = parserFactory.newSAXParser();
            // FIXME: log the SAX parser, check if this could be part of factory

            Report report = new Report();
            
            parser.parse(readerFactory.createInputSource(), new JSLintXmlSaxParser(report));
            
            return report;
        }
        catch (IOException | ParserConfigurationException | SAXException e) {
            throw new ParsingException(e);
        }
    }
}
