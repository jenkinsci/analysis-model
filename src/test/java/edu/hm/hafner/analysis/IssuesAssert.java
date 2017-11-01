package edu.hm.hafner.analysis;

import org.assertj.core.api.AbstractAssert;

/**
 * Custom assertions for {@link Issues}.
 *
 * @author Joscha Behrmann
 */
@SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
public class IssuesAssert extends AbstractAssert<IssuesAssert, Issues> {
    private static final String EXPECTED_BUT_WAS_MESSAGE = "%nExpecting:%n <%s>%nto be equal to:%n <%s>%nbut was not.";

    public IssuesAssert(final Issues actual) {
        super(actual, IssuesAssert.class);
    }

    public static IssuesAssert assertThat(final Issues actual) {
        return new IssuesAssert(actual);
    }


    public IssuesAssert hasSize(final int size) {
        isNotNull();

        if (actual.size() != size || actual.getSize() != size) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE,
                    "size", size);
        }

        return this;
    }

    public IssuesAssert isEmpty() {
        isNotNull();

        if (actual.size() > 0 || actual.getSize() > 0) {
            failWithMessage("Expecting collection to be empty but was not.");
        }

        return this;
    }

    public IssuesAssert hasHighPrioritySize(final int highPrioritySize) {
        isNotNull();

        if (actual.getHighPrioritySize() != highPrioritySize) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE,
                    "highPrioritySize", highPrioritySize);
        }

        return this;
    }

    public IssuesAssert hasNormalPrioritySize(final int normalPrioritySize) {
        isNotNull();

        if (actual.getNormalPrioritySize() != normalPrioritySize) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE,
                    "normalPrioritySize", normalPrioritySize);
        }

        return this;
    }

    public IssuesAssert hasLowPrioritySize(final int lowPrioritySize) {
        isNotNull();

        if (actual.getLowPrioritySize() != lowPrioritySize) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE,
                    "lowPrioritySize", lowPrioritySize);
        }

        return this;
    }

    public IssuesAssert hasNumberOfFiles(final int numberOfFiles) {
        isNotNull();

        if (actual.getNumberOfFiles() != numberOfFiles) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE,
                    "numberOfFiles", numberOfFiles);
        }

        return this;
    }
}
