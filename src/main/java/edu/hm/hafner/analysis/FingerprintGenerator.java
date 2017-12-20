package edu.hm.hafner.analysis;

import java.nio.charset.Charset;

/**
 * Creates a fingerprint of an issue. A fingerprint is a digest of the affected source code of an issue. Using this
 * fingerprint an issue can be tracked in the source code even after some minor refactorings.
 *
 * @author Ullrich Hafner
 */
public class FingerprintGenerator {
    public Issues<Issue> run(final FullTextFingerprint fingerprint, final Issues<Issue> issues, final IssueBuilder builder, final Charset charset) {
        Issues<Issue> enhanced = new Issues<>();
        for (Issue issue : issues) {
            String digest = fingerprint.compute(issue.getFileName(), issue.getLineStart(), charset);
            Issue issueWithFingerprint = builder.copy(issue).setFingerprint(digest).build();
            enhanced.add(issueWithFingerprint);
        }
        return enhanced;
    }
}
