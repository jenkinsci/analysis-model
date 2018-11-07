package edu.hm.hafner.analysis.assertj;

import org.assertj.core.api.AbstractAssert;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

/**
 * Assertions for {@link Report}.
 *
 * @author Marcel Binder
 */
@SuppressWarnings({"ParameterHidesMemberVariable", "NonBooleanMethodNameMayNotStartWithQuestion", "PMD.LinguisticNaming"})
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
     * @param expectedSizeError
     *         the expected size of issues with {@link Severity#ERROR} to compare the actual {@link Report} size to.
     * @param expectedSizeHigh
     *         the expected size of issues with {@link Severity#WARNING_HIGH} to compare the actual {@link Report} size to.
     * @param expectedSizeNormal
     *         the expected size of issues with {@link Severity#WARNING_NORMAL} to compare the actual {@link Report} size to.
     * @param expectedSizeLow
     *         the expected size of issues with {@link Severity#WARNING_LOW} to compare the actual {@link Report} size to.
     *
     * @return this assertion object.
     * @throws AssertionError
     *         if the actual {@link Report} size is not equal to the given one.
     */
    public IssuesAssert hasSeverities(final int expectedSizeError, 
            final int expectedSizeHigh, final int expectedSizeNormal,
            final int expectedSizeLow) {
        isNotNull();

        if (actual.getSizeOf(Severity.ERROR) != expectedSizeError) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "expectedSizeError", actual, expectedSizeError, actual.getSizeOf(Severity.ERROR));
        }
        if (actual.getSizeOf(Severity.WARNING_HIGH) != expectedSizeHigh) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "expectedSizeHigh", actual, expectedSizeHigh, actual.getSizeOf(Severity.WARNING_HIGH));
        }
        if (actual.getSizeOf(Severity.WARNING_NORMAL) != expectedSizeNormal) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "expectedSizeNormal", actual, expectedSizeNormal, actual.getSizeOf(Severity.WARNING_NORMAL));
        }
        if (actual.getSizeOf(Severity.WARNING_LOW) != expectedSizeLow) {
            failWithMessage(EXPECTED_BUT_WAS_MESSAGE, "expectedSizeLow", actual, expectedSizeLow, actual.getSizeOf(Severity.WARNING_LOW));
        }
        return this;
    }


}
