package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

import static j2html.TagCreator.*;

/**
 * Parser for Veracode Pipeline Scanner (pipeline-scan) tool.
 *
 * @author Juri Duval
 */
public class VeracodePipelineScannerParser extends JsonIssueParser {
    private static final String VALUE_NOT_SET = "-";
    private static final int VERACODE_LOW_THRESHOLD = 2;
    private static final int VERACODE_HIGH_THRESHOLD = 4;
    private static final long serialVersionUID = 1L;

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        JSONArray findings = jsonReport.optJSONArray("findings");
        if (findings != null) {
            parseFindings(report, findings, issueBuilder);
        }
    }

    private void parseFindings(final Report report, final JSONArray resources, final IssueBuilder issueBuilder) {
        for (int i = 0; i < resources.length(); i++) {
            final Object item = resources.get(i);
            if (item instanceof JSONObject) {
                final JSONObject finding = (JSONObject) item;
                report.add(convertToIssue(finding, issueBuilder));
            }
        }
    }

    private Issue convertToIssue(final JSONObject finding, final IssueBuilder issueBuilder) {
        final String fileName = getSourceFileField(finding, "file", VALUE_NOT_SET);
        final int line = getSourceFileField(finding, "line", 0);
        final int severity = finding.getInt("severity");
        final String title = finding.getString("title");
        final String issueType = finding.getString("issue_type");
        return issueBuilder
                .setFileName(fileName)
                .setLineStart(line)
                .setSeverity(mapSeverity(severity))
                .setMessage(title)
                .setType(issueType)
                .setDescription(formatDescription(fileName, finding))
                .buildAndClean();
    }

    /**
     * Retrieve source file values in a null safe manner.
     *
     * Veracode has nested json objects representing a source file ( files -> source_file -> values) for which we need
     * to do null checking.
     *
     * @param finding
     *         contains top level vulnerability information.
     * @param key
     *         used to retrieve values from nested source file.
     * @param altValue
     *         in case there is a missing source file.
     * @param <T>
     *         type of the return value.
     *
     * @return field value of the source file.
     */
    private <T> T getSourceFileField(final JSONObject finding, final String key, final T altValue) {
        final JSONObject files = finding.optJSONObject("files");
        if (files != null) {
            final JSONObject sourceFile = files.optJSONObject("source_file");
            if (sourceFile != null) {
                return (T) sourceFile.get(key);
            }
        }

        return altValue;
    }

    /**
     * Map veracode severity to analysis-model severity.
     *
     * See <a href="https://docs.veracode.com/r/review_severity_exploitability">Veracode severity table</a> for details
     * on scoring.
     *
     * @param severity
     *         as an integer from 0 to 5 (inclusive).
     *
     * @return {@link Severity}
     */
    private Severity mapSeverity(final int severity) {
        if (severity <= VERACODE_LOW_THRESHOLD) {
            return Severity.WARNING_LOW;
        }
        else if (severity >= VERACODE_HIGH_THRESHOLD) {
            return Severity.WARNING_HIGH;
        }
        else {
            return Severity.WARNING_NORMAL;
        }

    }

    private String formatDescription(final String fileName, final JSONObject finding) {
        final String cweId = finding.optString("cwe_id", VALUE_NOT_SET);
        final String flawLink = finding.optString("flaw_details_link", VALUE_NOT_SET);
        final String severity = finding.optString("severity", VALUE_NOT_SET);
        final String displayHtml = finding.optString("display_text", VALUE_NOT_SET);
        return join(div(b("Resource: "), text(fileName)),
                div(b("CWE Id: "), text(cweId)),
                div(b("Flaw Details: "), text(flawLink)),
                div(b("Severity: "), text(severity)),
                p(displayHtml)).render();
    }

}
