package edu.hm.hafner;

import javax.xml.parsers.SAXParser;

import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.binder.DigesterLoader;
import org.xml.sax.XMLReader;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import edu.hm.hafner.util.ArchitectureRules;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

/**
 * Defines several architecture rules for the static analysis model and parsers.
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings("hideutilityclassconstructor")
@AnalyzeClasses(packages = "edu.hm.hafner..")
class ArchitectureTest {
    /** Digester must not be used directly, rather use a SecureDigester instance. */
    @ArchTest
    static final ArchRule NO_DIGESTER_CONSTRUCTOR_CALLED =
            noClasses().that().doNotHaveSimpleName("SecureDigester")
                    .should().callConstructor(Digester.class)
                    .orShould().callConstructor(Digester.class, SAXParser.class)
                    .orShould().callConstructor(Digester.class, XMLReader.class)
                    .orShould().callMethod(DigesterLoader.class, "newDigester");

    // @ArchTest
    // static final ArchRule NO_PUBLIC_TEST_CLASSES = ArchitectureRules.NO_PUBLIC_TEST_CLASSES;

    @ArchTest
    static final ArchRule NO_TEST_API_CALLED = ArchitectureRules.NO_TEST_API_CALLED;

    @ArchTest
    static final ArchRule NO_FORBIDDEN_PACKAGE_ACCESSED = ArchitectureRules.NO_FORBIDDEN_PACKAGE_ACCESSED;

    @ArchTest
    static final ArchRule NO_FORBIDDEN_CLASSES_CALLED = ArchitectureRules.NO_FORBIDDEN_CLASSES_CALLED;
}
