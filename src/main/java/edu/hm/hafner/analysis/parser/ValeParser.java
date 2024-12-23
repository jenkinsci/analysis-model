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
        JSONArray span = data.getJSONArray(SPAN_KEY);
        int line = data.getInt(LINE_KEY);
        return issueBuilder.setFileName(fileName)
                .setDescription(data.getString(CHECK))
                .setMessage(data.getString(MESSAGE_KEY))
                .setSeverity(Severity.guessFromString(data.getString(SEVERITY_KEY)))
                .setReference(data.getString(LINK_KEY))
                .setLineStart(line)
                .setLineEnd(line)
                .setColumnStart(span.getInt(0))
                .setColumnEnd(span.getInt(1))
                .buildAndClean();
    }
}
