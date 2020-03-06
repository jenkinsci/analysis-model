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
    Ast createAst(final String fileName, final int lineNumber) {
        return new ClassAst(fileName, lineNumber);
    }

    /**
     * Verifies the AST contains the elements of the nested class only.
     */
    @Test
    void shouldPickNestedClass() {
        Ast ast = createAstFromClassWithNestedClass(2);
        assertThatAstIs(ast, "CLASS_DEF MODIFIERS LITERAL_PRIVATE LITERAL_STATIC LITERAL_CLASS IDENT OBJBLOCK LCURLY "
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
        assertThatAstIs(ast, "CLASS_DEF MODIFIERS LITERAL_PUBLIC LITERAL_CLASS IDENT OBJBLOCK LCURLY RCURLY ");
    }

    private Ast createAstFromClassWithNestedClass(final int lineNumber) {
        return createAst(createJavaSourceTemporaryFile("class.ast-test"), lineNumber);
    }

    @Test
    void shouldPickWholeClass() {
        for (int line = 14; line < 74; line++) {
            verifyAstAtLine(37, WHOLE_CLASS);
        }
    }

    @Test
    void shouldHandleNestedClass() {
        verifyAstAtLine(76, NESTED);
        verifyAstAtLine(77, NESTED);
        verifyAstAtLine(78, NESTED);
        verifyAstAtLine(79, NESTED);
        verifyAstAtLine(80, NESTED);
    }
}
