package edu.hm.hafner.analysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.InvalidPathException;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.input.ReaderInputStream;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.errorprone.annotations.MustBeClosed;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import static org.apache.xerces.impl.Constants.*;

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
            BufferedReader reader = new BufferedReader(create());
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
        catch (UncheckedIOException e) {
            throw new ParsingException(e);
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
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            setFeature(factory, SAX_FEATURE_PREFIX, DISALLOW_DOCTYPE_DECL_FEATURE, true);
            setFeature(factory, SAX_FEATURE_PREFIX, EXTERNAL_GENERAL_ENTITIES_FEATURE, false);
            setFeature(factory, SAX_FEATURE_PREFIX, EXTERNAL_PARAMETER_ENTITIES_FEATURE, false);
            setFeature(factory, SAX_FEATURE_PREFIX, RESOLVE_DTD_URIS_FEATURE, false);
            setFeature(factory, SAX_FEATURE_PREFIX, USE_ENTITY_RESOLVER2_FEATURE, false);
            setFeature(factory, XERCES_FEATURE_PREFIX, CREATE_ENTITY_REF_NODES_FEATURE, false);
            setFeature(factory, XERCES_FEATURE_PREFIX, LOAD_DTD_GRAMMAR_FEATURE, false);
            setFeature(factory, XERCES_FEATURE_PREFIX, LOAD_EXTERNAL_DTD_FEATURE, false);
            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);

            DocumentBuilder docBuilder = factory.newDocumentBuilder();
            return docBuilder.parse(new InputSource(new ReaderInputStream(reader, getCharset())));
        }
        catch (SAXException | IOException | InvalidPathException | ParserConfigurationException e) {
            throw new ParsingException(e);
        }
    }

    @SuppressFBWarnings
    @SuppressWarnings("illegalcatch")
    private void setFeature(final DocumentBuilderFactory factory, final String prefix, final String feature,
            final boolean value) {
        try {
            factory.setFeature(prefix + feature, value);
        }
        catch (Exception ignored) {
            // ignore and continue
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
}

