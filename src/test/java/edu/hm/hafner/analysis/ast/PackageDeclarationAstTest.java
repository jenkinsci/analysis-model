package edu.hm.hafner.analysis.ast;

import org.junit.jupiter.api.Test;

/**
 * Tests the class {@link PackageDeclarationAst}.
 *
 * @author Christian Möstl
 * @author Ullrich Hafner
 */
class PackageDeclarationAstTest extends AbstractAstTest {
    @Override
    protected Ast createAst(final String fileName, final int lineNumber) {
        return new PackageDeclarationAst(fileName, lineNumber);
    }

    /**
     * Verifies that the NamePackageAst works right.
     */
    @Test
    void testNamePackageAst() {
        String expectedResult = "PACKAGE_DEF ANNOTATIONS DOT DOT IDENT IDENT IDENT SEMI ";

        Ast ast = new PackageDeclarationAst(read("PackageName_Newline.java"), 7);

        assertThatAstIs(ast, expectedResult);
    }

    @Test
    void shouldFindPackage() {
        for (int line = 1; line < 89; line++) {
            verifyAstAtLine(line, LINE1_PACKAGE);
        }
    }
}
