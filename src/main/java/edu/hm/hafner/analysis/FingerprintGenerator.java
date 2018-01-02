package edu.hm.hafner.analysis;

import java.nio.charset.Charset;

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
     * @param builder
     *         the issue builder to create the new issues with
     * @param charset
     *         the character set to use when reading the source files
     *
     * @return the issues with fingerprints
     */
    public Issues<Issue> run(final FullTextFingerprint algorithm, final Issues<Issue> issues,
            final IssueBuilder builder, final Charset charset) {
        Issues<Issue> enhanced = new Issues<>();
        for (Issue issue : issues) {
            String digest = algorithm.compute(issue.getFileName(), issue.getLineStart(), charset);
            Issue issueWithFingerprint = builder.copy(issue).setFingerprint(digest).build();
            enhanced.add(issueWithFingerprint);
        }
        return enhanced;
    }
}
