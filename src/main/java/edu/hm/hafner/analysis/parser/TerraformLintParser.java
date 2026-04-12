package edu.hm.hafner.analysis.parser;

import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.umd.cs.findbugs.annotations.CheckForNull;

import java.io.Serial;

/**
 * A parser for Terraform Lint (tflint) JSON output.
 *
 * @author Akash Manna
 * @see <a href="https://github.com/terraform-linters/tflint">tflint on GitHub</a>
 */
public class TerraformLintParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 6629181893482024873L;
    private static final String START = "start";
    private static final String END = "end";
    private static final String LINE = "line";
    private static final String COLUMN = "column";

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
        if (jsonIssue == null) {
            return issueBuilder.buildAndClean();
        }

        applyRule(jsonIssue.optJSONObject("rule"), issueBuilder);

        issueBuilder.setMessage(jsonIssue.optString("message"));

        applyRange(jsonIssue.optJSONObject("range"), issueBuilder);

        return issueBuilder.buildAndClean();
    }

    private void applyRule(@CheckForNull final JSONObject rule, final IssueBuilder issueBuilder) {
        if (rule == null) {
            issueBuilder.setType("-").setSeverity(Severity.WARNING_NORMAL);
            return;
        }

        issueBuilder.setType(rule.optString("name"))
                .guessSeverity(rule.optString("severity", "warning"));
    }

    private void applyRange(@CheckForNull final JSONObject range, final IssueBuilder issueBuilder) {
        if (range == null) {
            return;
        }

        issueBuilder.setFileName(range.optString("filename"));
        applyStart(range.optJSONObject(START), issueBuilder, LINE, COLUMN);
        applyEnd(range.optJSONObject(END), issueBuilder, LINE, COLUMN);
    }
}
