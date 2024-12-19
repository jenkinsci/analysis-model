package edu.hm.hafner.analysis.parser;

import java.io.Serial;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

/**
 * Parser for vale reports.
 */
public class ValeParser extends JsonIssueParser {
    // Constants for JSON keys
    private static final String CHECK = "Check";
    private static final String LINE_KEY = "Line";
    private static final String LINK_KEY = "Link";
    private static final String MESSAGE_KEY = "Message";
    private static final String SPAN_KEY = "Span";
    private static final String SEVERITY_KEY = "Severity";

    @Serial
    private static final long serialVersionUID = -4034450901865555017L;

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        JSONArray fileNames = jsonReport.names();
        for (Object o : fileNames) {
            if (o instanceof String f) {
                JSONArray jsonArray = jsonReport.getJSONArray(f);
                for (Object data : jsonArray) {
                    if (data instanceof JSONObject dataObject) {
                        report.add(createIssue(issueBuilder, f, dataObject));
                    }
                }
            }
        }
    }

    private Issue createIssue(final IssueBuilder issueBuilder, final String fileName, final JSONObject data) {
        String checker = data.getString(CHECK);
        String message = data.getString(MESSAGE_KEY);
        String severity = data.getString(SEVERITY_KEY);
        String link = data.getString(LINK_KEY);
        int line = data.getInt(LINE_KEY);
        JSONArray span = data.getJSONArray(SPAN_KEY);
        int startColumn = span.getInt(0);
        int endColumn = span.getInt(1);
        final Severity analysisSeverity;
        switch (severity) {
            case "error":
                analysisSeverity = Severity.ERROR;
                break;
            case "warning":
                analysisSeverity = Severity.WARNING_NORMAL;
                break;
            case "suggestion":
                analysisSeverity = Severity.WARNING_LOW;
                break;
            default:
                analysisSeverity = Severity.WARNING_NORMAL;
                break;
        }
        return issueBuilder.setFileName(fileName).setDescription(checker)
                .setMessage(message).setSeverity(analysisSeverity)
                .setReference(link)
                .setLineStart(line)
                .setLineEnd(line)
                .setColumnStart(startColumn).setColumnEnd(endColumn)
                .buildAndClean();
    }
}
