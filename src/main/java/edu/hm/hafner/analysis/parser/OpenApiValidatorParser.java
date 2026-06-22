package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;

import java.io.Serial;

/**
 * A parser for IBM OpenAPI Validator JSON output.
 *
 * <p>The IBM OpenAPI Validator ({@code lint-openapi}) can produce a stable JSON output using the
 * {@code --json} flag. This parser handles that output format.</p>
 *
 * @author Akash Manna
 * @see <a href="https://github.com/IBM/openapi-validator">IBM OpenAPI Validator on GitHub</a>
 */
public class OpenApiValidatorParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 8316235924573216195L;

    private static final String MESSAGE = "message";
    private static final String RULE = "rule";
    private static final String PATH = "path";
    private static final String LINE = "line";
    private static final String SEVERITY = "severity";
    private static final String FILE = "file";

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
        issueBuilder.setMessage(jsonIssue.optString(MESSAGE, ""))
                .setType(jsonIssue.optString(RULE, "-"))
                .setFileName(jsonIssue.optString(FILE, "-"))
                .setLineStart(jsonIssue.optInt(LINE, 0))
                .guessSeverity(jsonIssue.optString(SEVERITY, "warning"))
                .setDescription(buildJsonArrayDescription(jsonIssue.optJSONArray(PATH), "Path"));

        return issueBuilder.buildAndClean();
    }
}
