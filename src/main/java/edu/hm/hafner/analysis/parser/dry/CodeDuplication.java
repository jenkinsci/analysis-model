package edu.hm.hafner.analysis.parser.dry;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.util.Ensure;

/**
 * A code duplication reported by a code duplication detector like CPD, DupFinder, or Simian.
 *
 * @author Ullrich Hafner
 */
public class CodeDuplication extends Issue {
    private static final long serialVersionUID = 5495679948804259058L;

    private final DuplicationGroup group;

    /**
     * Creates a new instance of {@link CodeDuplication}.
     *
     * @param issue
     *         the properties if this code duplication, represented by an {@link Issue} instance
     * @param group
     *         the group that links all references to the copied code fragment
     */
    public CodeDuplication(final Issue issue, final DuplicationGroup group) {
        super(issue);

        Ensure.that(group).isNotNull();
        this.group = group;
        group.add(this);
    }

    @Override
    public String getDescription() {
        if (StringUtils.isEmpty(group.getCodeFragment())) {
            return StringUtils.EMPTY;
        }
        return String.format("<pre>%s</pre>", group.getCodeFragment());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        CodeDuplication that = (CodeDuplication) o;

        return group.equals(that.group);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + group.hashCode();
        return result;
    }

    /**
     * Returns the other duplications of the affected code fragment.
     *
     * @return the other duplications
     */
    public List<CodeDuplication> getDuplications() {
        List<CodeDuplication> allDuplications = group.getDuplications();
        allDuplications.remove(this);
        return allDuplications;
    }

    /**
     * Links all affected files of a duplicated code fragment.
     */
    public static final class DuplicationGroup implements Serializable {
        private final List<CodeDuplication> occurrences = new ArrayList<>();
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
         * Creates a new duplication group. The code fragment is not set and should be set using {@link
         * #add(CodeDuplication)}.
         */
        public DuplicationGroup() {
            this(StringUtils.EMPTY);
        }

        /**
         * Sets the code fragment of the duplication group. Once this value has been set to a non-empty value it will
         * not  change again.
         *
         * @param codeFragment
         *         the copied code fragment
         */
        @SuppressWarnings("InstanceVariableUsedBeforeInitialized")
        public void setCodeFragment(final String codeFragment) {
            if (StringUtils.isBlank(this.codeFragment)) {
                this.codeFragment = StringUtils.defaultString(codeFragment);
            }
        }

        void add(final CodeDuplication codeDuplication) {
            occurrences.add(codeDuplication);
        }

        String getCodeFragment() {
            return codeFragment;
        }

        List<CodeDuplication> getDuplications() {
            return new ArrayList<>(occurrences);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            DuplicationGroup that = (DuplicationGroup) o;

            return codeFragment.equals(that.codeFragment);
        }

        @Override
        public int hashCode() {
            return codeFragment.hashCode();
        }
    }
}
