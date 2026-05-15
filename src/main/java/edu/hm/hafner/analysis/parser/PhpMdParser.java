package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

import java.io.Serial;

/**
 * A parser for PHP Mess Detector (PHPMD) JSON reports.
 *
 * @author Akash Manna
 * @see <a href="https://github.com/phpmd/phpmd">PHP Mess Detector on GitHub</a>
 * @see <a href="https://phpmd.org/">PHP Mess Detector</a>
 */
public class PhpMdParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final String FILES_TAG = "files";
    private static final String VIOLATIONS_TAG = "violations";
    private static final String NAME_TAG = "name";
    private static final String RULE_TAG = "rule";
    private static final String RULESET_TAG = "ruleset";
    private static final String MESSAGE_TAG = "message";
    private static final String BEGIN_LINE_TAG = "beginline";
    private static final String END_LINE_TAG = "endline";
    private static final String PRIORITY_TAG = "priority";

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        if (jsonReport.has(FILES_TAG)) {
            var files = jsonReport.getJSONArray(FILES_TAG);
            for (int i = 0; i < files.length(); i++) {
                var file = files.getJSONObject(i);
                parseFile(report, file, issueBuilder);
            }
        }
    }

    private void parseFile(final Report report, final JSONObject file, final IssueBuilder issueBuilder) {
        var fileName = file.optString(NAME_TAG, "-");
        if (file.has(VIOLATIONS_TAG)) {
            var violations = file.getJSONArray(VIOLATIONS_TAG);
            for (int i = 0; i < violations.length(); i++) {
                var violation = violations.getJSONObject(i);
                report.add(convertToIssue(violation, fileName, issueBuilder));
            }
        }
    }

    private Issue convertToIssue(final JSONObject violation, final String fileName, final IssueBuilder issueBuilder) {
        return issueBuilder
                .setFileName(fileName)
                .setType(violation.optString(RULE_TAG, "-"))
                .setCategory(violation.optString(RULESET_TAG, "-"))
                .setMessage(violation.optString(MESSAGE_TAG, ""))
                .setLineStart(violation.optInt(BEGIN_LINE_TAG, 0))
                .setLineEnd(violation.optInt(END_LINE_TAG, 0))
                .setSeverity(priorityToSeverity(violation.optInt(PRIORITY_TAG, 3)))
                .build();
    }

    private Severity priorityToSeverity(final int priority) {
        return switch (priority) {
            case 1 -> Severity.ERROR;
            case 2 -> Severity.WARNING_HIGH;
            case 3 -> Severity.WARNING_NORMAL;
            case 4 -> Severity.WARNING_NORMAL;
            case 5 -> Severity.WARNING_LOW;
            default -> Severity.WARNING_NORMAL;
        };
    }
}
