package edu.hm.hafner.analysis.parser;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.digester3.NodeCreateRule;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.SecureDigester;
import edu.hm.hafner.util.SecureXmlParserFactory;
import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.IOException;
import java.io.Serial;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A parser for Checkstyle XML files.
 *
 * @author Ullrich Hafner
 */
public class CheckStyleParser extends IssueParser {
    @Serial
    private static final long serialVersionUID = -3187275729854832128L;

    @Override
    public boolean accepts(final ReaderFactory readerFactory) {
        return isXmlFile(readerFactory);
    }

    @Override
    public Report parseReport(final ReaderFactory readerFactory) throws ParsingException {
        var digester = new SecureDigester(CheckStyleParser.class);

        var rootXPath = "checkstyle";
        digester.addObjectCreate(rootXPath, CheckStyle.class);
        digester.addSetProperties(rootXPath);

        var fileXPath = "checkstyle/file";
        digester.addObjectCreate(fileXPath, File.class);
        digester.addSetProperties(fileXPath);
        digester.addSetNext(fileXPath, "addFile", File.class.getName());

        var bugXPath = "checkstyle/file/error";
        digester.addObjectCreate(bugXPath, Error.class);
        digester.addSetProperties(bugXPath);
        digester.addSetNext(bugXPath, "addError", Error.class.getName());

        try (var reader = readerFactory.create()) {
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
        try (var issueBuilder = new IssueBuilder()) {
            var report = new Report();

            for (File file : collection.getFiles()) {
                if (isValidWarning(file)) {
                    for (Error error : file.getErrors()) {
                        issueBuilder.guessSeverity(error.getSeverity());
                        var source = error.getSource();
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

    /**
     * Java Bean class for a file of the Checkstyle format.
     *
     * @author Ullrich Hafner
     */
    public static class File {
        /** Name of the file. */
        @CheckForNull
        private String name;
        /** All errors of this file. */
        private final List<Error> errors = new ArrayList<>();

        /**
         * Adds a new violation to this file.
         *
         * @param violation
         *            the new violation
         */
        public void addError(final Error violation) {
            errors.add(violation);
        }

        /**
         * Returns all violations of this file. The returned collection is
         * read-only.
         *
         * @return all violations in this file
         */
        public Collection<Error> getErrors() {
            return Collections.unmodifiableCollection(errors);
        }

        /**
         * Returns the name of this file.
         *
         * @return the name of this file
         */
        @CheckForNull
        public String getName() {
            return name;
        }

        /**
         * Sets the name of this file to the specified value.
         *
         * @param name the value to set
         */
        public void setName(@CheckForNull final String name) {
            this.name = name;
        }
    }

    /**
     * Java Bean class for a violation of the Checkstyle format.
     *
     * @author Ullrich Hafner
     */
    @SuppressWarnings({"all", "JavaLangClash"})
    public static class Error {
        @CheckForNull
        private String source;
        @CheckForNull
        private String severity;
        @CheckForNull
        private String message;
        private int line;
        private int column;

        public int getColumn() {
            return column;
        }

        public void setColumn(final int column) {
            this.column = column;
        }

        @CheckForNull
        public String getSource() {
            return source;
        }

        public void setSource(final String source) {
            this.source = source;
        }

        @CheckForNull
        public String getSeverity() {
            return severity;
        }

        public void setSeverity(final String severity) {
            this.severity = severity;
        }

        @CheckForNull
        public String getMessage() {
            return message;
        }

        public void setMessage(final String message) {
            this.message = message;
        }

        public int getLine() {
            return line;
        }

        public void setLine(final int line) {
            this.line = line;
        }
    }

    /**
     * Java Bean class for a errors collection of the Checkstyle format.
     *
     * @author Ullrich Hafner
     */
    public static class CheckStyle {
        /** All files of this violations collection. */
        private final List<File> files = new ArrayList<>();

        /**
         * Adds a new file to this bug collection.
         *
         * @param file the file to add
         */
        public void addFile(final File file) {
            files.add(file);
        }

        /**
         * Returns all files of this violations collection. The returned collection is
         * read-only.
         *
         * @return all files of this bug collection
         */
        public Collection<File> getFiles() {
            return Collections.unmodifiableCollection(files);
        }
    }

    /**
     * Java Bean class representing a Checkstyle rule.
     *
     * @author Ullrich Hafner
     */
    @SuppressWarnings("PMD.DataClass")
    public static class Rule {
        /** Description to indicate that the rules stored in this plug-in don't match with the generators version. */
        static final String UNDEFINED_DESCRIPTION = StringUtils.EMPTY;
        /** The name of the subsection that defines a description in the docbook files. */
        private static final String DESCRIPTION_SUBSECTION_NAME = "Description";

        @CheckForNull
        private String name;
        @CheckForNull
        private String description;

        /**
         * Instantiates a new rule.
         */
        public Rule() {
            // nothing to do
        }

        /**
         * Instantiates a new rule.
         *
         * @param name
         *         the name of the rule
         */
        public Rule(@CheckForNull final String name) {
            this.name = name;
            description = UNDEFINED_DESCRIPTION;
        }

        /**
         * Returns the name of this rule.
         *
         * @return the name
         */
        public String getName() {
            return StringUtils.defaultString(name);
        }

        /**
         * Sets the name of this rule.
         *
         * @param name
         *         the name
         */
        public void setName(@CheckForNull final String name) {
            this.name = name;
        }

        /**
         * Returns the description of this rule.
         *
         * @return the description
         */
        public String getDescription() {
            return StringUtils.defaultString(description);
        }

        /**
         * Sets the description of this rule. The description is only set if the topic is a description.
         *
         * @param topic
         *         the topic that might contain the description
         */
        @SuppressFBWarnings("IMPROPER_UNICODE")
        public void setDescription(final Topic topic) {
            if (DESCRIPTION_SUBSECTION_NAME.equalsIgnoreCase(topic.getName())) {
                description = topic.getValue();
            }
        }
    }

    /**
     * Java Bean class representing a DocBook subsection.
     *
     * @author Ullrich Hafner
     */
    @SuppressWarnings("PMD.DataClass")
    public static class Topic {
        @CheckForNull
        private String name;
        @CheckForNull
        private String value;

        /**
         * Returns the name of this topic.
         *
         * @return the name
         */
        public String getName() {
            return StringUtils.defaultString(name);
        }

        /**
         * Sets the name of this topic.
         *
         * @param name
         *         the name
         */
        public void setName(@CheckForNull final String name) {
            this.name = name;
        }

        /**
         * Returns the value of this topic.
         *
         * @return the value
         */
        public String getValue() {
            return StringUtils.defaultString(value);
        }

        /**
         * Sets the value of this topic.
         *
         * @param value
         *         the value
         */
        public void setValue(@CheckForNull final String value) {
            this.value = value;
        }
    }

    /**
     * Digester rule to parse the actual content of a DocBook subsection node. Does not interpret XML elements that are
     * children of a subsection.
     *
     * @author Ullrich Hafner
     */
    public static class TopicRule extends NodeCreateRule {
        /**
         * Instantiates a new topic rule.
         *
         * @throws ParserConfigurationException
         *         the parser configuration exception
         */
        TopicRule() throws ParserConfigurationException {
            super(Node.ELEMENT_NODE);
        }

        @Override
        public void end(final String namespace, final String name)
                throws TransformerException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
            Element subsection = getDigester().pop();
            var description = extractNodeContent(subsection);

            MethodUtils.invokeExactMethod(getDigester().peek(), "setValue", description);
        }

        /**
         * Extracts the node content. Basically returns every character in the subsection element.
         *
         * @param subsection
         *         the subsection of a rule
         *
         * @return the node content
         * @throws TransformerException
         *         in case of an error
         */
        private String extractNodeContent(final Element subsection) throws TransformerException {
            var content = new StringWriter();

            var transformer = new SecureXmlParserFactory().createTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(new DOMSource(subsection), new StreamResult(content));
            var text = content.toString();
            var prefixRemoved = StringUtils.substringAfter(text, ">");
            var suffixRemoved = StringUtils.substringBeforeLast(prefixRemoved, "<");

            var endSourceRemoved = StringUtils.replace(suffixRemoved, "</source>", "</code></pre>");

            return StringUtils.replace(endSourceRemoved, "<source>", "<pre><code>");
        }
    }
}
