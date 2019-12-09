package edu.hm.hafner.util;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaCall;

/**
 * Matches if a call from outside the defining class uses a method or constructor annotated with {@link
 * VisibleForTesting}. There are two exceptions:
 * <ul>
 * <li>The method is called on the same class</li>
 * <li>The method is called in a method also annotated with {@link VisibleForTesting}</li>
 * </ul>
 */
public class AccessRestrictedToTests extends DescribedPredicate<JavaCall<?>> {
    /**
     * Creates a new instance of {@link AccessRestrictedToTests}.
     */
    public AccessRestrictedToTests() {
        super("access is restricted to tests");
    }

    @Override
    public boolean apply(final JavaCall<?> input) {
        return input.getTarget().isAnnotatedWith(VisibleForTesting.class)
                && !input.getOriginOwner().equals(input.getTargetOwner())
                && !input.getOrigin().isAnnotatedWith(VisibleForTesting.class);
    }
}
