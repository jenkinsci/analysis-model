package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link GccParser}.
 *
 * @author Ullrich Hafner
 * @author Raphael Furch
 */
class GccParserTest extends AbstractParserTest {
    private static final String GCC_ERROR = GccParser.GCC_ERROR;
    private static final String GCC_WARNING = "GCC warning";

    protected GccParserTest() {
        super("gcc.txt");
    }

    /**
     * Checks that a false positive is not reported anymore.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-34141">Issue 34141</a>
     */
    @Test
    void issue34141() {
        var warnings = parse("issue34141.txt");

        assertThat(warnings).isEmpty();
    }

    /**
     * Verifies that the message contains escaped XML characters.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-17309">Issue 17309</a>
     */
    @Test
    void issue17309() {
        var warnings = parse("issue17309.txt");

        assertThat(warnings).hasSize(1);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasLineStart(4)
                    .hasLineEnd(4)
                    .hasMessage("dereferencing pointer &apos;&lt;anonymous&gt;&apos; does break strict-aliasing rules")
                    .hasFileName("foo.cc")
                    .hasCategory(GCC_ERROR)
                    .hasSeverity(Severity.WARNING_HIGH);
        }
    }

    /**
     * Parses a file with one warning that are started by ant.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-9926">Issue 9926</a>
     */
    @Test
    void issue9926() {
        var warnings = parse("issue9926.txt");

        assertThat(warnings).hasSize(1);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasLineStart(52)
                    .hasLineEnd(52)
                    .hasMessage("large integer implicitly truncated to unsigned type")
                    .hasFileName("src/test_simple_sgs_message.cxx")

                    .hasCategory(GCC_WARNING)
                    .hasSeverity(Severity.WARNING_NORMAL);
        }
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(8);

        Iterator<Issue> iterator = report.iterator();

        softly.assertThat(iterator.next())
                .hasLineStart(451)
                .hasLineEnd(451)
                .hasMessage("`void yyunput(int, char*)&apos; defined but not used")
                .hasFileName("testhist.l")
                .hasCategory(GCC_WARNING)
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(73)
                .hasLineEnd(73)
                .hasMessage("implicit typename is deprecated, please see the documentation for details")
                .hasFileName("/u1/drjohn/bfdist/packages/RegrTest/V00-03-01/RgtAddressLineScan.cc")
                .hasCategory(GCC_ERROR)
                .hasSeverity(Severity.WARNING_HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(4)
                .hasLineEnd(4)
                .hasMessage("foo.h: No such file or directory")
                .hasFileName("foo.cc")
                .hasCategory(GCC_ERROR)
                .hasSeverity(Severity.WARNING_HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("undefined reference to &apos;missing_symbol&apos;")
                .hasFileName("foo.so")
                .hasCategory(GCC_ERROR)
                .hasSeverity(Severity.WARNING_HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(678)
                .hasLineEnd(678)
                .hasMessage("missing initializer for member sigaltstack::ss_sp")
                .hasFileName("../../lib/linux-i686/include/boost/test/impl/execution_monitor.ipp")
                .hasCategory(GCC_WARNING)
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(678)
                .hasLineEnd(678)
                .hasMessage("missing initializer for member sigaltstack::ss_flags")
                .hasFileName("../../lib/linux-i686/include/boost/test/impl/execution_monitor.ipp")
                .hasCategory(GCC_WARNING)
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(678)
                .hasLineEnd(678)
                .hasMessage("missing initializer for member sigaltstack::ss_size")
                .hasFileName("../../lib/linux-i686/include/boost/test/impl/execution_monitor.ipp")
                .hasCategory(GCC_WARNING)
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(52)
                .hasLineEnd(52)
                .hasMessage("large integer implicitly truncated to unsigned type")
                .hasFileName("src/test_simple_sgs_message.cxx")
                .hasCategory(GCC_WARNING)
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    /**
     * Parses a warning log with 2 new GCC warnings.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-3897">Issue 3897</a>
     */
    @Test
    void issue3897and3898() {
        var warnings = parse("issue3897.txt");

        assertThat(warnings).hasSize(3);

        Iterator<? extends Issue> iterator = warnings.iterator();

        try (var softly = new SoftAssertions()) {
            softly.assertThat(iterator.next())
                    .hasLineStart(12)
                    .hasLineEnd(12)
                    .hasMessage("file.h: No such file or directory")
                    .hasFileName("/dir1/dir2/file.c")
                    .hasCategory(GccParser.GCC_ERROR)
                    .hasSeverity(Severity.WARNING_HIGH);

            softly.assertThat(iterator.next())
                    .hasLineStart(233)
                    .hasLineEnd(233)
                    .hasMessage("undefined reference to `MyInterface::getValue() const&apos;")
                    .hasFileName("/dir1/dir3/file.cpp")
                    .hasCategory(GccParser.GCC_ERROR)
                    .hasSeverity(Severity.WARNING_HIGH);

            softly.assertThat(iterator.next())
                    .hasLineStart(20)
                    .hasLineEnd(20)
                    .hasMessage("invalid preprocessing directive #incldue")
                    .hasFileName("/dir1/dir2/file.cpp")
                    .hasCategory(GccParser.GCC_ERROR)
                    .hasSeverity(Severity.WARNING_HIGH);
        }
    }

    /**
     * Parses a warning log with 2 GCC warnings, one of them a note.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-4712">Issue 4712</a>
     */
    @Test
    void issue4712() {
        var warnings = parse("issue4712.txt");

        assertThat(warnings).hasSize(2);

        Iterator<? extends Issue> iterator = warnings.iterator();

        try (var softly = new SoftAssertions()) {
            softly.assertThat(iterator.next())
                    .hasLineStart(352)
                    .hasLineEnd(352)
                    .hasMessage("&apos;s2.mepSector2::lubrications&apos; may be used")
                    .hasFileName("main/mep.cpp")
                    .hasCategory(GCC_WARNING)
                    .hasSeverity(Severity.WARNING_NORMAL);

            softly.assertThat(iterator.next())
                    .hasLineStart(1477)
                    .hasLineEnd(1477)
                    .hasMessage("&apos;s2.mepSector2::lubrications&apos; was declared here")
                    .hasFileName("main/mep.cpp")
                    .hasCategory("GCC note")
                    .hasSeverity(Severity.WARNING_LOW);
        }
    }

    /**
     * Parses a warning log with a ClearCase command line that should not be parsed as a warning.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-4712">Issue 4712</a>
     */
    @Test
    void issue4700() {
        var warnings = parse("issue4700.txt");

        assertThat(warnings).isEmpty();
    }

    /**
     * Parses a warning log with [exec] prefix.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-4712">Issue 4707</a>
     */
    @Test
    void issue4707() {
        var warnings = parse("issue4707.txt");

        assertThat(warnings).hasSize(11).hasDuplicatesSize(11);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasLineStart(1128)
                    .hasLineEnd(1128)
                    .hasMessage("NULL used in arithmetic")
                    .hasFileName(
                            "/Users/rthomson/hudson/jobs/Bryce7-MacWarnings/workspace/bryce7/src/Bryce/Plugins/3DSExport/3DSExport.cpp")
                    .hasCategory(GCC_WARNING)
                    .hasSeverity(Severity.WARNING_NORMAL);
        }
    }

    /**
     * Parses a linker error.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-4010">Issue 4010</a>
     */
    @Test
    void issue4010() {
        var warnings = parse("issue4010.txt");

        assertThat(warnings).hasSize(1);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("cannot find -lMyLib")
                    .hasFileName("MyLib")
                    .hasCategory(GccParser.LINKER_ERROR)
                    .hasSeverity(Severity.WARNING_HIGH);
        }
    }

    /**
     * Parses a warning log with 6 new objective C warnings.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-4274">Issue 4274</a>
     */
    @Test
    void issue4274() {
        var warnings = parse("issue4274.txt");

        assertThat(warnings).hasSize(4);

        Iterator<? extends Issue> iterator = warnings.iterator();

        try (var softly = new SoftAssertions()) {
            softly.assertThat(iterator.next())
                    .hasLineStart(638)
                    .hasLineEnd(638)
                    .hasMessage("local declaration of &quot;command&quot; hides instance variable")
                    .hasFileName("folder1/file1.m")
                    .hasCategory(GCC_WARNING)
                    .hasSeverity(Severity.WARNING_NORMAL);

            softly.assertThat(iterator.next())
                    .hasLineStart(640)
                    .hasLineEnd(640)
                    .hasMessage("instance variable &quot;command&quot; accessed in class method")
                    .hasFileName("folder1/file1.m")
                    .hasCategory(GCC_WARNING)
                    .hasSeverity(Severity.WARNING_NORMAL);

            softly.assertThat(iterator.next())
                    .hasLineStart(47)
                    .hasLineEnd(47)
                    .hasMessage("&quot;oldGeb&quot; might be used uninitialized in this function")
                    .hasFileName("file1.m")
                    .hasCategory(GCC_WARNING)
                    .hasSeverity(Severity.WARNING_NORMAL);

            softly.assertThat(iterator.next())
                    .hasLineStart(640)
                    .hasLineEnd(640)
                    .hasMessage("local declaration of &quot;command&quot; hides instance variable")
                    .hasFileName("file1.m")
                    .hasCategory(GCC_WARNING)
                    .hasSeverity(Severity.WARNING_NORMAL);
        }
    }

    /**
     * Parses a file with one warning and matching warning that will be excluded afterward.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-4260">Issue 4260</a>
     */
    @Test
    void issue4260() {
        var warnings = parse("issue4260.txt");

        assertThat(warnings).hasSize(1);
    }

    @Test @org.junitpioneer.jupiter.Issue("JENKINS-70996")
    void ignoreDirectoriesFromOtherTools() {
        var warnings = parse("issue70996.txt");

        assertThat(warnings).hasSize(8);
    }

    @Override
    protected GccParser createParser() {
        return new GccParser();
    }
}
