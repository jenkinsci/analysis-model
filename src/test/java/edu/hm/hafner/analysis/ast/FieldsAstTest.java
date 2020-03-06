package edu.hm.hafner.analysis.ast;

import org.junit.jupiter.api.Test;

/**
 * Tests the class {@link FieldsAst}.
 *
 * @author Christian Möstl
 * @author Ullrich Hafner
 */
class FieldsAstTest extends AbstractAstTest {
    @Override
    protected Ast createAst(final String fileName, final int lineNumber) {
        return new FieldsAst(fileName, lineNumber);
    }

    @Test
    void shouldIgnoreNewLinesAndFindThreeFields() {
        String expectedResult = "VARIABLE_DEF MODIFIERS LITERAL_PRIVATE TYPE IDENT IDENT SEMI "
                + "VARIABLE_DEF MODIFIERS LITERAL_PRIVATE TYPE LITERAL_INT IDENT ASSIGN EXPR NUM_INT SEMI "
                + "VARIABLE_DEF MODIFIERS LITERAL_PRIVATE FINAL TYPE IDENT IDENT SEMI ";

        Ast ast = new FieldsAst(createJavaSourceTemporaryFile("ExplicitInitialization_Newline.java"), 17);

        assertThatAstIs(ast, expectedResult);
    }

    @Test
    void shouldFindEverything() {
        assertThatAstIs(createAst(16), LINE18_FIELD);
    }
}
