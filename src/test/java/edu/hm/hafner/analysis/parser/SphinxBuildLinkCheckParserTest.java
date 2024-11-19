package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link SphinxBuildLinkCheckParser}.
 */
class SphinxBuildLinkCheckParserTest extends AbstractParserTest {
    SphinxBuildLinkCheckParserTest() {
        super("sphinxbuildlinkcheck.txt");
    }

    private static final String SPHINX_LINKCHECK_BROKEN = "Broken";
    private static final String SPHINX_LINKCHECK_RED_FOUND = "Redirected with Found";
    private static final String SPHINX_LINKCHECK_RED_PERM = "Redirected permanently";
    private static final String SPHINX_LINKCHECK_RED_TEMP = "Redirected temporarily";
    private static final String SPHINX_LINKCHECK_RED_OTH = "Redirected with See Other";

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(6);

        softly.assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory(SPHINX_LINKCHECK_BROKEN)
                .hasLineStart(451)
                .hasLineEnd(451)
                .hasMessage("https://matplotlib.org/api/_as_gen/matplotlib.figure.Figure.html#matplotlib.figure.Figure: Anchor 'matplotlib.figure.Figure' not found")
                .hasFileName("modules/javert/mpl.rst");
        softly.assertThat(report.get(1))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory(SPHINX_LINKCHECK_BROKEN)
                .hasLineStart(451)
                .hasLineEnd(451)
                .hasMessage("https://matplotlib.org/api/_as_gen/matplotlib.pyplot.errorbar.html#matplotlib.pyplot.errorbar: Ancre 'matplotlib.pyplot.errorbar' non trouvée")
                .hasFileName("modules/javert/mpl.rst");
        softly.assertThat(report.get(2))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(SPHINX_LINKCHECK_RED_FOUND)
                .hasLineStart(9)
                .hasLineEnd(9)
                .hasMessage("https://www.sphinx-doc.org/en/stable/ to https://www.sphinx-doc.org/en/master/")
                .hasFileName("devs/documentation.rst");
        softly.assertThat(report.get(3))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(SPHINX_LINKCHECK_RED_PERM)
                .hasLineStart(52)
                .hasLineEnd(52)
                .hasMessage("https://docs.scipy.org/doc/numpy/user/basics.rec.html to https://numpy.org/doc/stable/user/basics.rec.html")
                .hasFileName("modules/eponine/tripoli4/common.rst");
        softly.assertThat(report.get(4))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(SPHINX_LINKCHECK_RED_OTH)
                .hasLineStart(221)
                .hasLineEnd(221)
                .hasMessage("https://docs.scipy.org/doc/numpy/user/basics.rec.html to https://numpy.org/doc/stable/user/basics.rec.html")
                .hasFileName("modules/eponine/tripoli4/common.rst");
        softly.assertThat(report.get(5))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(SPHINX_LINKCHECK_RED_TEMP)
                .hasLineStart(224)
                .hasLineEnd(224)
                .hasMessage("https://docs.scipy.org/doc/numpy/user/basics.rec.html to https://numpy.org/doc/stable/user/basics.rec.html")
                .hasFileName("modules/eponine/tripoli4/common.rst");
    }

    @Override
    protected SphinxBuildLinkCheckParser createParser() {
        return new SphinxBuildLinkCheckParser();
    }
}
