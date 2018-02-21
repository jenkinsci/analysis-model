package edu.hm.hafner.analysis.parser.dry;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.parser.dry.CodeDuplication.DuplicationGroup;
import edu.hm.hafner.util.SerializableTest;
import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link CodeDuplication}.
 *
 * @author Ullrich Hafner
 */
class CodeDuplicationTest extends SerializableTest<CodeDuplication> {
    private static final String SERIALIZATION_NAME = "dry.ser";
    private static final String CODE_FRAGMENT = "fragment";

    @Test
    void shouldHaveFragmentAndDuplications() {
        DuplicationGroup group = new DuplicationGroup(CODE_FRAGMENT);

        IssueBuilder builder = new IssueBuilder();

        CodeDuplication duplication = new CodeDuplication(builder.setFileName("file1").build(), group);
        assertThat(duplication.getDuplications()).isEmpty();
        assertThat(duplication.getDescription()).isEqualTo("<pre>fragment</pre>");

        CodeDuplication another = new CodeDuplication(builder.setFileName("file2").build(), group);

        assertThat(duplication.getDuplications()).containsExactly(another);
        assertThat(another.getDuplications()).containsExactly(duplication);
    }

    @Override
    protected CodeDuplication createSerializable() {
        DuplicationGroup group = new DuplicationGroup(CODE_FRAGMENT);

        IssueBuilder builder = new IssueBuilder();

        return new CodeDuplication(builder.setFileName("file1").build(), group);
    }

    /**
     * Verifies that saved serialized format (from a previous release) still can be resolved with the current
     * implementation of {@link CodeDuplication}.
     */
    @Test
    void shouldReadIssueFromOldSerialization() {
        byte[] restored = readResource(SERIALIZATION_NAME);

        assertThatSerializableCanBeRestoredFrom(restored);
    }

    /**
     * Tests for the class {@link DuplicationGroup}.
     */
    @Nested
    @DisplayName("Composing duplication group")
    class DuplicationGroupTest{
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
            assertThat(group.getCodeFragment()).isEqualTo(CODE_FRAGMENT);

            group.setCodeFragment("other");
            assertThat(group.getCodeFragment()).isEqualTo(CODE_FRAGMENT);
        }

        @Test
        void shouldReferenceAllDuplications() {
            DuplicationGroup group = new DuplicationGroup(CODE_FRAGMENT);

            assertThat(group.getDuplications()).isEmpty();

            IssueBuilder builder = new IssueBuilder();

            CodeDuplication duplication = new CodeDuplication(builder.build(), group);
            assertThat(group.getDuplications()).containsExactly(duplication);

            CodeDuplication another = new CodeDuplication(builder.build(), group);
            assertThat(group.getDuplications()).containsExactly(duplication, another);
        }
    }

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
        new CodeDuplicationTest().createSerializationFile();
    }
}