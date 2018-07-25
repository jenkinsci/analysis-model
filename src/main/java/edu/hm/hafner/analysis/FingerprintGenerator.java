package edu.hm.hafner.analysis;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.List;

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
        int errorCount = 0;
        List<String> errors = new ArrayList<>();

        for (Issue issue : report) {
            if (!issue.hasFingerprint()) {
                try {
                    String digest = algorithm.compute(issue.getFileName(), issue.getLineStart(), charset);
                    issue.setFingerprint(digest);
                }
                catch (IOException | InvalidPathException exception) {
                    issue.setFingerprint(createDefaultFingerprint(issue));
                    if (errorCount < 5) {
                        if (exception.getCause() instanceof MalformedInputException) {
                            errors.add(String.format(
                                    "- '%s', provided encoding '%s' seems to be wrong",
                                    issue.getFileName(), charset));
                        }
                        else {
                            errors.add(String.format(
                                    "- '%s', IO exception has been thrown: %s",
                                    issue.getFileName(), exception));
                        }
                    }
                    else if (errorCount == 5) {
                        errors.add("  ... skipped logging of additional file errors ...");
                    }
                    errorCount++;
                }
            }
        }
        if (errorCount > 0) {
            report.logError("Can't create fingerprints for %d files:", errorCount);
            errors.forEach(report::logError);
        }
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
