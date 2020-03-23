package edu.hm.hafner.analysis.parser; // NOPMD

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.digester3.Digester;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.xml.sax.SAXException;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.LineRange;
import edu.hm.hafner.analysis.LineRange.LineRangeBuilder;
import edu.hm.hafner.analysis.LineRangeList;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.SecureDigester;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.util.VisibleForTesting;
import edu.umd.cs.findbugs.BugAnnotation;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.Project;
import edu.umd.cs.findbugs.SortedBugCollection;
import edu.umd.cs.findbugs.SourceLineAnnotation;
import edu.umd.cs.findbugs.annotations.Nullable;
import edu.umd.cs.findbugs.ba.SourceFile;
import edu.umd.cs.findbugs.ba.SourceFinder;

import static edu.hm.hafner.analysis.parser.FindBugsParser.PriorityProperty.*;

/**
 * A parser for the native FindBugs XML files.
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings("ClassFanOutComplexity")
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
        /** Use the priority/confidence to create corresponding {@link Severity priorities}. */
        CONFIDENCE,
        /** Use rank to create corresponding {@link Severity priorities}. */
        RANK
    }

    private static final String DOT = ".";
    private static final String SLASH = "/";

    private static final int HIGH_PRIORITY_LOWEST_RANK = 4;
    private static final int NORMAL_PRIORITY_LOWEST_RANK = 9;

    /** Determines whether to use the rank when evaluation the priority. */
    private final PriorityProperty priorityProperty;

    /**
     * Creates a new instance of {@link FindBugsParser}.
     *
     * @param priorityProperty
     *         determines whether to use the rank or confidence when evaluation the {@link Severity}
     */
    public FindBugsParser(final PriorityProperty priorityProperty) {
        super();

        this.priorityProperty = priorityProperty;
    }

    @Override
    public Report parse(final ReaderFactory readerFactory) throws ParsingException {
        Collection<String> sources = new ArrayList<>();
        String moduleRoot = StringUtils.substringBefore(readerFactory.getFileName(), "/target/");
        sources.add(moduleRoot + "/src/main/java");
        sources.add(moduleRoot + "/src/test/java");
        sources.add(moduleRoot + "/src");
        return parse(readerFactory, sources, new IssueBuilder());
    }

    @VisibleForTesting
    Report parse(final ReaderFactory readerFactory, final Collection<String> sources, final IssueBuilder builder)
            throws ParsingException {
        Map<String, String> hashToMessageMapping = new HashMap<>();
        Map<String, String> categories = new HashMap<>();

        try (Reader input = readerFactory.create()) {
            List<XmlBugInstance> bugs = preParse(input);
            for (XmlBugInstance bug : bugs) {
                hashToMessageMapping.put(bug.getInstanceHash(), bug.getMessage());
                categories.put(bug.getType(), bug.getCategory());
            }
        }
        catch (SAXException | IOException exception) {
            throw new ParsingException(exception);
        }

        return parse(readerFactory, sources, builder, hashToMessageMapping, categories);
    }

    /**
     * Returns the parsed FindBugs analysis file. This scanner accepts files in the native FindBugs format.
     *
     * @param builder
     *         the issue builder
     * @param readerFactory
     *         the FindBugs analysis file
     * @param sources
     *         a collection of folders to scan for source files
     * @param hashToMessageMapping
     *         mapping of hash codes to messages
     * @param categories
     *         mapping from bug types to their categories
     *
     * @return the parsed result (stored in the module instance)
     */
    private Report parse(final ReaderFactory readerFactory, final Collection<String> sources,
            final IssueBuilder builder, final Map<String, String> hashToMessageMapping,
            final Map<String, String> categories) {
        try (Reader input = readerFactory.create()) {
            SortedBugCollection bugs = readXml(input);

            try (Project project = bugs.getProject()) {
                return convertBugsToIssues(sources, builder, hashToMessageMapping, categories, bugs, project);
            }
        }
        catch (DocumentException | IOException exception) {
            throw new ParsingException(exception);
        }
    }

    private Report convertBugsToIssues(final Collection<String> sources, final IssueBuilder builder,
            final Map<String, String> hashToMessageMapping, final Map<String, String> categories,
            final SortedBugCollection collection, final Project project) {
        project.addSourceDirs(sources);

        try (SourceFinder sourceFinder = new SourceFinder(project)) {
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
                builder.setSeverity(getPriority(warning))
                        .setMessage(createMessage(hashToMessageMapping, warning, message))
                        .setCategory(category)
                        .setType(type)
                        .setLineStart(sourceLine.getStartLine())
                        .setLineEnd(sourceLine.getEndLine())
                        .setFileName(findSourceFile(sourceFinder, sourceLine))
                        .setPackageName(warning.getPrimaryClass().getPackageName())
                        .setFingerprint(warning.getInstanceHash());
                setAffectedLines(warning, builder,
                        new LineRangeBuilder()
                                .setStart(sourceLine.getStartLine())
                                .setEnd(sourceLine.getEndLine())
                                .build()
                );

                report.add(builder.build());
            }
            return report;
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
    List<XmlBugInstance> preParse(final Reader file) throws SAXException, IOException {
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

    private String createMessage(final Map<String, String> hashToMessageMapping, final BugInstance warning,
            final String message) {
        return StringUtils.defaultIfEmpty(hashToMessageMapping.get(warning.getInstanceHash()), message);
    }

    private Severity getPriority(final BugInstance warning) {
        if (priorityProperty == RANK) {
            return getPriorityByRank(warning);
        }
        else {
            return getPriorityByPriority(warning);
        }
    }

    private SortedBugCollection readXml(final Reader file) throws IOException, DocumentException {
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
                LineRange lineRange = new LineRangeBuilder()
                        .setStart(annotation.getStartLine())
                        .setEnd(annotation.getEndLine())
                        .build();
                if (!lineRanges.contains(lineRange) && !primary.equals(lineRange)) {
                    lineRanges.add(lineRange);
                }
            }
        }
        builder.setLineRanges(lineRanges);
    }

    private String findSourceFile(final SourceFinder sourceFinder, final SourceLineAnnotation sourceLine) {
        try {
            SourceFile sourceFile = sourceFinder.findSourceFile(sourceLine);
            return sourceFile.getFullFileName();
        }
        catch (IOException ignored) {
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
    private Severity getPriorityByRank(final BugInstance warning) {
        int rank = warning.getBugRank();
        if (rank <= HIGH_PRIORITY_LOWEST_RANK) {
            return Severity.WARNING_HIGH;
        }
        if (rank <= NORMAL_PRIORITY_LOWEST_RANK) {
            return Severity.WARNING_NORMAL;
        }
        return Severity.WARNING_LOW;
    }

    /**
     * Maps the FindBugs library priority to plug-in priority enumeration.
     *
     * @param warning
     *         the FindBugs warning
     *
     * @return mapped priority enumeration
     */
    private Severity getPriorityByPriority(final BugInstance warning) {
        switch (warning.getPriority()) {
            case 1:
                return Severity.WARNING_HIGH;
            case 2:
                return Severity.WARNING_NORMAL;
            default:
                return Severity.WARNING_LOW;
        }
    }

    /**
     * Java Bean to create the mapping of hash codes to messages using the Digester XML parser.
     *
     * @author Ullrich Hafner
     */
    @SuppressWarnings("all")
    public static class XmlBugInstance {
        @Nullable
        private String instanceHash;
        @Nullable
        private String message;
        @Nullable
        private String type;
        @Nullable
        private String category;

        @Nullable
        public String getInstanceHash() {
            return instanceHash;
        }

        public void setInstanceHash(final String instanceHash) {
            this.instanceHash = instanceHash;
        }

        @Nullable
        public String getMessage() {
            return message;
        }

        public void setMessage(final String message) {
            this.message = message;
        }

        @Nullable
        public String getType() {
            return type;
        }

        public void setType(final String type) {
            this.type = type;
        }

        @Nullable
        public String getCategory() {
            return category;
        }

        public void setCategory(final String category) {
            this.category = category;
        }
    }
}
