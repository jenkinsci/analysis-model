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

    @Test
    void shouldPickNestedClass() {
        Ast ast = createAstFromClassWithNestedClass(2);
        assertThatAstIs(ast, "CLASS_DEF MODIFIERS LITERAL_PRIVATE LITERAL_STATIC LITERAL_CLASS IDENT OBJBLOCK LCURLY "
                + "METHOD_DEF MODIFIERS LITERAL_PRIVATE TYPE LITERAL_VOID IDENT LPAREN PARAMETERS "
                + "RPAREN SLIST EXPR METHOD_CALL DOT DOT IDENT IDENT IDENT ELIST EXPR STRING_LITERAL RPAREN SEMI RCURLY "
                + "RCURLY ");
    }

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
        for (int line = 7; line < 75; line++) {
            verifyAstAtLine(line, WHOLE_CLASS);
        }
        verifyAstAtLine(81, WHOLE_CLASS);
        verifyAstAtLine(82, WHOLE_CLASS);
    }

    @Test
    void shouldHandleNestedClass() {
        for (int line = 76; line < 80; line++) {
            verifyAstAtLine(line, NESTED);
        }
    }
}
