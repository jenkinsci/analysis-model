package edu.hm.hafner.analysis.parser;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.xml.sax.SAXException;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.ParsingCanceledException;
import edu.hm.hafner.analysis.ParsingException;

/**
 * Base class for parsers based on {@link JSLintXMLSaxParser}.
 *
 * @author Ullrich Hafner
 */
public abstract class LintParser extends AbstractParser {
    private static final long serialVersionUID = 3341424685245834156L;

    /**
     * Creates a new instance of {@link LintParser}.
     *
     * @param id ID of the parser
     */
    protected LintParser(final String id) {
        super(id);
    }

    @Override
    public Issues<Issue> parse(final Reader file) throws ParsingException, ParsingCanceledException {
        try {
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();

            SAXParser parser = parserFactory.newSAXParser();

            Issues<Issue> issues = new Issues<>();
            parser.parse(new ReaderInputStream(file, Charset
                    .forName("UTF-8")), new JSLintXMLSaxParser(getId(), issues));
            return issues;
        }
        catch (IOException | ParserConfigurationException | SAXException e) {
            throw new ParsingException(e);
        }
        finally {
            IOUtils.closeQuietly(file);
        }
    }
}
