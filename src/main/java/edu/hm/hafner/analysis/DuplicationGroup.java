package edu.hm.hafner.analysis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.util.Generated;
import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Links all affected files of a duplicated code fragment. A code duplication could be reported by a code duplication
 * detector like CPD, DupFinder, or Simian.
 */
public final class DuplicationGroup implements Serializable {
    private static final long serialVersionUID = -5005784523279541971L;

    private final List<Issue> occurrences = new ArrayList<>();
    private String codeFragment = StringUtils.EMPTY;

    /**
     * Creates a new duplication group for the specified code fragment.
     *
     * @param codeFragment
     *         the copied code fragment
     */
    public DuplicationGroup(@CheckForNull final String codeFragment) {
        setCodeFragment(codeFragment);
    }

    /**
     * Creates a new duplication group. The code fragment is not set and should be set using {@link #add(Issue)}.
     */
    public DuplicationGroup() {
        this(StringUtils.EMPTY);
    }

    /**
     * Sets the code fragment of the duplication group. Once this value has been set to a non-empty value it will not
     * change again.
     *
     * @param codeFragment
     *         the copied code fragment
     */
    @SuppressWarnings("InstanceVariableUsedBeforeInitialized")
    public void setCodeFragment(@CheckForNull final String codeFragment) {
        if (StringUtils.isBlank(this.codeFragment)) {
            this.codeFragment = StringUtils.defaultString(codeFragment);
        }
    }

    /**
     * Adds the specified duplication (represented by an {@link Issue} instance) to this group of duplications.
     *
     * @param issue
     *         the issues that reference the position of the duplicated code fragment
     */
    public void add(final Issue issue) {
        occurrences.add(issue);
    }

    /**
     * Returns the code fragment that has been duplicated by all members of this group.
     *
     * @return the duplicated code fragment
     */
    public String getCodeFragment() {
        return codeFragment;
    }

    public List<Issue> getDuplications() {
        return new ArrayList<>(occurrences);
    }

    @Override
    @Generated
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        var that = (DuplicationGroup) o;
        return Objects.equals(codeFragment, that.codeFragment);
    }

    @Override
    @Generated
    public int hashCode() {
        return Objects.hashCode(codeFragment);
    }
}
