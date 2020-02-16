package edu.hm.hafner.analysis.ast;

import org.junit.jupiter.api.Test;

/**
 * Tests the class {@link MethodOrClassAst}.
 *
 * @author Christian Möstl
 * @author Ullrich Hafner
 */
class MethodOrClassAstTest extends AbstractAstTest {
    protected MethodOrClassAst createAst(final String fileName, final int lineNumber) {
        return new MethodOrClassAst(fileName, lineNumber);
    }

    /**
     * Verifies the AST contains the elements of the whole method and the affected line.
     */
    @Test
    void shouldPickWholeMethod() {
        assertThatAstIs(createAst(37), LINE67_METHOD + WHOLE_METHOD);
        assertThatAstIs(createAst(38), LINE68_VAR + WHOLE_METHOD);
        assertThatAstIs(createAst(61), LINE91_CALL + WHOLE_METHOD);
        assertThatAstIs(createAst(73), LINE103_RETURN + WHOLE_METHOD);
    }

    /**
     * Verifies the AST contains the elements of the whole method.
     */
    @Test
    void shouldHandleBlankLines() {
        assertThatAstIs(createAst(36), WHOLE_METHOD);
        assertThatAstIs(createAst(42), WHOLE_METHOD);
        assertThatAstIs(createAst(44), WHOLE_METHOD);
        assertThatAstIs(createAst(72), WHOLE_METHOD);

        assertThatAstIs(createAst(17), WHOLE_CLASS);
    }

    /**
     * Verifies the AST contains the elements of the whole class and the affected line.
     */
    @Test
    void shouldPickWholeClass() {
        assertThatAstIs(createAst(14), LINE14_CLASS + WHOLE_CLASS);
        assertThatAstIs(createAst(16), LINE16_FIELD + WHOLE_CLASS);
    }
}
