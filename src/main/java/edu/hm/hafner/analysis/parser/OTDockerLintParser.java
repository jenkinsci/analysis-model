package edu.hm.hafner.analysis.parser;

import java.io.Serial;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

/**
 * A parser for ot-docker-linter json output.
 *
 * @author Abhishek Dubey
 * @see <a href="https://github.com/opstree/OT-Dockerlinter">ot-docker-linter</a>
 */
public class OTDockerLintParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 42L;

    @Override
    public boolean accepts(final ReaderFactory readerFactory) {
        return readerFactory.getFileName().endsWith(".json");
    }

    @Override
    protected void parseJsonArray(final Report report, final JSONArray jsonReport, final IssueBuilder issueBuilder) {
        for (Object entry : jsonReport) {
            if (entry instanceof JSONObject) {
                parseJsonObject(report, (JSONObject) entry, issueBuilder);
            }
        }
    }

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonIssue, final IssueBuilder issueBuilder) {
        if (jsonIssue.has("code")) {
            issueBuilder.setCategory(jsonIssue.getString("code"));
        }
        if (jsonIssue.has("severity")) {
            issueBuilder.setSeverity(Severity.guessFromString(jsonIssue.getString("severity")));
        }
        if (jsonIssue.has("line")) {
            issueBuilder.setLineStart(jsonIssue.getInt("line_number"));
        }
        if (jsonIssue.has("line")) {
            issueBuilder.setModuleName(jsonIssue.getString("line"));
        }
        if (jsonIssue.has("description")) {
            issueBuilder.setDescription(jsonIssue.getString("description"));
        }
        if (jsonIssue.has("message")) {
            issueBuilder.setMessage(jsonIssue.getString("message"));
        }
        if (jsonIssue.has("file")) {
            issueBuilder.setFileName(jsonIssue.getString("file"));
        }
        report.add(issueBuilder.buildAndClean());
    }
}
