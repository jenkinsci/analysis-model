package edu.hm.hafner.analysis.ast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.FileReaderFactory;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.parser.checkstyle.CheckStyleParser;
import edu.hm.hafner.util.ResourceTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Test cases for the new warnings detector.
 *
 * @author Ullrich Hafner
 * @author Christian Möstl
 */
@SuppressWarnings("PMD.ExcessivePublicCount")
class CheckStyleAstFactoryTest extends ResourceTest {
    private static final String PACKAGE_DECLARATION = "PackageDeclaration";
    private static final String REDUNDANT_MODIFIER = "RedundantModifier";
    private static final String FINAL_CLASS = "FinalClass";
    private static final String INTERFACE_IS_TYPE = "InterfaceIsType";
    private static final String EXPLICIT_INITIALIZATION = "ExplicitInitialization";
    private static final String PACKAGE_NAME = "PackageName";
    private static final String NEED_BRACES = "NeedBraces";
    private static final String METHOD_NAME = "MethodName";

    private static final String METHOD_AST_FOLDERNAME = "MethodAst";
    private static final String ENVIRONMENT_AST_FOLDERNAME = "EnvironmentAst";
    private static final String FILE_AST_FOLDERNAME = "FileAst";
    private static final String CLASS_AST_FOLDERNAME = "ClassAst";
    private static final String METHOD_OR_CLASS_AST_FOLDERNAME = "MethodOrClassAst";
    private static final String INSTANCEVARIABLE_AST_FOLDERNAME = "InstancevariableAst";
    private static final String NAME_PACKAGE_AST_FOLDERNAME = "NamePackageAst";

    // Compare Refactorings from Martin Fowler (http://refactoring.com/catalog/)
    private static final String REFACTORING_NEWLINE = "_Newline";
    private static final String REFACTORING_RENAME = "_Rename";
    private static final String REFACTORING_EXTRACT_METHOD = "_ExtractMethod";
    private static final String REFACTORING_INLINE_METHOD = "_InlineMethod";
    private static final String REFACTORING_PULL_UP_METHOD = "_PullUpMethod";
    private static final String REFACTORING_PUSH_DOWN_METHOD = "_PushDownMethod";
    private static final String REFACTORING_EXTRACT_CONSTANT = "_ExtractConstant";

    /**
     * FIXME: In the end this should report no new warnings...
     */
    @Test
    @Disabled("Identifies the problematic ast creation...")
    void shouldIdentifyTabWarnings() {
        String file = "Cobertura";
        String affectedClass = "MavenCoberturaPublisher";

        Set<Issue> affectedPrevious = parse("before/", file, affectedClass);
        assertEquals("Wrong Number of annotations found: ", 329, affectedPrevious.size());

        Set<Issue> affectedCurrent = parse("after/", file, affectedClass);
        assertEquals("Wrong Number of annotations found: ", 331, affectedCurrent.size());

        affectedCurrent.removeAll(affectedPrevious);
        assertEquals("Wrong Number of annotations found: ", 123, affectedCurrent.size());

        Set<String> hashes = new HashSet<String>();
        HashMap<String, Issue> map = new HashMap<String, Issue>();
        for (Issue warning : affectedCurrent) {
            Ast ast = getAst(file + ".java", warning, "", false);
            String key = ast.getDigest() + warning.getType();
            if (!hashes.add(key)) {
                System.out.println("-----------------------");
                System.out.println("Old: " + map.get(key));
                System.out.println("-----------------------");
                System.out.println("Now: " + warning);
                System.out.println("-----------------------");
                String digest = ast.getDigest();
                System.out.println(digest);

                ast = getAst(file + ".java", map.get(key), "", false);
                digest = ast.getDigest();
                System.out.println(digest);
            }
            map.put(key, warning);
        }
        assertEquals("Wrong Number of hashes found: ", 123, hashes.size());
    }

    private Set<Issue> parse(final String folder, final String file, final String affectedClass) {
        Set<Issue> affected = new HashSet<Issue>();
        for (Issue warning : parse(folder + file + ".xml")) {
            if (warning.getFileName().contains(affectedClass)) {
                affected.add(warning);
            }
        }
        return affected;
    }

