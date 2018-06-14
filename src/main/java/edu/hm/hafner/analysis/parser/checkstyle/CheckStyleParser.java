package edu.hm.hafner.analysis.parser.checkstyle;

import java.io.IOException;
import java.io.Reader;
import java.util.Optional;
import java.util.function.Function;

import org.apache.commons.digester3.Digester;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.SAXException;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.ParsingCanceledException;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.SecureDigester;

/**
 * A parser for Checkstyle XML files.
 *
 * @author Ullrich Hafner
 */
public class CheckStyleParser extends AbstractParser {
    private static final long serialVersionUID = -3187275729854832128L;

    @Override
    public Report parse(final Reader reader, final Function<String, String> preProcessor)
            throws ParsingCanceledException, ParsingException {
        try {
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
        Report report = new Report();

        for (File file : collection.getFiles()) {
            if (isValidWarning(file)) {
                for (Error error : file.getErrors()) {
                    IssueBuilder builder = new IssueBuilder();
                    mapPriority(error).ifPresent(builder::setPriority);

                    String source = error.getSource();
                    builder.setType(getType(source));
                    builder.setCategory(getCategory(source));
                    builder.setMessage(error.getMessage());
                    builder.setLineStart(error.getLine());
                    builder.setFileName(file.getName());
                    builder.setColumnStart(error.getColumn());
                    report.add(builder.build());
                }
            }
        }
        return report;
    }

    private String getCategory(final String source) {
        return StringUtils.capitalize(getType(StringUtils.substringBeforeLast(source, ".")));
    }

    private String getType(final String source) {
        return StringUtils.substringAfterLast(source, ".");
    }

    private Optional<Priority> mapPriority(final Error error) {
        if ("error".equalsIgnoreCase(error.getSeverity())) {
            return Optional.of(Priority.HIGH);
        }
        if ("warning".equalsIgnoreCase(error.getSeverity())) {
            return Optional.of(Priority.NORMAL);
        }
        if ("info".equalsIgnoreCase(error.getSeverity())) {
            return Optional.of(Priority.LOW);
        }
        return Optional.empty();
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
        return !file.getName().endsWith("package.html");
    }
}

