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

    /** Key for additionalProperties. */
    private static final String ADDITIONAL_PROPERTIES = "additionalProperties";
    /** Key for category. */
    private static final String CATEGORY = "category";
    /** Key for columnEnd. */
    private static final String COLUMN_END = "columnEnd";
    /** Key for columnStart. */
    private static final String COLUMN_START = "columnStart";
    /** Key for description. */
    private static final String DESCRIPTION = "description";
    /** Key for directory. */
    private static final String DIRECTORY = "directory";
    /** Key for end in lineRange. */
    private static final String END = "end";
    /** Key for fileName. */
    private static final String FILE_NAME = "fileName";
    /** Key for fingerprint. */
    private static final String FINGERPRINT = "fingerprint";
    /** Key for id. */
    private static final String ID = "id";
    /** Key for lineEnd. */
    private static final String LINE_END = "lineEnd";
    /** Key for lineRanges. */
    private static final String LINE_RANGES = "lineRanges";
    /** Key for lineStart. */
    private static final String LINE_START = "lineStart";
    /** Key for message. */
    private static final String MESSAGE = "message";
    /** Key for moduleName. */
    private static final String MODULE_NAME = "moduleName";
    /** Key for origin. */
    private static final String ORIGIN = "origin";
    /** Key for packageName. */
    private static final String PACKAGE_NAME = "packageName";
    /** Key for reference. */
    private static final String REFERENCE = "reference";
    /** Key for severity. */
    private static final String SEVERITY = "severity";
    /** Key for start in lineRange. */
    private static final String START = "start";
    /** Key for type. */
    private static final String TYPE = "type";

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
        if (jsonIssue.has(DIRECTORY)) {
            builder.setDirectory(jsonIssue.getString(DIRECTORY));
        }
        if (jsonIssue.has(FINGERPRINT)) {
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
            JSONArray jsonRanges = jsonIssue.getJSONArray(LINE_RANGES);
            LineRangeList lineRanges = convertToLineRangeList(jsonRanges);
            builder.setLineRanges(lineRanges);
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
        if (jsonIssue.has(ORIGIN)) {
            builder.setOrigin(jsonIssue.getString(ORIGIN));
        }
        if (jsonIssue.has(PACKAGE_NAME)) {
            builder.setPackageName(jsonIssue.getString(PACKAGE_NAME));
        }
        if (jsonIssue.has(REFERENCE)) {
            builder.setReference(jsonIssue.getString(REFERENCE));
        }
        if (jsonIssue.has(SEVERITY)) {
            builder.setSeverity(Severity.valueOf(jsonIssue.getString(SEVERITY)));
        }
        if (jsonIssue.has(TYPE)) {
            builder.setType(jsonIssue.getString(TYPE));
        }
        return builder.build();
    }

    private LineRangeList convertToLineRangeList(JSONArray jsonRanges) {
        LineRangeList lineRanges = new LineRangeList();
        for (int i = 0; i < jsonRanges.length(); i++) {
            JSONObject jsonRange = jsonRanges.getJSONObject(i);
            if (jsonRange.has(START)) {
                if (jsonRange.has(END)) {
                    lineRanges.add(new LineRange(jsonRange.getInt(START), jsonRange.getInt(END)));
                }
                else {
                    lineRanges.add(new LineRange(jsonRange.getInt(START)));
                }
            }
            else if (jsonRange.has(END)) {
                lineRanges.add(new LineRange(jsonRange.getInt(END), jsonRange.getInt(END)));
            }
        }
        return lineRanges;
    }
}