    /**
     * Verifies that the insertion of a new line above the warning does produce a different hashCode.
     */
    @Test
    void testInsertLineAboveWarning() {
        Issue beforeWarning = readWarning("before/" + METHOD_AST_FOLDERNAME + "/InsertLine.xml");
        Issue afterWarning = readWarning("after/" + METHOD_AST_FOLDERNAME + "/InsertLine.xml");

        verifyWarning(beforeWarning, "Javadoc", 7, "InsertLine.java");
        verifyWarning(afterWarning, "Javadoc", 8, "InsertLine.java");

    }

    private void assertNotEquals(final String s, final int beforeCode, final int afterCode) {
        assertThat(beforeCode).as(s).isNotEqualTo(afterCode);
    }

    /**
     * Verifies that the insertion of a new line before the package declaration does not change the hash code.
     */
    @Test
    void testInsertLineBeforePackage() {
        Issue beforeWarning = readWarning("before/" + METHOD_AST_FOLDERNAME + "/InsertLine.xml");
        Issue afterWarning = readWarning("after/" + METHOD_AST_FOLDERNAME + "/InsertLine2.xml");

        verifyWarning(beforeWarning, "Javadoc", 7, "InsertLine.java");
        verifyWarning(afterWarning, "Javadoc", 8, "InsertLine.java");

    }

    private String getTempFileName(final String fileName) {
        File warnings = createCopyInTemp(fileName);
        return warnings.getAbsolutePath();
    }

    private File createCopyInTemp(final String fileName) {
        try {
            File warnings = File.createTempFile("warnings", ".java");
            warnings.deleteOnExit();

            FileUtils.copyInputStreamToFile(asInputStream(fileName), warnings);
            return warnings;
        }
        catch (IOException cause) {
            throw new IllegalArgumentException(cause);
        }
    }

    private void verifyWarning(final Issue before, final String category, final int line, final String fileName) {
        assertEquals("Wrong category", category, before.getCategory());
        assertEquals("Wrong line", line, before.getLineStart());
        assertEquals("Wrong line", fileName, FilenameUtils.getName(before.getFileName()));
    }

    @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
    private Issue readWarning(final String fileName) {
        Report parse = parse(fileName);
        return parse.get(0);
    }

    /**
     * Verifies that the ast calculates the same hashcode. (inclusive Refactoring: Newline).
     */
    @Test
    void testFinalClassWithNewLines() {
        checkThatHashesMatching(FINAL_CLASS, REFACTORING_NEWLINE);
    }

    /**
     * Verifies that the ast calculates the same hashcode.
     */
    @Test
    void testNeedBracesWithNewLines() {
        checkThatHashesMatching(NEED_BRACES, REFACTORING_NEWLINE);
    }

    /**
     * Verifies that the ast calculates the same hashcode.
     */
    @Test
    void testInterfaceIsTypeWithNewLines() {
        checkThatHashesMatching(INTERFACE_IS_TYPE, REFACTORING_NEWLINE);
    }

    /**
     * Verifies that the ast calculates the same hashcode.
     */
    @Test
    void testExplicitInitializationWithNewLines() {
        checkThatHashesMatching(EXPLICIT_INITIALIZATION, REFACTORING_NEWLINE);
    }

    /**
     * Verifies that the ast calculates the same hashcode.
     */
    @Test
    void testMethodNameWithNewLines() {
        checkThatHashesMatching(METHOD_NAME, REFACTORING_NEWLINE);
    }

    /**
     * Verifies that the ast calculates the same hashcode.
     */
    @Test
    void testRedundantModifierWithNewLines() {
        checkThatHashesMatching(REDUNDANT_MODIFIER, REFACTORING_NEWLINE);
    }

    /**
     * Verifies that the ast calculates the same hashcode.
     */
    @Test
    void testPackageNameWithNewLines() {
        checkThatHashesMatching(PACKAGE_NAME, REFACTORING_NEWLINE);
    }

    /**
     * Verifies that the ast calculates the same hashcode.
     */
    @Test
    void testFinalClassWithRename() {
        checkThatHashesMatching(FINAL_CLASS, REFACTORING_RENAME);
    }

    /**
     * Verifies that the ast calculates the same hashcode.
     */
    @Test
    void testNeedBracesWithRename() {
        checkThatHashesMatching(NEED_BRACES, REFACTORING_RENAME);
    }

