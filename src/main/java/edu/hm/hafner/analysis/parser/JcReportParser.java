package edu.hm.hafner.analysis.parser;

import org.xml.sax.SAXException;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.SecureDigester;
import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * JcReportParser-Class. This class parses from the jcReport.xml and creates warnings from its content.
 *
 * @author Johann Vierthaler, johann.vierthaler@web.de
 */
public class JcReportParser extends IssueParser {
    @Serial
    private static final long serialVersionUID = -1302787609831475403L;

    @Override
    public Report parseReport(final ReaderFactory reader) {
        try (var issueBuilder = new IssueBuilder()) {
            var report = createReport(reader);
            var warnings = new Report();
            for (int i = 0; i < report.getFiles().size(); i++) {
                var file = report.getFiles().get(i);

                for (int j = 0; j < file.getItems().size(); j++) {
                    var item = file.getItems().get(j);
                    issueBuilder.setFileName(file.getName())
                            .setLineStart(item.getLine())
                            .setColumnStart(item.getColumn())
                            .setColumnEnd(item.getEndcolumn())
                            .setCategory(item.getFindingtype())
                            .setPackageName(file.getPackageName())
                            .setMessage(item.getMessage())
                            .guessSeverity(item.getSeverity());

                    warnings.add(issueBuilder.buildAndClean());
                }
            }
            return warnings;
        }
    }

    /**
     * Creates a Report-Object out of the content within the JcReport.xml.
     *
     * @param readerFactory
     *         the Reader-object that is the source to build the Report-Object.
     *
     * @return the finished Report-Object that creates the Warnings.
     * @throws ParsingException
     *         due to digester.parse(new InputSource(source))
     */
    JcReport createReport(final ReaderFactory readerFactory)
            throws ParsingException {
        var digester = new SecureDigester(JcReportParser.class);

        var report = "report";
        digester.addObjectCreate(report, JcReport.class);
        digester.addSetProperties(report);

        var file = "report/file";
        digester.addObjectCreate(file, File.class);
        digester.addSetProperties(file, "package", "packageName");
        digester.addSetProperties(file, "src-dir", "srcdir");
        digester.addSetProperties(file);
        digester.addSetNext(file, "addFile", File.class.getName());

        var item = "report/file/item";
        digester.addObjectCreate(item, Item.class);
        digester.addSetProperties(item);
        digester.addSetProperties(item, "finding-type", "findingtype");
        digester.addSetProperties(item, "end-line", "endline");
        digester.addSetProperties(item, "end-column", "endcolumn");
        digester.addSetNext(item, "addItem", Item.class.getName());

        try (var reader = readerFactory.create()) {
            return digester.parse(reader);
        }
        catch (IOException | SAXException e) {
            throw new ParsingException(e);
        }
    }

    /**
     * File-Class. Stores field to create a warning. It represents the File-Tags within the report.xml. The
     * Java-Bean-Conformity was chosen due to the digesters style of assigning.
     *
     * @author Johann Vierthaler, johann.vierthaler@web.de
     */
    @SuppressWarnings("PMD.DataClass")
    public static class File {
        @CheckForNull
        private String name;
        @CheckForNull
        private String packageName;
        @CheckForNull
        private String srcdir;
        private final List<Item> items = new ArrayList<>();

        /**
         * These properties are not used to create Warnings. It was decided to keep them available when Jenkins is modified
         * and needs access to these fields;
         */
        @CheckForNull
        private String level;
        @CheckForNull
        private String loc;
        @CheckForNull
        private String classname;

        /**
         * Getter for the Item-Collection.
         *
         * @return unmodifiable collection of Item-Objects
         */
        public List<Item> getItems() {
            return Collections.unmodifiableList(items);
        }

        /**
         * Adds an Item-Object to the collection items.
         *
         * @param item add this item.
         */
        public void addItem(final Item item) {
            items.add(item);
        }


        /**
         * Getter for className-Field.
         *
         * @return String className.
         */
        @CheckForNull
        public String getClassname() {
            return classname;
        }

        /**
         * Setter for className-Field.
         *
         * @param classname lassNamesetter
         */
        public void setClassname(@CheckForNull final String classname) {
            this.classname = classname;
        }

        /**
         * Getter for level-Field.
         *
         * @return level
         */
        @CheckForNull
        public String getLevel() {
            return level;
        }

        /**
         * Setter for level-Field.
         *
         * @param level set level
         */
        public void setLevel(@CheckForNull final String level) {
            this.level = level;
        }


        /**
         * Getter for loc-Field.
         *
         * @return loc loc
         */
        @CheckForNull
        public String getLoc() {
            return loc;
        }

        /**
         * Setter for loc-Field.
         *
         * @param loc locsetter
         */
        public void setLoc(@CheckForNull final String loc) {
            this.loc = loc;
        }


        /**
         * Getter for name-Field.
         *
         * @return name name
         */
        @CheckForNull
        public String getName() {
            return name;
        }

        /**
         * Setter for Name-Field.
         *
         * @param name name
         */
        public void setName(@CheckForNull final String name) {
            this.name = name;
        }


