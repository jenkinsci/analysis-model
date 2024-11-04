package edu.hm.hafner.analysis.parser.violations;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.util.PathUtil;
import se.bjurr.violations.lib.model.Violation;
import se.bjurr.violations.lib.parsers.SarifParser;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Parses SARIF files.
 *
 * @author Ullrich Hafner
 */
public class SarifAdapter extends AbstractViolationAdapter {
    private static final long serialVersionUID = -5699747899173867285L;

    @Override
    SarifParser createParser() {
        return new SarifParser();
    }

    private static final PathUtil PATH_UTIL = new PathUtil();

    @Override
    void updateIssueBuilder(final Violation violation, final IssueBuilder builder) {
        builder.setSeverity(convertSeverity(violation.getSeverity(), violation))
                .setFileName(convertFileUriSchemeToPath(violation.getFile()))
                .setMessage(violation.getMessage())
                .setLineStart(toValidInt(violation.getStartLine()))
                .setLineEnd(toValidInt(violation.getEndLine()))
                .setColumnStart(toValidInt(violation.getColumn()))
                .setColumnEnd(toValidInt(violation.getEndColumn()))
                .setType(violation.getRule())
                .setCategory(violation.getCategory());
    }

    String convertFileUriSchemeToPath(final String fileName) {
        if (!fileName.startsWith("file:")) {
            return fileName;
        }

        try {
            Path path = Paths.get(new URI(fileName));
            return path.toString();
        } catch (Exception e) {
            // ignore
        }
        return fileName;
    }

}