    /**
     * Verifies that the ast calculates the same hashcode.
     */
    @Test
    void testInterfaceIsTypeWithRename() {
        checkThatHashesMatching(INTERFACE_IS_TYPE, REFACTORING_RENAME);
    }

    /**
     * Verifies that the ast calculates the same hashcode.
     */
    @Test
    void testExplicitInitializationWithRename() {
        checkThatHashesMatching(EXPLICIT_INITIALIZATION, REFACTORING_RENAME);
    }

    /**
     * Verifies that the ast calculates the same hashcode.
     */
    @Test
    void testMethodNameWithRename() {
        checkThatHashesMatching(METHOD_NAME, REFACTORING_RENAME);
    }

    /**
     * Verifies that the ast calculates NOT the same hashcode.
     */
    @Test
    void testPackageNameWithRenameHaveNotTheSameHashcode() {
        checkThatHashesNotMatching(PACKAGE_NAME, REFACTORING_RENAME);
    }

    /**
     * Verifies that the ast calculates the same hashcode.
     */
    @Test
    void testNeedBracesWithExtractMethod() {
        checkThatHashesMatching(NEED_BRACES, "NeedBraces1", REFACTORING_EXTRACT_METHOD, true);
    }

    /**
     * Verifies that the ast calculates NOT the same hashcode.
     */
    @Test
    void testNeedBracesWithExtractMethodHaveNotTheSameHashcode() {
        checkThatHashesMatching(NEED_BRACES, "NeedBraces2", REFACTORING_EXTRACT_METHOD, false);
    }

    /**
     * Verifies that the ast calculates NOT the same hashcode.
     */
    @Test
    void testFinalClassWithExtractMethodHaveNotTheSameHashcode() {
        checkThatHashesMatching(FINAL_CLASS, "FinalClass1", REFACTORING_EXTRACT_METHOD, false);
    }

    /**
     * Verifies that the ast calculates NOT the same hashcode.
     */
    @Test
    void testPackageDeclarationWithExtractMethodHaveNotTheSameHashcode() {
        checkThatHashesMatching(PACKAGE_DECLARATION, "PackageDeclaration1", REFACTORING_EXTRACT_METHOD, false);
    }

    /**
     * Verifies that the ast calculates the same hashcode.
     */
    @Test
    void testExplicitInitializationWithExtractMethod() {
        checkThatHashesMatching(EXPLICIT_INITIALIZATION, "ExplicitInitialization2", REFACTORING_EXTRACT_METHOD, true);
    }

    /**
     * Verifies that the ast calculates the same hashcode.
     */
    @Test
    void testMethodNameWithExtractMethod() {
        checkThatHashesMatching(METHOD_NAME, "MethodName2", REFACTORING_EXTRACT_METHOD, true);
    }

    /**
     * Verifies that the ast calculates NOT the same hashcode.
     */
    @Test
    void testMethodNameWithExtractMethodHaveNotTheSameHashcode() {
        checkThatHashesMatching(METHOD_NAME, "MethodName2", "MethodName3", REFACTORING_EXTRACT_METHOD, false);
    }

    /**
     * Verifies that the ast calculates the same hashcode.
     */
    @Test
    void testPackageNameWithExtractMethod() {
        checkThatHashesMatching(PACKAGE_NAME, "PackageName", "PackageName2", REFACTORING_EXTRACT_METHOD, true);
    }

    /**
     * Verifies that the ast calculates the same hashcode.
     */
    @Test
    void testMethodNameWithPullUpMethod() {
        checkThatHashesMatching(METHOD_NAME, "MethodName1SubclassA", "MethodName1Superclass",
                REFACTORING_PULL_UP_METHOD, true);
    }

    /**
     * Verifies that the ast calculates the same hashcode.
     */
    @Test
    void testNeedBracesWithPullUpMethod() {
        checkThatHashesMatching(NEED_BRACES, "NeedBraces3SubclassA", "NeedBraces3Superclass",
                REFACTORING_PULL_UP_METHOD, true);
    }