        /**
         * Getter for packageName-Field.
         *
         * @return packageName packageName.
         */
        @CheckForNull
        public String getPackageName() {
            return packageName;
        }

        /**
         * Setter for packageName-Field.
         *
         * @param packageName packageName Setter
         */
        public void setPackageName(@CheckForNull final String packageName) {
            this.packageName = packageName;
        }

        /**
         * Getter for srcdir-Field.
         *
         * @return srcdir srcdir.
         */
        @CheckForNull
        public String getSrcdir() {
            return srcdir;
        }

        /**
         * Setter for srcdir-Field.
         *
         * @param srcdir srcdir
         */
        public void setSrcdir(@CheckForNull final String srcdir) {
            this.srcdir = srcdir;
        }
    }

    /**
     * This the Item-Class The Java-Bean-Conformity was chosen due to the digesters style of assigning. It represents the
     * Item-Tags within the report.xml. Items have properties, that are mandatory to create a warning.
     *
     * @author Johann Vierthaler, johann.vierthaler@web.de
     */
    @SuppressWarnings("PMD.DataClass")
    public static class Item {
        @CheckForNull
        private String column;
        @CheckForNull
        private String findingtype;
        @CheckForNull
        private String line;
        @CheckForNull
        private String message;
        @CheckForNull
        private String origin;
        @CheckForNull
        private String severity;
        @CheckForNull
        private String endcolumn;

        /**
         * Although this property is not used. It was decided to keep it available when Jenkins is modified and needs access
         * to this field;
         */
        @CheckForNull
        private String endline;

        /**
         * Getter for column-Field.
         *
         * @return column string
         */
        @CheckForNull
        public String getColumn() {
            return column;
        }

        /**
         * Setter for Column-Field.
         *
         * @param column setter
         */
        public void setColumn(@CheckForNull final String column) {
            this.column = column;
        }

        /**
         * Getter for findingtype-Field.
         *
         * @return findingtype getter
         */
        @CheckForNull
        public String getFindingtype() {
            return findingtype;
        }

        /**
         * Setter for findingtype-Field.
         *
         * @param findingtype setter
         */
        public void setFindingtype(@CheckForNull final String findingtype) {
            this.findingtype = findingtype;
        }

        /**
         * Getter for line-Field.
         *
         * @return line getter
         */
        @CheckForNull
        public String getLine() {
            return line;
        }

        /**
         * Setter for line-Field.
         *
         * @param line setter
         */
        public void setLine(@CheckForNull final String line) {
            this.line = line;
        }

        /**
         * Getter for message-Field.
         *
         * @return message getter
         */
        @CheckForNull
        public String getMessage() {
            return message;
        }

        /**
         * Setter for message-Field.
         *
         * @param message setter
         */
        public void setMessage(@CheckForNull final String message) {
            this.message = message;
        }

        /**
         * Getter for origin-Field.
         *
         * @return origin getter
         */
        @CheckForNull
        public String getOrigin() {
            return origin;
        }

        /**
         * Setter for origin-Field.
         *
         * @param origin setter
         */
        public void setOrigin(@CheckForNull final String origin) {
            this.origin = origin;
        }

        /**
         * Getter for severity-Field.
         *
         * @return severity getter
         */
        @CheckForNull
        public String getSeverity() {
            return severity;
        }

        /**
         * Setter for severtiy-Field.
         *
         * @param severity setter
         */
        public void setSeverity(@CheckForNull final String severity) {
            this.severity = severity;
        }


        /**
         * Getter for endline-Field.
         *
         * @return endline getter
         */
        @CheckForNull
        public String getEndline() {
            return endline;
        }

        /**
         * Setter for endline-Field.
         *
         * @param endline setter
         */
        public void setEndline(@CheckForNull final String endline) {
            this.endline = endline;
        }

        /**
         * Getter for endcolumn-Field.
         *
         * @return endcolumn getter
         */
        @CheckForNull
        public String getEndcolumn() {
            return endcolumn;
        }

        /**
         * Setter for endcolumn-Field.
         *
         * @param endcolumn setter
         */
        public void setEndcolumn(@CheckForNull final String endcolumn) {
            this.endcolumn = endcolumn;
        }
    }

    /**
     * This is the Report-Class. It is mandatory to create Warnings. It represents the outer-most node within the
     * report.xml.
     *
     * @author Johann Vierthaler, johann.vierthaler@web.de
     */
    @SuppressFBWarnings("EI")
    public static class JcReport {
        private List<File> files = new ArrayList<>();

        /**
         * Returns an unmodifiable Collection.
         *
         * @return files getter
         */
        public List<File> getFiles() {
            return Collections.unmodifiableList(files);
        }

        /**
         * Setter for the List files.
         *
         * @param files a list of files.
         */
        public void setFiles(final List<File> files) {
            this.files = files;
        }

        /**
         * Adds a new File to the Collection.
         *
         * @param file setter
         */
        public void addFile(final File file) {
            files.add(file);
        }
    }
}
