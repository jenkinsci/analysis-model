package edu.hm.hafner.analysis.parser;

import java.io.Serial;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

/**
 * Parser for ProtoLint JSON reports.
 *
 * <p>The recommended report format is JSON. The JSON report contains more information (affected rule, severity,
 * basedir) as the plaintext format. For full feature set please use protoLint &gt;= 0.50.2.
 *
 * <p>We still support plaintext reports and JSON reports generated with protoLint &lt; 0.50.2 as well.
 *
 * @author github@profhenry.de
 * @see <a href="https://github.com/yoheimuta/protolint">https://github.com/yoheimuta/protolint</a>
 */
public class ProtoLintJsonParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 573706779074579673L;

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        var basedir = jsonReport.optString("basedir");
        var results = jsonReport.optJSONArray("lints", new JSONArray(0));

        parseResults(report, basedir, results, issueBuilder);
    }

    private void parseResults(final Report report, final String basedir, final JSONArray jsonReport, final IssueBuilder issueBuilder) {
        for (int i = 0; i < jsonReport.length(); i++) {
            var finding = (JSONObject) jsonReport.get(i);
            report.add(convertToIssue(basedir, finding, issueBuilder));
        }
    }

    private Issue convertToIssue(final String basedir, final JSONObject finding, final IssueBuilder issueBuilder) {
        // The filename is always relative to the working dir the protoLint process was started with.
        // In order to get the absolute filename we need to prepend the basedir which is available with protoLint >= 0.50.2
        var filename = finding.getString("filename");
        if (!basedir.isEmpty()) {
            filename = basedir + "/" + filename;
        }
        return issueBuilder.setFileName(filename)
                .setLineStart(finding.getInt("line"))
                .setColumnStart(finding.getInt("column"))
                .setMessage(finding.getString("message"))
                .setSeverity(mapSeverity(finding.optString("severity")))
                .setType(finding.getString("rule"))
                .buildAndClean();
    }

    private Severity mapSeverity(final String aProtoLintSeverity) {
        // ProtoLint knows the following severities
        // https://github.com/yoheimuta/protolint/blob/master/linter/rule/rule.go#L9
        // which can be mapped with the provided mapping method
        return Severity.guessFromString(aProtoLintSeverity);
    }
}
