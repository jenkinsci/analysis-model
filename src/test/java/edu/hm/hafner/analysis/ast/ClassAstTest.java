package edu.hm.hafner.analysis.ast;

import org.junit.jupiter.api.Test;

/**
 * Tests the class {@link ClassAst}.
 *
 * @author Christian Möstl
 * @author Ullrich Hafner
 */
class ClassAstTest extends AbstractAstTest {
    @Override
    protected Ast createAst(final String fileName, final int lineNumber) {
        return new ClassAst(fileName, lineNumber);
    }

    /**
     * Verifies the AST contains the elements of the nested class only.
     */
    @Test
    void shouldPickNestedClass() {
        Ast ast = createAstFromClassWithNestedClass(2);
        assertThatAstIs(ast, "CLASS_DEF MODIFIERS LITERAL_PRIVATE LITERAL_STATIC LITERAL_CLASS IDENT OBJBLOCK LCURLY "
                + "CLASS_DEF MODIFIERS LITERAL_PRIVATE LITERAL_STATIC LITERAL_CLASS IDENT OBJBLOCK LCURLY "
                + "METHOD_DEF MODIFIERS LITERAL_PRIVATE TYPE LITERAL_VOID IDENT LPAREN PARAMETERS "
                + "RPAREN SLIST EXPR METHOD_CALL DOT DOT IDENT IDENT IDENT ELIST EXPR STRING_LITERAL RPAREN SEMI RCURLY "
                + "RCURLY ");
    }

    /**
     * Verifies the AST does not contain the elements of the nested class.
     */
    @Test
    void shouldNotContainNestedClass() {
        Ast ast = createAstFromClassWithNestedClass(1);
        assertThatAstIs(ast, "CLASS_DEF MODIFIERS LITERAL_PUBLIC LITERAL_CLASS IDENT OBJBLOCK LCURLY "
                + "CLASS_DEF MODIFIERS LITERAL_PUBLIC LITERAL_CLASS IDENT OBJBLOCK LCURLY RCURLY ");
    }

    private Ast createAstFromClassWithNestedClass(final int lineNumber) {
        String fileName = createJavaSourceTemporaryFile("class.ast-test");
        return createAst(fileName, lineNumber);
    }

    /**
     * Verifies the AST contains the elements of the whole class and the affected line.
     */
    @Test
    void shouldPickWholeClass() {
        assertThatAstIs(createAst(37), LINE67_METHOD + WHOLE_CLASS);
        assertThatAstIs(createAst(52), LINE82_FOR + WHOLE_CLASS);
    }

    /**
     * Verifies the AST contains the elements of the whole class.
     */
    @Test
    void shouldHandleBlankLines() {
        assertThatAstIs(createAst(19), WHOLE_CLASS);
        assertThatAstIs(createAst(42), WHOLE_CLASS);
        assertThatAstIs(createAst(44), WHOLE_CLASS);
        assertThatAstIs(createAst(72), WHOLE_CLASS);
        assertThatAstIs(createAst(73), WHOLE_CLASS);
        assertThatAstIs(createAst(74), WHOLE_CLASS);
    }

    /**
     * Verifies the AST contains the elements of the whole class.
     */
    @Test
    void shouldHandleJavaDocLines() {
        assertThatAstIs(createAst(20), WHOLE_CLASS);
        assertThatAstIs(createAst(21), WHOLE_CLASS);
    }

    @Test
    void shouldHandleNestedClass() {
        assertThatAstIs(createAst(76), NESTED);
        assertThatAstIs(createAst(77), NESTED);
        assertThatAstIs(createAst(78), NESTED);
        assertThatAstIs(createAst(79), NESTED);
        assertThatAstIs(createAst(80), NESTED);
    }
}
