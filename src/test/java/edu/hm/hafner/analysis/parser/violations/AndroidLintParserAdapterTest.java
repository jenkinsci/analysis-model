package edu.hm.hafner.analysis.parser.violations;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link AndroidLintParserAdapter}.
 *
 * @author Ullrich Hafner
 */
class AndroidLintParserAdapterTest extends AbstractParserTest<Issue> {
    AndroidLintParserAdapterTest() {
        super("android-lint.xml");
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {
        softly.assertThat(issues).hasSize(2);
        softly.assertThat(issues.get(0))
                .hasMessage("ScrollViewSize: ScrollView size validation\n"
                        + "This LinearLayout should use `android:layout_height=\"wrap_content\"`\n"
                        + "ScrollView children must set their `layout_width` or `layout_height` attributes to "
                        + "`wrap_content` rather than `fill_parent` or `match_parent` in the scrolling dimension")
                .hasFileName("app/src/main/res/layout/fragment_main.xml")
                .hasCategory("Correctness")
                .hasLineStart(10)
                .hasLineEnd(10)
                .hasColumnStart(9)
                .hasColumnEnd(9)
                .hasPriority(Priority.NORMAL);
        softly.assertThat(issues.get(1))
                .hasMessage("InvalidPackage: Package not included in Android\n"
                        + "Invalid package reference in library; not included in Android: `java.nio.file`. "
                        + "Referenced from `okio.Okio`.\n"
                        + "This check scans through libraries looking for calls to APIs that are not included in Android.\n"
                        + "        \n"
                        + "        When you create Android projects, the classpath is set up such that you can only "
                        + "access classes in the API packages that are included in Android. However, if you add other "
                        + "projects to your libs/ folder, there is no guarantee that those .jar files were built with an "
                        + "Android specific classpath, and in particular, they could be accessing unsupported APIs "
                        + "such as java.applet.\n"
                        + "        \n"
                        + "        This check scans through library jars and looks for references to API packages that "
                        + "are not included in Android and flags these. This is only an error if your code calls one of "
                        + "the library classes which wind up referencing the unsupported package.")
                .hasFileName(
                        ".gradle/caches/modules-2/files-2.1/com.squareup.okio/okio/1.4.0/5b72bf48563ea8410e650de14aa33ff69a3e8c35/okio-1.4.0.jar")
                .hasCategory("Correctness")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasColumnStart(0)
                .hasColumnEnd(0)
                .hasPriority(Priority.HIGH);
    }

    @Override
    protected AbstractParser<Issue> createParser() {
        return new AndroidLintParserAdapter();
    }
}