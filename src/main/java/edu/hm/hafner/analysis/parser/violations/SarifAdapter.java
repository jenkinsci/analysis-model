package edu.hm.hafner.analysis.parser.violations;

import edu.hm.hafner.analysis.IssueBuilder;
import se.bjurr.violations.lib.model.Violation;
import se.bjurr.violations.lib.parsers.SarifParser;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.InvalidPathException;
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
            Path path = Paths.get(new URI(fileName));
            return path.toString();
        } 
        catch (URISyntaxException | InvalidPathException e)
        {
            // ignore
        }
        return fileName;
    }
}
