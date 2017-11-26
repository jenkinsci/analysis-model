package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.assertj.Assertions.*;

/**
 * Tests the class {@link FingerprintGenerator}.
 *
 * @author Ullrich Hafner
 */
class FingerprintGeneratorTest {
    @Test
    void shouldCreateFingerPrintForIssues() {
        Issues<Issue> issues = new Issues<>();
        IssueBuilder builder = new IssueBuilder();
        builder.setFileName("file.txt");

        issues.add(builder.setPackageName("a").build());
        issues.add(builder.setPackageName("b").build());

        FingerprintGenerator generator = new FingerprintGenerator();
        Issues<Issue> enhanced = generator.run(issues, builder);

        Issue issueAWithFingerPrint = enhanced.get(0);
        Issue issueBWithFingerPrint = enhanced.get(1);

        assertThat(issueAWithFingerPrint).isNotEqualTo(issueBWithFingerPrint);
        assertThat(issueAWithFingerPrint.getFingerprint())
                .as("Fingerprint is not set")
                .isNotEmpty()
                .isNotEqualTo("-");
        assertThat(issueAWithFingerPrint.getFingerprint())
                .isEqualTo(issueBWithFingerPrint.getFingerprint());
    }
}