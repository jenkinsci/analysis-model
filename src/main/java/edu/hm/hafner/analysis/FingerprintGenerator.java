package edu.hm.hafner.analysis;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.InvalidPathException;

import edu.hm.hafner.util.VisibleForTesting;

/**
 * Creates fingerprints for a set of issues.
 *
 * @author Ullrich Hafner
 */
public class FingerprintGenerator {
    /**
     * Creates fingerprints for the specified set of issues.
     *
     * @param algorithm
     *         fingerprinting algorithm
     * @param issues
     *         the issues to analyze
     * @param charset
     *         the character set to use when reading the source files
     */
    public void run(final FullTextFingerprint algorithm, final Issues<?> issues, final Charset charset) {
        for (Issue issue : issues) {
            if (!issue.hasFingerprint()) {
                try {
                    String digest = algorithm.compute(issue.getFileName(), issue.getLineStart(), charset);
                    issue.setFingerprint(digest);
                }
                catch (IOException | UncheckedIOException | InvalidPathException exception) {
                    issue.setFingerprint(createDefaultFingerprint(issue.getFileName()));
                    if (exception.getCause() instanceof MalformedInputException) {
                        issues.logError("Provided encoding '%s' seems to be wrong for file '%s'",
                                charset, issue.getFileName());
                    }
                    else {
                        issues.logError("Can't read file '%s': %s", issue.getFileName(), exception);
                    }
                }
            }
        }
    }

    @VisibleForTesting
    static String createDefaultFingerprint(final String fileName) {
        return String.format("%x", fileName.hashCode());
    }
}
