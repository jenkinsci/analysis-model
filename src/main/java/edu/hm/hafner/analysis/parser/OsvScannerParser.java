package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.umd.cs.findbugs.annotations.CheckForNull;

import j2html.tags.DomContent;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static j2html.TagCreator.*;

/**
 * Parser for OSV-Scanner security vulnerability reports in JSON format.
 *
 * @author Akash Manna
 * @see <a href="https://google.github.io/osv-scanner/">OSV-Scanner</a>
 * @see <a href="https://github.com/google/osv-scanner">OSV-Scanner on GitHub</a>
 */
public class OsvScannerParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = -8767989929513034597L;

    private static final String RESULTS_TAG = "results";
    private static final String SOURCE_TAG = "source";
    private static final String PATH_TAG = "path";
    private static final String PACKAGES_TAG = "packages";
    private static final String PACKAGE_TAG = "package";
    private static final String NAME_TAG = "name";
    private static final String VERSION_TAG = "version";
    private static final String ECOSYSTEM_TAG = "ecosystem";
    private static final String VULNERABILITIES_TAG = "vulnerabilities";
    private static final String ID_TAG = "id";
    private static final String SUMMARY_TAG = "summary";
    private static final String DETAILS_TAG = "details";
    private static final String ALIASES_TAG = "aliases";
    private static final String DATABASE_SPECIFIC_TAG = "database_specific";
    private static final String SEVERITY_TAG = "severity";

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        if (!jsonReport.has(RESULTS_TAG)) {
            return;
        }
        var results = jsonReport.getJSONArray(RESULTS_TAG);
        for (int i = 0; i < results.length(); i++) {
            var result = results.getJSONObject(i);
            var source = result.optJSONObject(SOURCE_TAG);
            var sourcePath = source != null ? source.optString(PATH_TAG, "-") : "-";
            parsePackages(report, result, sourcePath, issueBuilder);
        }
    }

    private void parsePackages(final Report report, final JSONObject result, final String sourcePath,
            final IssueBuilder issueBuilder) {
        if (!result.has(PACKAGES_TAG)) {
            return;
        }
        var packages = result.getJSONArray(PACKAGES_TAG);
        for (int i = 0; i < packages.length(); i++) {
            var packageEntry = packages.getJSONObject(i);
            parseVulnerabilities(report, packageEntry, sourcePath, issueBuilder);
        }
    }

    private void parseVulnerabilities(final Report report, final JSONObject packageEntry, final String sourcePath,
            final IssueBuilder issueBuilder) {
        if (!packageEntry.has(VULNERABILITIES_TAG)) {
            return;
        }
        var vulnerabilities = packageEntry.getJSONArray(VULNERABILITIES_TAG);
        if (vulnerabilities.isEmpty()) {
            return;
        }
        var packageInfo = packageEntry.optJSONObject(PACKAGE_TAG);
        var packageName = formatPackageName(packageInfo);
        var ecosystem = getEcosystem(packageInfo);

        for (int i = 0; i < vulnerabilities.length(); i++) {
            var vulnerability = vulnerabilities.getJSONObject(i);
            report.add(convertToIssue(vulnerability, sourcePath, packageName, ecosystem, issueBuilder));
        }
    }

    private Issue convertToIssue(final JSONObject vulnerability, final String sourcePath,
            final String packageName, final String ecosystem, final IssueBuilder issueBuilder) {
        var id = vulnerability.optString(ID_TAG, "-");
        var summary = vulnerability.optString(SUMMARY_TAG, "");
        var details = vulnerability.optString(DETAILS_TAG, "");
        var severity = resolveSeverity(vulnerability);
        var description = buildDescription(summary, details, vulnerability);

        return issueBuilder
                .setFileName(sourcePath)
                .setPackageName(packageName)
                .setCategory(ecosystem)
                .setSeverity(severity)
                .setType(id)
                .setMessage(summary)
                .setDescription(description)
                .buildAndClean();
    }

    private String formatPackageName(@CheckForNull final JSONObject packageInfo) {
        if (packageInfo == null) {
            return "Unknown";
        }
        var name = packageInfo.optString(NAME_TAG, "Unknown");
        var version = packageInfo.optString(VERSION_TAG, "");
        return version.isEmpty() ? name : name + "@" + version;
    }

    private String getEcosystem(@CheckForNull final JSONObject packageInfo) {
        if (packageInfo == null) {
            return "Unknown";
        }
        return packageInfo.optString(ECOSYSTEM_TAG, "Unknown");
    }

    /**
     * Resolves severity from the vulnerability's {@code database_specific.severity} field,
     * falling back to WARNING_NORMAL if not present.
     *
     * @param vulnerability
     *         the vulnerability JSON object
     *
     * @return the resolved {@link Severity}
     */
    private Severity resolveSeverity(final JSONObject vulnerability) {
        var dbSpecific = vulnerability.optJSONObject(DATABASE_SPECIFIC_TAG);
        if (dbSpecific != null && dbSpecific.has(SEVERITY_TAG)) {
            return Severity.guessFromString(dbSpecific.optString(SEVERITY_TAG, ""));
        }
        return Severity.WARNING_NORMAL;
    }

    private String buildDescription(final String summary, final String details, final JSONObject vulnerability) {
        var tags = new ArrayList<DomContent>();
        if (!summary.isEmpty()) {
            tags.add(p(strong(summary)));
        }
        if (!details.isEmpty()) {
            tags.add(p(details));
        }
        appendAliases(vulnerability).ifPresent(tags::add);
        appendOsvLink(vulnerability).ifPresent(tags::add);

        return join((Object[]) tags.toArray(new DomContent[0])).render();
    }

    private Optional<DomContent> appendAliases(final JSONObject vulnerability) {
        if (!vulnerability.has(ALIASES_TAG)) {
            return Optional.empty();
        }
        var aliases = vulnerability.getJSONArray(ALIASES_TAG);
        if (aliases.isEmpty()) {
            return Optional.empty();
        }
        var links = buildAliasLinks(aliases);
        if (links.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(p(strong("Aliases:"), joinWithSeparator(links, text(", "))));
    }

    private List<DomContent> buildAliasLinks(final JSONArray aliases) {
        var links = new ArrayList<DomContent>();
        for (int i = 0; i < aliases.length(); i++) {
            var alias = aliases.optString(i, "").trim();
            if (alias.isEmpty()) {
                continue;
            }
            if (alias.startsWith("CVE-")) {
                links.add(a(alias).withHref("https://nvd.nist.gov/vuln/detail/" + alias));
            }
            else {
                links.add(text(alias));
            }
        }
        return links;
    }

    private Optional<DomContent> appendOsvLink(final JSONObject vulnerability) {
        var id = vulnerability.optString(ID_TAG, "");
        if (id.isEmpty() || "-".equals(id)) {
            return Optional.empty();
        }
        var osvUrl = "https://osv.dev/vulnerability/" + id;
        return Optional.of(p(strong("OSV Entry:"),
                text(" "),
                a(id).withHref(osvUrl)));
    }
}
