package edu.hm.hafner.analysis;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Locale;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import com.google.errorprone.annotations.MustBeClosed;

import edu.hm.hafner.util.VisibleForTesting;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Creates a fingerprint of the specified issue using the source code at the affected line. The fingerprint is computed
 * using the 1:1 content of a small number of lines before and after the affected line (see {@link #linesLookAhead}).
 *
 * @author Ullrich Hafner
 */
public class FullTextFingerprint {
    /** Number of lines before and after current line to consider. */
    private static final int DEFAULT_LINES_LOOK_AHEAD = 3;
    private static final int LINE_RANGE_BUFFER_SIZE = 1000;
    private static final char[] HEX_CHARACTERS = "0123456789ABCDEF".toCharArray();

    @SuppressWarnings("PMD.AvoidMessageDigestField")
    private final MessageDigest digest;
    private final FileSystem fileSystem;
    private final int linesLookAhead;

    /**
     * Creates a new instance of {@link FullTextFingerprint}.
     */
    public FullTextFingerprint() {
        this(DEFAULT_LINES_LOOK_AHEAD);
    }

    /**
     * Creates a new instance of {@link FullTextFingerprint}.
     *
     * @param linesLookAhead
     *          the number of lines which is used in the fingerprinting process
     */
    public FullTextFingerprint(final int linesLookAhead) {
        this(new FileSystem(), linesLookAhead);
    }

    @VisibleForTesting
    @SuppressFBWarnings(value = "WEAK_MESSAGE_DIGEST_MD5", justification = "The fingerprint is just used to track new warnings")
    FullTextFingerprint(final FileSystem fileSystem, final int linesLookAhead) {
        this.fileSystem = fileSystem;
        this.linesLookAhead = linesLookAhead;
        try {
            digest = MessageDigest.getInstance("MD5"); // lgtm [java/weak-cryptographic-algorithm]
        }
        catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Creates a fingerprint of the specified issue using the source code at the affected line. The fingerprint is
     * computed using the 1:1 content of a small number of lines before and after the affected line (see {@link
     * #linesLookAhead}).
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
        return String.format(Locale.ENGLISH, "%x", fileName.hashCode());
    }

    @VisibleForTesting
    String createFingerprint(final int line, final Stream<String> lines, final Charset charset) {
        var context = extractContext(line, lines.iterator());
        lines.close();
        digest.update(context.getBytes(charset));

        return asHex(digest.digest()).toUpperCase(Locale.ENGLISH);
    }

    private String asHex(final byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_CHARACTERS[v >>> 4];
            hexChars[j * 2 + 1] = HEX_CHARACTERS[v & 0x0F];
        }
        return String.valueOf(hexChars);
    }

    @VisibleForTesting
    String extractContext(final int affectedLine, final Iterator<String> lines) {
        if (affectedLine < 0) {
            return StringUtils.EMPTY;
        }

        int start = computeStartLine(affectedLine);

        var context = new StringBuilder(LINE_RANGE_BUFFER_SIZE);
        int line = 1;
        for (; lines.hasNext() && line < start - linesLookAhead; line++) {
            lines.next(); // skip the first lines
        }
        for (; lines.hasNext() && line <= start + linesLookAhead; line++) {
            context.append(lines.next());
        }

        return context.toString();
    }

    private int computeStartLine(final int affectedLine) {
        if (affectedLine == 0) { // indicates the whole file
            return linesLookAhead + 1;
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
            return Files.lines(Path.of(fileName), charset);
        }
    }
}
