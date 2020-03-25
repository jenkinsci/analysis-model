import java.util.ArrayList;
import java.util.List;

import edu.hm.hafner.analysis.ListTest;

public class ArrayListTest extends ListTest {

    @Override
    public List<Integer> create(final int numberOfInitalElements) {
        return new ArrayList<>(numberOfInitalElements);
    }
}
