package edu.hm.hafner.analysis;

import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

class IssuesInModifiedCodeMarkerTest {
    @Test
    void shouldNotMarkAnythingForEmptyMap() {
        var report = createReportWithTwoIssues();

        assertThat(report.get()).extracting(Issue::isPartOfModifiedCode).containsExactly(false, false);

        var marker = new IssuesInModifiedCodeMarker();
        marker.markIssuesInModifiedCode(report, Map.of());
        assertThat(report.get()).extracting(Issue::isPartOfModifiedCode).containsExactly(false, false);
    }

    @Test
    void shouldNotMarkIfFileMatchesButLinesNot() {
        var report = createReportWithTwoIssues();

        var marker = new IssuesInModifiedCodeMarker();

        marker.markIssuesInModifiedCode(report, Map.of("/part/of/modified/code.txt", Set.of(10)));
        assertThat(report.get()).extracting(Issue::isPartOfModifiedCode).containsExactly(false, false);
    }

    @Test
    void shouldNotMarkIfLineMatchesButFileNot() {
        var report = createReportWithTwoIssues();

        var marker = new IssuesInModifiedCodeMarker();

        marker.markIssuesInModifiedCode(report, Map.of("/wrong/path/code.txt", Set.of(12, 20)));
        assertThat(report.get()).extracting(Issue::isPartOfModifiedCode).containsExactly(false, false);
    }

    @Test
    void shouldMarkIssuesIfOneFileAndLinesMatch() {
        var report = createReportWithTwoIssues();

        var marker = new IssuesInModifiedCodeMarker();

        marker.markIssuesInModifiedCode(report, Map.of("/part/of/modified/code.txt", Set.of(10, 20)));
        assertThat(report.get()).extracting(Issue::isPartOfModifiedCode).containsExactly(true, false);
    }

    @Test
    void shouldMarkIssuesIfAllFilesAndLinesMatch() {
        var report = createReportWithTwoIssues();

        var marker = new IssuesInModifiedCodeMarker();

        marker.markIssuesInModifiedCode(report, Map.of(
                "/part/of/modified/code.txt", Set.of(12),
                "/part/of/additional/modified/code.txt", Set.of(20)));
        assertThat(report.get()).extracting(Issue::isPartOfModifiedCode).containsExactly(true, true);
    }

    @Test
    void shouldMarkIssuesIfFilesPrefixMatches() {
        var report = createReportWithTwoIssues();

        var marker = new IssuesInModifiedCodeMarker();

        marker.markIssuesInModifiedCode(report, Map.of(
                "modified/code.txt", Set.of(12, 20)));
        assertThat(report.get()).extracting(Issue::isPartOfModifiedCode).containsExactly(true, true);
    }

    @Test
    void shouldMarkIssuesIfFileNameUsesWindowsPath() {
        var report = createReportWithTwoIssues();

        var marker = new IssuesInModifiedCodeMarker();

        marker.markIssuesInModifiedCode(report, Map.of(
                "modified\\code.txt", Set.of(12, 20)));
        assertThat(report.get()).extracting(Issue::isPartOfModifiedCode).containsExactly(true, true);
    }

    private Report createReportWithTwoIssues() {
        var report = new Report();
        try (var builder = new IssueBuilder()) {
            builder.setLineStart(12).setLineEnd(20);
            report.add(builder.setFileName("/part/of/modified/code.txt").build());
            report.add(builder.setFileName("/part/of/additional/modified/code.txt").build());
        }
        return report;
    }
}
