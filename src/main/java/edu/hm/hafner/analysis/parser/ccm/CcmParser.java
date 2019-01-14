/*
 * The MIT License
 *
 * Copyright (c) <2012> <Bruno P. Kinoshita>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package edu.hm.hafner.analysis.parser.ccm;

import java.io.IOException;
import java.io.Reader;

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
 * @author Bruno P. Kinoshita - http://www.kinoshita.eti.br
 */
public class CcmParser extends IssueParser {
    private static final long serialVersionUID = -5172155190810975806L;

    @Override
    public Report parse(final ReaderFactory readerFactory) throws ParsingException {
        Ccm report = parseCCMXmlFile(readerFactory);

        return convert(report);
    }

    private Ccm parseCCMXmlFile(final ReaderFactory ccmXmlFile) {
        SecureDigester digester = new SecureDigester(CcmParser.class);

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
        Report report = new Report();

        for (Metric metric : collection.getMetrics()) {
            Severity priority = calculateMetricPriority(metric);

            String complexity = String.format("%s has a complexity of %d", metric.getUnit(), metric.getComplexity());

            IssueBuilder builder = new IssueBuilder();
            builder.setSeverity(priority)
                    .setMessage(complexity)
                    .setCategory(metric.getClassification())
                    .setLineStart(metric.getStartLineNumber())
                    .setLineEnd(metric.getEndLineNumber())
                    .setFileName(metric.getFile());
            report.add(builder.build());
        }

        return report;
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
        if (StringUtils.contains(metricClassification, "moderate")) {
            return true;
        }
        return "B".equals(metricClassification);
    }
}
