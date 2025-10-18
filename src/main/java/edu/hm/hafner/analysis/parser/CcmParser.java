package edu.hm.hafner.analysis.parser;

import org.apache.commons.lang3.Strings;
import org.xml.sax.SAXException;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.SecureDigester;
import edu.hm.hafner.analysis.Severity;
import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A parser for CCM XML files.
 *
 * @author Bruno P. Kinoshita
 */
public class CcmParser extends IssueParser {
    @Serial
    private static final long serialVersionUID = -5172155190810975806L;

    @Override
    public Report parseReport(final ReaderFactory readerFactory) throws ParsingException {
        var report = parseCcmXmlFile(readerFactory);

        return convert(report);
    }

    private Ccm parseCcmXmlFile(final ReaderFactory ccmXmlFile) {
        var digester = new SecureDigester(CcmParser.class);

        var rootXPath = "ccm";
        digester.addObjectCreate(rootXPath, Ccm.class);
        digester.addSetProperties(rootXPath);

        var fileMetric = "ccm/metric";
        digester.addObjectCreate(fileMetric, Metric.class);
        digester.addSetProperties(fileMetric);
        digester.addBeanPropertySetter("ccm/metric/complexity");
        digester.addBeanPropertySetter("ccm/metric/unit");
        digester.addBeanPropertySetter("ccm/metric/classification");
        digester.addBeanPropertySetter("ccm/metric/file");
        digester.addBeanPropertySetter("ccm/metric/startLineNumber");
        digester.addBeanPropertySetter("ccm/metric/endLineNumber");
        digester.addSetNext(fileMetric, "addMetric", Metric.class.getName());

        try (var reader = ccmXmlFile.create()) {
            Ccm report = digester.parse(reader);
            if (report == null) {
                throw new ParsingException(ccmXmlFile, "Input stream is not a CCM file.");
            }

            return report;
        }
        catch (IOException | SAXException exception) {
            throw new ParsingException(exception, ccmXmlFile);
        }
    }

    private Report convert(final Ccm collection) {
        try (var issueBuilder = new IssueBuilder()) {
            var report = new Report();

            for (Metric metric : collection.getMetrics()) {
                var priority = calculateMetricPriority(metric);

                var complexity = String.format(Locale.ENGLISH, "%s has a complexity of %d", metric.getUnit(),
                        metric.getComplexity());

                issueBuilder.setSeverity(priority)
                        .setMessage(complexity)
                        .setCategory(metric.getClassification())
                        .setLineStart(metric.getStartLineNumber())
                        .setLineEnd(metric.getEndLineNumber())
                        .setFileName(metric.getFile());
                report.add(issueBuilder.buildAndClean());
            }

            return report;
        }
    }

    private Severity calculateMetricPriority(final Metric metric) {
        if (isMetricHighPriority(metric)) {
            return Severity.WARNING_HIGH;
        }
        else if (isMetricModeratePriority(metric)) {
            return Severity.WARNING_NORMAL;
        }
        else {
            return Severity.WARNING_LOW;
        }
    }

    private boolean isMetricHighPriority(final Metric metric) {
        var metricClassification = metric.getClassification();
        if (Strings.CS.contains(metricClassification, "high")) {
            return true;
        }
        return "C".equals(metricClassification) || "D".equals(metricClassification)
                || "E".equals(metricClassification) || "F".equals(metricClassification);
    }

    private boolean isMetricModeratePriority(final Metric metric) {
        var metricClassification = metric.getClassification();

        return Strings.CS.contains(metricClassification, "moderate")
                || "B".equals(metricClassification);
    }

    /**
     * Entity used by {@link CcmParser} to represent the root node of CCM results file.
     *
     * @author Bruno P. Kinoshita - http://www.kinoshita.eti.br
     * @since 1.0
     */
    @SuppressWarnings("all")
    @SuppressFBWarnings("EI")
    public static class Ccm {
        /**
         * List of metrics present in the XML file.
         */
        private List<Metric> metrics = new ArrayList<>();

        public List<Metric> getMetrics() {
            return metrics;
        }

        public void setMetrics(List<Metric> metrics) {
            this.metrics = metrics;
        }

        public void addMetric(Metric metric) {
            this.metrics.add(metric);
        }
    }

    /**
     * Entity representing the Metric from CCM.exe output.
     *
     * <p>
     *     It has the {@link #complexity}, {@link #unit}, {@link #classification} and {@link #file} fields.
     * </p>
     *
     * @author Bruno P. Kinoshita - http://www.kinoshita.eti.br
     * @since 1.0
     */
    @SuppressWarnings("all")
    public static class Metric {
        /**
         * Total CC of the method.
         */
        private int complexity;

        /**
         * String containing Class_Name::Method_Name
         */
        @CheckForNull
        private String unit;

        /**
         * CCM outputs a String with a classification such as "complex, high risk", "untestable, very high risk", etc. As
         * there is no documentation on which values are used to determine a method's CC classification CCM Plugin only
         * outputs this value. But does not use the information as a constraint in any place.
         */
        @CheckForNull
        private String classification;

        /**
         * The file name (e.g.:\ascx\request\open\form.ascx.cs).
         */
        @CheckForNull
        private String file;

        /**
         * The start line number of the measurement
         */
        private int startLineNumber;

        /**
         * The end line number of the measurement
         */
        private int endLineNumber;

        public int getComplexity() {
            return complexity;
        }

        public void setComplexity(int complexity) {
            this.complexity = complexity;
        }

        @CheckForNull
        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        @CheckForNull
        public String getClassification() {
            return classification;
        }

        public void setClassification(String classification) {
            this.classification = classification;
        }

        @CheckForNull
        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public int getStartLineNumber() {
            return startLineNumber;
        }

        public void setStartLineNumber(int startLineNumber) {
            this.startLineNumber = startLineNumber;
        }

        public int getEndLineNumber() {
            return endLineNumber;
        }

        public void setEndLineNumber(int endLineNumber) {
            this.endLineNumber = endLineNumber;
        }
    }
}
