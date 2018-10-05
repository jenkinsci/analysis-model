package edu.hm.hafner.analysis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.InvalidPathException;

import org.apache.commons.lang3.builder.HashCodeBuilder;

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
     * @param report
     *         the issues to analyze
     * @param charset
     *         the character set to use when reading the source files
     */
    public void run(final FullTextFingerprint algorithm, final Report report, final Charset charset) {
        FilteredLog log = new FilteredLog(report, "Can't create fingerprints for some files:");
        int sum = 0;
        for (Issue issue : report) {
            if (!issue.hasFingerprint()) {
                try {
                    String digest = algorithm.compute(issue.getFileName(), issue.getLineStart(), charset);
                    issue.setFingerprint(digest);
                    sum++;
                }
                catch (FileNotFoundException exception) {
                    log.logError("- '%s' file not found", issue.getFileName());
                }
                catch (IOException | InvalidPathException exception) {
                    issue.setFingerprint(createDefaultFingerprint(issue));
                    if (exception.getCause() instanceof MalformedInputException) {
                        log.logError("- '%s', provided encoding '%s' seems to be wrong",
                                issue.getFileName(), charset);
                    }
                    else {
                        log.logError("- '%s', IO exception has been thrown: %s",
                                issue.getFileName(), exception);
                    }
                }
            }
        }
        report.logInfo("-> created fingerprints for %d issues", sum);
        log.logSummary();
    }

    @VisibleForTesting
    static String createDefaultFingerprint(final Issue issue) {
        HashCodeBuilder builder = new HashCodeBuilder();
        return String.format("FALLBACK-%x",
                builder.append(issue.getBaseName())
                        .append(issue.getType())
                        .append(issue.getCategory())
                        .append(issue.getSeverity())
                        .append(issue.getOrigin())
                        .append(issue.getLineStart()).build());
    }
}
