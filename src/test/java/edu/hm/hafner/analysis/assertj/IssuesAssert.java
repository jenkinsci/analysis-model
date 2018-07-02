package edu.hm.hafner.analysis.assertj;

import java.util.Objects;

import org.assertj.core.api.AbstractAssert;

import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

/**
 * Assertions for {@link Report}.
 *
 * @author Marcel Binder
 */
@SuppressWarnings({"ParameterHidesMemberVariable", "NonBooleanMethodNameMayNotStartWithQuestion"})
public class IssuesAssert extends AbstractAssert<IssuesAssert, Report> {
    private static final String EXPECTED_BUT_WAS_MESSAGE = "%nExpecting %s of:%n <%s>%nto be:%n <%s>%nbut was:%n <%s>.";

    /**
     * Creates a new {@link IssueAssert} to make assertions on actual {@link Report}.
     *
     * @param actual
     *         the issue we want to make assertions on
     */
    public IssuesAssert(final Report actual) {
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
    public static IssuesAssert assertThat(final Report actual) {
        return new IssuesAssert(actual);
    }

    /**
     * Verifies that there are no issues in the {@link Report} instance.
     *
     * @return this assertion object.
     * @throws AssertionError
     *         if the actual {@link Report} size is not zero.
     */
    public IssuesAssert isEmpty() {
        isNotNull();

        if (!actual.isEmpty()) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "empty issues", actual, "empty", "not empty");
        }

        return this;
    }

    /**
     * Verifies that the actual origin of the {@link Report} instance is equal to the given one.
     *
     * @param origin
     *         the expected origin to compare the actual {@link Report} origin to.
     *
     * @return this assertion object.
     * @throws AssertionError
     *         if the actual {@link Report} origin is not equal to the given one.
     */
    public IssuesAssert hasId(final String origin) {
        isNotNull();

        if (!Objects.equals(actual.getId(), origin)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "origin", actual, origin, actual.getId());
        }
        return this;
    }

    /**
     * Verifies that the actual reference of the {@link Report} instance is equal to the given one.
     *
     * @param reference
     *         the expected reference to compare the actual {@link Report} reference to.
     *
     * @return this assertion object.
     * @throws AssertionError
     *         if the actual {@link Report} reference is not equal to the given one.
     */
    public IssuesAssert hasReference(final String reference) {
        isNotNull();

        if (!Objects.equals(actual.getReference(), reference)) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "reference", actual, reference, actual.getReference());
        }
        return this;
    }

    /**
     * Verifies that the actual size of the {@link Report} instance is equal to the given one.
     *
     * @param size
     *         the expected size to compare the actual {@link Report} size to.
     *
     * @return this assertion object.
     * @throws AssertionError
     *         if the actual {@link Report} size is not equal to the given one.
     */
    public IssuesAssert hasSize(final int size) {
        isNotNull();

        if (actual.getSize() != size) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "size", actual, size, actual.size());
        }
        return this;
    }

    /**
     * Verifies that the actual size of duplicate issues of the {@link Report} instance is equal to the given one.
     *
     * @param size
     *         the expected size to compare the actual {@link Report} size to.
     *
     * @return this assertion object.
     * @throws AssertionError
     *         if the actual {@link Report} size is not equal to the given one.
     */
    public IssuesAssert hasDuplicatesSize(final int size) {
        isNotNull();

        if (actual.getDuplicatesSize() != size) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "duplicates size", actual, size, actual.getDuplicatesSize());
        }
        return this;
    }

    /**
     * Verifies that the actual size of the {@link Report} instance is equal to the given one.
     *
     * @param expectedSizeHigh
     *         the expected size of issues with {@link Priority#HIGH} to compare the actual {@link Report} size to.
     * @param expectedSizeNormal
     *         the expected size of issues with {@link Priority#NORMAL} to compare the actual {@link Report} size to.
     * @param expectedSizeLow
     *         the expected size of issues with {@link Priority#LOW} to compare the actual {@link Report} size to.
     *
     * @return this assertion object.
     * @throws AssertionError
     *         if the actual {@link Report} size is not equal to the given one.
     */
    public IssuesAssert hasPriorities(final int expectedSizeHigh, final int expectedSizeNormal, final int expectedSizeLow) {
        isNotNull();

        if (actual.getSizeOf(Severity.WARNING_HIGH) != expectedSizeHigh) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "expectedSizeHigh", actual, expectedSizeHigh, actual.size());
        }
        if (actual.getSizeOf(Severity.WARNING_NORMAL) != expectedSizeNormal) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "expectedSizeNormal", actual, expectedSizeHigh, actual.size());
        }
        if (actual.getSizeOf(Severity.WARNING_LOW) != expectedSizeLow) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "expectedSizeLow", actual, expectedSizeHigh, actual.size());
        }
        return this;
    }


}
