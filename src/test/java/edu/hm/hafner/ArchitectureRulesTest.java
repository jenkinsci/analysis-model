package edu.hm.hafner;

import javax.xml.parsers.SAXParser;

import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.binder.DigesterLoader;
import org.junit.jupiter.api.Test;
import org.xml.sax.XMLReader;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

/**
 * Defines several architecture rules for the static analysis model and parsers.
 *
 * @author Ullrich Hafner
 */
class ArchitectureRulesTest {
    /**
     * Digester must not be used directly, rather use a SecureDigester instance.
     */
    @Test
    void shouldNotCreateDigesterWithConstructor() {
        JavaClasses classes = new ClassFileImporter().importPackages("edu.hm.hafner");

        ArchRule noDigesterConstructor = noClasses().that().dontHaveSimpleName("SecureDigester")
                .should().callConstructor(Digester.class)
                .orShould().callConstructor(Digester.class, SAXParser.class)
                .orShould().callConstructor(Digester.class, XMLReader.class)
                .orShould().callMethod(DigesterLoader.class, "newDigester");

        noDigesterConstructor.check(classes);
    }

    /**
     * Test classes should not be public (Junit 5).
     */
    @Test
    void shouldNotUsePublicInTestCases() {
        JavaClasses classes = new ClassFileImporter().importPackages("edu.hm.hafner");

        ArchRule noPublicClasses = noClasses()
                .that().dontHaveModifier(JavaModifier.ABSTRACT)
                .and().haveSimpleNameEndingWith("Test")
                .and().dontHaveSimpleName("IssueTest")
                .and().dontHaveSimpleName("EclipseParserTest") // base class for warnings-plugin
                .and().dontHaveSimpleName("Pep8ParserTest")    // base class for warnings-plugin
                .should().bePublic();

        noPublicClasses.check(classes);
    }
}
