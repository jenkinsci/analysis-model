package edu.hm.hafner.analysis.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Provides some useful methods to process the DOM.
 *
 * @author Ullrich Hafner
 */
//CHECKSTYLE:OFF
@SuppressWarnings({"PMD", "all"})
public final class XmlElementUtil {
    /**
     * Returns all elements in the parent that match the specified name.
     *
     * @param parent
     *         the parent element
     * @param name
     *         the expected name of the childs
     *
     * @return the elements, the list might be empty if there is no match
     */
    public static List<Element> getChildElementsByName(final Element parent, final String name) {
        List<Element> elements = new ArrayList<>();
        if (parent != null) {
            Node child = parent.getFirstChild();
            while (child != null) {
                if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals(name)) {
                    elements.add((Element) child);
                }
                child = child.getNextSibling();
            }
        }
        return elements;
    }

    /**
     * Returns the first element in the parent that match the specified name.
     *
     * @param parent
     *         the parent element
     * @param name
     *         the expected name of the childs
     *
     * @return the first element if there is a match, {@link Optional#empty()} otherwise
     */
    public static Optional<Element> getFirstChildElementByName(final Element parent, final String name) {
        return getChildElementsByName(parent, name).stream().findFirst();
    }

    /**
     * Convert a {@link NodeList} into a {@code List<Element>}. Also filters out non elements from the node list.
     *
     * @param nodeList
     *         node list to convert.
     *
     * @return list of elements.
     */
    public static List<Element> nodeListToList(@CheckForNull final NodeList nodeList) {
        if (nodeList == null) {
            return new ArrayList<>();
        }
        int length = nodeList.getLength();
        List<Element> elements = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                elements.add((Element) node);
            }
        }
        return elements;
    }

    private XmlElementUtil() {
        // prevents instantiation
    }
}
