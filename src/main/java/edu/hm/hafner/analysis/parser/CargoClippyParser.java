package edu.hm.hafner.analysis.parser;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.LookaheadParser;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.util.IntegerParser;
import edu.hm.hafner.util.LookaheadStream;
import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.Serial;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static j2html.TagCreator.*;

/**
 * A parser that will attempt to parser for `cargo clippy` warnings/errors/help statements.
 *
 * @author Mike Delaney
 */
public class CargoClippyParser extends LookaheadParser {
    @Serial
    private static final long serialVersionUID = -2677728927938443703L;

    /** First line in a cargo-clippy message should just be the level and summary of the issue. */
    private static final String CARGO_CLIPPY_REGEX_STRING = "^(?<level>.+):\\s+(?<summary>(?!.+generated [0-9]+ warning).+)";

    /**
     * Find the line that contains the offending file, start line number, and starting column number.
     */
    private static final Pattern CARGO_CLIPPY_FILE_PATTERN = Pattern
            .compile("^\\s+-->\\s(?<file>.+):(?<line>\\d+):(?<column>\\d+)");

    /** Find the lines that are the rustc recommendation on what action should be taken. */
    private static final Pattern CARGO_CLIPPY_REC_PATTERN = Pattern.compile("^(\\s+(\\d+\\s+)?)\\|(.+|\\n)");

    /** Find the line that is a note ine from the rust compiler. */
    private static final Pattern CARGO_CLIPPY_NOTE_PATTERN = Pattern
            .compile("^\\s+=\\snote:\\s`#\\[(?<level>.+)\\((?<category>.+)\\)]`.+");

    /** Find the line that is a help line from the rust compiler. */
    private static final Pattern CARGO_CLIPPY_HELP_PATTERN = Pattern.compile("^\\s+=\\shelp:(.+?)(?<url>http?s:.*)?");

    /** RegEx to determine if the current issue continues to the next line. */
    private static final String CARGO_CLIPP_CONTEXT_CONTINUES = "^(\\s+|help\\:|[0-9]+).+";

    /** Creates a new instance of {@link CargoClippyParser}. */
    public CargoClippyParser() {
        super(CARGO_CLIPPY_REGEX_STRING);
    }

    @Override
    protected Optional<Issue> createIssue(final Matcher matcher, final LookaheadStream lookahead,
            final IssueBuilder builder) {
        final var defaultSeverity = Severity.guessFromString(matcher.group("level").trim());

        var description = createRecommendationMessage(lookahead);
        description.setLevel(matcher.group("level"));
        description.setSummary(matcher.group("summary"));

        builder.setFileName(description.getFile())
                .setLineStart(description.getLine())
                .setSeverity(defaultSeverity)
                .setCategory(description.getCategory())
                .setColumnStart(description.getColumnStart())
                .setColumnEnd(description.getColumnEnd())
                .setDescription(description.getHelp())
                .setType(description.getLevel())
                .setMessage(description.getSummary());

        return builder.buildOptional();
    }

    /**
     * Look ahead and try to pull out the pertinent information.
     *
     * @param lookahead
     *         input stream
     *
     * @return the collected information about the fix recommendation.
     */
    private FileInformation createRecommendationMessage(final LookaheadStream lookahead) {
        var description = new StringBuilder();
        var fileInformation = new FileInformation();

        while (lookahead.hasNext(CARGO_CLIPP_CONTEXT_CONTINUES)) {
            var line = lookahead.next();

            var fileInfoMatcher = CARGO_CLIPPY_FILE_PATTERN.matcher(line);
            if (fileInfoMatcher.matches()) {
                fileInformation.setFileName(fileInfoMatcher.group("file"));
                fileInformation.setFileLine(IntegerParser.parseInt(fileInfoMatcher.group("line")));
                fileInformation.setColumnStart(IntegerParser.parseInt(fileInfoMatcher.group("column")));
            }
            else {
                var clippyRecommendationMatcher = CARGO_CLIPPY_REC_PATTERN.matcher(line);
                if (clippyRecommendationMatcher.matches()) {
                    description.append(line);
                    continue;
                }

                var clippyHelpMatcher = CARGO_CLIPPY_HELP_PATTERN.matcher(line);
                if (clippyHelpMatcher.matches()) {
                    fileInformation.setHelp(clippyHelpMatcher.group(1), clippyHelpMatcher.group(2));
                    continue;
                }

                var clippyNotePatcher = CARGO_CLIPPY_NOTE_PATTERN.matcher(line);
                if (clippyNotePatcher.matches()) {
                    fileInformation.setCategory(clippyNotePatcher.group(2));
                    fileInformation.setLevel(clippyNotePatcher.group(1));
                }
            }
        }

        if (description.toString().indexOf('^') != -1) {
            fileInformation.setColumnEnd(StringUtils.countMatches(description.toString(), '^')
                    + fileInformation.getColumnStart());
        }

        fileInformation.setRecommendation(description.toString());
        return fileInformation;
    }

