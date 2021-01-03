package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.io.Reader;
import java.text.MessageFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingCanceledException;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * <p>
 * Parser for reports of aquasec trivy container vulnerability scanner.
 * </p>
 * <p>
 * <strong>Usage: </strong>trivy image -f json -o results.json golang:1.12-alpine
 * </p>
 *
 * @author Thomas Fürer - tfuerer.javanet@gmail.com
 */
public class TrivyParser extends IssueParser {
    private static final String VALUE_NOT_SET = "-";
    private static final String TRIVY_VULNERABILITY_LEVEL_TAG_CRITICAL = "critcal";
    private static final String TRIVY_VULNERABILITY_LEVEL_TAG_HIGH = "high";
    private static final String TRIVY_VULNERABILITY_LEVEL_TAG_MEDIUM = "medium";
    private static final String TRIVY_VULNERABILITY_LEVEL_TAG_LOW = "low";
    private static final long serialVersionUID = 1L;

    @Override
    public Report parse(final ReaderFactory readerFactory) throws ParsingException, ParsingCanceledException {
        final Report report = new Report();

        try (Reader reader = readerFactory.create()) {
            final JSONArray jsonReport = (JSONArray)new JSONTokener(reader).nextValue();

            for (int i = 0; i < jsonReport.length(); i++) {
                final JSONObject component = (JSONObject)jsonReport.get(i);
                if (!component.isNull("Vulnerabilities")) {
                    final JSONArray vulnerabilities = component.getJSONArray("Vulnerabilities");
                    for (Object vulnerability : vulnerabilities) {
                        report.add(convertToIssue((JSONObject) vulnerability));
                    }
                }
            }
        }
        catch (IOException | JSONException | ClassCastException e) {
            throw new ParsingException(e);
        }

        return report;
    }

    private Issue convertToIssue(final JSONObject vulnerability) {
        return new IssueBuilder().setFileName(vulnerability.optString("PkgName", VALUE_NOT_SET))
                .setCategory(vulnerability.optString("SeveritySource", VALUE_NOT_SET))
                .setSeverity(mapSeverity(vulnerability.optString("Severity", "UNKNOWN")))
                .setType(vulnerability.optString("VulnerabilityID", VALUE_NOT_SET))
                .setMessage(vulnerability.optString("Title", "UNKNOWN"))
                .setDescription(formatDescription(vulnerability))
                .build();
    }

    @SuppressFBWarnings("IMPROPER_UNICODE")
    private Severity mapSeverity(final String string) {
        if (TRIVY_VULNERABILITY_LEVEL_TAG_LOW.equalsIgnoreCase(string)) {
            return Severity.WARNING_LOW;
        }
        else if (TRIVY_VULNERABILITY_LEVEL_TAG_MEDIUM.equalsIgnoreCase(string)) {
            return Severity.WARNING_NORMAL;
        }
        else if (TRIVY_VULNERABILITY_LEVEL_TAG_HIGH.equalsIgnoreCase(string)
                || TRIVY_VULNERABILITY_LEVEL_TAG_CRITICAL.equalsIgnoreCase(string)) {
            return Severity.WARNING_HIGH;
        }
        else {
            return Severity.WARNING_HIGH;
        }
    }

    private String formatDescription(final JSONObject vulnerability) {
        return new StringBuilder().append(MessageFormat.format(
                "<p><div><b>File</b>: {0}</div><div><b>Installed Version:</b> {1}</div><div><b>Fixed Version:</b> {2}</div><div><b>Severity:</b> {3}</div>",
                vulnerability.optString("PkgName", VALUE_NOT_SET), vulnerability.optString("InstalledVersion", VALUE_NOT_SET),
                vulnerability.optString("FixedVersion", "still open"),
                vulnerability.optString("Severity", "UNKOWN")))
                .append("<p>")
                .append(vulnerability.optString("Description", ""))
                .append("</p>")
                .toString();
    }
}
