package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.io.Reader;
import java.io.Serial;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;

/**
 * Parser that reads the 1:1 JSON mapping of the properties of the {@link Issue} bean.
 *
 * @author Jeremie Bresson
 */
public class JsonParser extends JsonBaseParser {
    @Serial
    private static final long serialVersionUID = -6494117943149352139L;
    private static final String ISSUES = "issues";
    private static final boolean SEQUENTIAL = false;

    @Override
    public boolean accepts(final ReaderFactory readerFactory) {
        return readerFactory.getFileName().endsWith(".json");
    }

    @Override
    public Report parse(final ReaderFactory readerFactory) throws ParsingException {
        try (var reader = readerFactory.create(); var builder = new IssueBuilder()) {
            var jsonReport = (JSONObject) new JSONTokener(reader).nextValue();

            var report = new Report();
            if (jsonReport.has(ISSUES)) {
                var issues = jsonReport.getJSONArray(ISSUES);
                StreamSupport.stream(issues.spliterator(), SEQUENTIAL)
                        .filter(JSONObject.class::isInstance)
                        .map(o -> convertToIssue((JSONObject) o, builder))
                        .flatMap(Optional::stream)
                        .forEach(report::add);
            }
            return report;
        }
        catch (IOException | JSONException e) {
            throw new ParsingException(e);
        }
    }
}
