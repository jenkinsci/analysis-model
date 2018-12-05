package edu.hm.hafner.analysis;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.InvalidPathException;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.commons.lang3.ObjectUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.errorprone.annotations.MustBeClosed;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Provides several useful helper methods to read the contents of a resource that is given by a {@link Reader}.
 *
 * @author Ullrich Hafner
 */
public abstract class ReaderFactory {
    private final Charset charset;

    /**
     * Creates a new factory to read a resource with a given charset.
     *
     * @param charset
     *         the charset to use when reading the file
     */
    public ReaderFactory(final @CheckForNull Charset charset) {
        this.charset = charset;
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
     * Creates a new {@link BufferedReader} for the file.
     *
     * @return a buffered reader
     */
    @MustBeClosed
    public BufferedReader createBufferedReader() {
        return new BufferedReader(create());
    }

    /**
     * Creates a new {@link InputSource} for the file.
     *
     * @return a buffered reader
     */
    @MustBeClosed
    public InputSource createInputSource() {
        return new InputSource(new ReaderInputStream(create(), getCharset()));
    }

    /**
     * Provides the lines of the file as a {@link Stream} of strings.
     *
     * @return the file content as stream
     * @throws ParsingException
     *         if the file could not be read
     */
    @MustBeClosed
    public Stream<String> readStream() {
        return createBufferedReader().lines();
    }

    /**
     * Reads the whole file into a {@link String}.
     *
     * @return the file content as string
     * @throws ParsingException
     *         if the file could not be read
     */
    public String readString() {
        try {
            return IOUtils.toString(create());
        }
        catch (IOException | InvalidPathException e) {
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
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            return docBuilder.parse(createInputSource());
        }
        catch (SAXException | IOException | InvalidPathException | ParserConfigurationException e) {
            throw new ParsingException(e);
        }
    }

    protected Charset getCharset() {
        return ObjectUtils.defaultIfNull(charset, StandardCharsets.UTF_8);
    }
}

