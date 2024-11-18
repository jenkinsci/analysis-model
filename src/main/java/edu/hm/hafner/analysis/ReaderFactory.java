package edu.hm.hafner.analysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.xml.sax.helpers.DefaultHandler;

import com.google.errorprone.annotations.MustBeClosed;

import edu.hm.hafner.util.SecureXmlParserFactory;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Provides several useful helper methods to read the contents of a resource that is given by a {@link Reader}.
 *
 * @author Ullrich Hafner
 */
public abstract class ReaderFactory {
    private static final Function<String, String> IDENTITY = Function.identity();

    private final Charset charset;
    private final Function<String, String> lineMapper;

    private static final Pattern ANSI_COLOR_CODES
            = Pattern.compile("\u001B\\[[;\\d]*[ -/]*[@-~]");
    private static final Function<String, String> REMOVE_COLOR_CODES
            = string -> ANSI_COLOR_CODES.matcher(string).replaceAll(StringUtils.EMPTY);

    /**
     * Creates a new factory to read a resource with a given charset.
     *
     * @param charset
     *         the charset to use when reading the file
     */
    public ReaderFactory(final Charset charset) {
        this(charset, IDENTITY);
    }

    /**
     * Creates a new factory to read a resource with a given charset.
     *
     * @param charset
     *         the charset to use when reading the file
     * @param lineMapper
     *         provides a mapper to transform each of the resource lines
     */
    public ReaderFactory(final Charset charset, final Function<String, String> lineMapper) {
        this.charset = charset;
        this.lineMapper = REMOVE_COLOR_CODES.compose(lineMapper);
    }

    /**
     * Returns the name of the resource.
     *
     * @return the file name
     */
    public abstract String getFileName();

    /**
     * Creates a new {@link Reader} for the file.
     *
     * @return a reader
     */
    @MustBeClosed
    public abstract Reader create();

    /**
     * Provides the lines of the file as a {@link Stream} of strings.
     *
     * @return the file content as stream
     * @throws ParsingException
     *         if the file could not be read
     */
    @MustBeClosed
    @SuppressWarnings({"MustBeClosedChecker", "PMD.CloseResource"})
    @SuppressFBWarnings("OS_OPEN_STREAM")
    public Stream<String> readStream() {
        try {
            var reader = new BufferedReader(create());
            Stream<String> stringStream = reader.lines().onClose(closeReader(reader));
            if (hasLineMapper()) {
                return stringStream.map(lineMapper);
            }
            else {
                return stringStream;
            }
        }
        catch (UncheckedIOException e) {
            throw new ParsingException(e);
        }
    }

    @SuppressWarnings({"illegalcatch", "PMD.DoNotUseThreads", "PMD.AvoidThrowingRawExceptionTypes"})
    private Runnable closeReader(final AutoCloseable closeable) {
        return () -> {
            try {
                closeable.close();
            }
            catch (Exception e) {
                throw new ParsingException(e);
            }
        };
    }

    @SuppressWarnings("PMD.CompareObjectsWithEquals")
    @SuppressFBWarnings(value = "RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE", justification = "test stub")
    private boolean hasLineMapper() {
        return lineMapper != null && lineMapper != IDENTITY;
    }

    /**
     * Reads the whole file into a {@link String}.
     *
     * @return the file content as string
     * @throws ParsingException
     *         if the file could not be read
     */
    public String readString() {
        try (Stream<String> lines = readStream()) {
            return lines.collect(Collectors.joining("\n"));
        }
        catch (UncheckedIOException exception) {
            throw new ParsingException(exception);
        }
    }

    /**
     * Parses the whole file into a {@link Document}.
     *
     * @return the file content as document
     * @throws ParsingException
     *         if the file could not be parsed
     */
    public Document readDocument() {
        try (Reader reader = create()) {
            var parserFactory = new SecureXmlParserFactory();
            return parserFactory.readDocument(reader, getCharset());
        }
        catch (IOException | SecureXmlParserFactory.ParsingException exception) {
            throw new ParsingException(exception);
        }
    }

    /**
     * Returns the character set that is used to read the stream.
     *
     * @return the character set
     */
    public Charset getCharset() {
        return charset;
    }

    /**
     * Parses the whole file with the specified SAX {@link DefaultHandler}.
     *
     * @param handler
     *         the SAX handler to parse the file
     *
     * @throws ParsingException
     *         if the file could not be parsed
     */
    public void parse(final DefaultHandler handler) {
        try (Reader reader = create()) {
            new SecureXmlParserFactory().parse(reader, getCharset(), handler);
        }
        catch (IOException | SecureXmlParserFactory.ParsingException exception) {
            throw new ParsingException(exception);
        }
    }
}