    /**
     * Verifies that the ast calculates NOT the same hashcode.
     */
    @Test
    void testNeedBracesWithPullUpMethodHaveNotTheSameHashcode() {
        checkThatHashesMatching(NEED_BRACES, "NeedBraces4SubclassA", "NeedBraces4Superclass",
                REFACTORING_PULL_UP_METHOD, false);
    }

    /**
     * Verifies that the ast calculates NOT the same hashcode.
     */
    @Test
    void testFinalClassWithPullUpMethodHaveNotTheSameHashcode() {
        checkThatHashesMatching(FINAL_CLASS, "FinalClass2SubclassA", REFACTORING_PULL_UP_METHOD, false);
    }

    /**
     * Verifies that the ast calculates NOT the same hashcode.
     */
    @Test
    void testPackageDeclarationWithPullUpMethodHaveNotTheSameHashcode() {
        checkThatHashesMatching(PACKAGE_DECLARATION, "PackageDeclarationSubclass", REFACTORING_PULL_UP_METHOD, false);
    }

    /**
     * Verifies that the ast calculates the same hashcode.
     */
    @Test
    void testExplicitInitializationWithPullUpMethod() {
        checkThatHashesMatching(EXPLICIT_INITIALIZATION, "ExplicitInitialization1Subclass", REFACTORING_PULL_UP_METHOD,
                true);
    }

    /**
     * Verifies that the ast calculates the same hashcode.
     */
    @Test
    void testPackageNameWithPullUpMethod() {
        checkThatHashesMatching(PACKAGE_NAME, "PackageName1Subclass", REFACTORING_PULL_UP_METHOD, true);
    }

    /**
     * Verifies that the ast calculates the same hashcode.
     */
    @Test
    void testMethodNameWithPushDownMethod() {
        checkThatHashesMatching(METHOD_NAME, "MethodName4Superclass", "MethodName4Subclass",
                REFACTORING_PUSH_DOWN_METHOD, true);
    }

    /**
     * Verifies that the ast calculates the same hashcode.
     */
    @Test
    void testNeedBracesWithPushDownMethod() {
        checkThatHashesMatching(NEED_BRACES, "NeedBraces5Superclass", "NeedBraces5Subclass",
                REFACTORING_PUSH_DOWN_METHOD, true);
    }

    /**
     * Verifies that the ast calculates NOT the same hashcode.
     */
    @Test
    void testNeedBracesWithPushDownMethodHaveNotTheSameHashcode() {
        checkThatHashesMatching(NEED_BRACES, "NeedBraces6Superclass", "NeedBraces6Subclass",
                REFACTORING_PUSH_DOWN_METHOD, false);
    }

    /**
     * Verifies that the ast calculates NOT the same hashcode.
     */
    @Test
    void testFinalClassWithPushDownMethodHaveNotTheSameHashcode() {
        checkThatHashesMatching(FINAL_CLASS, "FinalClass3Subclass", REFACTORING_PUSH_DOWN_METHOD, false);
    }

    /**
     * Verifies that the ast calculates NOT the same hashcode.
     */
    @Test
    void testPackageDeclarationWithPushDownMethodHaveNotTheSameHashcode() {
        checkThatHashesMatching(PACKAGE_DECLARATION, "PackageDeclaration2Subclass", REFACTORING_PUSH_DOWN_METHOD,
                false);
    }

    /**
     * Verifies that the ast calculates the same hashcode.
     */
    @Test
    void testExplicitInitializationWithPushDownMethod() {
        checkThatHashesMatching(EXPLICIT_INITIALIZATION, "ExplicitInitialization3Subclass",
                REFACTORING_PUSH_DOWN_METHOD, true);
    }

    /**
     * Verifies that the ast calculates the same hashcode.
     */
    @Test
    void testPackageNameWithPushDownMethod() {
        checkThatHashesMatching(PACKAGE_NAME, "PackageName3Superclass", REFACTORING_PUSH_DOWN_METHOD, true);
    }

    /**
     * Verifies that the ast calculates NOT the same hashcode.
     */
    @Test
    void testFinalClassWithInlineMethodHaveNotTheSameHashcode() {
        checkThatHashesMatching(FINAL_CLASS, "FinalClass4", REFACTORING_INLINE_METHOD, false);
    }

