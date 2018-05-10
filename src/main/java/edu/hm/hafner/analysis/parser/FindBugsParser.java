package edu.hm.hafner.analysis.parser; // NOPMD

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.digester3.Digester;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.xml.sax.SAXException;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.LineRange;
import edu.hm.hafner.analysis.LineRangeList;
import edu.hm.hafner.analysis.ParsingCanceledException;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.SecureDigester;
import static edu.hm.hafner.analysis.parser.FindBugsParser.PriorityProperty.*;
import edu.hm.hafner.util.VisibleForTesting;
import edu.umd.cs.findbugs.BugAnnotation;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.Project;
import edu.umd.cs.findbugs.SortedBugCollection;
import edu.umd.cs.findbugs.SourceLineAnnotation;
import edu.umd.cs.findbugs.ba.SourceFile;
import edu.umd.cs.findbugs.ba.SourceFinder;

/**
 * A parser for the native FindBugs XML files.
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings("classfanoutcomplexity")
public class FindBugsParser extends IssueParser {
    private static final long serialVersionUID = 8306319007761954027L;

    /**
     * FindBugs 2 and 3 classifies issues using the bug rank and priority (now renamed confidence). Bugs are given a
     * rank 1-20, and grouped into the categories scariest (rank 1-4), scary (rank 5-9), troubling (rank 10-14), and of
     * concern (rank 15-20). Many people were confused by the priority reported by FindBugs, and considered all HIGH
     * priority issues to be important. To reflect the actually meaning of this attribute of issues, it has been renamed
     * confidence. Issues of different bug patterns should be compared by their rank, not their confidence.
     */
    public enum PriorityProperty {
        /** Use the priority/confidence to create corresponding {@link Priority priorities}. */
        CONFIDENCE,
        /** Use rank to create corresponding {@link Priority priorities}. */
        RANK
    }

    private static final String DOT = ".";
    private static final String SLASH = "/";

    private static final int HIGH_PRIORITY_LOWEST_RANK = 4;
    private static final int NORMAL_PRIORITY_LOWEST_RANK = 9;

    /** Determines whether to use the rank when evaluation the priority. */
    private final PriorityProperty priorityProperty;

    private boolean isFirstError = true;

    /**
     * Creates a new instance of {@link FindBugsParser}.
     *
     * @param priorityProperty
     *         determines whether to use the rank or confidence when evaluation the {@link Priority}
     */
    public FindBugsParser(final PriorityProperty priorityProperty) {
        super();

        this.priorityProperty = priorityProperty;
    }

    @Override
    @SuppressWarnings({"resource", "IOResourceOpenedButNotSafelyClosed"})
    public Report parse(final File file, final Charset charset,
            final Function<String, String> preProcessor)
            throws ParsingCanceledException, ParsingException {
        Collection<String> sources = new ArrayList<>();
        String moduleRoot = StringUtils.substringBefore(file.getAbsolutePath().replace('\\', '/'), "/target/");
        sources.add(moduleRoot + "/src/main/java");
        sources.add(moduleRoot + "/src/test/java");
        sources.add(moduleRoot + "/src");
        return parse(() -> new FileInputStream(file), sources, new IssueBuilder());
    }

    @VisibleForTesting
    Report parse(final InputStreamProvider file, final Collection<String> sources, final IssueBuilder builder)
            throws ParsingException {
        try (InputStream input = file.getInputStream()) {
            Map<String, String> hashToMessageMapping = new HashMap<>();
            Map<String, String> categories = new HashMap<>();
            for (XmlBugInstance bug : preParse(input)) {
                hashToMessageMapping.put(bug.getInstanceHash(), bug.getMessage());
                categories.put(bug.getType(), bug.getCategory());
            }

            return parse(file.getInputStream(), sources, builder, hashToMessageMapping, categories);
        }
        catch (SAXException | DocumentException | IOException exception) {
            throw new ParsingException(exception);
        }
    }

    /**
     * Pre-parses a file for some information not available from the FindBugs parser. Creates a mapping of FindBugs
     * warnings to messages. A bug is represented by its unique hash code. Also obtains original categories for bug
     * types.
     *
     * @param file
     *         the FindBugs XML file
     *
     * @return the map of warning messages
     * @throws SAXException
     *         if the file contains no valid XML
     * @throws IOException
     *         signals that an I/O exception has occurred.
     */
    @VisibleForTesting
    List<XmlBugInstance> preParse(final InputStream file) throws SAXException, IOException {
        Digester digester = new SecureDigester(FindBugsParser.class);

        String rootXPath = "BugCollection/BugInstance";
        digester.addObjectCreate(rootXPath, XmlBugInstance.class);
        digester.addSetProperties(rootXPath);

        String fileXPath = rootXPath + "/LongMessage";
        digester.addCallMethod(fileXPath, "setMessage", 0);

        digester.addSetNext(rootXPath, "add", Object.class.getName());
        ArrayList<XmlBugInstance> bugs = new ArrayList<>();
        digester.push(bugs);
        digester.parse(file);

        return bugs;
    }

    /**
     * Returns the parsed FindBugs analysis file. This scanner accepts files in the native FindBugs format.
     *
     * @param builder
     *         the issue builder
     * @param file
     *         the FindBugs analysis file
     * @param sources
     *         a collection of folders to scan for source files
     * @param hashToMessageMapping
     *         mapping of hash codes to messages
     * @param categories
     *         mapping from bug types to their categories
     *
     * @return the parsed result (stored in the module instance)
     * @throws IOException
     *         if the file could not be parsed
     * @throws DocumentException
     *         in case of a parser exception
     */
    private Report parse(final InputStream file, final Collection<String> sources,
            final IssueBuilder builder, final Map<String, String> hashToMessageMapping,
            final Map<String, String> categories) throws IOException, DocumentException {

        SortedBugCollection collection = readXml(file);

        Project project = collection.getProject();
        for (String sourceFolder : sources) {
            project.addSourceDir(sourceFolder);
        }

        SourceFinder sourceFinder = new SourceFinder(project);
        if (StringUtils.isNotBlank(project.getProjectName())) {
            builder.setModuleName(project.getProjectName());
        }

        Collection<BugInstance> bugs = collection.getCollection();

        Report report = new Report();
        for (BugInstance warning : bugs) {
            SourceLineAnnotation sourceLine = warning.getPrimarySourceLineAnnotation();

            String message = warning.getMessage();
            String type = warning.getType();
            String category = categories.get(type);
            if (category == null) { // alternately, only if warning.getBugPattern().getType().equals("UNKNOWN")
                category = warning.getBugPattern().getCategory();
            }
            builder.setPriority(getPriority(warning))
                    .setMessage(StringUtils.defaultIfEmpty(hashToMessageMapping.get(warning.getInstanceHash()), message))
                    .setCategory(category)
                    .setType(type)
                    .setLineStart(sourceLine.getStartLine())
                    .setLineEnd(sourceLine.getEndLine())
                    .setFileName(findSourceFile(project, sourceFinder, sourceLine))
                    .setPackageName(warning.getPrimaryClass().getPackageName())
                    .setFingerprint(warning.getInstanceHash());
            setAffectedLines(warning, builder, new LineRange(sourceLine.getStartLine(), sourceLine.getEndLine()));

            report.add(builder.build());
        }

        return report;
    }

    private Priority getPriority(final BugInstance warning) {
        if (priorityProperty == RANK) {
            return getPriorityByRank(warning);
        }
        else {
            return getPriorityByPriority(warning);
        }
    }

    private SortedBugCollection readXml(final InputStream file) throws IOException, DocumentException {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(FindBugsParser.class.getClassLoader());
            SortedBugCollection collection = new SortedBugCollection();
            collection.readXML(file);
            return collection;
        }
        finally {
            Thread.currentThread().setContextClassLoader(contextClassLoader);
        }
    }

    private void setAffectedLines(final BugInstance warning, final IssueBuilder builder,
            final LineRange primary) {
        Iterator<BugAnnotation> annotationIterator = warning.annotationIterator();
        LineRangeList lineRanges = new LineRangeList();
        while (annotationIterator.hasNext()) {
            BugAnnotation bugAnnotation = annotationIterator.next();
            if (bugAnnotation instanceof SourceLineAnnotation) {
                SourceLineAnnotation annotation = (SourceLineAnnotation) bugAnnotation;
                LineRange lineRange = new LineRange(annotation.getStartLine(), annotation.getEndLine());
                if (!lineRanges.contains(lineRange) && !primary.equals(lineRange)) {
                    lineRanges.add(lineRange);
                }
            }
        }
        builder.setLineRanges(lineRanges);
    }

    private String findSourceFile(final Project project, final SourceFinder sourceFinder,
            final SourceLineAnnotation sourceLine) {
        try {
            SourceFile sourceFile = sourceFinder.findSourceFile(sourceLine);
            return sourceFile.getFullFileName();
        }
        catch (IOException ignored) {
            if (isFirstError) {
                StringBuilder builder = new StringBuilder("Can't resolve absolute file name for file ");
                builder.append(sourceLine.getSourceFile());
                builder.append(", dir list = ");
                builder.append(project.getSourceDirList());
                isFirstError = false;
                Logger.getLogger(getClass().getName()).log(Level.WARNING, builder.toString());
            }
            return sourceLine.getPackageName().replace(DOT, SLASH) + SLASH + sourceLine.getSourceFile();
        }
    }

    /**
     * Maps the FindBugs library rank to plug-in priority enumeration.
     *
     * @param warning
     *         the FindBugs warning
     *
     * @return mapped priority enumeration
     */
    private Priority getPriorityByRank(final BugInstance warning) {
        int rank = warning.getBugRank();
        if (rank <= HIGH_PRIORITY_LOWEST_RANK) {
            return Priority.HIGH;
        }
        if (rank <= NORMAL_PRIORITY_LOWEST_RANK) {
            return Priority.NORMAL;
        }
        return Priority.LOW;
    }

    /**
     * Maps the FindBugs library priority to plug-in priority enumeration.
     *
     * @param warning
     *         the FindBugs warning
     *
     * @return mapped priority enumeration
     */
    private Priority getPriorityByPriority(final BugInstance warning) {
        switch (warning.getPriority()) {
            case 1:
                return Priority.HIGH;
            case 2:
                return Priority.NORMAL;
            default:
                return Priority.LOW;
        }
    }

    /**
     * Provides an input stream for the parser.
     */
    interface InputStreamProvider {
        InputStream getInputStream() throws IOException;
    }

    /**
     * Java Bean to create the mapping of hash codes to messages using the Digester
     * XML parser.
     *
     * @author Ullrich Hafner
     */
    @SuppressWarnings("all")
    public static class XmlBugInstance {
        private String instanceHash;
        private String message;
        private String type;
        private String category;

        public String getInstanceHash() {
            return instanceHash;
        }

        public void setInstanceHash(final String instanceHash) {
            this.instanceHash = instanceHash;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(final String message) {
            this.message = message;
        }

        public String getType() {
            return type;
        }

        public void setType(final String type) {
            this.type = type;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(final String category) {
            this.category = category;
        }

    }
}
