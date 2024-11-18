package edu.hm.hafner.analysis.parser;

import java.io.Serial;
import java.util.Optional;
import java.util.stream.Stream;

import org.json.JSONObject;
import org.json.JSONTokener;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingCanceledException;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

/**
 * A parser for {@code rustc} compiler messages in the JSON format emitted by {@code cargo check --message-format
 * json}.
 *
 * @author Gary Tierney
 */
public class CargoCheckParser extends IssueParser {
    @Serial
    private static final long serialVersionUID = 7953467739178377581L;

    /** The {@link #REASON} associated with messages that have code analysis information. */
    private static final String ANALYSIS_MESSAGE_REASON = "compiler-message";

    /** Top-level key indicating the reason for a message to be emitted, we only care about compiler-message. */
    private static final String REASON = "reason";

    /** Top-level key containing the code analysis message. */
    private static final String MESSAGE = "message";

    /** Key for {@code message.code}, an object containing the message category. */
    private static final String MESSAGE_CODE = "code";

    /** Key for {@code message.code.code}, a string representation of the message category. */
    private static final String MESSAGE_CODE_CATEGORY = "code";

    /** Key for {@code message.rendered}, the rendered string representation of the message. */
    private static final String MESSAGE_RENDERED = "message";

    /** Key for {@code message.level}, the string representation of the message severity. */
    private static final String MESSAGE_LEVEL = "level";

    /** Key for {@code message.spans}, an array of message location information. */
    private static final String MESSAGE_SPANS = "spans";

    /** Key for {@code message.spans.is_primary}, a boolean indicating if this is the primary error location". */
    private static final String MESSAGE_SPAN_IS_PRIMARY = "is_primary";

    /** Key for {@code message.spans.file_name}, a relative path to the file the message was emitted for. */
    private static final String MESSAGE_SPAN_FILE_NAME = "file_name";

    /** Key for {@code message.spans.line_start}, the line number where the associated code starts. */
    private static final String MESSAGE_SPAN_LINE_START = "line_start";

    /** Key for {@code message.spans.line_end}, the line number where the associated code ends. */
    private static final String MESSAGE_SPAN_LINE_END = "line_end";

    /** Key for {@code message.spans.column_start}, the column number where the associated code starts. */
    private static final String MESSAGE_SPAN_COLUMN_START = "column_start";

    /** Key for {@code message.spans.column_end}, the column number where the associated code ends. */
    private static final String MESSAGE_SPAN_COLUMN_END = "column_end";

    @Override
    public Report parse(final ReaderFactory readerFactory) throws ParsingException, ParsingCanceledException {
        var report = new Report();

        try (Stream<String> lines = readerFactory.readStream(); IssueBuilder issueBuilder = new IssueBuilder()) {
            lines.map(line -> (JSONObject) new JSONTokener(line).nextValue())
                    .map(object -> extractIssue(object, issueBuilder))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(report::add);
        }

        return report;
    }

    /**
     * Extract the compiler message from a cargo event if any is present.
     *
     * @param object
     *         A cargo event that may contain a compiler message.
     * @param issueBuilder
     *         the issue builder to use
     *
     * @return a built {@link Issue} object if any was present.
     */
    private Optional<Issue> extractIssue(final JSONObject object, final IssueBuilder issueBuilder) {
        String reason = object.getString(REASON);

        if (!ANALYSIS_MESSAGE_REASON.equals(reason)) {
            return Optional.empty();
        }

        JSONObject message = object.getJSONObject(MESSAGE);

        if (message.isNull(MESSAGE_CODE)) {
            return Optional.empty();
        }

        JSONObject code = message.getJSONObject(MESSAGE_CODE);
        String category = code.getString(MESSAGE_CODE_CATEGORY);
        String renderedMessage = message.getString(MESSAGE_RENDERED);
        Severity severity = Severity.guessFromString(message.getString(MESSAGE_LEVEL));

        return parseDetails(message)
                .map(details -> issueBuilder
                        .setFileName(details.fileName)
                        .setLineStart(details.lineStart)
                        .setLineEnd(details.lineEnd)
                        .setColumnStart(details.columnStart)
                        .setColumnEnd(details.columnEnd)
                        .setCategory(category)
                        .setMessage(renderedMessage)
                        .setSeverity(severity)
                        .buildAndClean());
    }

    private Optional<CompilerMessageDetails> parseDetails(final JSONObject message) {
        var spans = message.getJSONArray(MESSAGE_SPANS);

        for (int index = 0; index < spans.length(); index++) {
            var span = spans.getJSONObject(index);

            if (span.getBoolean(MESSAGE_SPAN_IS_PRIMARY)) {
                String fileName = span.getString(MESSAGE_SPAN_FILE_NAME);
                int lineStart = span.getInt(MESSAGE_SPAN_LINE_START);
                int lineEnd = span.getInt(MESSAGE_SPAN_LINE_END);
                int columnStart = span.getInt(MESSAGE_SPAN_COLUMN_START);
                int columnEnd = span.getInt(MESSAGE_SPAN_COLUMN_END);

                return Optional.of(new CompilerMessageDetails(fileName, lineStart, lineEnd, columnStart, columnEnd));
            }
        }

        return Optional.empty();
    }

    /**
     * A simplified representation of a primary {@code span} object in the {@code message.spans} an array.
     */
    private static final class CompilerMessageDetails {
        private final String fileName;
        private final int lineStart;
        private final int lineEnd;
        private final int columnStart;
        private final int columnEnd;

        CompilerMessageDetails(final String fileName, final int lineStart, final int lineEnd, final int columnStart,
                final int columnEnd) {
            this.fileName = fileName;
            this.lineStart = lineStart;
            this.lineEnd = lineEnd;
            this.columnStart = columnStart;
            this.columnEnd = columnEnd;
        }
    }
}
