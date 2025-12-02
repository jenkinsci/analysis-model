package edu.hm.hafner.analysis;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import edu.hm.hafner.util.PathUtil;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static edu.hm.hafner.analysis.IssueTest.*;
import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link FileNameResolver}.
 *
 * @author Ullrich Hafner
 */
class FileNameResolverTest {
    private static final URI RESOURCE_FOLDER = getResourceFolder();
    private static final Path RESOURCE_FOLDER_PATH = Path.of(RESOURCE_FOLDER);
    private static final String RESOURCE_FOLDER_STRING = getResourcePath();

    private static final String ID = "ID";
    private static final String RELATIVE_FILE = "relative.txt";
    private static final char SLASH = '/';

    /**
     * Ensures that illegal file names are processed without problems. Afterwards, the path name should be unchanged.
     *
     * @param fileName
     *         the file name to check
     */
    @ParameterizedTest(name = "[{index}] Illegal filename = {0}")
    @ValueSource(strings = {"/does/not/exist", "!<>$&/&(", "\0 Null-Byte", "C:/!<>$&/&( \0", "/!<>$&/&( \0"})
    @DisplayName("Should not change path on errors")
    void shouldReturnFallbackOnError(final String fileName) {
        var report = createIssuesSingleton(fileName, new IssueBuilder());

        resolvePaths(report, RESOURCE_FOLDER_PATH);

        assertThat(report.iterator()).toIterable().containsExactly(report.get(0));
        assertThatOneFileIsUnresolved(report);
    }

    private void resolvePaths(final Report report, final Path resourceFolderPath) {
        resolvePaths(report, resourceFolderPath, f -> false);
    }

    private void resolvePaths(final Report report, final Path resourceFolderPath,
            final Predicate<String> skipFileNamePredicate) {
        new FileNameResolver().run(report, resourceFolderPath.toString(), skipFileNamePredicate);
    }

    @Test
    @DisplayName("Should skip processing if there are no issues")
    void shouldDoNothingIfNoIssuesPresent() {
        var report = new Report();

        resolvePaths(report, RESOURCE_FOLDER_PATH);

        assertThat(report).hasSize(0);
        assertThat(report.getInfoMessages()).containsExactly(FileNameResolver.NOTHING_TO_DO);
    }

    @Test
    @DisplayName("Should set path if the relative file name exists")
    void shouldSetPath() {
        var report = new Report();

        try (var builder = new IssueBuilder()) {
            report.add(builder.setFileName(RELATIVE_FILE).build());
        }

        resolvePaths(report, RESOURCE_FOLDER_PATH);

        assertThat(report).hasSize(1);
        assertThat(report.get(0)).hasFileName(RELATIVE_FILE).hasPath(RESOURCE_FOLDER_STRING);

        assertThat(report.getInfoMessages()).hasSize(1);
        assertThat(report.getInfoMessages().get(0)).as("Files: "
                + report.stream().map(Issue::getFileName).collect(Collectors.joining(", ")))
                .contains("1 found", "0 not found");
        assertThat(report.getErrorMessages()).isEmpty();
    }

    @Test
    @DisplayName("Should not set path if the relative file name doe not exist")
    void shouldNotSetPath() {
        var report = new Report();

        try (var builder = new IssueBuilder()) {
            report.add(builder.setFileName("not here").build());
        }

        resolvePaths(report, RESOURCE_FOLDER_PATH);

        assertThat(report).hasSize(1);
        assertThat(report.get(0)).hasFileName("not here").hasPath(UNDEFINED);

        assertThat(report.getInfoMessages()).hasSize(1);
        assertThat(report.getInfoMessages().get(0)).as("Files: "
                + report.stream().map(Issue::getFileName).collect(Collectors.joining(", ")))
                .contains("0 found", "1 not found");
        assertThat(report.getErrorMessages()).isEmpty();
    }

