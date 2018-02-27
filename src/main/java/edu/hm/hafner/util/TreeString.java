package edu.hm.hafner.util;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * {@link TreeString} is an alternative string representation that saves the memory when you have a large number of
 * strings that share common prefixes (such as various file names.)
 * <p>
 * {@link TreeString} can be built with {@link TreeStringBuilder}.
 *
 * @author Kohsuke Kawaguchi
 */
public final class TreeString implements Serializable {
    private static final long serialVersionUID = 3621959682117480904L;

    /** Parent node that represents the prefix. */
    private TreeString parent;

    /** {@link #parent} + {@code label} is the string value of this node. */
    private char[] label;

    /**
     * Creates a new root {@link TreeString}.
     */
    TreeString() {
        this(null, "");
    }

    /**
     * Creates a new {@link TreeString} with the given parent and suffix.
     *
     * @param parent
     *         the parent
     * @param label
     *         the suffix
     */
    TreeString(@CheckForNull final TreeString parent, final String label) {
        Ensure.that(parent == null || !label.isEmpty())
                .isTrue("if there's a parent '%s', label '%s' can't be empty", parent, label);

        this.parent = parent;
        this.label = label.toCharArray(); // string created as a substring of another string can have a lot of garbage attached to it.
    }

    String getLabel() {
        return new String(label);
    }

    /**
     * Inserts a new node between this node and its parent, and returns the newly inserted node.
     * <p>
     * This operation doesn't change the string representation of this node.
     */
    TreeString split(final String prefix) {
        assert getLabel().startsWith(prefix);
        char[] suffix = new char[label.length - prefix.length()];
        System.arraycopy(label, prefix.length(), suffix, 0, suffix.length);

        TreeString middle = new TreeString(parent, prefix);
        label = suffix;
        parent = middle;

        return middle;
    }

    /**
     * How many nodes do we have from the root to this node (including 'this' itself?). Thus depth of the root node is
     * 1.
     */
    private int depth() {
        int i = 0;
        for (TreeString p = this; p != null; p = p.parent) {
            i++;
        }
        return i;
    }

    @Override
    public boolean equals(final Object rhs) {
        if (rhs == null) {
            return false;
        }
        return rhs.getClass() == TreeString.class
                && ((TreeString) rhs).getLabel().equals(getLabel());
    }

    @Override
    public int hashCode() {
        int h = parent == null ? 0 : parent.hashCode();

        for (int i = 0; i < label.length; i++) {
            h = 31 * h + label[i];
        }

        assert toString().hashCode() == h;
        return h;
    }

    /**
     * Returns the full string representation.
     */
    @Override
    public String toString() {
        char[][] tokens = new char[depth()][];
        int i = tokens.length;
        int sz = 0;
        for (TreeString p = this; p != null; p = p.parent) {
            tokens[--i] = p.label;
            sz += p.label.length;
        }

        StringBuilder buf = new StringBuilder(sz);
        for (char[] token : tokens) {
            buf.append(token);
        }

        return buf.toString();
    }

    /**
     * Interns {@link #label}
     */
    /* package */void dedup(final Map<String, char[]> table) {
        String l = getLabel();
        char[] v = table.get(l);
        if (v != null) {
            label = v;
        }
        else {
            table.put(l, label);
        }
    }

    public boolean isBlank() {
        return StringUtils.isBlank(toString());
    }

    /**
     * Returns the string representation of the provided {@link TreeString}.
     *
     * @param treeString
     *         the tree string
     *
     * @return the string representation or {@code null} if the parameter if {@code null}
     */
    public static String toString(@CheckForNull final TreeString treeString) {
        return treeString == null ? null : treeString.toString();
    }

    /**
     * Creates a {@link TreeString}. Useful if you need to create one-off {@link TreeString} without {@link
     * TreeStringBuilder}. Memory consumption is still about the same to {@code new String(s)}.
     *
     * @return the new {@link TreeString} or {@code null} if the parameter is {@code null}
     */
    public static TreeString of(@CheckForNull final String s) {
        if (s == null) {
            return null;
        }
        return new TreeString(null, s);
    }
}
