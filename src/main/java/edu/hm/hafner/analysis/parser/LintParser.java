package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.io.Reader;
import javax.xml.parsers.SAXParser;

import org.apache.commons.io.input.ReaderInputStream;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.SecureXmlParserFactory;

/**
 * Parser for Lint.
 *
 * @author Gavin Mogan
 */
public class LintParser extends IssueParser {
    private static final long serialVersionUID = 3341424685245834156L;

    @Override
    public Report parse(final ReaderFactory readerFactory) throws ParsingException {
        try (Reader reader = readerFactory.create()) {
            SAXParser parser = new SecureXmlParserFactory().createSaxParser();

            Report report = new Report();
            parser.parse(new InputSource(new ReaderInputStream(reader, readerFactory.getCharset())),
                    new JSLintXmlSaxParser(report));

            return report;
        }
        catch (IOException | SAXException e) {
            throw new ParsingException(e);
        }
    }
}
