package edu.hm.hafner.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Builds {@link TreeString}s that share common prefixes. Call {@link #intern(String)} and you get the {@link
 * TreeString} that represents the same string, but as you interns more strings that share the same prefixes, those
 * {@link TreeString}s that you get back start to share data.
 * <p>
 * Because the internal state of {@link TreeString}s get mutated as new strings are interned (to exploit new-found
 * common prefixes), {@link TreeString}s returned from {@link #intern(String)} aren't thread-safe until {@link
 * TreeStringBuilder} is disposed. That is, you have to make sure other threads don't see those {@link TreeString}s
 * until you are done interning strings.
 *
 * @author Kohsuke Kawaguchi
 */
public class TreeStringBuilder {
    private Child root = new Child(new TreeString());

    /**
     * Interns a string.
     *
     * @param string
     *         the string to intern
     *
     * @return the String as {@link TreeString} instance
     */
    public TreeString intern(final String string) {
        return getRoot().intern(string).getNode();
    }

    /**
     * Interns a {@link TreeString} created elsewhere.
     *
     * @param treeString
     *         the {@link TreeString} to intern
     *
     * @return the String as {@link TreeString} instance
     */
    public TreeString intern(final TreeString treeString) {
        return getRoot().intern(treeString.toString()).getNode();
    }

    /**
     * Further reduces the memory footprint by finding the same labels across multiple {@link TreeString}s.
     */
    public void dedup() {
        getRoot().dedup(new HashMap<>());
    }

    /** Place holder that represents no child node, until one is added. */
    private static final Map<String, Child> NO_CHILDREN = Collections.emptyMap();

    Child getRoot() {
        return root;
    }

    void setRoot(final Child root) {
        this.root = root;
    }

    /**
     * Child node that may store other elements.
     */
    private static final class Child {
        private final TreeString node;

        private Map<String, Child> children = NO_CHILDREN;

        Child(final TreeString node) {
            this.node = node;
        }

        /**
         * Adds one edge and leaf to this tree node, or returns an existing node if any.
         *
         * @param string
         *         the string to intern
         */
        public Child intern(final String string) {
            if (string.isEmpty()) {
                return this;
            }

            makeWritable();
            for (Entry<String, Child> entry : children.entrySet()) {
                int plen = commonPrefix(entry.getKey(), string);
                if (plen > 0) {
                    if (plen < entry.getKey().length()) {
                        // insert a node between this and entry.value
                        Child child = entry.getValue();
                        String prefix = string.substring(0, plen);
                        Child middle = child.split(prefix);

                        // add 'middle' instead of 'child'
                        children.remove(entry.getKey());
                        children.put(prefix, middle);

                        return middle.intern(string.substring(plen));
                    }
                    else {
                        return entry.getValue().intern(string.substring(plen)); // entire key is suffix
                    }
                }
            }

            // no common prefix. an entirely new node.
            Child t = children.get(string);
            if (t == null) {
                t = new Child(new TreeString(getNode(), string));
                children.put(string, t);
            }
            return t;
        }

        /**
         * Makes sure {@link #children} is writable.
         */
        @SuppressWarnings("PMD.CompareObjectsWithEquals")
        private void makeWritable() {
            if (children == NO_CHILDREN) {
                children = new HashMap<>();
            }
        }

        /**
         * Inserts a new node between this node and its parent, and returns that node. Newly inserted 'middle' node will
         * have this node as its sole child.
         *
         * @param prefix
         *         the prefix
         */
        private Child split(final String prefix) {
            String suffix = getNode().getLabel().substring(prefix.length());

            Child middle = new Child(getNode().split(prefix));
            middle.makeWritable();
            middle.children.put(suffix, this);

            return middle;
        }

        /**
         * Returns the common prefix between two strings.
         *
         * @param a
         *         a string
         * @param b
         *         another string
         */
        private int commonPrefix(final String a, final String b) {
            int m = Math.min(a.length(), b.length());

            for (int i = 0; i < m; i++) {
                if (a.charAt(i) != b.charAt(i)) {
                    return i;
                }
            }
            return m;
        }

        /**
         * Calls {@link TreeString#dedup(Map)} recursively.
         */
        private void dedup(final Map<String, char[]> table) {
            getNode().dedup(table);
            for (Child child : children.values()) {
                child.dedup(table);
            }
        }

        TreeString getNode() {
            return node;
        }
    }
}
