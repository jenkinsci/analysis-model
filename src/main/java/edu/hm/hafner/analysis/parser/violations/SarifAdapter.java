package edu.hm.hafner.analysis.parser.violations;

import java.io.Serial;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import edu.hm.hafner.analysis.IssueBuilder;

import se.bjurr.violations.lib.model.Violation;
import se.bjurr.violations.lib.parsers.SarifParser;

/**
 * Parses SARIF files.
 *
 * @author Ullrich Hafner
 */
public class SarifAdapter extends AbstractViolationAdapter {
    @Serial
    private static final long serialVersionUID = -5699747899173867285L;

    private static final Pattern WINDOWS_PATH_ON_UNIX = Pattern.compile("^/[a-zA-Z]:.*");

    @Override
    SarifParser createParser() {
        return new SarifParser();
    }

    @Override
    void extractAdditionalProperties(final IssueBuilder builder, final Violation violation) {
        try {
            var uri = new URI(violation.getFile());
            var path = uri.getPath();
            if (path != null) {
                builder.setFileName(removePrefix(path));
            }
        }
        catch (URISyntaxException exception) {
            // ignore
        }
    }

    private String removePrefix(final String fileName) {
        if (WINDOWS_PATH_ON_UNIX.matcher(fileName).matches()) {
            return fileName.substring(1);
        }
        return fileName;
    }
}
