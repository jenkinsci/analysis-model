package edu.hm.hafner.analysis.parser;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingCanceledException;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.util.XmlElementUtil;
import edu.umd.cs.findbugs.annotations.CheckForNull;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Parses a fxcop xml report file.
 */
@SuppressWarnings("unused")
public class FxCopParser extends IssueParser {
    @Serial
    private static final long serialVersionUID = -7208558002331355408L;
    private static final int CAPACITY = 1024;

    @Override
    public Report parseReport(final ReaderFactory readerFactory) throws ParsingException, ParsingCanceledException {
        return new XmlParser().parse(readerFactory);
    }

    /**
     * Handles parsing of the XML file.
     */
    private static class XmlParser {
        private final Report warnings = new Report();
        private final FxCopRuleSet ruleSet = new FxCopRuleSet();

        Report parse(final ReaderFactory readerFactory) throws ParsingException, ParsingCanceledException {
            try (var issueBuilder = new IssueBuilder()) {
                var doc = readerFactory.readDocument();

                var mainNode = doc.getElementsByTagName("FxCopReport");

                var rootElement = (Element) mainNode.item(0);
                parseRules(rootElement, issueBuilder);
                parseNamespaces(rootElement, issueBuilder);
                parseTargets(rootElement, issueBuilder);

                return warnings;
            }
        }

        private void parseRules(final Element rootElement, final IssueBuilder issueBuilder) {
            var rulesElement = XmlElementUtil.getFirstChildElementByName(rootElement, "Rules");
            if (rulesElement.isPresent()) {
                for (Element rule : XmlElementUtil.getChildElementsByName(rulesElement.get(), "Rule")) {
                    ruleSet.addRule(rule);
                }
            }
        }

        private void parseTargets(final Element rootElement, final IssueBuilder issueBuilder) {
            var targetsElement = XmlElementUtil.getFirstChildElementByName(rootElement, "Targets");
            if (targetsElement.isPresent()) {
                for (Element target : XmlElementUtil.getChildElementsByName(targetsElement.get(), "Target")) {
                    var name = getString(target, "Name");
                    parseMessages(target, name, issueBuilder);
                    parseModules(target, name, issueBuilder);
                    parseResources(target, name, issueBuilder);
                }
            }
        }

        private void parseResources(final Element target, final String parentName,
                final IssueBuilder issueBuilder) {
            var resources = XmlElementUtil.getFirstChildElementByName(target, "Resources");
            if (resources.isPresent()) {
                for (Element resource : XmlElementUtil.getChildElementsByName(resources.get(), "Resource")) {
                    var name = getString(resource, "Name");
                    parseMessages(resource, name, issueBuilder);
                }
            }
        }

        private void parseModules(final Element target, final String parentName,
                final IssueBuilder issueBuilder) {
            var modulesElement = XmlElementUtil.getFirstChildElementByName(target, "Modules");
            if (modulesElement.isPresent()) {
                for (Element module : XmlElementUtil.getChildElementsByName(modulesElement.get(), "Module")) {
                    var name = getString(module, "Name");
                    parseMessages(module, name, issueBuilder);
                    parseNamespaces(module, issueBuilder);
                }
            }
        }

        private void parseNamespaces(final Element rootElement, final IssueBuilder issueBuilder) {
            var namespacesElement = XmlElementUtil.getFirstChildElementByName(rootElement, "Namespaces");
            if (namespacesElement.isPresent()) {
                for (Element namespace : XmlElementUtil.getChildElementsByName(namespacesElement.get(), "Namespace")) {
                    var name = getString(namespace, "Name");

                    parseMessages(namespace, name, issueBuilder);
                    parseTypes(namespace, name, issueBuilder);
                }
            }
        }

        private void parseTypes(final Element typesElement, final String parentName,
                final IssueBuilder issueBuilder) {
            var types = XmlElementUtil.getFirstChildElementByName(typesElement, "Types");
            if (types.isPresent()) {
                for (Element type : XmlElementUtil.getChildElementsByName(types.get(), "Type")) {
                    var name = parentName + "." + getString(type, "Name");

                    parseMessages(type, name, issueBuilder);
                    parseMembers(type, name, issueBuilder);
                }
            }
        }

        private void parseMembers(final Element members, final String parentName,
                final IssueBuilder issueBuilder) {
            var membersElement = XmlElementUtil.getFirstChildElementByName(members, "Members");
            if (membersElement.isPresent()) {
                for (Element member : XmlElementUtil.getChildElementsByName(membersElement.get(), "Member")) {
                    parseMember(member, parentName, issueBuilder);
                }
            }
        }

        private void parseAccessors(final Element accessorsElement, final String parentName,
                final IssueBuilder issueBuilder) {
            var accessors = XmlElementUtil.getFirstChildElementByName(accessorsElement, "Accessors");
            if (accessors.isPresent()) {
                for (Element member : XmlElementUtil.getChildElementsByName(accessors.get(), "Accessor")) {
                    parseMember(member, parentName, issueBuilder);
                }
            }
        }

        private void parseMember(final Element member, final String parentName,
                final IssueBuilder issueBuilder) {
            parseMessages(member, parentName, issueBuilder);
            parseAccessors(member, parentName, issueBuilder);
        }

