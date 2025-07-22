package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import j2html.tags.ContainerTag;
import j2html.tags.DomContent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serial;

import static j2html.TagCreator.*;

/**
 * A parser for EsLint JSON files.
 *
 * @author Ulrich Grave
 */
public class EsLintParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = -2455926786089596397L;

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        var messages = jsonReport.optJSONArray("messages");
        if (messages != null) {
            var filePath = jsonReport.getString("filePath");
            issueBuilder.setFileName(filePath);
            parseMessages(report, messages, issueBuilder);
        }
    }

    @Override
    protected void parseJsonArray(final Report report, final JSONArray jsonReport, final IssueBuilder issueBuilder) {
        for (Object issue : jsonReport) {
            if (issue instanceof JSONObject object) {
                parseJsonObject(report, object, issueBuilder);
            }
        }
    }

    private void parseMessages(final Report report, final JSONArray jsonReport, final IssueBuilder issueBuilder) {
        for (Object issue : jsonReport) {
            if (issue instanceof JSONObject message) {
                report.add(convertToIssue(message, issueBuilder));
            }
        }
    }

    private Issue convertToIssue(final JSONObject jsonIssue, final IssueBuilder issueBuilder) {
        return issueBuilder
                .setType(jsonIssue.getString("ruleId"))
                .setMessage(jsonIssue.getString("message"))
                .setDescription(formatDescription(jsonIssue))
                .setSeverity(toSeverity(jsonIssue.getInt("severity")))
                .setLineStart(jsonIssue.getInt("line"))
                .setColumnStart(jsonIssue.getInt("column"))
                .setLineEnd(jsonIssue.getInt("endLine"))
                .setColumnEnd(jsonIssue.getInt("endColumn"))
                .build();
    }

    private static String formatDescription(final JSONObject jsonIssue) {
        JSONArray suggestions = jsonIssue.optJSONArray("suggestions");
        if (suggestions != null) {
            ContainerTag ul = ul();
            for (Object issue : suggestions) {
                if (issue instanceof JSONObject suggestion) {
                    ul.with(li(formatSuggestion(suggestion)));
                }
            }
            return join(p(text("Suggestions:")), ul).render();
        }
        return "";
    }

    private static DomContent formatSuggestion(final JSONObject suggestion) {
        var desc = suggestion.getString("desc");
        var fix = suggestion.getJSONObject("fix");
        var text = fix.getString("text");
        return join(p(desc), code(text));
    }

    private Severity toSeverity(final int severity) {
        if (severity == 1) {
            return Severity.WARNING_NORMAL;
        }
        else {
            return Severity.ERROR;
        }
    }
}