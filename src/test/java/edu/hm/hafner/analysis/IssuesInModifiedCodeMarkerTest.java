package edu.hm.hafner.analysis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

class IssuesInModifiedCodeMarkerTest {
    private static final String TO_STRING_UNMODIFIED = "code.txt(12,0): -: : ";
    private static final String TO_STRING_MODIFIED = "*code.txt(12,0): -: : ";

    @Test
    void shouldNotMarkAnythingForEmptyMap() {
        var report = createReportWithTwoIssues();

        assertThatModifiedCodeMarkers(report).containsExactly(false, false);

        var marker = new IssuesInModifiedCodeMarker();
        marker.markIssuesInModifiedCode(report, Map.of());
        assertThatModifiedCodeMarkers(report).containsExactly(false, false);
        assertThatIssuesToString(report).containsExactly(TO_STRING_UNMODIFIED, TO_STRING_UNMODIFIED);
    }

    @Test
    void shouldNotMarkIfFileMatchesButLinesNot() {
        var report = createReportWithTwoIssues();

        var marker = new IssuesInModifiedCodeMarker();
        marker.markIssuesInModifiedCode(report, Map.of("/part/of/modified/code.txt", Set.of(10)));

        assertThatModifiedCodeMarkers(report).containsExactly(false, false);
        assertThatIssuesToString(report).containsExactly(TO_STRING_UNMODIFIED, TO_STRING_UNMODIFIED);
    }

    @Test
    void shouldNotMarkIfLineMatchesButFileNot() {
        var report = createReportWithTwoIssues();

        var marker = new IssuesInModifiedCodeMarker();
        marker.markIssuesInModifiedCode(report, Map.of("/wrong/path/code.txt", Set.of(12, 20)));

        assertThatModifiedCodeMarkers(report).containsExactly(false, false);
        assertThatIssuesToString(report).containsExactly(TO_STRING_UNMODIFIED, TO_STRING_UNMODIFIED);
    }

    @Test
    void shouldMarkIssuesIfOneFileAndLinesMatch() {
        var report = createReportWithTwoIssues();

        var marker = new IssuesInModifiedCodeMarker();
        marker.markIssuesInModifiedCode(report, Map.of("/part/of/modified/code.txt", Set.of(10, 20)));

        assertThatModifiedCodeMarkers(report).containsExactly(true, false);
        assertThatIssuesToString(report).containsExactly(TO_STRING_MODIFIED, TO_STRING_UNMODIFIED);
    }

    @Test
    void shouldMarkIssuesIfAllFilesAndLinesMatch() {
        var report = createReportWithTwoIssues();

        var marker = new IssuesInModifiedCodeMarker();
        marker.markIssuesInModifiedCode(report, Map.of(
                "/part/of/modified/code.txt", Set.of(12),
                "/part/of/additional/modified/code.txt", Set.of(20)));

        assertThatModifiedCodeMarkers(report).containsExactly(true, true);
        assertThatIssuesToString(report).containsExactly(TO_STRING_MODIFIED, TO_STRING_MODIFIED);
    }

    @Test
    void shouldMarkIssuesIfFilesPrefixMatches() {
        var report = createReportWithTwoIssues();

        var marker = new IssuesInModifiedCodeMarker();
        marker.markIssuesInModifiedCode(report, Map.of("modified/code.txt", Set.of(12, 20)));

        assertThatModifiedCodeMarkers(report).containsExactly(true, true);
        assertThatIssuesToString(report).containsExactly(TO_STRING_MODIFIED, TO_STRING_MODIFIED);
    }

    @Test
    void shouldMarkIssuesIfFileNameUsesWindowsPath() {
        var report = createReportWithTwoIssues();

        var marker = new IssuesInModifiedCodeMarker();
        marker.markIssuesInModifiedCode(report, Map.of("modified\\code.txt", Set.of(12, 20)));

        assertThatModifiedCodeMarkers(report).containsExactly(true, true);
        assertThatIssuesToString(report).containsExactly(TO_STRING_MODIFIED, TO_STRING_MODIFIED);
    }

