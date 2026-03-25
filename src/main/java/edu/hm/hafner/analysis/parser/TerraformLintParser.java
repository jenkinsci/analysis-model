package edu.hm.hafner.analysis.parser;

import java.io.Serial;

import org.json.JSONObject;

import edu.umd.cs.findbugs.annotations.CheckForNull;
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

    private Issue convertToIssue(@CheckForNull final JSONObject jsonIssue, final IssueBuilder issueBuilder) {
        applyRule(jsonIssue.optJSONObject("rule"), issueBuilder);

        issueBuilder.setMessage(jsonIssue.optString("message"));

        applyRange(jsonIssue.optJSONObject("range"), issueBuilder);

        return issueBuilder.buildAndClean();
    }

    private void applyRule(@CheckForNull final JSONObject rule, final IssueBuilder issueBuilder) {
        if (rule == null) {
            issueBuilder.setType("-");
            issueBuilder.setSeverity(Severity.WARNING_NORMAL);
            return;
        }

        issueBuilder.setType(rule.optString("name"));
        issueBuilder.setSeverity(Severity.guessFromString(rule.optString("severity", "warning")));
    }

    private void applyRange(@CheckForNull final JSONObject range, final IssueBuilder issueBuilder) {
        if (range == null) {
            return;
        }

        issueBuilder.setFileName(range.optString("filename"));
        applyStart(range.optJSONObject("start"), issueBuilder);
        applyEnd(range.optJSONObject("end"), issueBuilder);
    }

    private void applyStart(@CheckForNull final JSONObject start, final IssueBuilder issueBuilder) {
        if (start == null) {
            return;
        }

        setLineStart(start.optInt("line"), issueBuilder);
        issueBuilder.setColumnStart(start.optInt("column", 0));
    }

    private void applyEnd(@CheckForNull final JSONObject end, final IssueBuilder issueBuilder) {
        if (end == null) {
            return;
        }

        setLineEnd(end.optInt("line", 0), issueBuilder);
        issueBuilder.setColumnEnd(end.optInt("column", 0));
    }

    private void setLineStart(final int line, final IssueBuilder issueBuilder) {
        if (line > 0) {
            issueBuilder.setLineStart(line);
        }
    }

    private void setLineEnd(final int line, final IssueBuilder issueBuilder) {
        if (line > 0) {
            issueBuilder.setLineEnd(line);
        }
    }
}
