package edu.hm.ahager.listtest;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;


abstract class BaseNullSafeListTest extends ListTest {

    public void addNullTest() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> create(0).add(null),
                "The element to add should not be null."
        );
    }

    public void addAllNullThrows() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> create(0).addAll(null),
                "The list should not be null"
        );
    }

    public void addAllWithNullThrows() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> create(0).addAll(
                        Arrays.asList(1, null, 5)
                ),
                "All elements should not be null"
        );
    }

    public void addAtIndexThrows() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> create(1).add(
                        0, null
                ),
                "All elements should not be null"
        );
    }

    public void setAtIndexThrows() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> create(1).add(
                        0, null
                ),
                "All elements should not be null"
        );
    }

    public void replaceIndexThrows() {
        final List<Integer> sut = create(0);
        sut.addAll(Arrays.asList(0, 1, -256, 2, 3));
        Assertions.assertThrows(
                NullPointerException.class,
                () -> sut.replaceAll(
                        a -> {
                            if (a == -256) {
                                return null;
                            } else {
                                return a+1;
                            }
                        }
                ),
                "All elements should not be null"
        );
    }
}
