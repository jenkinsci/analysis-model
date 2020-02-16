package edu.hm.hafner.analysis.ast;

import org.junit.jupiter.api.Test;

/**
 * Tests the class {@link NamePackageAst}.
 *
 * @author Christian Möstl
 * @author Ullrich Hafner
 */
// FIXME: update test using the base class methods and resources
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
}
