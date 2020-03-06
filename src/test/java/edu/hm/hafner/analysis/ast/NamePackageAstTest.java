package edu.hm.hafner.analysis.ast;

import org.junit.jupiter.api.Test;

/**
 * Tests the class {@link NamePackageAst}.
 *
 * @author Christian Möstl
 * @author Ullrich Hafner
 */
class NamePackageAstTest extends AbstractAstTest {
    @Override
    protected Ast createAst(final String fileName, final int lineNumber) {
        return new NamePackageAst(fileName, lineNumber);
    }

    /**
     * Verifies that the NamePackageAst works right.
     */
    @Test
    void testNamePackageAst() {
        String expectedResult = "PACKAGE_DEF ANNOTATIONS DOT DOT IDENT IDENT IDENT SEMI ";

        Ast ast = new NamePackageAst(createJavaSourceTemporaryFile("PackageName_Newline.java"), 7);

        assertThatAstIs(ast, expectedResult);
    }

    @Test
    void shouldFindWholeFileRegardlessOfLine() {
        verifyAstAtLine(16, LINE1_PACKAGE);
    }

}