    /** Class to return the additional content of a cargo-clippy message. */
    @SuppressWarnings("PMD.DataClass")
    private static final class FileInformation {
        private String fileName;
        private Integer fileLine;
        private String recommendation;
        private String category;
        private String level;
        private Integer columnStart;
        private Integer columnEnd;
        private String summary;
        private String help;

        FileInformation() {
            this.fileName = "";
            this.fileLine = 0;
            this.recommendation = "";
            this.category = "";
            this.level = "";
            this.columnStart = 0;
            this.columnEnd = 0;
            this.summary = "";
            this.help = "";
        }

        /**
         * Set the filename where the recommendation originated.
         *
         * @param fileName
         *         the filename of the recommendation.
         */
        @SuppressFBWarnings("NM")
        public void setFileName(final String fileName) {
            this.fileName = fileName;
        }

        /**
         * Get the filename.
         *
         * @return The filename (relative to the workspace)
         */
        public String getFile() {
            return this.fileName;
        }

        /**
         * Set the file line where the recommendation originated.
         *
         * @param fileLine
         *         The line number.
         */
        public void setFileLine(final Integer fileLine) {
            this.fileLine = fileLine;
        }

        /**
         * Get the file line.
         *
         * @return The line where the recommendation happened.
         */
        public Integer getLine() {
            return this.fileLine;
        }

        /**
         * Set the clippy recommendation.
         *
         * @param recommendation
         *         The recommendation string.
         */
        public void setRecommendation(final String recommendation) {
            this.recommendation = recommendation;
        }

        /**
         * Returns the current clippy recommendation.
         *
         * @return The recommendation.
         */
        public String getRecommendation() {
            return this.recommendation;
        }

        /**
         * Sets the category of the recommendation.
         *
         * @param category
         *         The category name.
         */
        public void setCategory(final String category) {
            this.category = category;
        }

        /**
         * Returns the category name.
         *
         * @return The category name.
         */
        public String getCategory() {
            return this.category;
        }

        /**
         * Set the level for the recommendation.
         *
         * @param level
         *         The component level.
         */
        public void setLevel(final String level) {
            this.level = level;
        }

        /**
         * Get the issue level.
         *
         * @return The current recommendation level.
         */
        public String getLevel() {
            return this.level;
        }

        /**
         * Get the column number where the recommendation.
         *
         * @param column
         *         The column number.
         */
        public void setColumnStart(final Integer column) {
            this.columnStart = column;
        }

        /**
         * Get the column where the recommendation occurred.
         *
         * @return The column.
         */
        public Integer getColumnStart() {
            return this.columnStart;
        }

        /**
         * Set column the end column for the current issue.
         *
         * @param column
         *         The value for set to notiate the endinging column.
         */
        public void setColumnEnd(final Integer column) {
            this.columnEnd = column;
        }

        /**
         * Get the end column of the current issue.
         *
         * @return The column number that ends the issue.
         */
        public Integer getColumnEnd() {
            return this.columnEnd;
        }

        /**
         * Set the recommendation summary.
         *
         * @param summary
         *         The summary itself.
         */
        public void setSummary(final String summary) {
            this.summary = summary;
        }

        /**
         * Get the recommendation summary.
         *
         * @return The summary.
         */
        public String getSummary() {
            return this.summary;
        }

        /**
         * Set the help context.
         *
         * @param text
         *         the help text
         * @param url
         *         the optional URL
         */
        public void setHelp(final String text, @CheckForNull final String url) {
            if (StringUtils.isBlank(url)) {
                help = text;
            }
            else {
                help = text + a().withHref(url).withText("cargo clippy documentation").render();
            }
        }

        /**
         * Get the help context.
         *
         * @return The help context.
         */
        public String getHelp() {
            return this.help;
        }
    }
}
