package edu.hm.hafner.analysis.parser;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

import org.apache.commons.io.input.ReaderInputStream;
import org.xml.sax.SAXException;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.ParsingCanceledException;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Report;

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
        try (InputStream inputStream = new ReaderInputStream(file, StandardCharsets.UTF_8);) {
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();

            SAXParser parser = parserFactory.newSAXParser();

            Report report = new Report();
            parser.parse(inputStream, new JSLintXmlSaxParser(report));
            return report;
        }
        catch (IOException | ParserConfigurationException | SAXException e) {
            throw new ParsingException(e);
        }
    }
}
