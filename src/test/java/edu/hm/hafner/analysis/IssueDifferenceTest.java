package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.registry.ParserRegistry;
import edu.hm.hafner.util.ResourceTest;

import java.nio.charset.StandardCharsets;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Unit Tests of the class {@link IssueDifference}.
 *
 * @author Artem Polovyi
 */
class IssueDifferenceTest extends ResourceTest {
    private static final String REFERENCE_BUILD = "100";
    private static final String CURRENT_BUILD = "2";

    @Test
    void shouldCreateIssueDifferenceBasedOnPropertiesAndThenOnFingerprint() {
        var referenceIssues = new Report().addAll(
                createIssue("OUTSTANDING 1", "OUT 1"),
                createIssue("OUTSTANDING 2", "OUT 2"),
                createIssue("OUTSTANDING 3", "OUT 3"),
                createIssue("TO FIX 1", "FIX 1"),
                createIssue("TO FIX 2", "FIX 2"));

        var currentIssues = new Report().addAll(
                createIssue("UPD OUTSTANDING 1", "OUT 1"),
                createIssue("OUTSTANDING 2", "UPD OUT 2"),
                createIssue("OUTSTANDING 3", "OUT 3"),
                createIssue("NEW 1", "NEW 1"));

        var issueDifference = new IssueDifference(currentIssues, CURRENT_BUILD, referenceIssues);

        var outstanding = issueDifference.getOutstandingIssues();
        assertThat(outstanding).hasSize(3);
        assertThat(outstanding.get(0)).hasMessage("OUTSTANDING 2").hasReference(REFERENCE_BUILD);
        assertThat(outstanding.get(1)).hasMessage("OUTSTANDING 3").hasReference(REFERENCE_BUILD);
        assertThat(outstanding.get(2)).hasMessage("UPD OUTSTANDING 1").hasReference(REFERENCE_BUILD);

        var fixed = issueDifference.getFixedIssues();
        assertThat(fixed).hasSize(2);
        assertThat(fixed.get(0)).hasMessage("TO FIX 1").hasReference(REFERENCE_BUILD);
        assertThat(fixed.get(1)).hasMessage("TO FIX 2").hasReference(REFERENCE_BUILD);

        assertThat(issueDifference.getNewIssues()).hasSize(1);
        assertThat(issueDifference.getNewIssues().get(0)).hasMessage("NEW 1").hasReference("2");
    }

    /**
     * Verifies that issue difference report has only outstanding issues when the current report and reference report
     * have same issues.
     */
    @Test
    void shouldCreateOutstandingIssueDifference() {
        shouldFindOutstandingFromEqualsOrFingerprint("NEW", "OLD");
        shouldFindOutstandingFromEqualsOrFingerprint("OLD", "OLD");
        shouldFindOutstandingFromEqualsOrFingerprint("OLD", "NEW");
    }

    private void shouldFindOutstandingFromEqualsOrFingerprint(
            final String currentMessage, final String currentFingerprint) {
        var referenceIssues = new Report().add(createIssue("OLD", "OLD"));
        var currentIssues = new Report().add(createIssue(currentMessage, currentFingerprint));

        var issueDifference = new IssueDifference(currentIssues, CURRENT_BUILD, referenceIssues);

        assertThat(issueDifference.getFixedIssues()).isEmpty();
        assertThat(issueDifference.getNewIssues()).isEmpty();
        var outstanding = issueDifference.getOutstandingIssues();

        assertThat(outstanding).hasSize(1);
        assertThat(outstanding.get(0))
                .hasMessage(currentMessage)
                .hasFingerprint(currentFingerprint)
                .hasReference(REFERENCE_BUILD);
    }

    @Test
    void shouldCreateIssueDifferenceWithEmptyCurrent() {
        var referenceIssues = new Report().addAll(createIssue("OLD 1", "FA"),
                createIssue("OLD 2", "FB"));
        var currentIssues = new Report();

        var issueDifference = new IssueDifference(currentIssues, CURRENT_BUILD, referenceIssues);

        assertThat(issueDifference.getNewIssues()).isEmpty();
        assertThat(issueDifference.getOutstandingIssues()).isEmpty();

        var fixed = issueDifference.getFixedIssues();

        assertThat(fixed).hasSize(2);
        assertThat(fixed.get(0)).hasMessage("OLD 1").hasReference(REFERENCE_BUILD);
        assertThat(fixed.get(1)).hasMessage("OLD 2").hasReference(REFERENCE_BUILD);
    }

