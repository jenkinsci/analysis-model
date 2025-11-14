package edu.hm.hafner.analysis.parser;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.FileLocation;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.LineRange;
import edu.hm.hafner.util.LineRangeList;

/**
 * Base Parser JSON format.
 *
 * @author Jeremie Bresson
 */
abstract class JsonBaseParser extends IssueParser {
    @Serial
    private static final long serialVersionUID = -2318844382394973833L;

    /**
     * Deserialize an Issue from a JSON object.
     *
     * @param jsonIssue
     *         the issue as JSON object
     *
     * @return issue instance
     */
    Optional<Issue> convertToIssue(final JSONObject jsonIssue) {
        try (var builder = new IssueBuilder()) {
            return convertToIssue(jsonIssue, builder);
        }
    }

    /**
     * Deserialize an Issue from a JSON object. Properties that are not part of equals like {@code reference} or
     * {@code directory} will be skipped.
     *
     * @param jsonIssue
     *         the issue as JSON object
     * @param builder
     *         the issue builder to use
     *
     * @return issue instance
     */
    @SuppressWarnings({"PMD.CyclomaticComplexity", "PMD.CognitiveComplexity", "PMD.NPathComplexity"})
    Optional<Issue> convertToIssue(final JSONObject jsonIssue, final IssueBuilder builder) {
        if (jsonIssue.has(ADDITIONAL_PROPERTIES)) {
            builder.setAdditionalProperties(jsonIssue.getString(ADDITIONAL_PROPERTIES));
        }
        if (jsonIssue.has(CATEGORY)) {
            builder.setCategory(jsonIssue.getString(CATEGORY));
        }
        if (jsonIssue.has(COLUMN_END)) {
            builder.setColumnEnd(jsonIssue.getInt(COLUMN_END));
        }
        if (jsonIssue.has(COLUMN_START)) {
            builder.setColumnStart(jsonIssue.getInt(COLUMN_START));
        }
        if (jsonIssue.has(DESCRIPTION)) {
            builder.setDescription(jsonIssue.getString(DESCRIPTION));
        }
        if (jsonIssue.has(FINGERPRINT)) { // even though not part of equals it makes sense to read if available
            builder.setFingerprint(jsonIssue.getString(FINGERPRINT));
        }
        if (jsonIssue.has(FILE_NAME)) {
            builder.setFileName(jsonIssue.getString(FILE_NAME));
        }
        if (jsonIssue.has(ID)) {
            builder.setId(UUID.fromString(jsonIssue.getString(ID)));
        }
        if (jsonIssue.has(LINE_END)) {
            builder.setLineEnd(jsonIssue.getInt(LINE_END));
        }
        if (jsonIssue.has(LINE_RANGES)) {
            var jsonRanges = jsonIssue.getJSONArray(LINE_RANGES);
            var lineRanges = convertToLineRangeList(jsonRanges);
            builder.setLineRanges(lineRanges);
        }
        if (jsonIssue.has(ADDITIONAL_FILE_LOCATIONS)) {
            var jsonLocations = jsonIssue.getJSONArray(ADDITIONAL_FILE_LOCATIONS);
            var fileLocations = convertToFileLocationList(jsonLocations);
            builder.setAdditionalFileLocations(fileLocations);
        }
        if (jsonIssue.has(LINE_START)) {
            builder.setLineStart(jsonIssue.getInt(LINE_START));
        }
        if (jsonIssue.has(MESSAGE)) {
            builder.setMessage(jsonIssue.getString(MESSAGE));
        }
        if (jsonIssue.has(MODULE_NAME)) {
            builder.setModuleName(jsonIssue.getString(MODULE_NAME));
        }
        if (jsonIssue.has(PACKAGE_NAME)) {
            builder.setPackageName(jsonIssue.getString(PACKAGE_NAME));
        }
        if (jsonIssue.has(SEVERITY)) {
            builder.setSeverity(Severity.valueOf(jsonIssue.getString(SEVERITY)));
        }
        if (jsonIssue.has(TYPE)) {
            builder.setType(jsonIssue.getString(TYPE));
        }
        return builder.buildOptional();
    }

    private LineRangeList convertToLineRangeList(final JSONArray jsonRanges) {
        var lineRanges = new LineRangeList();
        for (int i = 0; i < jsonRanges.length(); i++) {
            var jsonRange = jsonRanges.getJSONObject(i);
            if (jsonRange.has(LINE_RANGE_START)) {
                if (jsonRange.has(LINE_RANGE_END)) {
                    lineRanges.add(new LineRange(jsonRange.getInt(LINE_RANGE_START),
                            jsonRange.getInt(LINE_RANGE_END)));
                }
                else {
                    lineRanges.add(new LineRange(jsonRange.getInt(LINE_RANGE_START)));
                }
            }
            else if (jsonRange.has(LINE_RANGE_END)) {
                lineRanges.add(new LineRange(jsonRange.getInt(LINE_RANGE_END),
                        jsonRange.getInt(LINE_RANGE_END)));
            }
        }
        return lineRanges;
    }

    private List<FileLocation> convertToFileLocationList(final JSONArray jsonLocations) {
        var fileLocations = new ArrayList<FileLocation>();
        for (int i = 0; i < jsonLocations.length(); i++) {
            var jsonLocation = jsonLocations.getJSONObject(i);
            var fileName = jsonLocation.optString(FILE_LOCATION_FILE_NAME, "");
            var lineStart = jsonLocation.optInt(FILE_LOCATION_LINE_START, 0);
            var lineEnd = jsonLocation.optInt(FILE_LOCATION_LINE_END, lineStart);
            var columnStart = jsonLocation.optInt(FILE_LOCATION_COLUMN_START, 0);
            var columnEnd = jsonLocation.optInt(FILE_LOCATION_COLUMN_END, 0);
            var message = jsonLocation.has(FILE_LOCATION_MESSAGE)
                    ? jsonLocation.getString(FILE_LOCATION_MESSAGE) : null;

            fileLocations.add(new FileLocation(fileName, lineStart, lineEnd, columnStart, columnEnd, message));
        }
        return fileLocations;
    }
}
