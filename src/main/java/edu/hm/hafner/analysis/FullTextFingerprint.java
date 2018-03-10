package edu.hm.hafner.analysis;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Locale;
import java.util.stream.Stream;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.StringUtils;

import com.google.errorprone.annotations.MustBeClosed;

import edu.hm.hafner.util.VisibleForTesting;

/**
 * Creates a fingerprint of the specified issue using the source code at the affected line. The fingerprint is computed
 * using the 1:1 content of a small number of lines before and after the affected line (see {@link #LINES_LOOK_AHEAD}).
 *
 * @author Ullrich Hafner
 */
public class FullTextFingerprint {
    /** Number of lines before and after current line to consider. */
    private static final int LINES_LOOK_AHEAD = 3;
    private static final int LINE_RANGE_BUFFER_SIZE = 1000;

    private final MessageDigest digest;
    private final FileSystem fileSystem;

    /**
     * Creates a new instance of {@link FullTextFingerprint}.
     */
    public FullTextFingerprint() {
        this(new FileSystem());
    }

    @VisibleForTesting
    FullTextFingerprint(final FileSystem fileSystem) {
        this.fileSystem = fileSystem;
        try {
            digest = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Creates a fingerprint of the specified issue using the source code at the affected line. The fingerprint is
     * computed using the 1:1 content of a small number of lines before and after the affected line (see {@link
     * #LINES_LOOK_AHEAD}).
     *
     * @param fileName
     *         the absolute path of the affected file
     * @param line
     *         the line of the issue
     * @param charset
     *         the encoding to be used when reading the affected file
     *
     * @return a fingerprint of the selected range of source code lines (if the file could not be read then the
     *         fingerprint actually is the hashcode of the filename)
     * @throws IOException
     *         if the file could not be read
     */
    public String compute(final String fileName, final int line, final Charset charset) throws IOException {
        try (Stream<String> lines = fileSystem.readLinesFromFile(fileName, charset)) {
            return createFingerprint(line, lines, charset);
        }
    }

    @VisibleForTesting
    String getFallbackFingerprint(final String fileName) {
        return String.format("%x", fileName.hashCode());
    }

    @VisibleForTesting
    String createFingerprint(final int line, final Stream<String> lines, final Charset charset) {
        String context = extractContext(line, lines.iterator());
        lines.close();
        digest.update(context.getBytes(charset));

        return DatatypeConverter.printHexBinary(digest.digest()).toUpperCase(Locale.ENGLISH);
    }

    @VisibleForTesting
    String extractContext(final int affectedLine, final Iterator<String> lines) {
        if (affectedLine < 0) {
            return StringUtils.EMPTY;
        }

        int start = computeStartLine(affectedLine);

        StringBuilder context = new StringBuilder(LINE_RANGE_BUFFER_SIZE);
        int line = 1;
        for (; lines.hasNext() && line < start - LINES_LOOK_AHEAD; line++) {
            lines.next(); // skip the first lines
        }
        for (; lines.hasNext() && line <= start + LINES_LOOK_AHEAD; line++) {
            context.append(lines.next());
        }

        return context.toString();
    }

    private int computeStartLine(final int affectedLine) {
        if (affectedLine == 0) { // indicates the whole file
            return LINES_LOOK_AHEAD + 1;
        }
        else {
            return affectedLine;
        }
    }

    /**
     * Facade for file system operations. May be replaced by stubs in test cases.
     */
    @VisibleForTesting
    static class FileSystem {
        @MustBeClosed
        Stream<String> readLinesFromFile(final String fileName, final Charset charset)
                throws IOException, InvalidPathException {
            return Files.lines(Paths.get(fileName), charset);
        }
    }
}

