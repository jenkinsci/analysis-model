package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.umd.cs.findbugs.annotations.CheckForNull;

import java.io.Serial;

/**
 * A parser for kube-score JSON output.
 *
 * @author Akash Manna
 * @see <a href="https://github.com/zegl/kube-score">kube-score</a>
 */
public class KubeScoreParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 1754328680585270134L;

    private static final String NOT_AVAILABLE = "-";
    private static final String OBJECT_NAME = "object_name";
    private static final String OBJECT_NAME_LEGACY = "ObjectName";
    private static final String FILE_NAME = "file_name";
    private static final String FILE_NAME_LEGACY = "FileName";
    private static final String FILE_ROW = "file_row";
    private static final String FILE_ROW_LEGACY = "FileRow";
    private static final String FILE_LOCATION = "FileLocation";

    private static final String CHECKS = "checks";
    private static final String CHECKS_LEGACY = "Checks";
    private static final String CHECK = "check";
    private static final String CHECK_LEGACY = "Check";
    private static final String COMMENTS = "comments";
    private static final String COMMENTS_LEGACY = "Comments";

    private static final String ID = "id";
    private static final String ID_LEGACY = "ID";
    private static final String NAME = "name";
    private static final String NAME_LEGACY = "Name";
    private static final String COMMENT = "comment";
    private static final String COMMENT_LEGACY = "Comment";
    private static final String SUMMARY = "summary";
    private static final String SUMMARY_LEGACY = "Summary";
    private static final String DESCRIPTION = "description";
    private static final String DESCRIPTION_LEGACY = "Description";
    private static final String PATH = "path";
    private static final String PATH_LEGACY = "Path";

    private static final String GRADE = "grade";
    private static final String GRADE_LEGACY = "Grade";
    private static final String SKIPPED = "skipped";
    private static final String SKIPPED_LEGACY = "Skipped";

    private static final int CRITICAL_GRADE = 1;
    private static final int WARNING_GRADE = 5;

    @Override
    protected void parseJsonArray(final Report report, final JSONArray jsonReport, final IssueBuilder issueBuilder) {
        for (int i = 0; i < jsonReport.length(); i++) {
            var scoredObject = jsonReport.optJSONObject(i);
            if (scoredObject != null) {
                parseScoredObject(report, scoredObject, scoredObject.optString(OBJECT_NAME, NOT_AVAILABLE), issueBuilder);
            }
        }
    }

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        for (String key : jsonReport.keySet()) {
            var scoredObject = jsonReport.optJSONObject(key);
            if (scoredObject != null) {
                parseScoredObject(report, scoredObject, key, issueBuilder);
            }
        }
    }

    private void parseScoredObject(final Report report, final JSONObject scoredObject, final String fallbackObjectName,
            final IssueBuilder issueBuilder) {
        var checks = optJSONArray(scoredObject, CHECKS, CHECKS_LEGACY);
        if (checks == null) {
            return;
        }

        var objectName = stringValue(scoredObject, fallbackObjectName, OBJECT_NAME, OBJECT_NAME_LEGACY);
        var fileName = findFileName(scoredObject);
        var fileRow = findFileRow(scoredObject);

        for (int i = 0; i < checks.length(); i++) {
            var checkResult = checks.optJSONObject(i);
            if (checkResult != null) {
                parseCheck(report, checkResult, objectName, fileName, fileRow, issueBuilder);
            }
        }
    }

    private void parseCheck(final Report report, final JSONObject checkResult, final String objectName,
            final String fileName, final int fileRow, final IssueBuilder issueBuilder) {
        var grade = checkResult.optInt(GRADE, checkResult.optInt(GRADE_LEGACY, -1));
        if (shouldSkipCheck(checkResult, grade)) {
            return;
        }

        var severity = grade == CRITICAL_GRADE ? Severity.ERROR : Severity.WARNING_NORMAL;
        var check = findCheck(checkResult);
        var details = new IssueDetails(objectName, fileName, fileRow, severity);

        if (check == null) {
            report.add(createIssue(checkResult, null, details, issueBuilder));
            return;
        }

        addIssuesForCheck(report, checkResult, check, details, issueBuilder);
    }

    private boolean shouldSkipCheck(final JSONObject checkResult, final int grade) {
        return grade > WARNING_GRADE || isSkipped(checkResult);
    }

    @CheckForNull
    private JSONObject findCheck(final JSONObject checkResult) {
        var check = checkResult.optJSONObject(CHECK);
        if (check != null) {
            return check;
        }

        return checkResult.optJSONObject(CHECK_LEGACY);
    }

    private void addIssuesForCheck(final Report report, final JSONObject checkResult, final JSONObject check,
            final IssueDetails details, final IssueBuilder issueBuilder) {
        var comments = optJSONArray(checkResult, COMMENTS, COMMENTS_LEGACY);
        if (comments == null || comments.isEmpty()) {
            report.add(createIssue(check, null, details, issueBuilder));
            return;
        }

        var addedComment = false;
        for (int i = 0; i < comments.length(); i++) {
            var comment = comments.optJSONObject(i);
            if (comment != null) {
                report.add(createIssue(check, comment, details, issueBuilder));
                addedComment = true;
            }
        }

        if (!addedComment) {
            report.add(createIssue(check, null, details, issueBuilder));
        }
    }

    private boolean isSkipped(final JSONObject checkResult) {
        return checkResult.optBoolean(SKIPPED, checkResult.optBoolean(SKIPPED_LEGACY, false));
    }

    @SuppressWarnings("PMD.CloseResource")
    private Issue createIssue(final JSONObject check, @CheckForNull final JSONObject comment,
            final IssueDetails details, final IssueBuilder issueBuilder) {
        var builder = issueBuilder
                .setFileName(details.fileName())
                .setCategory(details.objectName())
                .setType(stringValue(check, NOT_AVAILABLE, ID, ID_LEGACY, NAME, NAME_LEGACY))
                .setMessage(comment == null ? stringValue(check, NOT_AVAILABLE, NAME, NAME_LEGACY)
                        : stringValue(comment, stringValue(check, NOT_AVAILABLE, NAME, NAME_LEGACY), SUMMARY,
                                SUMMARY_LEGACY))
                .setSeverity(details.severity())
                .setDescription(buildDescription(check, comment));

        if (details.fileRow() > 0) {
            builder.setLineStart(details.fileRow()).setLineEnd(details.fileRow());
        }

        return builder.buildAndClean();
    }

    private String buildDescription(final JSONObject check, @CheckForNull final JSONObject comment) {
        var description = new StringBuilder();

        if (comment != null) {
            var path = stringValue(comment, NOT_AVAILABLE, PATH, PATH_LEGACY);
            if (!NOT_AVAILABLE.equals(path)) {
                description.append("Path: ").append(path);
            }

            var commentDescription = stringValue(comment, "", DESCRIPTION, DESCRIPTION_LEGACY);
            if (!commentDescription.isBlank()) {
                appendLine(description, commentDescription);
            }
        }

        var checkComment = stringValue(check, "", COMMENT, COMMENT_LEGACY);
        if (!checkComment.isBlank()) {
            appendLine(description, checkComment);
        }

        return description.toString();
    }

    private void appendLine(final StringBuilder description, final String line) {
        if (description.length() > 0) {
            description.append('\n');
        }
        description.append(line);
    }

    private String findFileName(final JSONObject scoredObject) {
        var fileLocation = scoredObject.optJSONObject(FILE_LOCATION);
        if (fileLocation != null) {
            var fileName = fileLocation.optString("Name", "");
            if (!fileName.isBlank()) {
                return fileName;
            }
        }

        return stringValue(scoredObject, NOT_AVAILABLE, FILE_NAME, FILE_NAME_LEGACY);
    }

    private int findFileRow(final JSONObject scoredObject) {
        var fileRow = scoredObject.optInt(FILE_ROW, scoredObject.optInt(FILE_ROW_LEGACY, 0));
        var fileLocation = scoredObject.optJSONObject(FILE_LOCATION);
        if (fileLocation != null) {
            fileRow = fileLocation.optInt("Line", fileRow);
        }
        return fileRow;
    }

    private JSONArray optJSONArray(final JSONObject object, final String primaryKey, final String legacyKey) {
        var array = object.optJSONArray(primaryKey);
        if (array != null) {
            return array;
        }
        return object.optJSONArray(legacyKey);
    }

    private String stringValue(final JSONObject object, final String defaultValue, final String... keys) {
        for (String key : keys) {
            var value = object.optString(key, "");
            if (!value.isBlank()) {
                return value;
            }
        }
        return defaultValue;
    }

    private record IssueDetails(String objectName, String fileName, int fileRow, Severity severity) {
    }
}