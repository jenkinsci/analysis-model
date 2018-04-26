package edu.hm.hafner.analysis.assertj;

import java.util.Objects;

import org.assertj.core.api.AbstractAssert;

import edu.hm.hafner.analysis.Issues;

/**
 * Assertions for {@link Issues}.
 *
 * @author Marcel Binder
 */
@SuppressWarnings({"ParameterHidesMemberVariable", "NonBooleanMethodNameMayNotStartWithQuestion"})
public class IssuesAssert extends AbstractAssert<IssuesAssert, Issues<?>> {
    private static final String EXPECTED_BUT_WAS_MESSAGE = "%nExpecting %s of:%n <%s>%nto be:%n <%s>%nbut was:%n <%s>.";

    /**
     * Creates a new {@link IssueAssert} to make assertions on actual {@link Issues}.
     *
     * @param actual
     *         the issue we want to make assertions on
     */
    public IssuesAssert(final Issues<?> actual) {
        super(actual, IssuesAssert.class);
    }

    /**
     * An entry point for {@link IssuesAssert} to follow AssertJ standard {@code assertThat()}. With a static import,
     * one can write directly {@code assertThat(myIssues)} and get a specific assertion with code completion.
     *
     * @param actual
     *         the issues we want to make assertions on
     *
     * @return a new {@link IssuesAssert}
     */
    public static IssuesAssert assertThat(final Issues<?> actual) {
        return new IssuesAssert(actual);
    }

    /**
     * Verifies that there are no issues in the {@link Issues} instance.
     *
     * @return this assertion object.
     * @throws AssertionError
     *         if the actual {@link Issues} size is not zero.
     */
    public IssuesAssert isEmpty() {
        isNotNull();

        if (!actual.isEmpty()) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "empty issues", actual, "empty", "not empty");
        }

        return this;
    }

    /**
     * Verifies that the actual origin of the {@link Issues} instance is equal to the given one.
     *
     * @param origin
     *         the expected origin to compare the actual {@link Issues} origin to.
     *
     * @return this assertion object.
     * @throws AssertionError
     *         if the actual {@link Issues} origin is not equal to the given one.
     */
    public IssuesAssert hasOrigin(final String origin) {
        isNotNull();

        if (!Objects.equals(actual.getOrigin(), origin)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "origin", actual, origin, actual.getOrigin());
        }
        return this;
    }

    /**
     * Verifies that the actual reference of the {@link Issues} instance is equal to the given one.
     *
     * @param reference
     *         the expected reference to compare the actual {@link Issues} reference to.
     *
     * @return this assertion object.
     * @throws AssertionError
     *         if the actual {@link Issues} reference is not equal to the given one.
     */
    public IssuesAssert hasReference(final String reference) {
        isNotNull();

        if (!Objects.equals(actual.getReference(), reference)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "reference", actual, reference, actual.getReference());
        }
        return this;
    }

    /**
     * Verifies that the actual size of the {@link Issues} instance is equal to the given one.
     *
     * @param size
     *         the expected size to compare the actual {@link Issues} size to.
     *
     * @return this assertion object.
     * @throws AssertionError
     *         if the actual {@link Issues} size is not equal to the given one.
     */
    public IssuesAssert hasSize(final int size) {
        isNotNull();

        if (actual.getSize() != size) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "size", actual, size, actual.size());
        }
        return this;
    }

    /**
     * Verifies that the actual size of duplicate issues of the {@link Issues} instance is equal to the given one.
     *
     * @param size
     *         the expected size to compare the actual {@link Issues} size to.
     *
     * @return this assertion object.
     * @throws AssertionError
     *         if the actual {@link Issues} size is not equal to the given one.
     */
    public IssuesAssert hasDuplicatesSize(final int size) {
        isNotNull();

        if (actual.getDuplicatesSize() != size) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "duplicates size", actual, size, actual.getDuplicatesSize());
        }
        return this;
    }

    /**
     * Verifies that the actual size of high priority issues in the {@link Issues} instance is equal to the given one.
     *
     * @param highPrioritySize
     *         the expected size of high priority issues to compare the actual {@link Issues} size to.
     *
     * @return this assertion object.
     * @throws AssertionError
     *         if the actual {@link Issues} size of high priority issues is not equal to the given one.
     */
    public IssuesAssert hasHighPrioritySize(final int highPrioritySize) {
        isNotNull();

        if (actual.getHighPrioritySize() != highPrioritySize) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "high priority size", actual, highPrioritySize,
                    actual.getHighPrioritySize());
        }
        return this;
    }

    /**
     * Verifies that the actual size of normal priority issues in the {@link Issues} instance is equal to the given
     * one.
     *
     * @param normalPrioritySize
     *         the expected size of normal priority issues to compare the actual {@link Issues} size to.
     *
     * @return this assertion object.
     * @throws AssertionError
     *         if the actual {@link Issues} size of normal priority issues is not equal to the given one.
     */
    public IssuesAssert hasNormalPrioritySize(final int normalPrioritySize) {
        isNotNull();

        if (actual.getNormalPrioritySize() != normalPrioritySize) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "normal priority size", actual, normalPrioritySize,
                    actual.getNormalPrioritySize());
        }
        return this;
    }

    /**
     * Verifies that the actual size of low priority issues in the {@link Issues} instance is equal to the given one.
     *
     * @param lowPrioritySize
     *         the expected size of low priority issues to compare the actual {@link Issues} size to.
     *
     * @return this assertion object.
     * @throws AssertionError
     *         if the actual {@link Issues} size of low priority issues is not equal to the given one.
     */
    public IssuesAssert hasLowPrioritySize(final int lowPrioritySize) {
        isNotNull();

        if (actual.getLowPrioritySize() != lowPrioritySize) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "low priority size", actual, lowPrioritySize,
                    actual.getLowPrioritySize());
        }
        return this;
    }
}
