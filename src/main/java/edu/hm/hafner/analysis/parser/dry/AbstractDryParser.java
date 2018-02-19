package edu.hm.hafner.analysis.parser.dry;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.digester3.Digester;
import org.xml.sax.SAXException;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.ParsingCanceledException;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.SecureDigester;

/**
 * A duplication parser template for Digester based parsers.
 *
 * @param <T>
 *         the type of the parsed warnings
 */
public abstract class AbstractDryParser<T> extends AbstractParser<CodeDuplication> {
    /** Unique ID of this class. */
    private static final long serialVersionUID = 6328121785037117886L;

    /** Minimum number of duplicate lines for high priority warnings. @since 2.5 */
    private final int highThreshold;
    /** Minimum number of duplicate lines for normal priority warnings. @since 2.5 */
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
        this.highThreshold = highThreshold;
        this.normalThreshold = normalThreshold;
    }

    /**
     * Returns the priority of the warning.
     *
     * @param lines
     *            number of duplicate lines
     * @return the priority of the warning
     */
    protected Priority getPriority(final int lines) {
        if (lines >= highThreshold) {
            return Priority.HIGH;
        }
        if (lines >= normalThreshold) {
            return Priority.NORMAL;
        }
        return Priority.LOW;
    }

    @Override
    public Issues<CodeDuplication> parse(@Nonnull final Reader reader, final Function<String, String> preProcessor)
            throws ParsingCanceledException, ParsingException {
        try {
            Digester digester = new SecureDigester(CodeDuplication.class);

            configureParser(digester);

            List<T> duplications = new ArrayList<>();
            digester.push(duplications);

            Object result = digester.parse(reader);
            if (result != duplications) { // NOPMD
                throw new ParsingException("Input stream is not a valid duplications file.");
            }

            return convertDuplicationsToIssues(duplications);
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
     * Converts the parsed duplications from the original format to an {@link Issues} instance.
     *
     * @param duplications
     *         the parsed warnings
     * @return the converted warnings
     */
    protected abstract Issues<CodeDuplication> convertDuplicationsToIssues(List<T> duplications);
}
