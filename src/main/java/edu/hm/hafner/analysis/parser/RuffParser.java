package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

import java.io.Serial;
import java.util.Locale;
import java.util.Map;

/**
 * A parser for Ruff JSON output.
 *
 * @author Akash Manna
 * @see <a href="https://github.com/astral-sh/ruff">Ruff on GitHub</a>
 */
public class RuffParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final Map<Character, Severity> SEVERITY_MAP = Map.of(
            'E', Severity.ERROR,           // Errors
            'F', Severity.WARNING_HIGH,    // Pyflakes (likely bugs)
            'B', Severity.WARNING_HIGH,    // flake8-bugbear (likely bugs)
            'W', Severity.WARNING_NORMAL,  // Warnings
            'C', Severity.WARNING_NORMAL,  // Complexity
            'D', Severity.WARNING_LOW,     // Documentation
            'I', Severity.WARNING_LOW,     // Import sorting
            'N', Severity.WARNING_LOW      // Naming conventions
    );

    private static final Map<String, String> CATEGORY_MAP = Map.ofEntries(
            Map.entry("E", "pycodestyle"),
            Map.entry("W", "pycodestyle"),
            Map.entry("F", "pyflakes"),
            Map.entry("C", "mccabe"),
            Map.entry("I", "isort"),
            Map.entry("N", "pep8-naming"),
            Map.entry("D", "pydocstyle"),
            Map.entry("UP", "pyupgrade"),
            Map.entry("B", "flake8-bugbear"),
            Map.entry("A", "flake8-builtins"),
            Map.entry("COM", "flake8-commas"),
            Map.entry("S", "flake8-bandit")
    );

    @Override
    protected void parseJsonArray(final Report report, final JSONArray jsonReport, final IssueBuilder issueBuilder) {
        for (Object issue : jsonReport) {
            if (issue instanceof JSONObject object) {
                report.add(convertToIssue(object, issueBuilder));
            }
        }
    }

    Issue convertToIssue(final JSONObject jsonIssue, final IssueBuilder issueBuilder) {
        setCodeAndType(jsonIssue, issueBuilder);
        setMessageWithFixable(jsonIssue, issueBuilder);
        setLocation(jsonIssue, issueBuilder);
        setEndLocation(jsonIssue, issueBuilder);
        setFileName(jsonIssue, issueBuilder);
        
        String code = jsonIssue.optString("code", "");
        issueBuilder.setSeverity(mapSeverity(code));
        issueBuilder.setCategory(extractCategory(jsonIssue, code));
        
        return issueBuilder.buildAndClean();
    }

    private void setCodeAndType(final JSONObject jsonIssue, final IssueBuilder issueBuilder) {
        if (jsonIssue.has("code")) {
            issueBuilder.setType(jsonIssue.getString("code"));
        }
    }

    private void setMessageWithFixable(final JSONObject jsonIssue, final IssueBuilder issueBuilder) {
        String message = jsonIssue.optString("message", "");
        
        if (jsonIssue.has("fix") && !jsonIssue.isNull("fix")) {
            JSONObject fix = jsonIssue.getJSONObject("fix");
            if (!fix.isEmpty()) {
                message = message + " [fixable]";
            }
        }
        
        issueBuilder.setMessage(message);
    }

    private void setLocation(final JSONObject jsonIssue, final IssueBuilder issueBuilder) {
        if (jsonIssue.has("location")) {
            JSONObject location = jsonIssue.getJSONObject("location");
            
            if (location.has("row")) {
                issueBuilder.setLineStart(location.getInt("row"));
            }
            if (location.has("column")) {
                issueBuilder.setColumnStart(location.getInt("column"));
            }
        }
    }

    private void setEndLocation(final JSONObject jsonIssue, final IssueBuilder issueBuilder) {
        if (jsonIssue.has("end_location")) {
            JSONObject endLocation = jsonIssue.getJSONObject("end_location");
            
            if (endLocation.has("row")) {
                issueBuilder.setLineEnd(endLocation.getInt("row"));
            }
            if (endLocation.has("column")) {
                issueBuilder.setColumnEnd(endLocation.getInt("column"));
            }
        }
    }

    private void setFileName(final JSONObject jsonIssue, final IssueBuilder issueBuilder) {
        if (jsonIssue.has("filename")) {
            issueBuilder.setFileName(jsonIssue.getString("filename"));
        }
    }

    private String extractCategory(final JSONObject jsonIssue, final String code) {
        String category = getCategoryFromCode(code);
        
        if (jsonIssue.has("url")) {
            String url = jsonIssue.getString("url");
            String urlCategory = extractCategoryFromUrl(url);
            if (!urlCategory.isEmpty()) {
                category = urlCategory;
            }
        }
        
        return category;
    }

    private String extractCategoryFromUrl(final String url) {
        if (!url.contains("/rules/")) {
            return "";
        }
        
        int rulesIndex = url.lastIndexOf("/rules/");
        int lastSlashIndex = url.lastIndexOf('/');
        
        if (lastSlashIndex <= rulesIndex + 7) {
            return "";
        }
        
        String urlCategory = url.substring(rulesIndex + 7);
        int slashIndex = urlCategory.indexOf('/');
        
        return slashIndex > 0 ? urlCategory.substring(0, slashIndex) : "";
    }

    private Severity mapSeverity(final String code) {
        if (code.isEmpty()) {
            return Severity.WARNING_NORMAL;
        }
        return SEVERITY_MAP.getOrDefault(code.charAt(0), Severity.WARNING_NORMAL);
    }

    private String getCategoryFromCode(final String code) {
        if (code.isEmpty()) {
            return "ruff";
        }
        
        String prefix = extractPrefix(code);
        if (prefix.isEmpty()) {
            return "ruff";
        }
        
        return CATEGORY_MAP.getOrDefault(prefix, "ruff-" + prefix.toLowerCase(Locale.ENGLISH));
    }

    private String extractPrefix(final String code) {
        int i = 0;
        while (i < code.length() && !Character.isDigit(code.charAt(i))) {
            i++;
        }
        return i > 0 ? code.substring(0, i) : "";
    }
}
