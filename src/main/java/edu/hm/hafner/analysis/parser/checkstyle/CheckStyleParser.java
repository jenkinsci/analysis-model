package edu.hm.hafner.analysis.parser.checkstyle;

import java.io.IOException;
import java.io.Reader;
import java.io.Serial;

import org.apache.commons.digester3.Digester;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.SAXException;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.SecureDigester;
import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * A parser for Checkstyle XML files.
 *
 * @author Ullrich Hafner
 */
public class CheckStyleParser extends IssueParser {
    @Serial
    private static final long serialVersionUID = -3187275729854832128L;

    @Override
    public Report parse(final ReaderFactory readerFactory) throws ParsingException {
        Digester digester = new SecureDigester(CheckStyleParser.class);

        String rootXPath = "checkstyle";
        digester.addObjectCreate(rootXPath, CheckStyle.class);
        digester.addSetProperties(rootXPath);

        String fileXPath = "checkstyle/file";
        digester.addObjectCreate(fileXPath, File.class);
        digester.addSetProperties(fileXPath);
        digester.addSetNext(fileXPath, "addFile", File.class.getName());

        String bugXPath = "checkstyle/file/error";
        digester.addObjectCreate(bugXPath, Error.class);
        digester.addSetProperties(bugXPath);
        digester.addSetNext(bugXPath, "addError", Error.class.getName());

        try (Reader reader = readerFactory.create()) {
            CheckStyle checkStyle = digester.parse(reader);
            if (checkStyle == null) {
                throw new ParsingException("Input stream is not a Checkstyle file.");
            }

            return convert(checkStyle);
        }
        catch (IOException | SAXException exception) {
            throw new ParsingException(exception);
        }
    }

    /**
     * Converts the internal structure to the annotations API.
     *
     * @param collection
     *         the internal maven module
     *
     * @return a maven module of the annotations API
     */
    private Report convert(final CheckStyle collection) {
        try (IssueBuilder issueBuilder = new IssueBuilder()) {
            var report = new Report();

            for (File file : collection.getFiles()) {
                if (isValidWarning(file)) {
                    for (Error error : file.getErrors()) {
                        issueBuilder.guessSeverity(error.getSeverity());
                        String source = error.getSource();
                        issueBuilder.setType(getType(source));
                        issueBuilder.setCategory(getCategory(source));
                        issueBuilder.setMessage(error.getMessage());
                        issueBuilder.setLineStart(error.getLine());
                        issueBuilder.setFileName(file.getName());
                        issueBuilder.setColumnStart(error.getColumn());
                        report.add(issueBuilder.buildAndClean());
                    }
                }
            }
            return report;
        }
    }

    @CheckForNull
    private String getCategory(@CheckForNull final String source) {
        return StringUtils.capitalize(getType(StringUtils.substringBeforeLast(source, ".")));
    }

    @CheckForNull
    private String getType(@CheckForNull final String source) {
        if (StringUtils.contains(source, '.')) {
            return StringUtils.substringAfterLast(source, ".");
        }
        return source;
    }

    /**
     * Returns {@code true} if this warning is valid or {@code false} if the warning can't be processed by the
     * checkstyle plug-in.
     *
     * @param file
     *         the file to check
     *
     * @return {@code true} if this warning is valid
     */
    private boolean isValidWarning(final File file) {
        return !StringUtils.endsWith(file.getName(), "package.html");
    }
}
