package edu.hm.hafner.analysis.parser;

import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.umd.cs.findbugs.annotations.CheckForNull;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * A parser for KICS JSON output.
 *
 * @author Akash Manna
 * @see <a href="https://docs.kics.io/latest/">KICS</a>
 */
public class KicsParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 7641061102483954809L;

    private static final String QUERIES = "queries";
    private static final String QUERY_NAME = "query_name";
    private static final String QUERY_NAME_LEGACY = "queryName";
    private static final String QUERY_ID = "query_id";
    private static final String QUERY_ID_LEGACY = "queryId";
    private static final String QUERY_URL = "query_url";
    private static final String QUERY_URL_LEGACY = "queryUrl";
    private static final String SEVERITY = "severity";
    private static final String PLATFORM = "platform";
    private static final String PLATFORM_LEGACY = "Platform";
    private static final String CWE = "cwe";
    private static final String CWE_LEGACY = "CWE";
    private static final String RISK_SCORE = "risk_score";
    private static final String RISK_SCORE_LEGACY = "riskScore";
    private static final String CLOUD_PROVIDER = "cloud_provider";
    private static final String CLOUD_PROVIDER_LEGACY = "cloudProvider";
    private static final String CATEGORY = "category";
    private static final String CATEGORY_LEGACY = "Category";
    private static final String DESCRIPTION = "description";
    private static final String FILES = "files";
    private static final String FILE_NAME = "file_name";
    private static final String FILE_NAME_LEGACY = "fileName";
    private static final String LINE = "line";
    private static final String START_LINE = "start_line";
    private static final String END_LINE = "end_line";
    private static final String RESOURCE_TYPE = "resource_type";
    private static final String RESOURCE_TYPE_LEGACY = "resourceType";
    private static final String RESOURCE_NAME = "resource_name";
    private static final String RESOURCE_NAME_LEGACY = "resourceName";
    private static final String ISSUE_TYPE = "issue_type";
    private static final String ISSUE_TYPE_LEGACY = "issueType";
    private static final String SEARCH_KEY = "search_key";
    private static final String SEARCH_KEY_LEGACY = "searchKey";
    private static final String SEARCH_LINE = "search_line";
    private static final String SEARCH_LINE_LEGACY = "searchLine";
    private static final String SEARCH_VALUE = "search_value";
    private static final String SEARCH_VALUE_LEGACY = "searchValue";
    private static final String EXPECTED_VALUE = "expected_value";
    private static final String EXPECTED_VALUE_LEGACY = "expectedValue";
    private static final String ACTUAL_VALUE = "actual_value";
    private static final String ACTUAL_VALUE_LEGACY = "actualValue";

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        var queries = jsonReport.optJSONArray(QUERIES);
        if (queries == null) {
            return;
        }

        for (int i = 0; i < queries.length(); i++) {
            var query = queries.optJSONObject(i);
            if (query != null) {
                parseQuery(report, query, issueBuilder);
            }
        }
    }

    private void parseQuery(final Report report, final JSONObject query, final IssueBuilder issueBuilder) {
        var files = query.optJSONArray(FILES);
        if (files == null || files.isEmpty()) {
            return;
        }

        for (int i = 0; i < files.length(); i++) {
            var file = files.optJSONObject(i);
            if (file != null) {
                report.add(createIssue(query, file, issueBuilder));
            }
        }
    }

    private Issue createIssue(final JSONObject query, final JSONObject file, final IssueBuilder issueBuilder) {
        var lineStart = file.optInt(LINE, file.optInt(START_LINE, 0));
        var lineEnd = file.optInt(END_LINE, lineStart);
        var fileName = firstNonBlank(file, FILE_NAME, FILE_NAME_LEGACY, "-");
        var category = firstNonBlank(query, PLATFORM, PLATFORM_LEGACY, CLOUD_PROVIDER, CLOUD_PROVIDER_LEGACY,
                CATEGORY, CATEGORY_LEGACY);

        var builder = issueBuilder
                .setFileName(fileName)
                .setType(firstNonBlank(query, QUERY_ID, QUERY_ID_LEGACY, "-"))
                .setMessage(firstNonBlank(query, QUERY_NAME, QUERY_NAME_LEGACY))
                .guessSeverity(query.optString(SEVERITY, "medium"))
                .setDescription(buildDescription(query, file));

        if (!category.isBlank()) {
            builder = builder.setCategory(category);
        }

        return builder.setLineStart(lineStart).setLineEnd(lineEnd).buildAndClean();
    }

    private String buildDescription(final JSONObject query, final JSONObject file) {
        var sections = new ArrayList<String>();

        appendIfNotBlank(sections, "Description", query.optString(DESCRIPTION, ""));
        appendIfNotBlank(sections, "Query URL", firstNonBlank(query, QUERY_URL, QUERY_URL_LEGACY));
        appendIfNotBlank(sections, "Platform", firstNonBlank(query, PLATFORM, PLATFORM_LEGACY));
        appendIfNotBlank(sections, "Cloud provider", firstNonBlank(query, CLOUD_PROVIDER, CLOUD_PROVIDER_LEGACY));
        appendIfNotBlank(sections, "Category", firstNonBlank(query, CATEGORY, CATEGORY_LEGACY));
        appendIfNotBlank(sections, "CWE", firstNonBlank(query, CWE, CWE_LEGACY));
        appendIfNotBlank(sections, "Risk score", firstNonBlank(query, RISK_SCORE, RISK_SCORE_LEGACY));
        appendIfNotBlank(sections, "Resource type", firstNonBlank(file, RESOURCE_TYPE, RESOURCE_TYPE_LEGACY));
        appendIfNotBlank(sections, "Resource name", firstNonBlank(file, RESOURCE_NAME, RESOURCE_NAME_LEGACY));
        appendIfNotBlank(sections, "Issue type", firstNonBlank(file, ISSUE_TYPE, ISSUE_TYPE_LEGACY));
        appendIfNotBlank(sections, "Search key", firstNonBlank(file, SEARCH_KEY, SEARCH_KEY_LEGACY));
        appendIfNotBlank(sections, "Search value", firstNonBlank(file, SEARCH_VALUE, SEARCH_VALUE_LEGACY));
        appendIfNotBlank(sections, "Expected value", firstNonBlank(file, EXPECTED_VALUE, EXPECTED_VALUE_LEGACY));
        appendIfNotBlank(sections, "Actual value", firstNonBlank(file, ACTUAL_VALUE, ACTUAL_VALUE_LEGACY));

        var searchLine = file.optInt(SEARCH_LINE, file.optInt(SEARCH_LINE_LEGACY, -1));
        if (searchLine >= 0) {
            appendIfNotBlank(sections, "Search line", String.valueOf(searchLine));
        }

        return String.join("\n\n", sections);
    }

    private void appendIfNotBlank(final List<String> sections, final String label, @CheckForNull final String value) {
        if (value == null || value.isBlank()) {
            return;
        }

        sections.add(label + ": " + value.trim());
    }
}