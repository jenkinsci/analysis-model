package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

/**
 * Provides descriptions for all pylint rules.
 */
public class PyLintDescriptions {
    static final String NO_DESCRIPTION_FOUND = "no description found";

    private final Map<String, String> descriptionByName = new HashMap<>();
    private final Map<String, String> descriptionById = new HashMap<>();

    /**
     * Loads the available rules into a map.
     */
    public PyLintDescriptions() {
        var parser = new JSONParser(JSONParser.MODE_JSON_SIMPLE);

        try (var inputStream = PyLintDescriptions.class.getResourceAsStream("pylint-descriptions.json")) {
            var elements = (JSONArray) parser.parse(inputStream);
            for (Object element : elements) {
                var object = (JSONObject) element;
                var description = object.getAsString("description");
                descriptionByName.put(object.getAsString("name"), description);
                descriptionById.put(object.getAsString("code"), description);
            }
        }
        catch (IOException | ParseException ignored) {
            // ignore all exceptions
        }
    }

    /**
     * Returns the size of this messages cache.
     *
     * @return the number of stored messages (English locale)
     */
    public int size() {
        return descriptionById.size();
    }

    /**
     * Returns the description of PyLint rule with the specified name.
     *
     * @param name
     *         the name of the rule, like 'missing-docstring'
     *
     * @return the description for the specified rule
     */
    public String getDescription(final String name) {
        return descriptionByName.getOrDefault(name, descriptionById.getOrDefault(name, NO_DESCRIPTION_FOUND));
    }
}
