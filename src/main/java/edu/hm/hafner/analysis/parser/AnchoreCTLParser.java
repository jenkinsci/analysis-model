package edu.hm.hafner.analysis.parser;

import java.io.Serial;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;

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
public class AnchoreCTLParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 3847261938475610293L;

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport,
            final IssueBuilder issueBuilder) {
        extractVulnArray(jsonReport).ifPresent(vulns -> parseJsonArray(report, vulns, issueBuilder));
    }

    @Override
    protected void parseJsonArray(final Report report, final JSONArray jsonReport,
            final IssueBuilder issueBuilder) {
        for (int i = 0; i < jsonReport.length(); i++) {
            parseVuln(report, jsonReport.getJSONObject(i), issueBuilder);
        }
    }

    private Optional<JSONArray> extractVulnArray(final JSONObject root) {
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

    private void parseVuln(final Report report, final JSONObject vuln, final IssueBuilder builder) {
        var vulnId = vuln.optString("vuln", "").trim();
        if (vulnId.isBlank()) {
            return;
        }

        var packageName = firstNonBlank(vuln, "packageName", "package_name");
        var packageVersion = firstNonBlank(vuln, "packageVersion", "package_version");
        var packageType = firstNonBlank(vuln, "packageType", "package_type");
        var packagePath = firstNonBlank(vuln, "packagePath", "package_path");
        var purl = vuln.optString("purl", "");
        var fix = cleanNone(vuln.optString("fix", ""));
        var url = vuln.optString("url", "");
        var willNotFix = vuln.optBoolean("willNotFix", false)
                || vuln.optBoolean("will_not_fix", false);
        var isKev = isKevFromNvdData(vuln);

        String fileName;
        if (!packagePath.isBlank()) {
            fileName = packagePath;
        }
        else if (!purl.isBlank()) {
            fileName = purl;
        }
        else {
            fileName = "-";
        }

        builder.setMessage(vulnId)
                .guessSeverity(vuln.optString("severity", ""))
                .setPackageName(packageName)
                .setCategory(packageType)
                .setFileName(fileName)
                .setDescription(buildDescription(fix, packageVersion, isKev, willNotFix, url, vulnId))
                .setFingerprint(vulnId + ":" + packageName + ":" + packageVersion + ":" + packagePath);

        report.add(builder.build());
    }

    private boolean isKevFromNvdData(final JSONObject vuln) {
        var nvdData = vuln.optJSONArray("nvdData");
        if (nvdData == null) {
            nvdData = vuln.optJSONArray("nvd_data");
        }
        if (nvdData == null) {
            return false;
        }
        for (int i = 0; i < nvdData.length(); i++) {
            var entry = nvdData.optJSONObject(i);
            if (entry != null
                    && (entry.optBoolean("isKev", false) || entry.optBoolean("is_kev", false))) {
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
        return StringUtils.equalsIgnoreCase(value.trim(), "none") ? "" : value;
    }
}
