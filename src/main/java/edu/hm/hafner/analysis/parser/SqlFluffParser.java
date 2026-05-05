package edu.hm.hafner.analysis.parser;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;

import java.io.Serial;

/**
 * Parser for SQLFluff JSON reports.
 *
 * @author Akash Manna
 * @see <a href="https://www.sqlfluff.com/">SQLFluff</a>
 * @see <a href="https://github.com/sqlfluff/sqlfluff">SQLFluff on GitHub</a>
 */
public class SqlFluffParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = -1234567890123456789L;

    private static final String VIOLATIONS = "violations";
    private static final String RULE_CODE = "rule_code";
    private static final String RULE_NAME = "rule_name";
    private static final String RULE_DESCRIPTION = "rule_description";
    private static final String DESCRIPTION = "description";
    private static final String PATH = "path";
    private static final String LINE_NO = "line_no";
    private static final String END_LINE_NO = "end_line_no";
    private static final String LINE_POS = "line_pos";
    private static final String END_LINE_POS = "end_line_pos";

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        if (jsonReport.has(VIOLATIONS)) {
            var violations = jsonReport.getJSONArray(VIOLATIONS);
            parseViolationsArray(report, violations, issueBuilder);
        }
    }

    @Override
    protected void parseJsonArray(final Report report, final JSONArray jsonReport, final IssueBuilder issueBuilder) {
        parseViolationsArray(report, jsonReport, issueBuilder);
    }

    private void parseViolationsArray(final Report report, final JSONArray array, final IssueBuilder issueBuilder) {
        for (int i = 0; i < array.length(); i++) {
            var violation = array.getJSONObject(i);
            report.add(convertToIssue(violation, issueBuilder));
        }
    }

    private Issue convertToIssue(final JSONObject violation, final IssueBuilder issueBuilder) {
        var ruleCode = violation.optString(RULE_CODE, "-");
        var ruleName = violation.optString(RULE_NAME, "");
        var ruleDescription = violation.optString(RULE_DESCRIPTION, "");
        var description = violation.optString(DESCRIPTION, "");
        var fileName = violation.optString(PATH, "-");

        issueBuilder.setType(ruleCode);

        var message = buildMessage(description, ruleName, ruleDescription);
        issueBuilder.setMessage(message);

        issueBuilder.setFileName(fileName);

        if (violation.has(LINE_NO)) {
            issueBuilder.setLineStart(violation.optInt(LINE_NO));
        }

        if (violation.has(END_LINE_NO)) {
            issueBuilder.setLineEnd(violation.optInt(END_LINE_NO));
        }

        if (violation.has(LINE_POS)) {
            issueBuilder.setColumnStart(violation.optInt(LINE_POS));
        }

        if (violation.has(END_LINE_POS)) {
            issueBuilder.setColumnEnd(violation.optInt(END_LINE_POS));
        }

        if (StringUtils.isNotBlank(ruleName)) {
            issueBuilder.setCategory(ruleName);
        }

        issueBuilder.guessSeverity("warning");

        return issueBuilder.buildAndClean();
    }

    /**
     * Builds the issue message from description, rule name, and rule description.
     *
     * @param description 
     *         the violation description
     * @param ruleName 
     *         the rule name
     * @param ruleDescription 
     *         the rule description
     * @return the combined message
     */
    private String buildMessage(final String description, final String ruleName, final String ruleDescription) {
        if (StringUtils.isNotBlank(description)) {
            return description;
        }
        if (StringUtils.isNotBlank(ruleName)) {
            return ruleName;
        }
        return ruleDescription;
    }
}
