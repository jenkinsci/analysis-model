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
import java.util.function.Function;

import org.xml.sax.SAXException;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.ParsingCanceledException;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.SecureDigester;

/**
 * A parser for CCM XML files.
 *
 * @author Bruno P. Kinoshita - http://www.kinoshita.eti.br
 */
public class CcmParser extends AbstractParser {
    private static final long serialVersionUID = -5172155190810975806L;

    @Override
    public Report parse(final Reader reader, final Function<String, String> preProcessor)
            throws ParsingCanceledException, ParsingException {
        try {
            Ccm module = parseCCMXmlFile(reader);
            if (module == null) {
                throw new SAXException("Input stream is not a CCM file.");
            }

            return convert(module);
        }
        catch (IOException | SAXException exception) {
            throw new ParsingException(exception);
        }
    }

    private Ccm parseCCMXmlFile(final Reader ccmXmlFile) throws IOException, SAXException {
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

        return digester.parse(ccmXmlFile);
    }

    private Report convert(final Ccm collection) {
        Report report = new Report();

        for (Metric metric : collection.getMetrics()) {
            Priority priority = calculateMetricPriority(metric);

            String complexity = String.format("%s has a complexity of %d", metric.getUnit(), metric.getComplexity());

            IssueBuilder builder = new IssueBuilder();
            builder.setPriority(priority)
                    .setMessage(complexity)
                    .setCategory(metric.getClassification())
                    .setLineStart(metric.getStartLineNumber())
                    .setLineEnd(metric.getEndLineNumber())
                    .setFileName(metric.getFile());
            report.add(builder.build());
        }

        return report;
    }

    private Priority calculateMetricPriority(final Metric metric) {
        if (isMetricHighPriority(metric)) {
            return Priority.HIGH;
        }
        else if (isMetricModeratePriority(metric)) {
            return Priority.NORMAL;
        }
        else {
            return Priority.LOW;
        }
    }

    private boolean isMetricHighPriority(final Metric metric) {
        String metricClassification = metric.getClassification();
        if (metricClassification.contains("high")) {
            return true;
        }
        return metricClassification.contentEquals("C") || metricClassification.contentEquals("D") 
                || metricClassification.contentEquals("E") || metricClassification.contentEquals("F");
    }

    private boolean isMetricModeratePriority(final Metric metric) {
        String metricClassification = metric.getClassification();
        if (metricClassification.contains("moderate")) {
            return true;
        }
        return metricClassification.contentEquals("B");
    }
}
