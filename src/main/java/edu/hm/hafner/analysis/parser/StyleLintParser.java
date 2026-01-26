package edu.hm.hafner.analysis.parser;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;

import java.io.Serial;

import static j2html.TagCreator.*;

/**
 * A parser for Stylelint JSON files.
 *
 * @author Ulrich Grave
 */
public class StyleLintParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 761110211350972670L;

    @Override
    protected void parseJsonArray(final Report report, final JSONArray jsonReport, final IssueBuilder issueBuilder) {
        for (Object issue : jsonReport) {
            if (issue instanceof JSONObject object) {
                parseWarnings(report, object, issueBuilder);
            }
        }
    }

    private void parseWarnings(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        issueBuilder.setFileName(jsonReport.getString("source"));
        var warnings = jsonReport.optJSONArray("warnings");
        for (Object issue : warnings) {
            if (issue instanceof JSONObject warning) {
                report.add(convertToIssue(warning, issueBuilder));
            }
        }
    }

    private Issue convertToIssue(final JSONObject jsonIssue, final IssueBuilder issueBuilder) {
        return issueBuilder
                .setType(jsonIssue.getString("rule"))
                .setMessage(jsonIssue.getString("text"))
                .setDescription(formatDescription(jsonIssue))
                .guessSeverity(jsonIssue.getString("severity"))
                .setLineStart(jsonIssue.getInt("line"))
                .setColumnStart(jsonIssue.getInt("column"))
                .setLineEnd(jsonIssue.getInt("endLine"))
                .setColumnEnd(jsonIssue.getInt("endColumn"))
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
        return "";
    }
}
