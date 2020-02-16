package edu.hm.hafner.analysis.ast;

import org.junit.jupiter.api.Test;

/**
 * Tests the class {@link SurroundingElementsAst}.
 *
 * @author Christian Möstl
 * @author Ullrich Hafner
 */
class SurroundingElementsAstTest extends AbstractAstTest {
    @Override
    protected Ast createAst(final String fileName, final int lineNumber) {
        return new SurroundingElementsAst(fileName, lineNumber, 3);
    }

    /**
     * Verifies the AST consists of the affected line as well
     * as of the 3 lines (that actually contain code) above and before.
     */
    @Test
    void shouldPickExactly3LinesBeforeAndAfter() {
        assertThatAstIs(createAst(40), LINE70_VAR + LINE69_VAR + LINE68_VAR + LINE67_METHOD + LINE71_VAR + LINE73_VAR + LINE75_VAR);
        assertThatAstIs(createAst(41), LINE71_VAR + LINE70_VAR + LINE69_VAR + LINE68_VAR + LINE73_VAR + LINE75_VAR + LINE76_IF);
        assertThatAstIs(createAst(43), LINE73_VAR + LINE71_VAR + LINE70_VAR + LINE69_VAR + LINE75_VAR + LINE76_IF + LINE77_ASSIGN);
        assertThatAstIs(createAst(45), LINE75_VAR + LINE73_VAR + LINE71_VAR + LINE70_VAR + LINE76_IF + LINE77_ASSIGN + LINE78_RCURLY);

        assertThatAstIs(createAst(70), LINE100_RCURLY + LINE99_BREAK + LINE98_ELSE + LINE97_RCURLY + LINE101_RCURLY + LINE103_RETURN + LINE104_RCURLY);
    }

    /**
     * Verifies the AST consists of the 3 lines (that actually contain code) above and before the
     * affected line. Since the affected line is blank, it is skipped in the AST.
     */
    @Test
    void shouldHandleBlankLines() {
        assertThatAstIs(createAst(42), LINE71_VAR + LINE70_VAR + LINE69_VAR + LINE73_VAR + LINE75_VAR + LINE76_IF);
        assertThatAstIs(createAst(44), LINE73_VAR + LINE71_VAR + LINE70_VAR + LINE75_VAR + LINE76_IF + LINE77_ASSIGN);
    }

    // TODO: define what to do at the beginning or end of a method
}
