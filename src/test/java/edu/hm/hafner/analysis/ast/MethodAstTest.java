package edu.hm.hafner.analysis.ast;

import org.junit.jupiter.api.Test;

/**
 * Tests the class {@link MethodAst}.
 *
 * @author Christian Möstl
 * @author Ullrich Hafner
 */
class MethodAstTest extends AbstractAstTest {
    MethodAst createAst(final String fileName, final int lineNumber) {
        return new MethodAst(fileName, lineNumber);
    }

    @Test
    void shouldPickConstructor() {
        for (int line = 25; line < 27; line++) {
            verifyAstAtLine(line, LINE25_CTOR + LINE26_SUPER + LINE27_RCURLY);
        }
    }

    @Test
    void shouldPickWholeMethod() {
        for (int line = 29; line < 31; line++) {
            verifyAstAtLine(line, LINE29_METHOD + LINE30_RETURN + LINE31_RCURLY);
        }
        for (int line = 32; line < 35; line++) {
            verifyAstAtLine(line, LINE33_METHOD + LINE34_RETURN + LINE35_RCURLY);
        }
        for (int line = 37; line < 74; line++) {
            verifyAstAtLine(line, WHOLE_METHOD);
        }
    }

    @Test
    void shouldPickNestedMethod() {
        for (int line = 77; line < 79; line++) {
            verifyAstAtLine(line, LINE77_METHOD + LINE78_METHOD_CALL + LINE79_NESTED_METHOD_RCURLY);
        }
    }
}
