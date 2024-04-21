package edu.hm.hafner.analysis.parser.pmd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import net.sourceforge.pmd.lang.rule.Rule;
import net.sourceforge.pmd.lang.rule.RuleSet;
import net.sourceforge.pmd.lang.rule.RuleSetLoader;

import static j2html.TagCreator.*;

/**
 * Provides access to rule descriptions and examples.
 *
 * @author Ullrich Hafner
 */
public class PmdMessages {
    private final Map<String, RuleSet> rules = new HashMap<>();

    /**
     * Loads the available rules into a map.
     */
    public PmdMessages() {
        RuleSetLoader loader = new RuleSetLoader();
        List<RuleSet> ruleSets = loader.getStandardRuleSets();
        for (RuleSet ruleSet : ruleSets) {
            rules.put(ruleSet.getName(), ruleSet);
        }
        if (rules.isEmpty()) {
            throw new IllegalStateException("No rule sets found");
        }
    }

    /**
     * Returns the number of available rule sets.
     *
     * @return the number of rule sets
     */
    public int size() {
        return rules.size();
    }

    /**
     * Returns the message for the specified PMD rule.
     *
     * @param ruleSetName
     *         PMD rule set
     * @param ruleName
     *         PMD rule ID
     *
     * @return the message
     */
    public String getMessage(final String ruleSetName, final String ruleName) {
        if (rules.containsKey(ruleSetName)) {
            RuleSet ruleSet = rules.get(ruleSetName);
            Rule rule = ruleSet.getRuleByName(ruleName);
            if (rule != null) {
                return createMessage(rule);
            }
        }
        return StringUtils.EMPTY;
    }

    /**
     * Creates the message string to be shown for the specified rule.
     *
     * @param rule
     *         the rule
     *
     * @return the message string to be shown for the specified rule
     */
    private String createMessage(final Rule rule) {
        StringBuilder message = new StringBuilder(rule.getDescription());
        List<String> examples = rule.getExamples();
        if (!examples.isEmpty()) {
            message.append(pre().with(code(examples.get(0))).renderFormatted());
        }
        if (StringUtils.isNotBlank(rule.getExternalInfoUrl())) {
            message.append(a().withHref(rule.getExternalInfoUrl()).withText("See PMD documentation.").renderFormatted());
        }
        return message.toString();
    }
}

