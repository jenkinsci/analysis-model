package edu.hm.hafner.analysis;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import edu.hm.hafner.analysis.assertions.Assertions;

/**
 * abstract class for list tests, e.g. {@link ArrayListTest}.
 *
 * @author Simon Symhoven
 */
public abstract class ListTest {
    abstract List<Integer> create(int numberOfInitialElements);

    @Test
    @DisplayName("should be empty")
    void isEmpty() {
        List list  = this.create(0);
        Assertions.assertThatObject(list.size()).isEqualTo(0);
    }
}
