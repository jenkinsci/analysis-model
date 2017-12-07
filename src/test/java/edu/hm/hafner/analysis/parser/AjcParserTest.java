package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

/**
 * Tests the class {@link AjcParser}.
 */
public class AjcParserTest extends ParserTester {
    private static final String WARNING_TYPE = new AjcParser().getId();

    /**
     * Parses a file with various warnings: - message not found / unknown source - deprecation (class / method) - advice
     * not applied
     * <p>
     * Both unix and windows file paths.
     *
     * @throws java.io.IOException if the file could not be read
     */
    @Test
    public void parseDeprecation() throws IOException {
        Issues warnings = new AjcParser().parse(openFile());

        assertEquals(9, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();
        checkWarning(annotation,
                0,
                "incorrect classpath: /home/hudson/.m2/repository/org/apache/cxf/cxf/2.6.1/cxf-2.6.1.pom",
                "<unknown source file>",
                WARNING_TYPE, DEFAULT_CATEGORY, Priority.NORMAL);

        annotation = iterator.next();
        checkWarning(annotation,
                12,
                "The type SimpleFormController is deprecated",
                "/home/hudson/workspace/project/project-ejb/src/main/java/com/product/foo/pro/controllers/BarController.java",
                WARNING_TYPE, AbstractParser.DEPRECATION, Priority.NORMAL);

        annotation = iterator.next();
        checkWarning(annotation,
                19,
                "The type SimpleFormController is deprecated",
                "/home/hudson/workspace/project/project-ejb/src/main/java/com/product/foo/pro/controllers/BarController.java",
                WARNING_TYPE, AbstractParser.DEPRECATION, Priority.NORMAL);

        annotation = iterator.next();
        checkWarning(annotation,
                32,
                "The method BarController.initBinder(HttpServletRequest, ServletRequestDataBinder) overrides a deprecated method from BaseCommandController",
                "/home/hudson/workspace/project/project-ejb/src/main/java/com/product/foo/pro/controllers/BarController.java",
                WARNING_TYPE, AbstractParser.DEPRECATION, Priority.NORMAL);

        annotation = iterator.next();
        checkWarning(annotation,
                33,
                "The method initBinder(HttpServletRequest, ServletRequestDataBinder) from the type BaseCommandController is deprecated",
                "/home/hudson/workspace/project/project-ejb/src/main/java/com/product/foo/pro/controllers/BarController.java",
                WARNING_TYPE, AbstractParser.DEPRECATION, Priority.NORMAL);

        annotation = iterator.next();
        checkWarning(annotation,
                31,
                "The method NewBarController.initBinder(HttpServletRequest, ServletRequestDataBinder) overrides a deprecated method from BaseCommandController",
                "/home/hudson/workspace/project/project-ejb/src/main/java/com/product/foo/pro/controllers/NewBarController.java",
                WARNING_TYPE, AbstractParser.DEPRECATION, Priority.NORMAL);

        annotation = iterator.next();
        checkWarning(annotation,
                28,
                "The method NewFooController.onSubmit(HttpServletRequest, HttpServletResponse, Object, BindException) overrides a deprecated method from SimpleFormController",
                "C:/Users/hudson/workspace/project/project-ejb/src/main/java/com/product/foo/pro/controllers/NewFooController.java",
                WARNING_TYPE, AbstractParser.DEPRECATION, Priority.NORMAL);

        annotation = iterator.next();
        checkWarning(annotation,
                38,
                "advice defined in com.company.foo.common.security.aspect.FooBarAspect has not been applied [Xlint:adviceDidNotMatch]",
                "/home/hudson/workspace/project/project-ejb/src/main/java/com/product/foo/common/security/aspect/FooBarAspect.java",
                WARNING_TYPE, AjcParser.ADVICE, Priority.NORMAL);

        annotation = iterator.next();
        checkWarning(annotation,
                32,
                "advice defined in org.springframework.orm.jpa.aspectj.JpaExceptionTranslatorAspect has not been applied [Xlint:adviceDidNotMatch]",
                "/home/hudson/.m2/repository/org/springframework/spring-aspects/3.2.8.RELEASE/spring-aspects-3.2.8.RELEASE.jar!org/springframework/orm/jpa/aspectj/JpaExceptionTranslatorAspect.class",
                WARNING_TYPE, AjcParser.ADVICE, Priority.NORMAL);

        assertFalse(iterator.hasNext());
    }

    @Override
    protected String getWarningsFile() {
        return "ajc.txt";
    }
}
