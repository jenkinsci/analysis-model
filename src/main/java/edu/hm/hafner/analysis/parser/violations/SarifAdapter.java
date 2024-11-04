package edu.hm.hafner.analysis.parser.violations;

import edu.hm.hafner.analysis.IssueBuilder;
import se.bjurr.violations.lib.model.Violation;
import se.bjurr.violations.lib.parsers.SarifParser;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.InvalidPathException;

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

    @Override
    void updateIssueBuilder(final Violation violation, final IssueBuilder builder) {
        super.updateIssueBuilder(violation, builder);
        builder.setFileName(convertFileUriSchemeToPath(violation.getFile()));
    }

    String convertFileUriSchemeToPath(final String fileName) {
        if (!fileName.startsWith("file:")) {
            return fileName;
        }

        try {
            File file = new File(new URI(fileName));
            return file.toPath().toString();
        } 
        catch (URISyntaxException | InvalidPathException ignored) {
            // ignore
        }
        return fileName;
    }
}