    @Test
    @DisplayName("Should skip existing absolute paths")
    void shouldNotTouchAbsolutePathOrEmptyPath() {
        var report = new Report();

        try (var builder = new IssueBuilder()) {
            report.add(builder.setFileName("").build());
            report.add(builder.setFileName("skip").build());
            report.add(builder.setFileName(RELATIVE_FILE).build());
            report.add(builder.setDirectory(RESOURCE_FOLDER_STRING)
                    .setFileName("relative.txt").build());
            report.add(builder.setDirectory(RESOURCE_FOLDER_STRING)
                    .setFileName(normalize("../../hafner/analysis/normalized.txt"))
                    .build());
            report.add(builder.setDirectory(RESOURCE_FOLDER_STRING)
                    .setFileName("not-existing.txt")
                    .build());
            report.add(builder.setDirectory("/")
                    .setFileName("not-existing-parent.txt")
                    .build());
        }

        resolvePaths(report, RESOURCE_FOLDER_PATH, "skip"::equals);

        assertThat(report).hasSize(7);
        assertThat(report.get(0)).as("Issue with no file name")
                .hasFileName("-");
        assertThat(report.get(1)).as("Issue with path name resolution skipped")
                .hasFileName("skip");

        assertThat(report.get(2)).as("Issue with relative file name")
                .hasFileName(RELATIVE_FILE);

        assertThat(report.get(3)).as("Issue with absolute file name (normalized)")
                .hasFileName(RELATIVE_FILE);
        assertThat(report.get(4)).as("Issue with absolute file name (not normalized)")
                .hasFileName("normalized.txt");
        assertThat(report.get(5)).as("Issue with absolute file name (not existing)")
                .hasFileName(RESOURCE_FOLDER_STRING + "/not-existing.txt");
        assertThat(report.get(6)).as("Issue with absolute file name (not existing parent path)")
                .hasFileName("/not-existing-parent.txt");

        assertThat(report.getInfoMessages()).hasSize(1);
        assertThat(report.getInfoMessages().get(0)).as("Files: "
                + report.stream().map(Issue::getFileName).collect(Collectors.joining(", ")))
                .contains("3 found", "2 not found");
        assertThat(report.getErrorMessages()).isEmpty();
    }

    @ParameterizedTest(name = "[{index}] Relative file name = {0}")
    @ValueSource(strings = {"../analysis/relative.txt", "./relative.txt", "../../hafner/analysis/relative.txt"})
    @DisplayName("Should normalize different relative paths to the same file (file name is relative)")
    void shouldResolveRelativePath(final String fileName) {
        try (var builder = new IssueBuilder()) {
            var report = createIssuesSingleton(fileName, builder.setOrigin(ID));
            resolvePaths(report, RESOURCE_FOLDER_PATH);

            assertThatFileResolvesToRelativeFile(report, fileName);
        }
    }

    @ParameterizedTest(name = "[{index}] Relative filename = {0}")
    @ValueSource(strings = {"../analysis/relative.txt", "./relative.txt", "../../hafner/analysis/relative.txt"})
    @DisplayName("Should normalize different relative paths to the same file (file name is absolute)")
    void shouldNormalizePaths(final String fileName) {
        try (var issueBuilder = new IssueBuilder()) {
            var issue = issueBuilder
                    .setDirectory(RESOURCE_FOLDER_STRING)
                    .setFileName(normalize(fileName))
                    .build();
            var report = new Report();
            report.add(issue);

            resolvePaths(report, Path.of(RESOURCE_FOLDER));

            assertThatFileResolvesToRelativeFile(report, fileName);
        }
    }

    private void assertThatFileResolvesToRelativeFile(final Report report, final String fileName) {
        var description = "Resolving file '%s'".formatted(normalize(fileName));
        assertThat(report.get(0).getFileName()).as(description).isEqualTo(RELATIVE_FILE);
        assertThat(report.getErrorMessages()).as(description).isEmpty();
        assertThat(report.getInfoMessages()).as(description).hasSize(1);
        assertThat(report.getInfoMessages().get(0)).as(description).contains("1 found");
    }

    @Test
    @DisplayName("Should replace relative issue path with absolute path in relative path of workspace")
    void shouldResolveRelativePathInWorkspaceSubFolder() {
        try (var builder = new IssueBuilder()) {
            Report report;

            var fileName = "child.txt";
            report = createIssuesSingleton(fileName, builder.setOrigin(ID));

            resolvePaths(report, RESOURCE_FOLDER_PATH);

            assertThatOneFileIsUnresolved(report);

            report = createIssuesSingleton(fileName, builder.setOrigin(ID));

            resolvePaths(report, RESOURCE_FOLDER_PATH.resolve("child"));

            assertThat(report.get(0).getFileName()).isEqualTo("child.txt");
            assertThat(report.getErrorMessages()).isEmpty();
            assertThat(report.getInfoMessages()).hasSize(1);
            assertThat(report.getInfoMessages().get(0)).contains("1 found");
        }
    }

    private void assertThatOneFileIsUnresolved(final Report report) {
        assertThat(report.getInfoMessages()).hasSize(1);
        assertThat(report.getInfoMessages().get(0)).contains("1 not found");
    }

