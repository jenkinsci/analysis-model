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
        var packageName = advisory.optString(PACKAGE, "unknown");

        return issueBuilder
                .setFileName(packageName)
                .setPackageName(packageName)
                .setType(advisory.optString(ID, "-"))
                .setMessage(advisory.optString(TITLE, ""))
                .setDescription(buildDescription(advisory.optString(TITLE, ""), 
                    advisory.optString(DESCRIPTION, ""), advisory))
                .setSeverity(adaptSeverity(advisory.optString(SEVERITY, "")))
                .build();
    }

    private Severity adaptSeverity(final String severityString) {
        var severity = Severity.guessFromString(severityString);
        
        if (severity.equals(Severity.WARNING_HIGH)) {
            return Severity.ERROR;
        }
        
        if (!isKnownSeverity(severityString)) {
            return Severity.WARNING_NORMAL;
        }
        
        return severity;
    }

    private boolean isKnownSeverity(final String severityString) {
        var lower = severityString.toLowerCase(Locale.ENGLISH);
        if (lower.contains("critical") || lower.contains("high") || lower.contains("medium")) {
            return true;
        }
        return lower.contains("low") || lower.contains("error") || lower.contains("warning");
    }

    private String buildDescription(final String title, final String description, final JSONObject advisory) {
        var tags = new java.util.ArrayList<>();
        
        if (!title.isEmpty()) {
            tags.add(p(strong(title)));
        }
        
        if (!description.isEmpty()) {
            tags.add(p(description));
        }

        if (advisory.has(CVSS) && !advisory.optString(CVSS, "").isEmpty()) {
            tags.add(p(strong("CVSS:"), new UnescapedText("&nbsp;"), new Text(advisory.optString(CVSS, ""))));
        }

        if (advisory.has(URL)) {
            var url = advisory.optString(URL, "");
            if (!url.isEmpty()) {
                tags.add(p(strong("Reference:"), new UnescapedText("&nbsp;"), a().withHref(url).withText(url)));
            }
        }

        if (advisory.has(DATE) && !advisory.optString(DATE, "").isEmpty()) {
            tags.add(p(strong("Published:"), new UnescapedText("&nbsp;"), new Text(advisory.optString(DATE, ""))));
        }

        return join(tags.toArray()).render();
    }
}
