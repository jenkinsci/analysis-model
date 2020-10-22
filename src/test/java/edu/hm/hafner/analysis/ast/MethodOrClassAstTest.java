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
        verifyAstAtLine(37, WHOLE_METHOD);
        verifyAstAtLine(38, WHOLE_METHOD);
        verifyAstAtLine(61, WHOLE_METHOD);
        verifyAstAtLine(73, WHOLE_METHOD);
    }

    /**
     * Verifies the AST contains the elements of the whole method.
     */
    @Test
    void shouldHandleBlankLines() {
        verifyAstAtLine(36, WHOLE_METHOD);
        verifyAstAtLine(42, WHOLE_METHOD);
        verifyAstAtLine(44, WHOLE_METHOD);
        verifyAstAtLine(72, WHOLE_METHOD);

        verifyAstAtLine(17, WHOLE_CLASS);
    }

    /**
     * Verifies the AST contains the elements of the whole class and the affected line.
     */
    @Test
    void shouldPickWholeClass() {
        verifyAstAtLine(14, WHOLE_CLASS);
        verifyAstAtLine(16, WHOLE_CLASS);
    }
}
