package edu.hm.hafner.edu.hm.hafner.analysis;

import java.util.List;

import org.assertj.core.api.ErrorCollector;
import org.assertj.core.api.SoftAssertionError;

import static org.assertj.core.groups.Properties.*;

public class IssueSoftAssert {

    private final ErrorCollector collector = new ErrorCollector();

    /**
     * Verifies that no proxied assertion methods have failed.
     *
     * @throws SoftAssertionError if any proxied assertion objects threw
     */
    public void assertAll() {
        List<Throwable> errors = collector.errors();
        if (!errors.isEmpty()) {
            throw new SoftAssertionError(extractProperty("message", String.class).from(errors));
        }
    }
}
