package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.umd.cs.findbugs.annotations.CheckForNull;

import java.io.Serial;

/**
 * A parser for CFN-Lint JSON output.
 *
 * @author Akash Manna
 * @see <a href="https://github.com/aws-cloudformation/cfn-lint">cfn-lint on GitHub</a>
 */
public class CfnLintParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 8088265606805109371L;

    private static final String FILE_NAME = "Filename";
    private static final String LEVEL = "Level";
    private static final String MESSAGE = "Message";
    private static final String RULE = "Rule";
    private static final String RULE_ID = "Id";
    private static final String RULE_DESCRIPTION = "Description";
    private static final String LOCATION = "Location";
    private static final String START = "Start";
    private static final String END = "End";
    private static final String LINE_NUMBER = "LineNumber";
    private static final String COLUMN_NUMBER = "ColumnNumber";

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        if (jsonReport.has("matches")) {
            var issues = jsonReport.getJSONArray("matches");
            for (int i = 0; i < issues.length(); i++) {
                var issue = issues.getJSONObject(i);
                report.add(convertToIssue(issue, issueBuilder));
            }
        }
    }

    @Override
    protected void parseJsonArray(final Report report, final JSONArray jsonReport, final IssueBuilder issueBuilder) {
        for (int i = 0; i < jsonReport.length(); i++) {
            report.add(convertToIssue(jsonReport.getJSONObject(i), issueBuilder));
        }
    }

    private Issue convertToIssue(final JSONObject jsonIssue, final IssueBuilder issueBuilder) {
        issueBuilder.setFileName(jsonIssue.optString(FILE_NAME))
                .guessSeverity(jsonIssue.optString(LEVEL, "warning"))
                .setMessage(jsonIssue.optString(MESSAGE));

        applyRule(jsonIssue.optJSONObject(RULE), issueBuilder);
        applyLocation(jsonIssue.optJSONObject(LOCATION), issueBuilder);

        return issueBuilder.buildAndClean();
    }

    private void applyRule(@CheckForNull final JSONObject rule, final IssueBuilder issueBuilder) {
        if (rule == null) {
            issueBuilder.setType("-");
            return;
        }

        issueBuilder.setType(rule.optString(RULE_ID, "-"));
        var description = rule.optString(RULE_DESCRIPTION);
        if (!description.isBlank()) {
            issueBuilder.setDescription(description);
        }
    }

    private void applyLocation(@CheckForNull final JSONObject location, final IssueBuilder issueBuilder) {
        if (location == null) {
            return;
        }

        applyStart(location.optJSONObject(START), issueBuilder);
        applyEnd(location.optJSONObject(END), issueBuilder);
    }

    private void applyStart(@CheckForNull final JSONObject start, final IssueBuilder issueBuilder) {
        if (start == null) {
            return;
        }

        issueBuilder.setLineStart(start.optInt(LINE_NUMBER)).setColumnStart(start.optInt(COLUMN_NUMBER));
    }

    private void applyEnd(@CheckForNull final JSONObject end, final IssueBuilder issueBuilder) {
        if (end == null) {
            return;
        }

        issueBuilder.setLineEnd(end.optInt(LINE_NUMBER)).setColumnEnd(end.optInt(COLUMN_NUMBER));
    }
}
