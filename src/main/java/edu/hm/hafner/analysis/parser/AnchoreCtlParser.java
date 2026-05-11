package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;

import java.io.Serial;
import java.util.Optional;

import static j2html.TagCreator.*;

/**
 * JSON report parser for anchorectl image one-time-scan output.
 *
 * <p>Supports three output layouts produced by anchorectl:
 * <ul>
 *   <li>Unified output ({@code -o json IMAGE}): top-level {@code vulnerabilities} is a JSON object
 *       envelope containing an inner {@code vulnerabilities} array.</li>
 *   <li>Standalone file ({@code -o json --output-directory DIR}): top-level {@code vulnerabilities}
 *       is already the array.</li>
 *   <li>Raw snake_case output ({@code -o json-raw --output-directory DIR}): same shape as the
 *       standalone file but field names use snake_case instead of camelCase.</li>
 * </ul>
 *
 * @see <a href="https://github.com/anchore/anchorectl">anchorectl</a>
 */
public class AnchoreCtlParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 3847261938475610293L;

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport,
            final IssueBuilder issueBuilder) {
        extractVulnerabilities(jsonReport).ifPresent(vulnerabilities -> parseJsonArray(report, vulnerabilities, issueBuilder));
    }

    @Override
    protected void parseJsonArray(final Report report, final JSONArray jsonReport,
            final IssueBuilder issueBuilder) {
        for (int i = 0; i < jsonReport.length(); i++) {
            parseVulnerability(report, jsonReport.getJSONObject(i), issueBuilder);
        }
    }

    private Optional<JSONArray> extractVulnerabilities(final JSONObject root) {
        if (!root.has("vulnerabilities") || root.isNull("vulnerabilities")) {
            return Optional.empty();
        }
        var raw = root.get("vulnerabilities");
        if (raw instanceof JSONArray array) {
            return Optional.of(array);
        }
        if (raw instanceof JSONObject envelope) {
            return Optional.ofNullable(envelope.optJSONArray("vulnerabilities"));
        }
        return Optional.empty();
    }

    private void parseVulnerability(final Report report, final JSONObject vulnerability, final IssueBuilder builder) {
        var vulnerabilityId = vulnerability.optString("vuln", "").trim();
        if (vulnerabilityId.isBlank()) {
            return;
        }

        var packagePath = firstNonBlank(vulnerability, "packagePath", "package_path");
        var purl = vulnerability.optString("purl", "");

        var fix = cleanNone(vulnerability.optString("fix", ""));
        var url = vulnerability.optString("url", "");
        var willNotFix = vulnerability.optBoolean("willNotFix") || vulnerability.optBoolean("will_not_fix");
        var isKev = isKevFromNvdData(vulnerability);

        var packageVersion = firstNonBlank(vulnerability, "packageVersion", "package_version");
        builder.setMessage(vulnerabilityId)
                .guessSeverity(vulnerability.optString("severity", ""))
                .setPackageName(firstNonBlank(vulnerability, "packageName", "package_name"))
                .setCategory(firstNonBlank(vulnerability, "packageType", "package_type"))
                .setFileName(extractFileName(packagePath, purl))
                .setDescription(buildDescription(fix, packageVersion, isKev, willNotFix, url, vulnerabilityId))
                .setFingerprint(vulnerabilityId + ":" + firstNonBlank(vulnerability, "packageName", "package_name") + ":" + packageVersion + ":" + packagePath);

        report.add(builder.build());
    }

    private String extractFileName(final String packagePath, final String purl) {
        if (!packagePath.isBlank()) {
            return packagePath;
        }
        else if (!purl.isBlank()) {
            return purl;
        }
        else {
            return "-";
        }
    }

    private boolean isKevFromNvdData(final JSONObject vulnerability) {
        var nvdData = vulnerability.optJSONArray("nvdData");
        if (nvdData == null) {
            nvdData = vulnerability.optJSONArray("nvd_data");
        }
        if (nvdData == null) {
            return false;
        }
        for (int i = 0; i < nvdData.length(); i++) {
            var entry = nvdData.optJSONObject(i);
            if (entry != null && (entry.optBoolean("isKev") || entry.optBoolean("is_kev"))) {
                return true;
            }
        }
        return false;
    }

    private String buildDescription(final String fix, final String packageVersion,
            final boolean isKev, final boolean willNotFix, final String url, final String vulnId) {
        return join(
                fix.isBlank() ? p(text("No fix available")) : p(join(b("Fix:"), text(" " + fix))),
                iff(!packageVersion.isBlank(), p(join(text("Affected version: "), text(packageVersion)))),
                iff(isKev, p(b("CISA Known Exploited Vulnerability (KEV)"))),
                iff(willNotFix, p(text("Vendor will not fix"))),
                iff(!url.isBlank(), p(a(vulnId).withHref(url)))
        ).render();
    }

    private static String cleanNone(final String value) {
        return equalsIgnoreCase(value.trim(), "none") ? "" : value;
    }
}
