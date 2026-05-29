package edu.hm.hafner.analysis.parser;

import java.io.Serial;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Report;

/**
 * Parser for OpenSCAP vulnerability/compliance reports in JSON format.
 *
 * @author Akash Manna
 * @see <a href="https://github.com/OpenSCAP/openscap">OpenSCAP on GitHub</a>
 * @see <a href="https://www.open-scap.org/resources/documentation/">OpenSCAP Documentation</a>
 */
public class OpenSCAPParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = -1234567890123456789L;

    private static final String TEST_RESULTS_TAG = "test_results";
    private static final String RULES_TAG = "rules";
    private static final String RESULT_TAG = "result";
    private static final String RULE_TAG = "rule";
    private static final String RULE_ID_TAG = "id";
    private static final String TITLE_TAG = "title";
    private static final String DESCRIPTION_TAG = "description";
    private static final String SEVERITY_TAG = "severity";
    private static final String EVIDENCE_TAG = "evidence";
    private static final String FILE_TAG = "file";

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        JSONArray testResults = jsonReport.optJSONArray(TEST_RESULTS_TAG);
        if (testResults != null) {
            parseTestResults(report, testResults, issueBuilder);
        }

        JSONArray rules = jsonReport.optJSONArray(RULES_TAG);
        if (rules != null) {
            parseRules(report, rules, issueBuilder);
        }
    }

    private void parseTestResults(final Report report, final JSONArray testResults, final IssueBuilder issueBuilder) {
        for (int i = 0; i < testResults.length(); i++) {
            JSONObject result = testResults.getJSONObject(i);
            
            String resultStatus = result.optString(RESULT_TAG, "");
            if (shouldReportResult(resultStatus)) {
                report.add(createIssueFromTestResult(result, issueBuilder));
            }
        }
    }

    private void parseRules(final Report report, final JSONArray rules, final IssueBuilder issueBuilder) {
        for (int i = 0; i < rules.length(); i++) {
            JSONObject rule = rules.getJSONObject(i);
            
            String resultStatus = rule.optString(RESULT_TAG, "");
            if (shouldReportResult(resultStatus)) {
                report.add(createIssueFromRule(rule, issueBuilder));
            }
        }
    }

    private boolean shouldReportResult(final String resultStatus) {
        return resultStatus.equalsIgnoreCase("fail") || resultStatus.equalsIgnoreCase("error");
    }

    private Issue createIssueFromTestResult(final JSONObject testResult, final IssueBuilder issueBuilder) {
        JSONObject rule = testResult.optJSONObject(RULE_TAG);
        
        String ruleId = "-";
        String title = "Unknown";
        String description = "";
        
        if (rule != null) {
            ruleId = rule.optString(RULE_ID_TAG, "-");
            title = rule.optString(TITLE_TAG, "Unknown");
            description = rule.optString(DESCRIPTION_TAG, "");
        }
        
        String severity = testResult.optString(SEVERITY_TAG, "medium");
        String evidence = testResult.optString(EVIDENCE_TAG, "");
        String file = testResult.optString(FILE_TAG, "-");
        String resultStatus = testResult.optString(RESULT_TAG, "");
        
        String message = title;
        if (!evidence.isEmpty()) {
            message = title + " - " + evidence;
        }
        
        String descriptionText = description;
        if (!evidence.isEmpty() && description.isEmpty()) {
            descriptionText = evidence;
        } 
        else if (!evidence.isEmpty()) {
            descriptionText = description + "\n\nEvidence: " + evidence;
        }
        
        return issueBuilder
                .setFileName(file)
                .setType(ruleId)
                .setMessage(message)
                .setDescription(descriptionText)
                .setCategory(resultStatus)
                .guessSeverity(severity)
                .buildAndClean();
    }

    private Issue createIssueFromRule(final JSONObject rule, final IssueBuilder issueBuilder) {
        String ruleId = rule.optString(RULE_ID_TAG, "-");
        String title = rule.optString(TITLE_TAG, "Unknown");
        String description = rule.optString(DESCRIPTION_TAG, "");
        String severity = rule.optString(SEVERITY_TAG, "medium");
        String file = rule.optString(FILE_TAG, "-");
        String resultStatus = rule.optString(RESULT_TAG, "");
        
        return issueBuilder
                .setFileName(file)
                .setType(ruleId)
                .setMessage(title)
                .setDescription(description)
                .setCategory(resultStatus)
                .guessSeverity(severity)
                .buildAndClean();
    }
}
