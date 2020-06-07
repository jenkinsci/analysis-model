package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.assertions.Assertions.*;
import static java.util.Collections.*;

class IssueDifferenceBuilderTest {

    private IssueDifferenceBuilder createSut() {
        return new IssueDifferenceBuilder();
    }

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
                .setReference("reference");
        return builder.build();
    }

    @Test
    public void Test0() {
        IssueDifferenceBuilder sut = createSut();
        Report current = new Report().add(createIssue("message", "fingerprint"));
        Report reference = new Report();
        String id = "Id";

        sut.setCurrentIssues(current).setReferenceIssues(reference).setReferenceId(id);
        IssueDifference result = sut.build();

        assertThat(result.getNewIssues()).isEqualTo(current);
        assertThat(result.getFixedIssues()).isEqualTo(reference);
    }
}