    /**
     * Verifies that the ast calculates the same hashcode.
     */
    @Test
    void testNeedBracesWithInlineMethod() {
        checkThatHashesMatching(NEED_BRACES, "NeedBraces7", REFACTORING_INLINE_METHOD, true);
    }

    /**
     * Verifies that the ast calculates NOT the same hashcode.
     */
    @Test
    void testNeedBracesWithInlineMethodHaveNotTheSameHashcode() {
        checkThatHashesMatching(NEED_BRACES, "NeedBraces8", REFACTORING_INLINE_METHOD, false);
    }

    /**
     * Verifies that the ast calculates NOT the same hashcode.
     */
    @Test
    void testPackageDeclarationWithInlineMethod() {
        checkThatHashesMatching(PACKAGE_DECLARATION, "PackageDeclaration3", REFACTORING_INLINE_METHOD, false);
    }

    /**
     * Verifies that the ast calculates the same hashcode.
     */
    @Test
    void testExplicitInitializationWithInlineMethod() {
        checkThatHashesMatching(EXPLICIT_INITIALIZATION, "ExplicitInitialization4", REFACTORING_INLINE_METHOD, true);
    }

    /**
     * Verifies that the ast calculates the same hashcode.
     */
    @Test
    void testMethodNameWithInlineMethod() {
        checkThatHashesMatching(METHOD_NAME, "MethodName5", REFACTORING_INLINE_METHOD, true);
    }

    /**
     * Verifies that the ast calculates NOT the same hashcode.
     */
    @Test
    void testMethodNameWithInlineMethodHaveNotTheSameHashcode() {
        checkThatHashesMatching(METHOD_NAME, "MethodName6", REFACTORING_INLINE_METHOD, false);
    }

    /**
     * Verifies that the ast calculates the same hashcode.
     */
    @Test
    void testPackageNameWithInlineMethod() {
        checkThatHashesMatching(PACKAGE_NAME, "PackageName4", REFACTORING_INLINE_METHOD, true);
    }

    /**
     * Verifies that the ast calculates the same hashcode.
     */
    @Test
    void testMethodNameWithExtractConstantAsAVariable() {
        checkThatHashesMatching(METHOD_NAME, "MethodName7", REFACTORING_EXTRACT_CONSTANT, true);
    }

    /**
     * Verifies that the ast calculates the same hashcode.
     */
    @Test
    void testMethodNameWithExtractConstantInSysout() {
        checkThatHashesMatching(METHOD_NAME, "MethodName8", REFACTORING_EXTRACT_CONSTANT, true);
    }

    /**
     * Verifies that the ast calculates the same hashcode.
     */
    @Test
    void testPackageNameWithExtractConstant() {
        checkThatHashesMatching(PACKAGE_NAME, "PackageName5", REFACTORING_EXTRACT_CONSTANT, true);
    }

    /**
     * Verifies that the ast calculates NOT the same hashcode.
     */
    @Test
    void testFinalClassWithExtractConstantHaveNotTheSameHashcode() {
        checkThatHashesMatching(FINAL_CLASS, "FinalClass5", REFACTORING_EXTRACT_CONSTANT, false);
    }

    /**
     * Verifies that the ast calculates the same hashcode.
     */
    @Test
    void testNeedBracesWithExtractConstant() {
        checkThatHashesMatching(NEED_BRACES, "NeedBraces9", REFACTORING_EXTRACT_CONSTANT, true);
    }

    /**
     * Verifies that the ast calculates NOT the same hashcode.
     */
    @Test
    void testNeedBracesWithExtractConstantHaveNotTheSameHashcode() {
        checkThatHashesMatching(NEED_BRACES, "NeedBraces10", REFACTORING_EXTRACT_CONSTANT, false);
    }

    /**
     * Verifies that the ast calculates NOT the same hashcode.
     */
    @Test
    void testPackageDeclarationWithExtractConstantHaveNotTheSameHashcode() {
        checkThatHashesMatching(PACKAGE_DECLARATION, "PackageDeclaration4", REFACTORING_EXTRACT_CONSTANT, false);
    }

    /**
     * Verifies that the ast calculates the same hashcode.
     */
    @Test
    void testExplicitInitializationWithExtractConstant() {
        checkThatHashesMatching(EXPLICIT_INITIALIZATION, "ExplicitInitialization5", REFACTORING_EXTRACT_CONSTANT, true);
    }

