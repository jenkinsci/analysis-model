package edu.hm.hafner.analysis.parser;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

import j2html.tags.ContainerTag;
import j2html.tags.DomContent;
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

    // Parser for eslint --format json-with-metadata
    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        var results = jsonReport.getJSONArray("results");
        for (Object result : results) {
            if (result instanceof JSONObject object) {
                parseMessages(report, object, issueBuilder);
            }
        }
    }

    // Parser for eslint --format json
    @Override
    protected void parseJsonArray(final Report report, final JSONArray jsonReport, final IssueBuilder issueBuilder) {
        for (Object issue : jsonReport) {
            if (issue instanceof JSONObject object) {
                parseMessages(report, object, issueBuilder);
            }
        }
    }

    private void parseMessages(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        issueBuilder.setFileName(jsonReport.getString("filePath"));
        var messages = jsonReport.getJSONArray("messages");
        for (Object issue : messages) {
            if (issue instanceof JSONObject message) {
                report.add(convertToIssue(message, issueBuilder));
            }
        }
    }

    private Issue convertToIssue(final JSONObject jsonIssue, final IssueBuilder issueBuilder) {
        return issueBuilder
                .setType(jsonIssue.optString("ruleId", "None"))
                .setMessage(jsonIssue.optString("message", "n/a"))
                .setDescription(formatDescription(jsonIssue))
                .setSeverity(toSeverity(jsonIssue.optInt("severity")))
                .setLineStart(jsonIssue.optInt("line"))
                .setColumnStart(jsonIssue.optInt("column"))
                .setLineEnd(jsonIssue.optInt("endLine"))
                .setColumnEnd(jsonIssue.optInt("endColumn"))
                .build();
    }

    private static String formatDescription(final JSONObject jsonIssue) {
        var fix = jsonIssue.optJSONObject("fix");
        if (fix != null) {
            var text = fix.getString("text");
            if (StringUtils.isNotBlank(text)) {
                return join(p("Fix:"), pre().with(code(text))).render();
            }
        }
        var suggestions = jsonIssue.optJSONArray("suggestions");
        if (suggestions != null) {
            ContainerTag ul = ul();
            for (Object issue : suggestions) {
                if (issue instanceof JSONObject suggestion) {
                    ul.with(li(formatSuggestion(suggestion)));
                }
            }
            return join(p("Suggestions:"), ul).render();
        }
        return "";
    }

    private static DomContent formatSuggestion(final JSONObject suggestion) {
        var desc = suggestion.getString("desc");
        var fix = suggestion.getJSONObject("fix");
        var text = fix.getString("text");
        return StringUtils.isBlank(text) ? p(desc) : join(p(desc), pre().with(code(text)));
    }

    private static Severity toSeverity(final int severity) {
        if (severity == 1) {
            return Severity.WARNING_NORMAL;
        }
        else {
            return Severity.ERROR;
        }
    }
}
