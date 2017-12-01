package edu.hm.hafner.analysis;

import java.nio.charset.Charset;

import com.google.common.annotations.VisibleForTesting;

import edu.hm.hafner.analysis.LineRangeFingerprint.FileSystem;

/**
 * Creates a fingerprint of an issue. A fingerprint is a digest of the affected source code of an issue. Using this
 * fingerprint an issue can be tracked in the source code even after some minor refactorings.
 *
 * @author Ullrich Hafner
 */
public class FingerprintGenerator {
    private final LineRangeFingerprint fingerprint;

    /**
     * Creates a new instance of {@link FingerprintGenerator}.
     */
    public FingerprintGenerator() {
        fingerprint = new LineRangeFingerprint();
    }

    @VisibleForTesting
    FingerprintGenerator(final FileSystem fileSystem) {
        fingerprint = new LineRangeFingerprint(fileSystem);
    }

    public Issues<Issue> run(final Issues<Issue> issues, final IssueBuilder builder, final Charset charset) {
        Issues<Issue> enhanced = new Issues<>();
        for (Issue issue : issues) {
            Issue issueWithFingerprint = builder.copy(issue)
                    .setFingerprint(createFingerprintFor(issue, charset))
                    .build();
            enhanced.add(issueWithFingerprint);
        }
        return enhanced;
    }

    private String createFingerprintFor(final Issue issue, final Charset charset) {

        return fingerprint.compute(issue.getFileName(), issue.getLineStart(), charset);
    }
}
