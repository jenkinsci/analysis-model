package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

import java.io.Serial;
import java.util.Locale;

/**
 * A parser for Ruff JSON output.
 *
 * @author Akash Manna
 * @see <a href="https://github.com/astral-sh/ruff">Ruff on GitHub</a>
 */
public class RuffParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    protected void parseJsonArray(final Report report, final JSONArray jsonReport, final IssueBuilder issueBuilder) {
        for (Object issue : jsonReport) {
            if (issue instanceof JSONObject object) {
                report.add(convertToIssue(object, issueBuilder));
            }
        }
    }

    Issue convertToIssue(final JSONObject jsonIssue, final IssueBuilder issueBuilder) {
        if (jsonIssue.has("code")) {
            issueBuilder.setType(jsonIssue.getString("code"));
        }
        
        String message = "";
        if (jsonIssue.has("message")) {
            message = jsonIssue.getString("message");
        }
        
        if (jsonIssue.has("fix") && !jsonIssue.isNull("fix")) {
            JSONObject fix = jsonIssue.getJSONObject("fix");
            if (!fix.isEmpty()) {
                message = message + " [fixable]";
            }
        }
        
        issueBuilder.setMessage(message);
        
        if (jsonIssue.has("location")) {
            JSONObject location = jsonIssue.getJSONObject("location");
            
            if (location.has("row")) {
                issueBuilder.setLineStart(location.getInt("row"));
            }
            
            if (location.has("column")) {
                issueBuilder.setColumnStart(location.getInt("column"));
            }
        }
        
        if (jsonIssue.has("end_location")) {
            JSONObject endLocation = jsonIssue.getJSONObject("end_location");
            
            if (endLocation.has("row")) {
                issueBuilder.setLineEnd(endLocation.getInt("row"));
            }
            
            if (endLocation.has("column")) {
                issueBuilder.setColumnEnd(endLocation.getInt("column"));
            }
        }
        
        if (jsonIssue.has("filename")) {
            issueBuilder.setFileName(jsonIssue.getString("filename"));
        }
        
        String code = jsonIssue.optString("code", "");
        issueBuilder.setSeverity(mapSeverity(code));
        
        String category = getCategoryFromCode(code);
        if (jsonIssue.has("url")) {
            String url = jsonIssue.getString("url");
            if (url.contains("/rules/") && url.lastIndexOf("/") > url.lastIndexOf("/rules/") + 7) {
                String urlCategory = url.substring(url.lastIndexOf("/rules/") + 7);
                if (urlCategory.contains("/")) {
                    category = urlCategory.substring(0, urlCategory.indexOf("/"));
                }
            }
        }
        issueBuilder.setCategory(category);
        
        return issueBuilder.buildAndClean();
    }

    private Severity mapSeverity(final String code) {
        if (code.isEmpty()) {
            return Severity.WARNING_NORMAL;
        }
        
        // Ruff uses various rule codes:
        // E/W - pycodestyle errors and warnings
        // F - Pyflakes
        // C - mccabe complexity
        // I - isort
        // N - pep8-naming
        // D - pydocstyle
        // etc.
        
        char prefix = code.charAt(0);
        return switch (prefix) {
            case 'E' -> Severity.ERROR;           // Errors
            case 'F' -> Severity.WARNING_HIGH;    // Pyflakes (likely bugs)
            case 'B' -> Severity.WARNING_HIGH;    // flake8-bugbear (likely bugs)
            case 'W' -> Severity.WARNING_NORMAL;  // Warnings
            case 'C' -> Severity.WARNING_NORMAL;  // Complexity
            case 'D' -> Severity.WARNING_LOW;     // Documentation
            case 'I' -> Severity.WARNING_LOW;     // Import sorting
            case 'N' -> Severity.WARNING_LOW;     // Naming conventions
            default -> Severity.WARNING_NORMAL;
        };
    }

    private String getCategoryFromCode(final String code) {
        if (code.isEmpty()) {
            return "ruff";
        }
        
        int i = 0;
        while (i < code.length() && !Character.isDigit(code.charAt(i))) {
            i++;
        }
        
        if (i > 0) {
            String prefix = code.substring(0, i);
            return switch (prefix) {
                case "E", "W" -> "pycodestyle";
                case "F" -> "pyflakes";
                case "C" -> "mccabe";
                case "I" -> "isort";
                case "N" -> "pep8-naming";
                case "D" -> "pydocstyle";
                case "UP" -> "pyupgrade";
                case "B" -> "flake8-bugbear";
                case "A" -> "flake8-builtins";
                case "COM" -> "flake8-commas";
                case "S" -> "flake8-bandit";
                default -> "ruff-" + prefix.toLowerCase(Locale.ENGLISH);
            };
        }
        
        return "ruff";
    }
}
