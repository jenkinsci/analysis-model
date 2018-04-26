package edu.hm.hafner.analysis.parser.dry;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.util.SerializableTest;
import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link DuplicationGroup}.
 *
 * @author Ullrich Hafner
 */
class DuplicationGroupTest extends SerializableTest<DuplicationGroup> {
    /**
     * Serializes an code duplication to a file. Use this method in case the properties have been changed and the
     * readResolve method has been adapted accordingly so that the old serialization still can be read.
     *
     * @param args
     *         not used
     *
     * @throws IOException
     *         if the file could not be written
     */
    public static void main(final String... args) throws IOException {
        new DuplicationGroupTest().createSerializationFile();
    }

    private static final String SERIALIZATION_NAME = "dry.ser";
    private static final String CODE_FRAGMENT = "fragment";

    @Override
    protected DuplicationGroup createSerializable() {
        DuplicationGroup group = new DuplicationGroup();

        IssueBuilder builder = new IssueBuilder();
        group.add(builder.setAdditionalProperties(group).setFileName("1").build());
        group.add(builder.setAdditionalProperties(group).setFileName("2").build());

        return group;
    }

    /**
     * Verifies that saved serialized format (from a previous release) still can be resolved with the current
     * implementation of {@link DuplicationGroup}.
     */
    @Test
    void shouldReadIssueFromOldSerialization() {
        byte[] restored = readAllBytes(SERIALIZATION_NAME);

        assertThatSerializableCanBeRestoredFrom(restored);
    }

    @Test
    void shouldBeEmptyWhenCreated() {
        DuplicationGroup group = new DuplicationGroup();

        assertThat(group.getCodeFragment()).isEmpty();
        assertThat(group.getDuplications()).isEmpty();
    }

    @Test
    void shouldNotOverwriteFragmentOnceItHasBeenSet() {
        DuplicationGroup group = new DuplicationGroup();

        assertThat(group.getCodeFragment()).isEmpty();

        group.setCodeFragment(CODE_FRAGMENT);
        assertThat(group.getCodeFragment()).isEqualTo(CODE_FRAGMENT);

        group.setCodeFragment("other");
        assertThat(group.getCodeFragment()).isEqualTo(CODE_FRAGMENT);

        DuplicationGroup groupWithFragment = new DuplicationGroup(CODE_FRAGMENT);
        assertThat(groupWithFragment.getCodeFragment()).isEqualTo(CODE_FRAGMENT);

        groupWithFragment.setCodeFragment("other");
        assertThat(groupWithFragment.getCodeFragment()).isEqualTo(CODE_FRAGMENT);
    }

    @Test
    void shouldReferenceAllDuplications() {
        DuplicationGroup group = new DuplicationGroup(CODE_FRAGMENT);

        assertThat(group.getDuplications()).isEmpty();

        IssueBuilder builder = new IssueBuilder();
        Issue first = builder.setAdditionalProperties(group).build();
        Issue second = builder.setAdditionalProperties(group).build();

        group.add(first);
        group.add(second);

        assertThat(group.getDuplications()).containsExactly(first, second);
        assertThat(first.getAdditionalProperties()).isEqualTo(second.getAdditionalProperties());
    }
}