        private void parseMessages(final Element messages, final String parentName,
                final IssueBuilder issueBuilder) {
            var messagesElement = XmlElementUtil.getFirstChildElementByName(messages, "Messages");
            if (messagesElement.isPresent()) {
                for (Element message : XmlElementUtil.getChildElementsByName(messagesElement.get(), "Message")) {
                    for (Element issue : XmlElementUtil.getChildElementsByName(message, "Issue")) {
                        parseIssue(issue, message, parentName, issueBuilder);
                    }
                }
            }
        }

        private void parseIssue(final Element issue, final Element parent, final String parentName,
                final IssueBuilder issueBuilder) {
            var typeName = getString(parent, "TypeName");
            var category = getString(parent, "Category");
            var checkId = getString(parent, "CheckId");
            var issueLevel = getString(issue, "Level");

            var msgBuilder = new StringBuilder(CAPACITY);
            var rule = ruleSet.getRule(category, checkId);
            if (rule == null) {
                msgBuilder.append(typeName);
            }
            else {
                msgBuilder.append("<a href=\"");
                msgBuilder.append(rule.getUrl());
                msgBuilder.append("\">");
                msgBuilder.append(typeName);
                msgBuilder.append("</a>");
            }
            msgBuilder.append(" - ");
            msgBuilder.append(issue.getTextContent());

            var filePath = getString(issue, "Path");
            var fileName = getString(issue, "File");
            var fileLine = getString(issue, "Line");

            issueBuilder.setFileName(filePath + "/" + fileName)
                    .setLineStart(fileLine)
                    .setCategory(category)
                    .setMessage(msgBuilder.toString())
                    .guessSeverity(issueLevel);
            if (rule != null) {
                issueBuilder.setDescription(rule.getDescription());
            }
            warnings.add(issueBuilder.buildAndClean());
        }

        private String getString(final Element element, final String name) {
            if (element.hasAttribute(name)) {
                return element.getAttribute(name);
            }
            else {
                return "";
            }
        }
    }

    /**
     * Internal model for a FxCop rule.
     *
     * @author Erik Ramfelt
     */
    @SuppressWarnings({"all", "CheckStyle"})
    public static class FxCopRule {
        private final String typeName;
        private final String category;
        private final String checkId;
        @CheckForNull
        private String name;
        @CheckForNull
        private String url;
        @CheckForNull
        private String description;

        public FxCopRule(final String typeName, final String category, final String checkId) {
            this.typeName = typeName;
            this.category = category;
            this.checkId = checkId;
        }

        @CheckForNull
        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        @CheckForNull
        public String getUrl() {
            return url;
        }

        public void setUrl(final String url) {
            this.url = url;
        }

        @CheckForNull
        public String getDescription() {
            return description;
        }

        public void setDescription(final String description) {
            this.description = description;
        }

        public String getTypeName() {
            return typeName;
        }

        public String getCategory() {
            return category;
        }

        public String getCheckId() {
            return checkId;
        }
    }

    /**
     * Internal set containing rules for FxCop.
     *
     * @author Erik Ramfelt
     */
    @SuppressWarnings({"all", "CheckStyle"})
    public static class FxCopRuleSet {
        private final Map<String, FxCopRule> rules = new HashMap<>();

        /***
         * Parse the element and insert the rule into the rule set.
         * @param element the element
         */
        public void addRule(final Element element) {
            var rule = new FxCopRule(element.getAttribute("TypeName"), element.getAttribute("Category"), element
                    .getAttribute("CheckId"));
            rule.setUrl(getNamedTagText(element, "Url"));
            rule.setDescription(getNamedTagText(element, "Description"));
            rule.setName(getNamedTagText(element, "Name"));

            rules.put(getRuleKey(rule.getCategory(), rule.getCheckId()), rule);
        }

        /**
         * Returns the text value of the named child element if it exists
         *
         * @param element
         *         the element to check look for child elements
         * @param tagName
         *         the name of the child element
         *
         * @return the text value; or "" if no element was found
         */
        private String getNamedTagText(final Element element, final String tagName) {
            Optional<Element> foundElement = XmlElementUtil.getFirstChildElementByName(element, tagName);
            if (foundElement.isPresent()) {
                return foundElement.get().getTextContent();
            }
            else {
                return StringUtils.EMPTY;
            }
        }

        /**
         * Returns if the rule set contains a rule for the specified category and id
         *
         * @param category
         *         the rule category
         * @param checkId
         *         the rule id
         *
         * @return {@code true}  if the rule set contains a rule for the specified category and id, {@code false} otherwise
         */
        public boolean contains(final String category, final String checkId) {
            return rules.containsKey(getRuleKey(category, checkId));
        }

        /**
         * Returns the specified rule if it exists
         *
         * @param category
         *         the rule category
         * @param checkId
         *         the id of the rule
         *
         * @return the rule; null otherwise
         */
        @CheckForNull
        public FxCopRule getRule(final String category, final String checkId) {
            var key = getRuleKey(category, checkId);
            FxCopRule rule = null;
            if (rules.containsKey(key)) {
                rule = rules.get(key);
            }
            return rule;
        }

        /**
         * Returns the key for the map
         *
         * @param category
         *         category of the rule
         * @param checkId
         *         id of the rule
         *
         * @return category + "#" + checkid
         */
        private String getRuleKey(final String category, final String checkId) {
            return category + "#" + checkId;
        }
    }
}