    @Test
    void shouldCreateIssueDifferenceWithEmptyReference() {
        var referenceIssues = new Report();
        var currentIssues = new Report().addAll(createIssue("NEW 1", "FA"),
                createIssue("NEW 2", "FB"));

        var issueDifference = new IssueDifference(currentIssues, CURRENT_BUILD, referenceIssues);

        assertThat(issueDifference.getFixedIssues()).isEmpty();
        assertThat(issueDifference.getOutstandingIssues()).isEmpty();

        var newIssues = issueDifference.getNewIssues();
        assertThat(newIssues).hasSize(2);
        assertThat(newIssues.get(0)).hasMessage("NEW 1").hasReference(CURRENT_BUILD);
        assertThat(newIssues.get(1)).hasMessage("NEW 2").hasReference(CURRENT_BUILD);
    }

    @Test
    @org.junitpioneer.jupiter.Issue("JENKINS-56324")
    void shouldAlsoUseFingerprintIfIssuesAreEqual() {
        var referenceIssues = new Report().addAll(
                createIssue("OLD 1", "FP"));
        var currentIssues = new Report().addAll(
                createIssue("NEW 1", "FP"),
                createIssue("OLD 1", "FP"));

        var issueDifference = new IssueDifference(currentIssues, CURRENT_BUILD, referenceIssues);

        assertThat(issueDifference.getFixedIssues()).isEmpty();

        var outstandingIssues = issueDifference.getOutstandingIssues();
        assertThat(outstandingIssues).hasSize(1);
        assertThat(outstandingIssues.get(0)).hasMessage("OLD 1").hasReference(REFERENCE_BUILD);

        var newIssues = issueDifference.getNewIssues();
        assertThat(newIssues).hasSize(1);
        assertThat(newIssues.get(0)).hasMessage("NEW 1").hasReference(CURRENT_BUILD);
    }

    @Test
    void shouldRemoveForSecondPass() {
        var referenceIssues = new Report().addAll(
                createIssue("NEW 1", "FP1"),
                createIssue("NEW 2", "FP1"));
        var currentIssues = new Report().addAll(
                createIssue("NEW 1", "FP1"),
                createIssue("NEW 3", "FP2"));

        var issueDifference = new IssueDifference(currentIssues, CURRENT_BUILD, referenceIssues);

        assertThat(issueDifference.getFixedIssues()).hasSize(1);
        assertThat(issueDifference.getNewIssues()).hasSize(1);
        assertThat(issueDifference.getOutstandingIssues()).hasSize(1);
    }

    @Test
    @org.junitpioneer.jupiter.Issue("JENKINS-61383")
    void shouldMatchClosestLineWhenMultipleIssuesShareSameFingerprint() {
        var sharedFingerprint = "SAME-FP";

        var referenceIssues = new Report().addAll(
                createIssueAtLine("WARNING", sharedFingerprint, 10),
                createIssueAtLine("WARNING", sharedFingerprint, 11),
                createIssueAtLine("WARNING", sharedFingerprint, 12),
                createIssueAtLine("WARNING", sharedFingerprint, 13),
                createIssueAtLine("WARNING", sharedFingerprint, 14));

        var currentIssues = new Report().addAll(
                createIssueAtLine("WARNING", sharedFingerprint, 11),
                createIssueAtLine("WARNING", sharedFingerprint, 12),
                createIssueAtLine("WARNING", sharedFingerprint, 13),
                createIssueAtLine("WARNING", sharedFingerprint, 14),
                createIssueAtLine("WARNING", sharedFingerprint, 15));

        var issueDifference = new IssueDifference(currentIssues, CURRENT_BUILD, referenceIssues);

        assertThat(issueDifference.getNewIssues())
                .as("No issue should be marked new after a simple line shift")
                .isEmpty();
        assertThat(issueDifference.getOutstandingIssues())
                .as("All issues should remain outstanding after a line shift")
                .hasSize(5);
        assertThat(issueDifference.getFixedIssues())
                .as("No issue should be marked fixed after a simple line shift")
                .isEmpty();
    }

