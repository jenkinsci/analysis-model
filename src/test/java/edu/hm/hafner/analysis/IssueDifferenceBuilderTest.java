package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.assertions.Assertions.*;
import static java.util.Collections.*;

public class IssueDifferenceBuilderTest {
    private static final String REFERENCE_BUILD = "100";
    private static final String CURRENT_BUILD = "2";

    private Issue createIssue(final String message, final String fingerprint) {
        IssueBuilder builder = new IssueBuilder();
        builder.setFileName("file-name")
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
                .setLineRanges(new LineRangeList(singletonList(new LineRange(5, 6))))
                .setFingerprint(fingerprint)
                .setReference(REFERENCE_BUILD);
        return builder.build();
    }

    @Test
    void usingSetterToDefine() {
        IssueDifferenceBuilder builder = new IssueDifferenceBuilder();

        Report referenceIssues = new Report().addAll(
                createIssue("OUTSTANDING 1", "OUT 1"),
                createIssue("OUTSTANDING 2", "OUT 2"),
                createIssue("OUTSTANDING 3", "OUT 3"),
                createIssue("TO FIX 1", "FIX 1"),
                createIssue("TO FIX 2", "FIX 2"));

        Report currentIssues = new Report().addAll(
                createIssue("UPD OUTSTANDING 1", "OUT 1"),
                createIssue("OUTSTANDING 2", "UPD OUT 2"),
                createIssue("OUTSTANDING 3", "OUT 3"),
                createIssue("NEW 1", "NEW 1"));


        builder.setCurrentIssues(currentIssues)
                .setReferenceId(CURRENT_BUILD)
                .setReferenceIssues(referenceIssues);

        IssueDifference issueDifference = builder.build();

        Report outstanding = issueDifference.getOutstandingIssues();
        assertThat(outstanding).hasSize(3);
        assertThat(outstanding.get(0)).hasMessage("OUTSTANDING 2").hasReference(REFERENCE_BUILD);
        assertThat(outstanding.get(1)).hasMessage("OUTSTANDING 3").hasReference(REFERENCE_BUILD);
        assertThat(outstanding.get(2)).hasMessage("UPD OUTSTANDING 1").hasReference(REFERENCE_BUILD);

        Report fixed = issueDifference.getFixedIssues();
        assertThat(fixed).hasSize(2);
        assertThat(fixed.get(0)).hasMessage("TO FIX 1").hasReference(REFERENCE_BUILD);
        assertThat(fixed.get(1)).hasMessage("TO FIX 2").hasReference(REFERENCE_BUILD);

        assertThat(issueDifference.getNewIssues()).hasSize(1);
        assertThat(issueDifference.getNewIssues().get(0)).hasMessage("NEW 1").hasReference("2");
    }

    @Test
    void usingOverloadedConstructor() {

        Report referenceIssues = new Report().addAll(
                createIssue("OUTSTANDING 1", "OUT 1"),
                createIssue("OUTSTANDING 2", "OUT 2"),
                createIssue("OUTSTANDING 3", "OUT 3"),
                createIssue("TO FIX 1", "FIX 1"),
                createIssue("TO FIX 2", "FIX 2"));

        Report currentIssues = new Report().addAll(
                createIssue("UPD OUTSTANDING 1", "OUT 1"),
                createIssue("OUTSTANDING 2", "UPD OUT 2"),
                createIssue("OUTSTANDING 3", "OUT 3"),
                createIssue("NEW 1", "NEW 1"));


        IssueDifferenceBuilder builder = new IssueDifferenceBuilder(currentIssues, CURRENT_BUILD, referenceIssues);


        IssueDifference issueDifference = builder.build();

        Report outstanding = issueDifference.getOutstandingIssues();
        assertThat(outstanding).hasSize(3);
        assertThat(outstanding.get(0)).hasMessage("OUTSTANDING 2").hasReference(REFERENCE_BUILD);
        assertThat(outstanding.get(1)).hasMessage("OUTSTANDING 3").hasReference(REFERENCE_BUILD);
        assertThat(outstanding.get(2)).hasMessage("UPD OUTSTANDING 1").hasReference(REFERENCE_BUILD);

        Report fixed = issueDifference.getFixedIssues();
        assertThat(fixed).hasSize(2);
        assertThat(fixed.get(0)).hasMessage("TO FIX 1").hasReference(REFERENCE_BUILD);
        assertThat(fixed.get(1)).hasMessage("TO FIX 2").hasReference(REFERENCE_BUILD);

        assertThat(issueDifference.getNewIssues()).hasSize(1);
        assertThat(issueDifference.getNewIssues().get(0)).hasMessage("NEW 1").hasReference("2");
    }
}
