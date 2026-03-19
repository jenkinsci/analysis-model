package edu.hm.hafner.analysis.parser;

import java.io.Serial;

import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

/**
 * A parser for Terraform Lint (tflint) JSON output.
 *
 * @author Akash Manna
 * @see <a href="https://github.com/terraform-linters/tflint">tflint on GitHub</a>
 */
public class TerraformLintParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 6629181893482024873L;

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        if (jsonReport.has("issues")) {
            var issues = jsonReport.getJSONArray("issues");
            for (int i = 0; i < issues.length(); i++) {
                var issue = issues.getJSONObject(i);
                report.add(convertToIssue(issue, issueBuilder));
            }
        }
    }

    private Issue convertToIssue(final JSONObject jsonIssue, final IssueBuilder issueBuilder) {
        var rule = jsonIssue.optJSONObject("rule");
        if (rule != null) {
            issueBuilder.setType(rule.optString("name", "-"));
            issueBuilder.setSeverity(Severity.guessFromString(rule.optString("severity", "warning")));
        }
        else {
            issueBuilder.setType("-");
            issueBuilder.setSeverity(Severity.WARNING_NORMAL);
        }

        issueBuilder.setMessage(jsonIssue.optString("message", ""));

        var range = jsonIssue.optJSONObject("range");
        if (range != null) {
            issueBuilder.setFileName(range.optString("filename", ""));

            var start = range.optJSONObject("start");
            if (start != null) {
                int line = start.optInt("line", 0);
                if (line > 0) {
                    issueBuilder.setLineStart(line);
                }
                issueBuilder.setColumnStart(start.optInt("column", 0));
            }

            var end = range.optJSONObject("end");
            if (end != null) {
                int line = end.optInt("line", 0);
                if (line > 0) {
                    issueBuilder.setLineEnd(line);
                }
                issueBuilder.setColumnEnd(end.optInt("column", 0));
            }
        }

        return issueBuilder.buildAndClean();
    }
}
