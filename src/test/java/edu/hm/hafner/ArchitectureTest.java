package edu.hm.hafner;

import javax.xml.parsers.SAXParser;

import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.binder.DigesterLoader;
import org.xml.sax.XMLReader;

import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import edu.hm.hafner.util.AccessRestrictedToTests;

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
            noClasses().that().dontHaveSimpleName("SecureDigester")
                    .should().callConstructor(Digester.class)
                    .orShould().callConstructor(Digester.class, SAXParser.class)
                    .orShould().callConstructor(Digester.class, XMLReader.class)
                    .orShould().callMethod(DigesterLoader.class, "newDigester");

    /** Test classes should not be public (Junit 5). */
    @ArchTest
    static final ArchRule NO_PUBLIC_TEST_CLASSES =
            noClasses().that().haveSimpleNameEndingWith("Test")
                    .and().dontHaveModifier(JavaModifier.ABSTRACT)
                    .should().bePublic();

    /**
     * Methods or constructors that are annotated with {@link VisibleForTesting} must not be called by other classes.
     * These methods are meant to be {@code private}. Only test classes are allowed to call these methods.
     */
    @ArchTest
    static final ArchRule NO_TEST_API_CALLED =
            noClasses().that().haveSimpleNameNotEndingWith("Test")
                    .should().callCodeUnitWhere(new AccessRestrictedToTests());

    /** Prevents that classes use visible but forbidden API. */
    @ArchTest
    static final ArchRule NO_RESTRICTED_API_CALLED =
            noClasses()
            .should().accessClassesThat().resideInAnyPackage(
                    "org.apache.commons.lang..", "javax.xml.bind..");
}
