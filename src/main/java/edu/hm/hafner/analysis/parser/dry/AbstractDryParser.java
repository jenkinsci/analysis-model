package edu.hm.hafner.analysis.parser.dry;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.digester3.Digester;
import org.xml.sax.SAXException;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingCanceledException;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.SecureDigester;
import edu.hm.hafner.analysis.Severity;

/**
 * A duplication parser template for Digester based parsers.
 *
 * @param <T>
 *         the type of the parsed warnings
 */
public abstract class AbstractDryParser<T> extends IssueParser {
    /** Unique ID of this class. */
    private static final long serialVersionUID = 6328121785037117886L;

    /** Minimum number of duplicate lines for high priority warnings. */
    private final int highThreshold;
    /** Minimum number of duplicate lines for normal priority warnings. */
    private final int normalThreshold;

    /**
     * Creates a new instance of {@link AbstractDryParser}.
     *
     * @param highThreshold
     *         minimum number of duplicate lines for high priority warnings
     * @param normalThreshold
     *         minimum number of duplicate lines for normal priority warnings
     */
    protected AbstractDryParser(final int highThreshold, final int normalThreshold) {
        super();

        this.highThreshold = highThreshold;
        this.normalThreshold = normalThreshold;
    }

    /**
     * Returns the priority of the warning.
     *
     * @param lines
     *         number of duplicate lines
     *
     * @return the priority of the warning
     */
    protected Severity getPriority(final int lines) {
        if (lines >= highThreshold) {
            return Severity.WARNING_HIGH;
        }
        if (lines >= normalThreshold) {
            return Severity.WARNING_NORMAL;
        }
        return Severity.WARNING_LOW;
    }

    @Override
    public Report parse(final ReaderFactory readerFactory) throws ParsingCanceledException, ParsingException {
        Digester digester = new SecureDigester(AbstractDryParser.class);

        configureParser(digester);

        List<T> duplications = new ArrayList<>();
        digester.push(duplications);

        try (Reader reader = readerFactory.create(); IssueBuilder issueBuilder = new IssueBuilder()) {
            Object result = digester.parse(reader);
            if (result != duplications) { // NOPMD
                throw new ParsingException("Input stream is not a valid duplications file.");
            }

            issueBuilder.setMessage("Found duplicated code.")
                        .setCategory("Code Duplication");
            return convertDuplicationsToIssues(duplications, issueBuilder);
        }
        catch (IOException | SAXException exception) {
            throw new ParsingException(exception);
        }
    }

    /**
     * Configures the Digester parser. Register all rules that are required to parse the file.
     *
     * @param digester
     *         the parser to configure
     */
    protected abstract void configureParser(Digester digester);

    /**
     * Converts the parsed duplications from the original format to an {@link Report} instance.
     *
     * @param duplications
     *         the parsed warnings
     * @param issueBuilder
     *         the issue builder to use
     *
     * @return the converted warnings
     */
    protected abstract Report convertDuplicationsToIssues(List<T> duplications, IssueBuilder issueBuilder);
}
