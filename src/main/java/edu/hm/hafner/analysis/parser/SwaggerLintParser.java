package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.umd.cs.findbugs.annotations.CheckForNull;

import java.io.Serial;
import java.util.StringJoiner;

/**
 * A parser for Swagger Lint (swaggerlint) JSON output.
 *
 * @author Akash Manna
 * @see <a href="https://github.com/antonk52/swaggerlint">swaggerlint on GitHub</a>
 */
public class SwaggerLintParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 3141592653589793238L;

    private static final String NAME = "name";
    private static final String MSG = "msg";
    private static final String LOCATION = "location";

    @Override
    protected void parseJsonArray(final Report report, final JSONArray jsonReport, final IssueBuilder issueBuilder) {
        for (int i = 0; i < jsonReport.length(); i++) {
            var item = jsonReport.optJSONObject(i);
            if (item != null) {
                report.add(convertToIssue(item, issueBuilder));
            }
        }
    }

    private Issue convertToIssue(final JSONObject jsonIssue, final IssueBuilder issueBuilder) {
        issueBuilder.setType(jsonIssue.optString(NAME, "-"))
                .setMessage(jsonIssue.optString(MSG, ""))
                .setDescription(buildLocationDescription(jsonIssue.optJSONArray(LOCATION)));

        return issueBuilder.buildAndClean();
    }

    private String buildLocationDescription(@CheckForNull final JSONArray location) {
        if (location == null || location.isEmpty()) {
            return "";
        }

        var joiner = new StringJoiner(" &rsaquo; ");
        for (int i = 0; i < location.length(); i++) {
            joiner.add(String.valueOf(location.opt(i)));
        }
        return "<p><strong>Location:</strong> " + joiner + "</p>";
    }
}
