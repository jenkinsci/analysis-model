package edu.hm.hafner.analysis.parser;

import java.util.UUID;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.LineRange;
import edu.hm.hafner.analysis.LineRangeList;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

/**
 * Base class for JSON parsers.
 *
 * @author Jeremie Bresson
 */
public class JsonParser extends IssueParser {
    private static final long serialVersionUID = 1349282064371959197L;

    @Override
    public boolean accepts(final ReaderFactory readerFactory) {
        return true;
    }

    @Override
    public Report parse(final ReaderFactory readerFactory) throws ParsingException {
        try (Stream<String> lines = readerFactory.readStream()) {
            Report report = new Report();
            lines.map(line -> line.trim())
                .filter(line -> !line.isEmpty())
                .map(line -> parseIssue(line))
                .forEach(report::add);
            return report;
        }
    }

    /**
     * Convert a line to an issue.
     * @param line a String containing the Issue serialized into JSON
     * @return an Issue
     */
    protected Issue parseIssue(final String line) {
        JSONObject jsonIssue = new JSONObject(line);
        return convertToIssue(jsonIssue);
    }

    private Issue convertToIssue(JSONObject jsonIssue) {
        IssueBuilder builder = new IssueBuilder();
        if (jsonIssue.has(Issue.ADDITIONAL_PROPERTIES)) {
            builder.setAdditionalProperties(jsonIssue.getString(Issue.ADDITIONAL_PROPERTIES));
        }
        if (jsonIssue.has(Issue.CATEGORY)) {
            builder.setCategory(jsonIssue.getString(Issue.CATEGORY));
        }
        if (jsonIssue.has(Issue.COLUMN_END)) {
            builder.setColumnEnd(jsonIssue.getInt(Issue.COLUMN_END));
        }
        if (jsonIssue.has(Issue.COLUMN_START)) {
            builder.setColumnStart(jsonIssue.getInt(Issue.COLUMN_START));
        }
        if (jsonIssue.has(Issue.DESCRIPTION)) {
            builder.setDescription(jsonIssue.getString(Issue.DESCRIPTION));
        }
        if (jsonIssue.has(Issue.DIRECTORY)) {
            builder.setDirectory(jsonIssue.getString(Issue.DIRECTORY));
        }
        if (jsonIssue.has(Issue.FINGERPRINT)) {
            builder.setFingerprint(jsonIssue.getString(Issue.FINGERPRINT));
        }
        if (jsonIssue.has(Issue.FILE_NAME)) {
            builder.setFileName(jsonIssue.getString(Issue.FILE_NAME));
        }
        if (jsonIssue.has(Issue.ID)) {
            builder.setId(UUID.fromString(jsonIssue.getString(Issue.ID)));
        }
        if (jsonIssue.has(Issue.LINE_END)) {
            builder.setLineEnd(jsonIssue.getInt(Issue.LINE_END));
        }
        if (jsonIssue.has(Issue.LINE_RANGES)) {
            JSONArray jsonRanges = jsonIssue.getJSONArray(Issue.LINE_RANGES);
            LineRangeList lineRanges = convertToLineRangeList(jsonRanges);
            builder.setLineRanges(lineRanges);
        }
        if (jsonIssue.has(Issue.LINE_START)) {
            builder.setLineStart(jsonIssue.getInt(Issue.LINE_START));
        }
        if (jsonIssue.has(Issue.MESSAGE)) {
            builder.setMessage(jsonIssue.getString(Issue.MESSAGE));
        }
        if (jsonIssue.has(Issue.MODULE_NAME)) {
            builder.setModuleName(jsonIssue.getString(Issue.MODULE_NAME));
        }
        if (jsonIssue.has(Issue.ORIGIN)) {
            builder.setOrigin(jsonIssue.getString(Issue.ORIGIN));
        }
        if (jsonIssue.has(Issue.PACKAGE_NAME)) {
            builder.setPackageName(jsonIssue.getString(Issue.PACKAGE_NAME));
        }
        if (jsonIssue.has(Issue.REFERENCE)) {
            builder.setReference(jsonIssue.getString(Issue.REFERENCE));
        }
        if (jsonIssue.has(Issue.SEVERITY)) {
            builder.setSeverity(Severity.valueOf(jsonIssue.getString(Issue.SEVERITY)));
        }
        if (jsonIssue.has(Issue.TYPE)) {
            builder.setType(jsonIssue.getString(Issue.TYPE));
        }
        return builder.build();
    }

    private LineRangeList convertToLineRangeList(JSONArray jsonRanges) {
        LineRangeList lineRanges = new LineRangeList();
        for (int i = 0; i < jsonRanges.length(); i++) {
            JSONObject jsonRange = jsonRanges.getJSONObject(i);
            if (jsonRange.has(Issue.LINE_RANGE_START)) {
                if (jsonRange.has(Issue.LINE_RANGE_END)) {
                    lineRanges.add(new LineRange(jsonRange.getInt(Issue.LINE_RANGE_START), jsonRange.getInt(Issue.LINE_RANGE_END)));
                }
                else {
                    lineRanges.add(new LineRange(jsonRange.getInt(Issue.LINE_RANGE_START)));
                }
            }
            else if (jsonRange.has(Issue.LINE_RANGE_END)) {
                lineRanges.add(new LineRange(jsonRange.getInt(Issue.LINE_RANGE_END), jsonRange.getInt(Issue.LINE_RANGE_END)));
            }
        }
        return lineRanges;
    }
}