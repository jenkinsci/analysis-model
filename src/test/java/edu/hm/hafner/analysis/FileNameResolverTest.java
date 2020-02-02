package edu.hm.hafner.analysis;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import edu.hm.hafner.util.PathUtil;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link FileNameResolver}.
 *
 * @author Ullrich Hafner
 */
@SuppressFBWarnings("DMI")
class FileNameResolverTest {
    private static final URI RESOURCE_FOLDER = getResourceFolder();
    private static final Path RESOURCE_FOLDER_PATH = Paths.get(RESOURCE_FOLDER);
    private static final String RESOURCE_FOLDER_STRING = getResourcePath();

    private static final String ID = "ID";
    private static final String RELATIVE_FILE = "relative.txt";
    private static final char SLASH = '/';

    /**
     * Ensures that illegal file names are processed without problems. Afterwards, the path name should be unchanged.
     */
    @ParameterizedTest(name = "[{index}] Illegal filename = {0}")
    @ValueSource(strings = {"/does/not/exist", "!<>$&/&(", "\0 Null-Byte", "C:/!<>$&/&( \0", "/!<>$&/&( \0"})
    @DisplayName("Should not change path on errors")
    void shouldReturnFallbackOnError(final String fileName) {
        Report report = createIssuesSingleton(fileName, new IssueBuilder());

        resolvePaths(report, RESOURCE_FOLDER_PATH);

        assertThat(report.iterator()).toIterable().containsExactly(report.get(0));
        assertThatOneFileIsUnresolved(report);
    }

    private FileNameResolver resolvePaths(final Report report, final Path resourceFolderPath) {
        return resolvePaths(report, resourceFolderPath, f -> false);
    }

    private FileNameResolver resolvePaths(final Report report, final Path workspace,
            final Predicate<String> skipFileNamePredicate) {
        FileNameResolver absolutePathGenerator = new FileNameResolver();
        absolutePathGenerator.run(report, workspace.toString(), skipFileNamePredicate);
        return absolutePathGenerator;
    }

    @Test
    @DisplayName("Should skip processing if there are no issues")
    void shouldDoNothingIfNoIssuesPresent() {
        Report report = new Report();

        resolvePaths(report, RESOURCE_FOLDER_PATH);

        assertThat(report).hasSize(0);
        assertThat(report.getInfoMessages()).containsExactly(FileNameResolver.NOTHING_TO_DO);
    }

    @Test
    @DisplayName("Should skip existing absolute paths")
    void shouldNotTouchAbsolutePathOrEmptyPath() {
        Report report = new Report();

        IssueBuilder builder = new IssueBuilder();

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
                .contains("2 changed", "3 unchanged");
        assertThat(report.getErrorMessages()).isEmpty();
    }

    @ParameterizedTest(name = "[{index}] Relative file name = {0}")
    @ValueSource(strings = {"../analysis/relative.txt", "./relative.txt", "../../hafner/analysis/relative.txt"})
    @DisplayName("Should normalize different relative paths to the same file (file name is relative)")
    void shouldResolveRelativePath(final String fileName) {
        IssueBuilder builder = new IssueBuilder();

        Report report = createIssuesSingleton(fileName, builder.setOrigin(ID));

        resolvePaths(report, RESOURCE_FOLDER_PATH);

        assertThatFileResolvesToRelativeFile(report, fileName);
    }

    @ParameterizedTest(name = "[{index}] Relative filename = {0}")
    @ValueSource(strings = {"../analysis/relative.txt", "./relative.txt", "../../hafner/analysis/relative.txt"})
    @DisplayName("Should normalize different relative paths to the same file (file name is absolute)")
    void shouldNormalizePaths(final String fileName) {
        Report report = new Report();

        Issue issue = new IssueBuilder()
                .setDirectory(RESOURCE_FOLDER_STRING)
                .setFileName(normalize(fileName))
                .build();
        report.add(issue);

        resolvePaths(report, Paths.get(RESOURCE_FOLDER));

        assertThatFileResolvesToRelativeFile(report, fileName);
    }

    private void assertThatFileResolvesToRelativeFile(final Report report, final String fileName) {
        String description = String.format("Resolving file '%s'", normalize(fileName));
        assertThat(report.get(0).getFileName()).as(description).isEqualTo(RELATIVE_FILE);
        assertThat(report.getErrorMessages()).as(description).isEmpty();
        assertThat(report.getInfoMessages()).as(description).hasSize(1);
        assertThat(report.getInfoMessages().get(0)).as(description).contains("1 changed");
    }

    @Test
    @DisplayName("Should replace relative issue path with absolute path in relative path of workspace")
    void shouldResolveRelativePathInWorkspaceSubFolder() {
        IssueBuilder builder = new IssueBuilder();

        String fileName = "child.txt";
        Report report = createIssuesSingleton(fileName, builder.setOrigin(ID));

        resolvePaths(report, RESOURCE_FOLDER_PATH);

        assertThatOneFileIsUnresolved(report);

        report = createIssuesSingleton(fileName, builder.setOrigin(ID));

        resolvePaths(report, RESOURCE_FOLDER_PATH.resolve("child"));

        assertThat(report.get(0).getFileName()).isEqualTo("child.txt");
        assertThat(report.getErrorMessages()).isEmpty();
        assertThat(report.getInfoMessages()).hasSize(1);
        assertThat(report.getInfoMessages().get(0)).contains("1 unchanged");
    }

    private void assertThatOneFileIsUnresolved(final Report report) {
        assertThat(report.getInfoMessages()).hasSize(1);
        assertThat(report.getInfoMessages().get(0)).contains("1 unchanged");
    }

    private static String getResourcePath() {
        String workspace = RESOURCE_FOLDER.getPath();
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
        Report report = new Report();
        Issue issue = issueBuilder.setFileName(fileName).build();
        report.add(issue);
        return report;
    }

    private String normalize(final String fileName) {
        return fileName.replace("/", File.separator);
    }

    private static URI getResourceFolder() {
        try {
            URL resource = FileNameResolverTest.class.getResource(RELATIVE_FILE);
            String fileName = resource.toExternalForm();
            return new URL(fileName.replace(RELATIVE_FILE, "")).toURI();
        }
        catch (MalformedURLException | URISyntaxException e) {
            throw new AssertionError(e);
        }
    }
}
