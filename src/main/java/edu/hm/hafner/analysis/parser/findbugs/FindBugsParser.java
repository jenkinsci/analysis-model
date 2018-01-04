package edu.hm.hafner.analysis.parser.findbugs; // NOPMD

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.digester3.Digester;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.xml.sax.SAXException;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.ParsingCanceledException;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Priority;
import edu.umd.cs.findbugs.BugAnnotation;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.Project;
import edu.umd.cs.findbugs.SortedBugCollection;
import edu.umd.cs.findbugs.SourceLineAnnotation;
import edu.umd.cs.findbugs.ba.SourceFile;
import edu.umd.cs.findbugs.ba.SourceFinder;

/**
 * A parser for the native FindBugs XML files (ant task, batch file or maven-findbugs-plugin >= 1.2).
 *
 * @author Ulli Hafner
 */
// CHECKSTYLE:COUPLING-OFF
public class FindBugsParser implements Serializable {
    /** Unique ID of this class. */
    private static final long serialVersionUID = 8306319007761954027L;

    private static final String DOT = ".";
    private static final String SLASH = "/";

    private static final int HIGH_PRIORITY_LOWEST_RANK = 4;
    private static final int NORMAL_PRIORITY_LOWEST_RANK = 9;

    /** Determines whether to use the rank when evaluation the priority. @since 4.26 */
    private final boolean isRankActivated;

    private boolean isFirstError = true;

    /**
     * Creates a new instance of {@link FindBugsParser}.
     *
     * @param isRankActivated
     *         determines whether to use the rank when evaluation the priority
     */
    public FindBugsParser(final boolean isRankActivated) {
        this.isRankActivated = isRankActivated;
    }

    public Issues<Issue> parse(final File file, final IssueBuilder builder)
            throws ParsingCanceledException, ParsingException {
        Collection<String> sources = new ArrayList<>();
        String moduleRoot = StringUtils.substringBefore(file.getAbsolutePath().replace('\\', '/'), "/target/");
        sources.add(moduleRoot + "/src/main/java");
        sources.add(moduleRoot + "/src/test/java");
        sources.add(moduleRoot + "/src");
        return parse(file, sources, builder);
    }

    /**
     * Returns the parsed FindBugs analysis file. This scanner accepts files in the native FindBugs format.
     *
     * @param builder
     *         the issue builde
     * @param file
     *         the FindBugs analysis file
     * @param sources
     *         a collection of folders to scan for source files
     *
     * @return the parsed result (stored in the module instance)
     * @throws ParsingException
     *         if the file could not be parsed
     */
    public Issues<Issue> parse(final File file, final Collection<String> sources, final IssueBuilder builder)
            throws ParsingException {
        return parse(() -> new FileInputStream(file), sources, builder);
    }

    public Issues<Issue> parse(final InputStreamProvider file, final Collection<String> sources,
            final IssueBuilder builder) throws ParsingException {
        InputStream input = null;
        try {
            input = file.getInputStream();
            Map<String, String> hashToMessageMapping = new HashMap<>();
            Map<String, String> categories = new HashMap<>();
            for (XmlBugInstance bug : preParse(input)) {
                hashToMessageMapping.put(bug.getInstanceHash(), bug.getMessage());
                categories.put(bug.getType(), bug.getCategory());
            }
            IOUtils.closeQuietly(input);

            input = file.getInputStream();
            return parse(input, sources, builder, hashToMessageMapping, categories);
        }
        catch (SAXException | DocumentException | IOException exception) {
            throw new ParsingException(exception);
        }
        finally {
            IOUtils.closeQuietly(input);
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
    public List<XmlBugInstance> preParse(final InputStream file) throws SAXException, IOException {
        Digester digester = new Digester();
        digester.setValidating(false);
        digester.setClassLoader(FindBugsParser.class.getClassLoader());

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
    private Issues<Issue> parse(final InputStream file, final Collection<String> sources,
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

        Issues<Issue> issues = new Issues<>();
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

            // FIXME: bug.setInstanceHash(warning.getInstanceHash());
            // FIXME: bug.setRank(warning.getBugRank());
            setAffectedLines(warning, builder);

            issues.add(builder.build());
        }

        return issues;
    }

    private Priority getPriority(final BugInstance warning) {
        if (isRankActivated) {
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

    private void setAffectedLines(final BugInstance warning, final IssueBuilder builder) {
        Iterator<BugAnnotation> annotationIterator = warning.annotationIterator();
        while (annotationIterator.hasNext()) {
            BugAnnotation bugAnnotation = annotationIterator.next();
            if (bugAnnotation instanceof SourceLineAnnotation) {
                SourceLineAnnotation annotation = (SourceLineAnnotation) bugAnnotation;
                // FIXME: bug.addLineRange(new LineRange(annotation.getStartLine(), annotation.getEndLine()));
            }
        }
    }

    private String findSourceFile(final Project project, final SourceFinder sourceFinder,
            final SourceLineAnnotation sourceLine) {
        try {
            SourceFile sourceFile = sourceFinder.findSourceFile(sourceLine);
            return sourceFile.getFullFileName();
        }
        catch (IOException ignored) {
            StringBuilder sb = new StringBuilder("Can't resolve absolute file name for file ");
            sb.append(sourceLine.getSourceFile());
            if (isFirstError) {
                sb.append(", dir list = ");
                sb.append(project.getSourceDirList());
                isFirstError = false;
            }
            Logger.getLogger(getClass().getName()).log(Level.WARNING, sb.toString());
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
    public interface InputStreamProvider {
        InputStream getInputStream() throws IOException;
    }
}
