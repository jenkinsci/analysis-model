package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.IssuesAssert.*;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link AjcParser}.
 */
public class AjcParserTest extends AbstractParserTest {
    private static final String CATEGORY = new IssueBuilder().build().getCategory();

    AjcParserTest() {
        super("ajc.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {
        assertThat(issues).hasSize(9);

        Iterator<Issue> iterator = issues.iterator();
        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("incorrect classpath: /home/hudson/.m2/repository/org/apache/cxf/cxf/2.6.1/cxf-2.6.1.pom")
                .hasFileName("<unknown source file>");
        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(AbstractParser.DEPRECATION)
                .hasLineStart(12)
                .hasLineEnd(12)
                .hasMessage("The type SimpleFormController is deprecated")
                .hasFileName("/home/hudson/workspace/project/project-ejb/src/main/java/com/product/foo/pro/controllers/BarController.java");
        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(AbstractParser.DEPRECATION)
                .hasLineStart(19)
                .hasLineEnd(19)
                .hasMessage("The type SimpleFormController is deprecated")
                .hasFileName("/home/hudson/workspace/project/project-ejb/src/main/java/com/product/foo/pro/controllers/BarController.java");
        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(AbstractParser.DEPRECATION)
                .hasLineStart(32)
                .hasLineEnd(32)
                .hasMessage("The method BarController.initBinder(HttpServletRequest, ServletRequestDataBinder) overrides a deprecated method from BaseCommandController")
                .hasFileName("/home/hudson/workspace/project/project-ejb/src/main/java/com/product/foo/pro/controllers/BarController.java");
        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(AbstractParser.DEPRECATION)
                .hasLineStart(33)
                .hasLineEnd(33)
                .hasMessage("The method initBinder(HttpServletRequest, ServletRequestDataBinder) from the type BaseCommandController is deprecated")
                .hasFileName("/home/hudson/workspace/project/project-ejb/src/main/java/com/product/foo/pro/controllers/BarController.java");
        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(AbstractParser.DEPRECATION)
                .hasLineStart(31)
                .hasLineEnd(31)
                .hasMessage("The method NewBarController.initBinder(HttpServletRequest, ServletRequestDataBinder) overrides a deprecated method from BaseCommandController")
                .hasFileName("/home/hudson/workspace/project/project-ejb/src/main/java/com/product/foo/pro/controllers/NewBarController.java");
        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(AbstractParser.DEPRECATION)
                .hasLineStart(28)
                .hasLineEnd(28)
                .hasMessage("The method NewFooController.onSubmit(HttpServletRequest, HttpServletResponse, Object, BindException) overrides a deprecated method from SimpleFormController")
                .hasFileName("C:/Users/hudson/workspace/project/project-ejb/src/main/java/com/product/foo/pro/controllers/NewFooController.java");
        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(AjcParser.ADVICE)
                .hasLineStart(38)
                .hasLineEnd(38)
                .hasMessage("advice defined in com.company.foo.common.security.aspect.FooBarAspect has not been applied [Xlint:adviceDidNotMatch]")
                .hasFileName("/home/hudson/workspace/project/project-ejb/src/main/java/com/product/foo/common/security/aspect/FooBarAspect.java");
        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(AjcParser.ADVICE)
                .hasLineStart(32)
                .hasLineEnd(32)
                .hasMessage("advice defined in org.springframework.orm.jpa.aspectj.JpaExceptionTranslatorAspect has not been applied [Xlint:adviceDidNotMatch]")
                .hasFileName("/home/hudson/.m2/repository/org/springframework/spring-aspects/3.2.8.RELEASE/spring-aspects-3.2.8.RELEASE.jar!org/springframework/orm/jpa/aspectj/JpaExceptionTranslatorAspect.class");
        softly.assertThat(iterator.hasNext()).isFalse();
    }

    @Override
    protected AbstractParser createParser() {
        return new AjcParser();
    }
}
