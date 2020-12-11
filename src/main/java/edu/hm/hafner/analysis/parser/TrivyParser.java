package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.io.Reader;
import java.text.MessageFormat;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingCanceledException;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

/**
 * <p>
 * Parser for reports of aquasec trivy container vulnerability scanner
 * </p>
 * <p>
 * <strong>Usage: </strong>trivy image -f json -o results.json golang:1.12-alpine
 * </p>
 *
 * @author Thomas Fürer - tfuerer.javanet@gmail.com
 */
public class TrivyParser extends IssueParser {
	private static final long serialVersionUID = 1L;

	@Override
	public Report parse(ReaderFactory readerFactory) throws ParsingException, ParsingCanceledException {
		final Report report = new Report();

		try (Reader reader = readerFactory.create()) {
			final JSONArray jsonReport = (JSONArray)new JSONTokener(reader).nextValue();

			final JSONArray vulnatbilites = ((JSONObject)jsonReport.get(0)).getJSONArray("Vulnerabilities");
			for (Object vulnatbility : vulnatbilites ) {
				report.add(convertToIssue((JSONObject)vulnatbility));
			}
		} catch (IOException e) {
			throw new ParsingException(e);
		}

		return report;
	}

	private Issue convertToIssue(JSONObject vulnatbility) {
		return new IssueBuilder()
				.setFileName(vulnatbility.getString("PkgName"))
				.setCategory(vulnatbility.getString("SeveritySource"))
				.setSeverity(mapSeverity(vulnatbility.getString("Severity")))
				.setType(vulnatbility.getString("VulnerabilityID"))
				.setMessage(vulnatbility.optString("Title", "UNKNOWN"))
				.setDescription(formatDescription(vulnatbility))
				.build();
	}

	private Severity mapSeverity(String string) {
		if ("low".equalsIgnoreCase(string)) {
			return Severity.WARNING_LOW;
		} else if ("medium".equalsIgnoreCase(string)) {
			return Severity.WARNING_NORMAL;
		} else if ("high".equalsIgnoreCase(string) || "critcal".equalsIgnoreCase(string)) {
			return Severity.WARNING_HIGH;
		} else {
			return Severity.WARNING_HIGH;
		}
	}

	private String formatDescription(JSONObject vulnatbility) {
		return new StringBuilder()
				.append(MessageFormat.format("<p><div><b>File</b>: {0}</div><div><b>Installed Version:</b> {1}</div><div><b>Fixed Version:</b> {2}</div><div><b>Severity:</b> {3}</div>",
						vulnatbility.getString("PkgName"),
						vulnatbility.getString("InstalledVersion"),
						vulnatbility.getString("FixedVersion"),
						vulnatbility.getString("Severity")))
				.append("<p>")
				.append(vulnatbility.getString("Description"))
				.append("</p>")
				.toString();
	}

}
