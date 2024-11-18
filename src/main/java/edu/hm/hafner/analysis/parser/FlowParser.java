package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.io.Reader;
import java.io.Serial;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

/**
 * A parser for Flow warnings.
 *
 * @author PCTao
 */
public class FlowParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 2379734578953758L;

    /** Determines whether the flow report has been passed or not. */
    private static final String FLOW_PASSED = "passed";
    /** The issues array. */
    private static final String ISSUES = "errors";

    private static final String FLOW_VERSION = "flowVersion";
    private static final String ISSUE_MESSAGE = "message";
    private static final String ISSUE_LEVEL = "level";
    private static final String ISSUE_KIND = "kind";

    private static final String MESSAGE_PATH = "path";
    private static final String MESSAGE_DESCR = "descr";
    private static final String MESSAGE_LINE_START = "line";
    private static final String MESSAGE_LINE_END = "endLine";
    private static final String MESSAGE_COLUMN_START = "start";
    private static final String MESSAGE_COLUMN_END = "end";

    private static final String LEVEL_ERROR = "error";
    private static final String LEVEL_WARNING = "warning";

    @Override
    public boolean accepts(final ReaderFactory readerFactory) {
        try (Reader reader = readerFactory.create()) {
            Object value = new JSONTokener(reader).nextValue();
            return value instanceof JSONObject && ((JSONObject) value).has(FLOW_VERSION);
        }
        catch (IOException | JSONException ignored) {
            return false;
        }
    }

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        if (!jsonReport.getBoolean(FLOW_PASSED)) {
            extractIssues(jsonReport.optJSONArray(ISSUES), report, issueBuilder);
        }
    }

    private void extractIssues(final JSONArray elements, final Report report,
            final IssueBuilder issueBuilder) {
        for (Object object : elements) {
            if (object instanceof JSONObject) {
                var issue = (JSONObject) object;
                findFirstMessage(issue).ifPresent(
                        jsonObject -> report.add(createIssueFromJsonObject(issue, jsonObject, issueBuilder)));
            }
        }
    }

    /**
     * Find the first message of issue.
     *
     * @param issue
     *         the object to find the message from.
     *
     * @return first message
     */
    private Optional<JSONObject> findFirstMessage(final JSONObject issue) {
        var message = issue.optJSONArray(ISSUE_MESSAGE);
        if (message == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(message.optJSONObject(0));
    }

    private Issue createIssueFromJsonObject(final JSONObject issue, final JSONObject message,
            final IssueBuilder issueBuilder) {
        return issueBuilder
                .setFileName(parseFileNameFromMessage(message))
                .setType(parseType(issue))
                .setSeverity(parseSeverity(issue))
                .setLineStart(parseLocFromMessage(message, MESSAGE_LINE_START))
                .setLineEnd(parseLocFromMessage(message, MESSAGE_LINE_END))
                .setColumnStart(parseLocFromMessage(message, MESSAGE_COLUMN_START))
                .setColumnEnd(parseLocFromMessage(message, MESSAGE_COLUMN_END))
                .setMessage(parseMessageFromMessage(message))
                .buildAndClean();
    }

    /**
     * Parse function for severity.
     *
     * @param issue
     *         the object to parse.
     *
     * @return the severity.
     */
    private Severity parseSeverity(final JSONObject issue) {
        String level = issue.optString(ISSUE_LEVEL);
        if (LEVEL_ERROR.equals(level)) {
            return Severity.ERROR;
        }
        if (LEVEL_WARNING.equals(level)) {
            return Severity.WARNING_NORMAL;
        }
        return Severity.WARNING_NORMAL;
    }

    /**
     * Parse function for type.
     *
     * @param issue
     *         the object to parse.
     *
     * @return the type.
     */
    private String parseType(final JSONObject issue) {
        return issue.optString(ISSUE_KIND);
    }

    /**
     * Parse function for filename from message.
     *
     * @param message
     *         the object to parse.
     *
     * @return the filename.
     */
    private String parseFileNameFromMessage(final JSONObject message) {
        return message.optString(MESSAGE_PATH);
    }

    /**
     * Parse function for message from message.
     *
     * @param message
     *         the object to parse.
     *
     * @return the message.
     */
    private String parseMessageFromMessage(final JSONObject message) {
        return message.optString(MESSAGE_DESCR);
    }

    /**
     * Parse function for locations from message.
     *
     * @param message
     *         the object to parse.
     * @param key
     *         the attribute name of location
     *
     * @return the attribute of location.
     */
    private Integer parseLocFromMessage(final JSONObject message, final String key) {
        return message.optInt(key);
    }
}
