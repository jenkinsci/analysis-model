package edu.hm.hafner.analysis.parser;

import java.io.Serial;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * A parser for Gradle Lint JSON warnings.
 *
 * @author Akash Manna
 * @see <a href="https://github.com/nebula-plugins/gradle-lint-plugin">Gradle Lint Plugin</a>
 */
public class GradleLintParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    protected void parseJsonArray(final Report report, final JSONArray jsonReport, final IssueBuilder issueBuilder) {
        for (int i = 0; i < jsonReport.length(); i++) {
            JSONObject issue = jsonReport.optJSONObject(i);
            if (issue != null) {
                report.add(convertToIssue(issue, issueBuilder));
            }
        }
    }

    private Issue convertToIssue(@CheckForNull final JSONObject issue, final IssueBuilder issueBuilder) {
        if (issue == null) {
            return issueBuilder.buildAndClean();
        }

        return issueBuilder
                .setFileName(issue.optString("file"))
                .setLineStart(issue.optInt("line"))
                .setCategory(issue.optString("category"))
                .guessSeverity(issue.optString("severity"))
                .setMessage(issue.optString("message"))
                .buildAndClean();
    }
}
