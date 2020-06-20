package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.IOException;
import java.io.Reader;

/**
 * A parser for Flow warnings.
 *
 * @author PCTao
 */
public class FlowParser extends IssueParser {
    private static final long serialVersionUID = 2379734578953758L;

    /** The flow report is passed or not */
    private static final String FLOW_PASSED = "passed";
    /** The issues array. */
    private static final String ISSUES = "errors";
    /** flowVersion attribute. */
    private static final String FLOW_VERSION = "flowVersion";

    /** issue.message attribute. */
    private static final String ISSUE_MESSAGE = "message";
    /** issue.level attribute. */
    private static final String ISSUE_LEVEL = "level";
    /** issue.kind attribute. */
    private static final String ISSUE_KIND = "kind";

    /** message.path attribute. */
    private static final String MESSAGE_PATH = "path";
    /** message.descr attribute. */
    private static final String MESSAGE_DESCR = "descr";
    /** message.line attribute. */
    private static final String MESSAGE_LINE_START = "line";
    /** message.endLine attribute. */
    private static final String MESSAGE_LINE_END = "endLine";
    /** message.start attribute. */
    private static final String MESSAGE_COLUMN_START = "start";
    /** message.end attribute. */
    private static final String MESSAGE_COLUMN_END = "end";

    /** level value: error. */
    private static final String LEVEL_ERROR = "error";
    /** level value: warning. */
    private static final String LEVEL_WARNING = "warning";


    @Override
    public boolean accepts(final ReaderFactory readerFactory) {
        try (Reader reader = readerFactory.create()) {
            return accepts((JSONObject) new JSONTokener(reader).nextValue());
        }
        catch (IOException ignored) {
            return false;
        }
    }

    protected boolean accepts(final JSONObject object) {
        return object.has(FLOW_VERSION);
    }

    @Override
    public Report parse(final ReaderFactory readerFactory) throws ParsingException {
        try (Reader reader = readerFactory.create()) {
            JSONObject jsonReport = (JSONObject) new JSONTokener(reader).nextValue();

            if (!jsonReport.getBoolean(FLOW_PASSED)) {
                return extractIssues(jsonReport.optJSONArray(ISSUES));
            }

            return new Report();
        }
        catch (IOException e) {
            throw new ParsingException(e);
        }
    }

    private Report extractIssues(final JSONArray elements) {
        Report report = new Report();
        for (Object object: elements) {
            if (object instanceof JSONObject) {
                JSONObject issue = (JSONObject) object;
                JSONObject message = findFirstMessage(issue);
                if (message != null) {
                    report.add(createIssueFromJsonObject(issue, message));
                }
            }
        }
        return report;
    }

    /**
     * Find the first message of issue.
     *
     * @param issue the object to find the message from.
     *
     * @return first message
     */
    private JSONObject findFirstMessage(final JSONObject issue) {
        JSONArray message = issue.optJSONArray(ISSUE_MESSAGE);
        if (message == null) {
            return null;
        }

        return message.optJSONObject(0);
    }

    private Issue createIssueFromJsonObject(final JSONObject issue, final JSONObject message) {
        return new IssueBuilder()
                .setFileName(parseFileNameFromMessage(message))
                .setType(parseType(issue))
                .setSeverity(parseSeverity(issue))
                .setLineStart(parseLocFromMessage(message, MESSAGE_LINE_START))
                .setLineEnd(parseLocFromMessage(message, MESSAGE_LINE_END))
                .setColumnStart(parseLocFromMessage(message, MESSAGE_COLUMN_START))
                .setColumnEnd(parseLocFromMessage(message, MESSAGE_COLUMN_END))
                .setMessage(parseMessageFromMessage(message))
                .build();
    }

    /**
     * Parse function for severity.
     *
     * @param issue the object to parse.
     *
     * @return the severity.
     */
    private Severity parseSeverity(final JSONObject issue) {
        String str = issue.optString(ISSUE_LEVEL, null);
        Severity severity = Severity.WARNING_NORMAL;

        if (str != null) {
            if (LEVEL_ERROR.equals(str)) {
                severity = Severity.ERROR;
            } else if (LEVEL_WARNING.equals(str)) {
                severity = Severity.WARNING_NORMAL;
            }
        }
        return severity;
    }

    /**
     * Parse function for type.
     *
     * @param issue the object to parse.
     *
     * @return the type.
     */
    private String parseType(final JSONObject issue) {
        return issue.optString(ISSUE_KIND, "");
    }

    /**
     * Parse function for filename from message.
     *
     * @param message the object to parse.
     *
     * @return the filename.
     */
    private String parseFileNameFromMessage(final JSONObject message) {
        return message.optString(MESSAGE_PATH, "");
    }

    /**
     * Parse function for message from message.
     *
     * @param message the object to parse.
     *
     * @return the message.
     */
    private String parseMessageFromMessage(final JSONObject message) {
        return message.optString(MESSAGE_DESCR, "");
    }

    /**
     * Parse function for locations from message.
     *
     * @param message the object to parse.
     * @param key the attribute name of location
     *
     * @return the attribute of location.
     */
    private Integer parseLocFromMessage(final JSONObject message, final String key) {
        return message.optInt(key);
    }
}
