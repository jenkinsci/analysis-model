package edu.hm.hafner.analysis.parser.fxcop;

import org.w3c.dom.Element;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingCanceledException;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.util.XmlElementUtil;

/**
 * Parses a fxcop xml report file.
 */
@SuppressWarnings("unused")
public class FxCopParser extends IssueParser {
    private static final long serialVersionUID = -7208558002331355408L;
    private static final int CAPACITY = 1024;

    @Override
    public Report parse(final ReaderFactory readerFactory) throws ParsingException, ParsingCanceledException {
        return new XmlParser().parse(readerFactory);
    }

    /**
     * Handles parsing of the XML file.
     */
    private static class XmlParser {
        private final Report warnings = new Report();
        private final FxCopRuleSet ruleSet = new FxCopRuleSet();

        public Report parse(final ReaderFactory readerFactory) throws ParsingException, ParsingCanceledException {
            try (IssueBuilder issueBuilder = new IssueBuilder()) {
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
                    String name = getString(target, "Name");
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
                    String name = getString(resource, "Name");
                    parseMessages(resource, name, issueBuilder);
                }
            }
        }

        private void parseModules(final Element target, final String parentName,
                final IssueBuilder issueBuilder) {
            var modulesElement = XmlElementUtil.getFirstChildElementByName(target, "Modules");
            if (modulesElement.isPresent()) {
                for (Element module : XmlElementUtil.getChildElementsByName(modulesElement.get(), "Module")) {
                    String name = getString(module, "Name");
                    parseMessages(module, name, issueBuilder);
                    parseNamespaces(module, issueBuilder);
                }
            }
        }

        private void parseNamespaces(final Element rootElement, final IssueBuilder issueBuilder) {
            var namespacesElement = XmlElementUtil.getFirstChildElementByName(rootElement, "Namespaces");
            if (namespacesElement.isPresent()) {
                for (Element namespace : XmlElementUtil.getChildElementsByName(namespacesElement.get(), "Namespace")) {
                    String name = getString(namespace, "Name");

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
                    String name = parentName + "." + getString(type, "Name");

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
            String typeName = getString(parent, "TypeName");
            String category = getString(parent, "Category");
            String checkId = getString(parent, "CheckId");
            String issueLevel = getString(issue, "Level");

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

            String filePath = getString(issue, "Path");
            String fileName = getString(issue, "File");
            String fileLine = getString(issue, "Line");

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
}
