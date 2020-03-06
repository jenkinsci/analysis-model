package edu.hm.hafner.analysis.ast;

import org.junit.jupiter.api.Test;

/**
 * Tests the class {@link FileAst}.
 *
 * @author Christian Möstl
 * @author Ullrich Hafner
 */
class FileAstTest extends AbstractAstTest {
    @Override
    protected Ast createAst(final String fileName, final int lineNumber) {
        return new FileAst(fileName, lineNumber);
    }

    @Test
    void shouldFindWholeFile() {
        String expectedResult = "PACKAGE_DEF ANNOTATIONS DOT DOT IDENT IDENT IDENT SEMI IMPORT DOT DOT IDENT IDENT IDENT SEMI INTERFACE_DEF MODIFIERS LITERAL_PUBLIC LITERAL_INTERFACE IDENT OBJBLOCK LCURLY VARIABLE_DEF MODIFIERS TYPE LITERAL_INT IDENT ASSIGN EXPR NUM_INT SEMI VARIABLE_DEF MODIFIERS TYPE IDENT IDENT ASSIGN EXPR STRING_LITERAL SEMI VARIABLE_DEF MODIFIERS TYPE IDENT IDENT ASSIGN EXPR LITERAL_NEW IDENT LPAREN ELIST EXPR NUM_INT RPAREN SEMI RCURLY ";

        Ast ast = new FileAst(read("InterfaceIsType_Newline.java"), 15);

        assertThatAstIs(ast, expectedResult);
    }

    @Test
    void shouldFindWholeFileRegardlessOfLine() {
        verifyAstAtLine(16, WHOLE_FILE);

        verifyAstAtLine(77, WHOLE_FILE);
    }
}
