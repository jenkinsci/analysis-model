package edu.hm.hafner.analysis;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.commons.lang3.ObjectUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.errorprone.annotations.MustBeClosed;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * FIXME: write comment.
 *
 * @author Ullrich Hafner
 */
public class ReaderFactory {
    private final Path file;
    private final Charset charset;
    private final String fileName;

    /**
     * Creates a new factory to read the specified file with a given charset.
     *
     * @param file
     *         the file to open
     * @param charset
     *         the charset to use when reading the file
     */
    public ReaderFactory(final Path file, final @CheckForNull Charset charset) {
        this.file = file;
        this.charset = charset;

        fileName = file.toAbsolutePath().toString().replace('\\', '/');
    }

    /**
     * Creates a new {@link Reader} for the file.
     *
     * @return a reader
     */
    @MustBeClosed
    public Reader create() {
        try (InputStream inputStream = Files.newInputStream(file)) {
            return createReader(inputStream);
        }
        catch (FileNotFoundException exception) {
            throw new ParsingException(exception, "Can't find file: " + fileName);
        }
        catch (IOException | UncheckedIOException exception) {
            throw new ParsingException(exception, "Can't scan file for issues: " + fileName);
        }
    }

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

    private Charset getCharset() {
        return ObjectUtils.defaultIfNull(charset, StandardCharsets.UTF_8);
    }

    /**
     * Reads the whole file into a {@link String}.
     *
     * @return the file content as string
     * @throws ParsingException if the file could not be read
     */
    public String readString() {
        try {
            return IOUtils.toString(create());
        }
        catch (IOException e) {
            throw new ParsingException(e);
        }
    }

    /**
     * Parses the whole file into a {@link Document}.
     *
     * @return the file content as document
     * @throws ParsingException if the file could not be parsed
     */
    public Document readDocument() {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            return docBuilder.parse(createInputSource());
        }
        catch (SAXException | IOException | ParserConfigurationException e) {
            throw new ParsingException(e);
        }
    }

    private Reader createReader(final InputStream inputStream) {
        return new InputStreamReader(new BOMInputStream(inputStream), charset);
    }

    /**
     * Returns the absolute path of the file. The file name uses UNIX path separators.
     * 
     * @return the file name
     */
    public String getFileName() {
        return fileName;
    }
}

