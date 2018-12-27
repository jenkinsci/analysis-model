package edu.hm.hafner.analysis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

import org.apache.commons.io.input.BOMInputStream;

import com.google.errorprone.annotations.MustBeClosed;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Provides a {@link ReaderFactory} that returns readers for a given file.
 *
 * @author Ullrich Hafner
 */
public class FileReaderFactory extends ReaderFactory {
    private final Path file;
    private final String fileName;

    /**
     * Creates a new factory to read the specified file with a given charset.
     *
     * @param file
     *         the file to open
     * @param charset
     *         the charset to use when reading the file
     */
    public FileReaderFactory(final Path file, final @CheckForNull Charset charset) {
        super(charset);
        
        this.file = file;
        fileName = file.toAbsolutePath().toString().replace('\\', '/');
    }

    @Override @MustBeClosed
    public Reader create() {
        try {
            InputStream inputStream = Files.newInputStream(file);

            return new InputStreamReader(new BOMInputStream(inputStream), getCharset());
        }
        catch (FileNotFoundException | InvalidPathException exception) {
            throw new ParsingException(exception, "Can't find file: " + fileName);
        }
        catch (IOException | UncheckedIOException exception) {
            throw new ParsingException(exception, "Can't scan file for issues: " + fileName);
        }
    }

    /**
     * Returns the absolute path of the resource. The file name uses UNIX path separators.
     *
     * @return the file name
     */
    @Override
    public String getFileName() {
        return fileName;
    }
}