    @Test
    void shouldNotMarkAnythingForEmptySetOfFiles() {
        var report = createReportWithTwoIssues();

        assertThatModifiedCodeMarkers(report).containsExactly(false, false);

        var marker = new IssuesInModifiedCodeMarker();
        marker.markIssuesInModifiedFiles(report, Set.of());
        assertThatModifiedCodeMarkers(report).containsExactly(false, false);
        assertThatIssuesToString(report).containsExactly(TO_STRING_UNMODIFIED, TO_STRING_UNMODIFIED);
    }

    @Test
    void shouldNotMarkIfFileDoesNotMatch() {
        var report = createReportWithTwoIssues();

        var marker = new IssuesInModifiedCodeMarker();
        marker.markIssuesInModifiedFiles(report, Set.of("/wrong/path/code.txt"));

        assertThatModifiedCodeMarkers(report).containsExactly(false, false);
        assertThatIssuesToString(report).containsExactly(TO_STRING_UNMODIFIED, TO_STRING_UNMODIFIED);
    }

    @Test
    void shouldMarkIssuesIfOneFileMatches() {
        var report = createReportWithTwoIssues();

        var marker = new IssuesInModifiedCodeMarker();
        marker.markIssuesInModifiedFiles(report, Set.of("/part/of/modified/code.txt"));

        assertThatModifiedCodeMarkers(report).containsExactly(true, false);
        assertThatIssuesToString(report).containsExactly(TO_STRING_MODIFIED, TO_STRING_UNMODIFIED);
    }

    @Test
    void shouldMarkIssuesIfAllFilesMatch() {
        var report = createReportWithTwoIssues();

        var marker = new IssuesInModifiedCodeMarker();
        marker.markIssuesInModifiedFiles(report, Set.of(
                "/part/of/modified/code.txt",
                "/part/of/additional/modified/code.txt"));

        assertThatModifiedCodeMarkers(report).containsExactly(true, true);
        assertThatIssuesToString(report).containsExactly(TO_STRING_MODIFIED, TO_STRING_MODIFIED);
    }

    @Test
    void shouldMarkIssuesIfFilesPrefixMatchesForFiles() {
        var report = createReportWithTwoIssues();

        var marker = new IssuesInModifiedCodeMarker();
        marker.markIssuesInModifiedFiles(report, Set.of("modified/code.txt", "additional/modified/code.txt"));

        assertThatModifiedCodeMarkers(report).containsExactly(true, true);
        assertThatIssuesToString(report).containsExactly(TO_STRING_MODIFIED, TO_STRING_MODIFIED);
    }

    @Test
    void shouldMarkIssuesIfFileNameUsesWindowsPathForFiles() {
        var report = createReportWithTwoIssues();

        var marker = new IssuesInModifiedCodeMarker();
        marker.markIssuesInModifiedFiles(report, Set.of("modified\\code.txt", "additional\\modified\\code.txt"));

        assertThatModifiedCodeMarkers(report).containsExactly(true, true);
        assertThatIssuesToString(report).containsExactly(TO_STRING_MODIFIED, TO_STRING_MODIFIED);
    }

    @Test
    void shouldMarkAllIssuesInFileRegardlessOfLineNumbers() {
        var report = new Report();
        try (var builder = new IssueBuilder()) {
            builder.setFileName("/part/of/modified/code.txt");
            report.add(builder.setLineStart(1).setLineEnd(5).build());
            report.add(builder.setLineStart(10).setLineEnd(15).build());
            report.add(builder.setLineStart(100).setLineEnd(200).build());
            builder.setFileName("/other/file.txt");
            report.add(builder.setLineStart(1).setLineEnd(5).build());
        }

        var marker = new IssuesInModifiedCodeMarker();
        marker.markIssuesInModifiedFiles(report, Set.of("modified/code.txt"));

        assertThatModifiedCodeMarkers(report).containsExactly(true, true, true, false);
    }

    private AbstractListAssert<?, List<? extends String>, String, ObjectAssert<String>> assertThatIssuesToString(
            final Report report) {
        return assertThat(report.get()).extracting(Issue::toString);
    }

    private AbstractListAssert<?, List<? extends Boolean>, Boolean, ObjectAssert<Boolean>> assertThatModifiedCodeMarkers(
            final Report report) {
        return assertThat(report.get()).extracting(Issue::isPartOfModifiedCode);
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
