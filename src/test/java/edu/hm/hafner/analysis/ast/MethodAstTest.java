package edu.hm.hafner.analysis.ast;

import org.junit.jupiter.api.Test;

/**
 * Tests the class {@link MethodAst}.
 *
 * @author Christian Möstl
 * @author Ullrich Hafner
 */
class MethodAstTest extends AbstractAstTest {
    protected MethodAst createAst(final String fileName, final int lineNumber) {
        return new MethodAst(fileName, lineNumber);
    }

    @Test
    void shouldPickWholeMethod() {
        for (int line = 37; line < 74; line++) {
            assertThatAstIs(createAst(line), WHOLE_METHOD);
        }
    }

    @Test
    void shouldPickSimpleMethod() {
        for (int line = 29; line < 31; line++) {
            assertThatAstIs(createAst(line), LINE29_METHOD + LINE30_RETURN + LINE31_RCURLY);
        }
    }
}
