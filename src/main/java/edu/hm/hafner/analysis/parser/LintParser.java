package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.function.Function;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.xml.sax.SAXException;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.ParsingCanceledException;
import edu.hm.hafner.analysis.ParsingException;

/**
 * Parser for Lint.
 *
 * @author Gavin Mogan
 */
public class LintParser extends AbstractParser {
    private static final long serialVersionUID = 3341424685245834156L;

    @Override
    public Report parse(final Reader file, final Function<String, String> preProcessor)
            throws ParsingException, ParsingCanceledException {
        try {
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();

            SAXParser parser = parserFactory.newSAXParser();

            Report report = new Report();
            parser.parse(new ReaderInputStream(file, Charset.forName("UTF-8")), new JSLintXmlSaxParser(report));
            return report;
        }
        catch (IOException | ParserConfigurationException | SAXException e) {
            throw new ParsingException(e);
        }
        finally {
            IOUtils.closeQuietly(file);
        }
    }
}
