package edu.hm.hafner.analysis.parser;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.util.XmlElementUtil;
import edu.umd.cs.findbugs.annotations.CheckForNull;

import java.io.Serial;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static edu.hm.hafner.analysis.util.IntegerParser.*;

/**
 * Parses Gendarme violations.
 *
 * @author mathias.kluba@gmail.com
 */
public class GendarmeParser extends IssueParser {
    @Serial
    private static final long serialVersionUID = 1677715364464119907L;

    private static final Pattern FILE_PATTERN = Pattern.compile("^(.*)\\(.(\\d+)\\).*$");
    private static final String TYPE = "Type";
    private static final String METHOD = "Method";
    private static final String ASSEMBLY = "Assembly";

    @Override
    public Report parseReport(final ReaderFactory factory) throws ParsingException {
        var document = factory.readDocument();

        var mainNode = document.getElementsByTagName("gendarme-output");

        var rootElement = (Element) mainNode.item(0);
        var resultsElement = (Element) rootElement.getElementsByTagName("results").item(0);
        var rulesElement = (Element) rootElement.getElementsByTagName("rules").item(0);

        Map<String, GendarmeRule> rules = parseRules(XmlElementUtil.getChildElementsByName(rulesElement, "rule"));
        return parseViolations(XmlElementUtil.getChildElementsByName(resultsElement, "rule"), rules);
    }

    private Report parseViolations(final List<Element> ruleElements, final Map<String, GendarmeRule> rules) {
        try (var issueBuilder = new IssueBuilder()) {
            var warnings = new Report();
            for (Element ruleElement : ruleElements) {
                var ruleName = ruleElement.getAttribute("Name");
                var problem = ruleElement.getElementsByTagName("problem").item(0).getTextContent();
                List<Element> targetElements = XmlElementUtil.getChildElementsByName(ruleElement, "target");

                var rule = rules.get(ruleName);
                if (rule != null) {
                    for (Element targetElement : targetElements) {
                        var defectElement = (Element) targetElement.getElementsByTagName("defect").item(0);
                        var source = defectElement.getAttribute("Source");

                        var fileName = extractFileNameMatch(rule, source, 1);
                        var priority = extractPriority(defectElement);
                        int line = parseInt(extractFileNameMatch(rule, source, 2));

                        issueBuilder.setFileName(fileName)
                                .setLineStart(line)
                                .setCategory(rule.getName())
                                .setMessage(problem)
                                .setSeverity(priority);
                        warnings.add(issueBuilder.buildAndClean());
                    }
                }
            }
            return warnings;
        }
    }

    private Severity extractPriority(final Element defectElement) {
        return switch (defectElement.getAttribute("Severity")) {
            case "Low" -> Severity.WARNING_LOW;
            case "High" -> Severity.WARNING_HIGH;
            default -> Severity.WARNING_NORMAL;
        };
    }

    private String extractFileNameMatch(final GendarmeRule rule, final String source, final int group) {
        var fileName = StringUtils.EMPTY;
        if (rule.getType() == GendarmeRuleType.METHOD) {
            var matcher = FILE_PATTERN.matcher(source);
            if (matcher.matches()) {
                fileName = matcher.group(group);
            }
        }
        return fileName;
    }

    private Map<String, GendarmeRule> parseRules(final List<Element> ruleElements) {
        Map<String, GendarmeRule> rules = new HashMap<>();

        for (var ruleElement : ruleElements) {
            var rule = new GendarmeRule();
            rule.setName(ruleElement.getAttribute("Name"));
            rule.setTypeName(ruleElement.getTextContent());

            var typeString = ruleElement.getAttribute(TYPE);
            switch (typeString) {
                case TYPE -> rule.setType(GendarmeRuleType.TYPE);
                case METHOD -> rule.setType(GendarmeRuleType.METHOD);
                case ASSEMBLY -> rule.setType(GendarmeRuleType.ASSEMBLY);
                default -> {
                    // ignore the type
                }
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

    @SuppressWarnings("all")
    static class GendarmeRule {
        @CheckForNull
        private String name;
        @CheckForNull
        private String typeName;
        @CheckForNull
        private GendarmeRuleType type;
        @CheckForNull
        private URL url;

        @CheckForNull
        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(final String typeName) {
            this.typeName = typeName;
        }

        @CheckForNull
        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        @CheckForNull
        public GendarmeRuleType getType() {
            return type;
        }

        public void setType(final GendarmeRuleType type) {
            this.type = type;
        }

        @CheckForNull
        public URL getUrl() {
            return url;
        }

        public void setUrl(@CheckForNull final URL url) {
            this.url = url;
        }
    }

    enum GendarmeRuleType {
        METHOD, TYPE, ASSEMBLY
    }
}