    /**
     * Shows that a previous file has the same warnings like the current file, if refactorings were realised. It means
     * that the same hashcode is calculated. (The warnings wasn't changed!)
     */
    @Test
    void testFileWithManyWarningsHasSameHashcode() {
        evaluateHashes("MoreWarningsInClass", "MoreWarningsInClass_Refactored", 5, 5, 5);
    }

    /**
     * Shows that a previous file has more warnings than the current file, if warnings were fixed. Other new warnings
     * were added.
     */
    @Test
    void testPreviousHasMoreWarningsThanCurrent() {
        evaluateHashes("MoreWarningsInClass2", "MoreWarningsInClass2_Refactored", 5, 4, 3);
    }

    /**
     * Shows that a previous file has less warnings than the current file, if the current files have additional
     * warnings. The difference in set theory is (current-warnings minus previous warnings), which are the new
     * warnings.
     */
    @Test
    void testCurrentHasMoreWarningsThanPrevious() {
        evaluateHashes("MoreWarningsInClass", "MoreWarningsInClass1_Refactored", 5, 7, 5);
    }

    private void evaluateHashes(final String fileBefore, final String fileAfter, final int expectedPrev,
            final int expectedCur, final int intersection) {
        Set<String> hashSetPevious = calculateHashes(fileBefore, true);
        Set<String> hashSetCurrent = calculateHashes(fileAfter, false);

        HashSet<String> i = new HashSet<>(hashSetCurrent);
        i.retainAll(hashSetPevious);

        assertEquals("Not expected count of previous warnings", expectedPrev, hashSetPevious.size());
        assertEquals("Not expected count of current warnings", expectedCur, hashSetCurrent.size());
        assertEquals("The warnings aren't equal.", intersection, i.size());
    }

    private Set<String> calculateHashes(final String file, final boolean before) {
        Report annotations;
        String fileWithXmlExtension = file + ".xml";
        String fileWithJavaExtension = file + ".java";

        if (before) {
            annotations = parse("before/" + fileWithXmlExtension);
        }
        else {
            annotations = parse("after/" + fileWithXmlExtension);
        }

        Set<String> hashSet = new HashSet<String>();

        Ast ast;
        String hash;
        for (int i = 0; i < annotations.size(); i++) {
            ast = getAst(fileWithJavaExtension, annotations.get(i), "", before);
            hash = ast.getDigest();
            hashSet.add(hash);
        }

        return hashSet;
    }

    private Report parse(final String fileName) {
        CheckStyleParser parser = new CheckStyleParser();

        return parser.parse(new FileReaderFactory(getResourceAsFile(fileName), StandardCharsets.UTF_8));
    }

    private String matchWarningTypeToFoldername(final String warningType) {
        String ordnerName = "";
        if (Arrays.asList(CheckStyleAstFactory.getClassAst()).contains(warningType)) {
            ordnerName = CLASS_AST_FOLDERNAME;
        }
        else if (Arrays.asList(CheckStyleAstFactory.getSurroundingElementsAst()).contains(warningType)) {
            ordnerName = ENVIRONMENT_AST_FOLDERNAME;
        }
        else if (Arrays.asList(CheckStyleAstFactory.getFileAst()).contains(warningType)) {
            ordnerName = FILE_AST_FOLDERNAME;
        }
        else if (Arrays.asList(CheckStyleAstFactory.getFieldsAst()).contains(warningType)) {
            ordnerName = INSTANCEVARIABLE_AST_FOLDERNAME;
        }
        else if (Arrays.asList(CheckStyleAstFactory.getMethodAst()).contains(warningType)) {
            ordnerName = METHOD_AST_FOLDERNAME;
        }
        else if (Arrays.asList(CheckStyleAstFactory.getMethodOrClassAst()).contains(warningType)) {
            ordnerName = METHOD_OR_CLASS_AST_FOLDERNAME;
        }
        else if (Arrays.asList(CheckStyleAstFactory.getNamePackageAst()).contains(warningType)) {
            ordnerName = NAME_PACKAGE_AST_FOLDERNAME;
        }

        return ordnerName;
    }

