package edu.hm.hafner.analysis.parser.gendarme;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.input.ReaderInputStream;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.ParsingCanceledException;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.XmlElementUtil;

/**
 * Parses Gendarme violations.
 *
 * @author mathias.kluba@gmail.com
 */
public class GendarmeParser extends AbstractParser {
    private static final long serialVersionUID = 1677715364464119907L;

    private static final Pattern FILE_PATTERN = Pattern.compile("^(.*)\\(.(\\d+)\\).*$");

    @Override
    public Report parse(final Reader reader, final Function<String, String> preProcessor)
            throws ParsingException, ParsingCanceledException {
        try (InputStream input = new ReaderInputStream(reader, StandardCharsets.UTF_8)) {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(input);

            NodeList mainNode = doc.getElementsByTagName("gendarme-output");

            Element rootElement = (Element)mainNode.item(0);
            Element resultsElement = (Element)rootElement.getElementsByTagName("results").item(0);
            Element rulesElement = (Element)rootElement.getElementsByTagName("rules").item(0);

            Map<String, GendarmeRule> rules = parseRules(XmlElementUtil.getNamedChildElements(rulesElement, "rule"));
            return parseViolations(XmlElementUtil.getNamedChildElements(resultsElement, "rule"), rules);
        }
        catch (IOException | ParserConfigurationException | SAXException e) {
            throw new ParsingException(e);
        }
    }

    private Report parseViolations(final List<Element> ruleElements, final Map<String, GendarmeRule> rules) {
        Report warnings = new Report();
        for (Element ruleElement : ruleElements) {
            String ruleName = ruleElement.getAttribute("Name");
            String problem = ruleElement.getElementsByTagName("problem").item(0).getTextContent();
            List<Element> targetElements = XmlElementUtil.getNamedChildElements(ruleElement, "target");

            GendarmeRule rule = rules.get(ruleName);
            for (Element targetElement : targetElements) {
                Element defectElement = (Element)targetElement.getElementsByTagName("defect").item(0);
                String source = defectElement.getAttribute("Source");

                String fileName = extractFileNameMatch(rule, source, 1);
                Priority priority = extractPriority(defectElement);
                int line = parseInt(extractFileNameMatch(rule, source, 2));

                IssueBuilder builder = new IssueBuilder().setFileName(fileName)
                        .setLineStart(line)
                        .setCategory(rule.getName())
                        .setMessage(problem)
                        .setPriority(priority);
                warnings.add(builder.build());
            }
        }
        return warnings;
    }

    private Priority extractPriority(final Element defectElement) {
        switch (defectElement.getAttribute("Severity")) {
            case "Low":
                return Priority.LOW;
            case "High":
                return Priority.HIGH;
            default:
                return Priority.NORMAL;
        }
    }

    private String extractFileNameMatch(final GendarmeRule rule, final String source, final int group) {
        String fileName = StringUtils.EMPTY;
        if (rule.getType() == GendarmeRuleType.Method) {
            Matcher matcher = FILE_PATTERN.matcher(source);
            if (matcher.matches()) {
                fileName = matcher.group(group);
            }
        }
        return fileName;
    }

    private Map<String, GendarmeRule> parseRules(final List<Element> ruleElements) {
        Map<String, GendarmeRule> rules = new HashMap<>();

        for (Element ruleElement : ruleElements) {
            GendarmeRule rule = new GendarmeRule();
            rule.setName(ruleElement.getAttribute("Name"));
            rule.setTypeName(ruleElement.getTextContent());

            String typeString = ruleElement.getAttribute("Type");
            if ("Type".equals(typeString)) {
                rule.setType(GendarmeRuleType.Type);
            }
            else if ("Method".equals(typeString)) {
                rule.setType(GendarmeRuleType.Method);
            }
            else if ("Assembly".equals(typeString)) {
                rule.setType(GendarmeRuleType.Assembly);
            }
            try {
                rule.setUrl(new URL(ruleElement.getAttribute("Uri")));
            }
            catch (MalformedURLException ignored) {
                rule.setUrl(null);
            }

            // add the rule to the cache
            rules.put(rule.getName(), rule);
        }

        return rules;
    }
}
