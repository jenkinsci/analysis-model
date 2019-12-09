package edu.hm.hafner.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link TreeStringBuilder}.
 *
 * @author Kohsuke Kawaguchi
 */
class TreeStringBuilderTest {
    @SuppressWarnings("ConstantConditions")
    @Test
    void shouldCreateSimpleTreeStringsWithBuilder() {
        TreeStringBuilder builder = new TreeStringBuilder();
        TreeString foo = builder.intern("foo");
        assertThat(foo).hasToString("foo");
        assertThat(foo.getLabel()).isEqualTo("foo");

        TreeString treeString = builder.intern("foo/bar/zot");
        assertThat(treeString).hasToString("foo/bar/zot");
        assertThat(treeString.getLabel()).isEqualTo("/bar/zot");
        assertThat(treeString.getParent()).isSameAs(foo);

        TreeString interned = builder.intern(treeString);
        assertThat(interned).hasToString("foo/bar/zot");
        assertThat(interned.getLabel()).isEqualTo("/bar/zot");

        assertThat(treeString).isSameAs(interned);

        assertThat(builder.intern("")).hasToString("");

        TreeString otherMiddleChild = builder.intern("foo/bar/xxx");
        assertThat(otherMiddleChild).hasToString("foo/bar/xxx");
        assertThat(otherMiddleChild.getLabel()).isEqualTo("xxx");

        assertThat(otherMiddleChild.getParent()).isSameAs(treeString.getParent());
        assertThat(otherMiddleChild.getParent().getParent()).isSameAs(foo);

        // middle node changed label but not toString
        assertThat(treeString.getLabel()).isEqualTo("zot");
        assertThat(treeString).hasToString("foo/bar/zot");

        assertThat(builder.intern("").isBlank()).isTrue();
        assertThat(TreeString.valueOf("foo/bar/zot")).hasToString("foo/bar/zot");
    }

    @Test
    void shouldProvideProperEqualsAndHashCode() {
        TreeStringBuilder builder = new TreeStringBuilder();

        TreeString foo = builder.intern("foo");
        TreeString bar = builder.intern("foo/bar");
        TreeString zot = builder.intern("foo/bar/zot");

        assertThat(new TreeStringBuilder().intern("foo")).isEqualTo(foo);
        assertThat(new TreeStringBuilder().intern("foo/bar")).isEqualTo(bar);
        assertThat(new TreeStringBuilder().intern("foo/bar/zot")).isEqualTo(zot);

        assertThat(new TreeStringBuilder().intern("foo")).hasSameHashCodeAs(foo);
        assertThat(new TreeStringBuilder().intern("foo/bar")).hasSameHashCodeAs(bar);
        assertThat(new TreeStringBuilder().intern("foo/bar/zot")).hasSameHashCodeAs(zot);
    }

    /**
     * Pseudo random (but deterministic) test.
     */
    @Test
    void shouldCreateRandomTreeStrings() {
        String[] dictionary = {"aa", "b", "aba", "ba"};
        TreeStringBuilder builder = new TreeStringBuilder();

        Random random = new Random(0);

        List<String> a = new ArrayList<>();
        List<TreeString> o = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            StringBuilder b = new StringBuilder();
            for (int j = 0; j < random.nextInt(10) + 3; j++) {
                b.append(dictionary[random.nextInt(4)]);
            }
            String s = b.toString();

            a.add(s);

            TreeString p = builder.intern(s);
            assertThat(p).hasToString(s);
            o.add(p);
        }

        // make sure values are still all intact
        for (int i = 0; i < a.size(); i++) {
            assertThat(o.get(i)).hasToString(a.get(i));
        }

        builder.dedup();

        // verify one more time
        for (int i = 0; i < a.size(); i++) {
            assertThat(o.get(i)).hasToString(a.get(i));
        }
    }
}
