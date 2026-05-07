package edu.hm.hafner.analysis.parser;

import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

import j2html.tags.Text;
import j2html.tags.UnescapedText;
import java.io.Serial;
import java.util.Locale;

import static j2html.TagCreator.*;

/**
 * A parser for cargo audit JSON output.
 *
 * @author Akash Manna
 * @see <a href="https://github.com/rustsec/cargo-audit">cargo-audit on GitHub</a>
 * @see <a href="https://crates.io/crates/cargo-audit/">cargo-audit on crates.io</a>
 */
public class CargoAuditParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 5329184956473295822L;

    private static final String VULNERABILITIES = "vulnerabilities";
    private static final String ADVISORY = "advisory";
    private static final String ID = "id";
    private static final String PACKAGE = "package";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String SEVERITY = "severity";
    private static final String URL = "url";
    private static final String CVSS = "cvss";
    private static final String DATE = "date";

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        if (jsonReport.has(VULNERABILITIES)) {
            var vulnerabilities = jsonReport.getJSONObject(VULNERABILITIES);
            vulnerabilities.keys().forEachRemaining(key -> {
                var vulnerability = vulnerabilities.getJSONObject(key);
                if (vulnerability.has(ADVISORY)) {
                    report.add(createIssue(issueBuilder, vulnerability));
                }
            });
        }
    }

    private Issue createIssue(final IssueBuilder issueBuilder, final JSONObject vulnerability) {
        var advisory = vulnerability.optJSONObject(ADVISORY);

        var advisoryId = advisory.optString(ID, "-");
        var packageName = advisory.optString(PACKAGE, "unknown");
        var title = advisory.optString(TITLE, "");
        var description = advisory.optString(DESCRIPTION, "");
        var severityString = advisory.optString(SEVERITY, "unknown");

        var descriptionHtml = buildDescription(title, description, advisory);

        return issueBuilder
                .setFileName(packageName)
                .setPackageName(packageName)
                .setType(advisoryId)
                .setMessage(title)
                .setDescription(descriptionHtml)
                .setSeverity(mapSeverity(severityString))
                .build();
    }

    private Severity mapSeverity(final String severityString) {
        if (severityString == null) {
            return Severity.WARNING_NORMAL;
        }
        String severity = severityString.toLowerCase(Locale.ENGLISH);
        if (severity.contains("critical")) {
            return Severity.ERROR;
        }
        if (severity.contains("high")) {
            return Severity.ERROR;
        }
        if (severity.contains("medium")) {
            return Severity.WARNING_NORMAL;
        }
        if (severity.contains("low")) {
            return Severity.WARNING_LOW;
        }
        return Severity.WARNING_NORMAL;
    }

    private String buildDescription(final String title, final String description, final JSONObject advisory) {
        var tags = new java.util.ArrayList<>();
        
        if (!title.isEmpty()) {
            tags.add(p(strong(title)));
        }
        
        if (!description.isEmpty()) {
            tags.add(p(description));
        }

        if (advisory.has(CVSS)) {
            var cvss = advisory.optString(CVSS, "");
            if (!cvss.isEmpty()) {
                tags.add(p(strong("CVSS:"), new UnescapedText("&nbsp;"), new Text(cvss)));
            }
        }

        if (advisory.has(URL)) {
            var url = advisory.optString(URL, "");
            if (!url.isEmpty()) {
                tags.add(p(strong("Reference:"), new UnescapedText("&nbsp;"), a().withHref(url).withText(url)));
            }
        }

        if (advisory.has(DATE)) {
            var date = advisory.optString(DATE, "");
            if (!date.isEmpty()) {
                tags.add(p(strong("Published:"), new UnescapedText("&nbsp;"), new Text(date)));
            }
        }

        return join(tags.toArray()).render();
    }
}