    @Test
    @org.junitpioneer.jupiter.Issue("JENKINS-61383")
    void shouldStillDetectTrulyNewIssueAmongIdenticalAdjacentWarnings() {
        var sharedFingerprint = "SAME-FP";

        var referenceIssues = new Report().addAll(
                createIssueAtLine("WARNING", sharedFingerprint, 10),
                createIssueAtLine("WARNING", sharedFingerprint, 11),
                createIssueAtLine("WARNING", sharedFingerprint, 12));

        var currentIssues = new Report().addAll(
                createIssueAtLine("WARNING", sharedFingerprint, 11),
                createIssueAtLine("WARNING", sharedFingerprint, 12),
                createIssueAtLine("WARNING", sharedFingerprint, 13),
                createIssueAtLine("NEW WARNING", "DIFFERENT-FP", 100));

        var issueDifference = new IssueDifference(currentIssues, CURRENT_BUILD, referenceIssues);

        assertThat(issueDifference.getOutstandingIssues())
                .as("The 3 shifted issues should all be outstanding")
                .hasSize(3);
        assertThat(issueDifference.getNewIssues())
                .as("Only the genuinely new issue should be marked new")
                .hasSize(1);
        assertThat(issueDifference.getNewIssues().get(0))
                .hasMessage("NEW WARNING")
                .hasReference(CURRENT_BUILD);
        assertThat(issueDifference.getFixedIssues()).isEmpty();
    }

    private Issue createIssueAtLine(final String message, final String fingerprint, final int line) {
        try (var builder = new IssueBuilder()) {
            builder.setFileName("file-name")
                    .setLineStart(line)
                    .setLineEnd(line)
                    .setColumnStart(1)
                    .setColumnEnd(1)
                    .setCategory("category")
                    .setType("type")
                    .setPackageName("package-name")
                    .setModuleName("module-name")
                    .setSeverity(Severity.WARNING_HIGH)
                    .setMessage(message)
                    .setDescription("description")
                    .setOrigin("origin")
                    .setFingerprint(fingerprint)
                    .setReference(REFERENCE_BUILD);
            return builder.build();
        }
    }

    private Issue createIssue(final String message, final String fingerprint) {
        return createIssue(message, fingerprint, "file-name");
    }

    private Issue createIssue(final String message, final String fingerprint, final String fileName) {
        try (var builder = new IssueBuilder()) {
            builder.setFileName(fileName)
                    .setLineStart(1)
                    .setLineEnd(2)
                    .setColumnStart(3)
                    .setColumnEnd(4)
                    .setCategory("category")
                    .setType("type")
                    .setPackageName("package-name")
                    .setModuleName("module-name")
                    .setSeverity(Severity.WARNING_HIGH)
                    .setMessage(message)
                    .setDescription("description")
                    .setOrigin("origin")
                    .setFingerprint(fingerprint)
                    .setReference(REFERENCE_BUILD);
            return builder.build();
        }
    }

    @Test
    @org.junitpioneer.jupiter.Issue("JENKINS-65482")
    void shouldHandleAggregatedResults() {
        var firstAxis = readSpotBugsWarnings();
        assertThat(firstAxis).hasSize(2);

        var secondAxis = readSpotBugsWarnings();
        assertThat(secondAxis).hasSize(2);

        var aggregation = new Report();
        aggregation.addAll(firstAxis, secondAxis);
        assertThat(aggregation).hasSize(2);

        var reference = new Report();
        reference.addAll(firstAxis, secondAxis);

        var issueDifference = new IssueDifference(aggregation, CURRENT_BUILD, reference);
        assertThat(issueDifference).hasNoFixedIssues().hasNoNewIssues();
        assertThat(issueDifference.getOutstandingIssues()).hasSize(2);
    }

    private Report readSpotBugsWarnings() {
        var reportFile = new FileReaderFactory(getResourceAsFile("parser/findbugs/spotbugsXml.xml"),
                StandardCharsets.UTF_8);
        return new ParserRegistry().get("spotbugs").createParser().parseReport(reportFile);
    }
}
