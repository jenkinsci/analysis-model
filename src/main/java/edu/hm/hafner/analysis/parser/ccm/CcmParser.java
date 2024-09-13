package edu.hm.hafner.analysis.parser.ccm;

import java.io.IOException;
import java.io.Reader;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.xml.sax.SAXException;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.SecureDigester;
import edu.hm.hafner.analysis.Severity;

/**
 * A parser for CCM XML files.
 *
 * @author Bruno P. Kinoshita
 */
public class CcmParser extends IssueParser {
    private static final long serialVersionUID = -5172155190810975806L;

    @Override
    public Report parse(final ReaderFactory readerFactory) throws ParsingException {
        var report = parseCcmXmlFile(readerFactory);

        return convert(report);
    }

    private Ccm parseCcmXmlFile(final ReaderFactory ccmXmlFile) {
        var digester = new SecureDigester(CcmParser.class);

        String rootXPath = "ccm";
        digester.addObjectCreate(rootXPath, Ccm.class);
        digester.addSetProperties(rootXPath);

        String fileMetric = "ccm/metric";
        digester.addObjectCreate(fileMetric, Metric.class);
        digester.addSetProperties(fileMetric);
        digester.addBeanPropertySetter("ccm/metric/complexity");
        digester.addBeanPropertySetter("ccm/metric/unit");
        digester.addBeanPropertySetter("ccm/metric/classification");
        digester.addBeanPropertySetter("ccm/metric/file");
        digester.addBeanPropertySetter("ccm/metric/startLineNumber");
        digester.addBeanPropertySetter("ccm/metric/endLineNumber");
        digester.addSetNext(fileMetric, "addMetric", Metric.class.getName());

        try (Reader reader = ccmXmlFile.create()) {
            Ccm report = digester.parse(reader);
            if (report == null) {
                throw new ParsingException("Input stream is not a CCM file.");
            }

            return report;
        }
        catch (IOException | SAXException exception) {
            throw new ParsingException(exception);
        }
    }

    private Report convert(final Ccm collection) {
        try (IssueBuilder issueBuilder = new IssueBuilder()) {
            var report = new Report();

            for (Metric metric : collection.getMetrics()) {
                Severity priority = calculateMetricPriority(metric);

                String complexity = String.format(Locale.ENGLISH, "%s has a complexity of %d", metric.getUnit(),
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
        String metricClassification = metric.getClassification();
        if (StringUtils.contains(metricClassification, "high")) {
            return true;
        }
        return "C".equals(metricClassification) || "D".equals(metricClassification)
                || "E".equals(metricClassification) || "F".equals(metricClassification);
    }

    private boolean isMetricModeratePriority(final Metric metric) {
        String metricClassification = metric.getClassification();

        return StringUtils.contains(metricClassification, "moderate")
                || "B".equals(metricClassification);
    }
}
