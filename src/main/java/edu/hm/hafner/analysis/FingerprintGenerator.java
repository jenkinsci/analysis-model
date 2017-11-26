package edu.hm.hafner.analysis;

/**
 * Creates a fingerprint of an issue. A fingerprint is a digest of the affected source code of an issue. Using this
 * fingerprint an issue can be tracked in the source code even after some minor refactorings.
 *
 * @author Ullrich Hafner
 */
public class FingerprintGenerator {
    public Issues<Issue> run(final Issues<Issue> issues, final IssueBuilder builder) {
        Issues<Issue> enhanced = new Issues<>();
        for (Issue issue : issues) {
            enhanced.add(builder.copy(issue).setFingerprint("bla").build());
        }
        return enhanced;
    }
}
