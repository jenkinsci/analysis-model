package edu.hm.hafner.analysis.parser;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.digester3.Digester;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.xml.sax.SAXException;

import edu.hm.hafner.analysis.SecureDigester;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Reads the meta data of the Checkstyle rules from the DocBook files of the Checkstyle distribution.
 *
 * @author Ullrich Hafner
 */
public class CheckStyleRules {
    private final Map<String, CheckStyleParser.Rule> rulesByName = new HashMap<>();

    /**
     * Loads the available rules into a map.
     */
    public CheckStyleRules() {
        String[] ruleFiles = {"annotation", "blocks", "coding", "design", "filters", "header",
                "imports", "javadoc", "metrics", "misc", "modifier", "naming", "regexp",
                "reporting", "sizes", "whitespace"};
        for (String ruleFile : ruleFiles) {
            try (var inputStream = CheckStyleRules.class.getResourceAsStream("checkstyle/config_" + ruleFile + ".xml")) {
                var digester = createDigester();
                List<CheckStyleParser.Rule> rules = new ArrayList<>();
                digester.push(rules);
                digester.parse(inputStream);
                for (CheckStyleParser.Rule rule : rules) {
                    if (StringUtils.isNotBlank(rule.getDescription())) {
                        rulesByName.put(rule.getName(), rule);
                    }
                }
            }
            catch (ParserConfigurationException | IOException | SAXException exception) {
                log(exception);
            }
        }
    }

    private void log(final Throwable exception) {
        Logger.getLogger(CheckStyleRules.class.getName())
                .log(Level.SEVERE, "Can't initialize CheckStyle rules.", exception);
    }

    /**
     * Creates a new digester.
     *
     * @return the new digester.
     * @throws ParserConfigurationException
     *         if digester is not configured properly
     */
    private Digester createDigester() throws ParserConfigurationException {
        var digester = new SecureDigester(CheckStyleRules.class);

        var section = "*/section";
        digester.addObjectCreate(section, CheckStyleParser.Rule.class);
        digester.addSetProperties(section);
        digester.addSetNext(section, "add");

        var subSection = "*/section/subsection";
        digester.addObjectCreate(subSection, CheckStyleParser.Topic.class);
        digester.addSetProperties(subSection);
        digester.addSetNext(subSection, "setDescription");
        digester.addRule(subSection, new CheckStyleParser.TopicRule());
        return digester;
    }

    /**
     * Returns all Checkstyle rules.
     *
     * @return all Checkstyle rules
     */
    public Collection<CheckStyleParser.Rule> getRules() {
        return Collections.unmodifiableCollection(rulesByName.values());
    }

    /**
     * Returns the Checkstyle rule with the specified name.
     *
     * @param name
     *         the name of the rule
     *
     * @return the Checkstyle rule with the specified name.
     */
    public CheckStyleParser.Rule getRule(final String name) {
        var rule = rulesByName.get(name);
        if (rule == null) {
            rule = rulesByName.get(Strings.CS.removeEnd(name, "Check"));
        }
        if (rule == null) {
            return new CheckStyleParser.Rule(name);
        }
        return rule;
    }

    /**
     * Returns the description of the Checkstyle rule with the specified name.
     *
     * @param name
     *         the name of the rule
     *
     * @return the description for the specified rule
     */
    public String getDescription(final String name) {
        return getRule(name).getDescription();
    }
}
