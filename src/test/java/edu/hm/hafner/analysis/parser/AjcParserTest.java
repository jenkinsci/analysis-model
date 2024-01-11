package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import edu.hm.hafner.analysis.Categories;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link AjcParser}.
 */
class AjcParserTest extends AbstractParserTest {
    private static final String CATEGORY = new IssueBuilder().build().getCategory();

    AjcParserTest() {
        super("ajc.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(9);

        Iterator<Issue> iterator = report.iterator();
        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(0)
                .hasMessage("incorrect classpath: /home/hudson/.m2/repository/org/apache/cxf/cxf/2.6.1/cxf-2.6.1.pom")
                .hasFileName("<unknown source file>");
        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(Categories.DEPRECATION)
                .hasLineStart(12)
                .hasMessage("The type SimpleFormController is deprecated")
                .hasFileName("/home/hudson/workspace/project/project-ejb/src/main/java/com/product/foo/pro/controllers/BarController.java");
        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(Categories.DEPRECATION)
                .hasLineStart(19)
                .hasMessage("The type SimpleFormController is deprecated")
                .hasFileName("/home/hudson/workspace/project/project-ejb/src/main/java/com/product/foo/pro/controllers/BarController.java");
        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(Categories.DEPRECATION)
                .hasLineStart(32)
                .hasMessage("The method BarController.initBinder(HttpServletRequest, ServletRequestDataBinder) overrides a deprecated method from BaseCommandController")
                .hasFileName("/home/hudson/workspace/project/project-ejb/src/main/java/com/product/foo/pro/controllers/BarController.java");
        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(Categories.DEPRECATION)
                .hasLineStart(33)
                .hasMessage("The method initBinder(HttpServletRequest, ServletRequestDataBinder) from the type BaseCommandController is deprecated")
                .hasFileName("/home/hudson/workspace/project/project-ejb/src/main/java/com/product/foo/pro/controllers/BarController.java");
        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(Categories.DEPRECATION)
                .hasLineStart(31)
                .hasMessage("The method NewBarController.initBinder(HttpServletRequest, ServletRequestDataBinder) overrides a deprecated method from BaseCommandController")
                .hasFileName("/home/hudson/workspace/project/project-ejb/src/main/java/com/product/foo/pro/controllers/NewBarController.java");
        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(Categories.DEPRECATION)
                .hasLineStart(28)
                .hasMessage("The method NewFooController.onSubmit(HttpServletRequest, HttpServletResponse, Object, BindException) overrides a deprecated method from SimpleFormController")
                .hasFileName("C:/Users/hudson/workspace/project/project-ejb/src/main/java/com/product/foo/pro/controllers/NewFooController.java");
        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(AjcParser.ADVICE)
                .hasLineStart(38)
                .hasMessage("advice defined in com.company.foo.common.security.aspect.FooBarAspect has not been applied [Xlint:adviceDidNotMatch]")
                .hasFileName("/home/hudson/workspace/project/project-ejb/src/main/java/com/product/foo/common/security/aspect/FooBarAspect.java");
        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(AjcParser.ADVICE)
                .hasLineStart(32)
                .hasMessage("advice defined in org.springframework.orm.jpa.aspectj.JpaExceptionTranslatorAspect has not been applied [Xlint:adviceDidNotMatch]")
                .hasFileName("/home/hudson/.m2/repository/org/springframework/spring-aspects/3.2.8.RELEASE/spring-aspects-3.2.8.RELEASE.jar!org/springframework/orm/jpa/aspectj/JpaExceptionTranslatorAspect.class");
        softly.assertThat(iterator.hasNext()).isFalse();
    }

    @Override
    protected AjcParser createParser() {
        return new AjcParser();
    }
}
