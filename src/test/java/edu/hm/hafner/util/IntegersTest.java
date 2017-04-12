package edu.hm.hafner.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link Integers}.
 *
 * @author Ullrich Hafner
 */
public class IntegersTest {
    @Rule
    public final TextFromStandardInputStream input = TextFromStandardInputStream.emptyStandardInputStream();

    /** Should read some integers. */
    @Test
    public void shouldReadInteger() {
        input.provideLines("1234");
        assertThat(Integers.read("Hello World")).isEqualTo(1234);

        input.provideLines("0");
        assertThat(Integers.read("Hello World")).isEqualTo(0);

        input.provideLines("-1");
        assertThat(Integers.read("Hello World")).isEqualTo(-1);
    }
}