    /**
     * Use this method for calculating the hashcode, if the filename (inclusive file extension) before is equal after,
     * except that the after-filename has only(!) a postfix of the refactoring.
     *
     * @param warningType
     *         the warningType
     * @param refactoring
     *         the refactoring
     */
    private void checkThatHashesMatching(final String warningType, final String refactoring) {
        checkThatHashesMatching(warningType, warningType, warningType, refactoring, true);
    }

    private void checkThatHashesNotMatching(final String warningType, final String refactoring) {
        checkThatHashesMatching(warningType, warningType, warningType, refactoring, false);
    }

    /**
     * Use this method for calculating the hashcode, if the filename before is different as after, disregarded that the
     * after-filename has a postfix of the refactoring.
     *
     * @param warningType
     *         the warningType
     * @param beforeClass
     *         the name of the class before
     * @param afterClass
     *         the name of the class after
     * @param refactoring
     *         the refactoring
     * @param expectedEqualHashcode
     *         <code>true</code>, if the expected hashcode is equal, otherwise <code>false</code>.
     */
    private void checkThatHashesMatching(final String warningType, final String beforeClass, final String afterClass,
            final String refactoring, final boolean expectedEqualHashcode) {
        String foldername = matchWarningTypeToFoldername(warningType);
        long hashBefore = calcHashcode(beforeClass, foldername, true);
        long hashAfter = calcHashcode(afterClass + refactoring, foldername, false);

        if (expectedEqualHashcode) {
            compareHashcode(hashBefore, hashAfter);
        }
        else {
            compareHashcodeOnNonEquality(hashBefore, hashAfter);
        }
    }

    private void checkThatHashesMatching(final String warningType, final String fileName, final String refactoring,
            final boolean expectedEqualHashcode) {
        checkThatHashesMatching(warningType, fileName, fileName, refactoring, expectedEqualHashcode);
    }

    private long calcHashcode(final String filename, final String foldername, final boolean beforeRefactoring) {
        String javaFile = filename.concat(".java");
        String xmlFile = filename.concat(".xml");

        return calcHashcode(javaFile, foldername, xmlFile, beforeRefactoring);
    }

    private void compareHashcode(final long hashBefore, final long hashAfter) {
        assertEquals("Hash codes don't match: ", hashBefore, hashAfter);
    }

    private void assertEquals(final String message, final long hashBefore, final long hashAfter) {
        assertThat(hashBefore).as(message).isEqualTo(hashAfter);
    }

    private void assertEquals(final String message, final String hashBefore, final String hashAfter) {
        assertThat(hashBefore).as(message).isEqualTo(hashAfter);
    }

    private void compareHashcodeOnNonEquality(final long hashBefore, final long hashAfter) {
        getAssertNotEquals(hashBefore, hashAfter);
    }

    private void getAssertNotEquals(final long hashBefore, final long hashAfter) {
        assertThat(hashBefore).as("Hash codes aren't different: ").isNotEqualTo(hashAfter);
    }

    private long calcHashcode(final String javaFile, final String foldername, final String xmlFile,
            final boolean before) {
        Ast ast = getAst(javaFile, xmlFile, foldername, before);
        return ast.getContextHashCode();
    }

    private Ast getAst(final String javaFile, final String xmlFile, final String foldername, final boolean before) {
        Issue warning = readWarning(calcCorrectPath(xmlFile, foldername, before));
        return CheckStyleAstFactory.getInstance(getTempFileName(calcCorrectPath(javaFile, foldername, before)),
                warning.getType(), warning.getLineStart());
    }

    private Ast getAst(final String javaFile, final Issue warning, final String foldername,
            final boolean before) {
        return CheckStyleAstFactory.getInstance(getTempFileName(calcCorrectPath(javaFile, foldername, before)),
                warning.getType(), warning.getLineStart());
    }

    private String calcCorrectPath(final String nameOfFile, final String foldername, final boolean before) {
        StringBuilder stringBuilder = new StringBuilder();
        if (before) {
            stringBuilder.append("before/");
        }
        else {
            stringBuilder.append("after/");
        }
        stringBuilder.append(foldername);
        stringBuilder.append('/');
        stringBuilder.append(nameOfFile);

        return stringBuilder.toString();
    }
}