    @Test
    @DisplayName("Should remap paths from source to target prefix")
    void shouldRemapPathPrefixes() {
        var dockerPath = "/app/src";
        var dockerFilePath = dockerPath + "/relative.txt";

        try (var builder = new IssueBuilder()) {
            var report = new Report();
            report.add(builder.setFileName(dockerFilePath).build());

            new FileNameResolver().run(report, RESOURCE_FOLDER_STRING, f -> false, dockerPath, RESOURCE_FOLDER_STRING);

            assertThat(report).hasSize(1);
            assertThat(report.get(0)).hasFileName(RELATIVE_FILE).hasPath(RESOURCE_FOLDER_STRING);
            assertThat(report.getInfoMessages()).hasSize(2); 
            assertThat(report.getInfoMessages().get(0)).contains("remapping paths from");
            assertThat(report.getInfoMessages().get(1)).contains("1 found");
        }
    }

    @Test
    @DisplayName("Should not remap paths when source prefix is empty")
    void shouldNotRemapWhenSourcePrefixEmpty() {
        try (var builder = new IssueBuilder()) {
            var report = new Report();
            report.add(builder.setFileName(RELATIVE_FILE).build());

            new FileNameResolver().run(report, RESOURCE_FOLDER_STRING, f -> false, "", "/some/path");

            assertThat(report).hasSize(1);
            assertThat(report.get(0)).hasFileName(RELATIVE_FILE).hasPath(RESOURCE_FOLDER_STRING);
            assertThat(report.getInfoMessages()).hasSize(1); 
            assertThat(report.getInfoMessages().get(0)).contains("1 found");
        }
    }

    @Test
    @DisplayName("Should not remap paths when target prefix is empty")
    void shouldNotRemapWhenTargetPrefixEmpty() {
        try (var builder = new IssueBuilder()) {
            var report = new Report();
            report.add(builder.setFileName(RELATIVE_FILE).build());

            new FileNameResolver().run(report, RESOURCE_FOLDER_STRING, f -> false, "/some/path", "");

            assertThat(report).hasSize(1);
            assertThat(report.get(0)).hasFileName(RELATIVE_FILE).hasPath(RESOURCE_FOLDER_STRING);
            assertThat(report.getInfoMessages()).hasSize(1); // only resolving, no remapping
            assertThat(report.getInfoMessages().get(0)).contains("1 found");
        }
    }

    @Test
    @DisplayName("Should only remap paths that start with source prefix")
    void shouldOnlyRemapMatchingPaths() {
        var dockerPath = "/docker/src";
        var otherPath = "/other/path";

        try (var builder = new IssueBuilder()) {
            var report = new Report();
            report.add(builder.setFileName(dockerPath + "/relative.txt").build());
            report.add(builder.setFileName(otherPath + "/other.txt").build());
            report.add(builder.setFileName(RELATIVE_FILE).build());

            new FileNameResolver().run(report, RESOURCE_FOLDER_STRING, f -> false, dockerPath, RESOURCE_FOLDER_STRING);

            assertThat(report).hasSize(3);
            assertThat(report.get(0)).hasFileName(RELATIVE_FILE).hasPath(RESOURCE_FOLDER_STRING);
            assertThat(report.get(1)).hasFileName(otherPath + "/other.txt");
            assertThat(report.get(2)).hasFileName(RELATIVE_FILE).hasPath(RESOURCE_FOLDER_STRING);

            assertThat(report.getInfoMessages()).hasSize(2); 
            assertThat(report.getInfoMessages().get(0)).contains("remapping paths");
        }
    }

    private static String getResourcePath() {
        var workspace = RESOURCE_FOLDER.getPath();
        if (isWindows() && workspace.charAt(0) == SLASH) {
            workspace = workspace.substring(1);
        }
        return new PathUtil().getAbsolutePath(workspace);
    }

    /**
     * Returns whether the OS under test is Windows or Unix.
     *
     * @return {@code true} if the OS is Windows, {@code false} otherwise
     */
    private static boolean isWindows() {
        return File.pathSeparatorChar == ';';
    }

    private Report createIssuesSingleton(final String fileName, final IssueBuilder issueBuilder) {
        var report = new Report();
        var issue = issueBuilder.setFileName(fileName).build();
        report.add(issue);
        return report;
    }

    private String normalize(final String fileName) {
        return fileName.replace("/", File.separator);
    }

    private static URI getResourceFolder() {
        try {
            var resource = FileNameResolverTest.class.getResource(RELATIVE_FILE);
            var fileName = resource.toExternalForm();
            return new URL(fileName.replace(RELATIVE_FILE, "")).toURI();
        }
        catch (MalformedURLException | URISyntaxException e) {
            throw new AssertionError(e);
        }
    }
}
