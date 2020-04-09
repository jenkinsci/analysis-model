import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.ListTest;
import edu.hm.hafner.analysis.NullSafeList;

import static org.assertj.core.api.Assertions.*;

public class NullSafeListTest extends ListTest {

    @Override
    public List<Integer> create(final int numberOfInitalElements) {
        return new NullSafeList<>(new ArrayList<>(numberOfInitalElements));
    }

    @Test
    void shouldThrowNPEAfterAddingNull(){
        List<Integer> SUT = create(0);

        assertThatThrownBy(() -> SUT.add(null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNPEAfterAddingACollectionWithNull(){
        List<Integer> SUT = create(0);

        List<Integer> toBeAdded = new ArrayList<>();
        toBeAdded.add(null);

        assertThatThrownBy(() -> SUT.addAll(toBeAdded))
                .isExactlyInstanceOf(NullPointerException.class);
    }

}
