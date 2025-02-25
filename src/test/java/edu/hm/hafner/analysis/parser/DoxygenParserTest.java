package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link DoxygenParser} using input files of Doxygen.
 */
class DoxygenParserTest extends AbstractParserTest {
    private static final String NO_FILE_NAME = "<unknown>";

    /**
     * Creates a new instance of {@link DoxygenParserTest}.
     */
    protected DoxygenParserTest() {
        super("doxygen.txt");
    }

    /**
     * Verifies that errors and warnings at the start of the line are matches as well.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-64657">Issue 64657</a>
     */
    @Test
    void issue64657() {
        var report = parse("issue64657.txt");
        assertThat(report).hasSize(3);

        assertThat(report.get(0))
                .hasLineStart(0)
                .hasMessage(
                        """
                        doxygen no longer ships with the FreeSans font.
                                 You may want to clear or change DOT_FONTNAME.
                                 Otherwise you run the risk that the wrong font is being used for dot generated graphs.""")
                .hasFileName("-")
                .hasSeverity(Severity.WARNING_NORMAL);

        assertThat(report.get(1))
                .hasLineStart(0)
                .hasMessage("source ../path/to/file is not a readable file or directory... skipping.")
                .hasFileName("-")
                .hasSeverity(Severity.WARNING_NORMAL);

        assertThat(report.get(2))
                .hasLineStart(0)
                .hasMessage("tag INPUT: input source `../../example/inc/example2.h' does not exist")
                .hasFileName("-")
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    /**
     * Verifies that parsing of long files does not fail.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-7178">Issue 7178</a>
     * @see <a href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6882582">JDK Bug 6882582</a>
     */
    @Test
    void issue7178() {
        var report = parse("issue7178.txt");
        assertThat(report).hasSize(1);

        assertThat(report.get(0))
                .hasLineStart(977)
                .hasMessage(
                        """
                        ‘t_day_of_year.boost::CV::constrained_value<boost::CV::simple_exception_policy<short unsigned int, 1u, 366u, boost::gregorian::bad_day_of_year> >::value_’ may be used uninitialized in this function
                        Compiling library/datetimelib/Timezone.cpp
                        Compiling library/datetimelib/TimezoneDB.cpp
                        Creating libdatetimelib.a
                        ar: creating /home/hudson_slave/workspace/RX-kernel-daily_i686-linux-gcc41/trunk/src/arch/i686-linux-gcc41/build/Debug/libdatetimelib.a""")
                .hasFileName(
                        "/home/hudson_slave/workspace/RX-kernel-daily_i686-linux-gcc41/trunk/src/3rdparty/boost/exp/boost/date_time/time_facet.hpp")
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    /**
     * Verifies that the path names are correctly mapped.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-55840">Issue 55840</a>
     */
    @Test
    void issue55840() {
        var linuxReport = parse("issue55840.linux.txt");
        assertThat(linuxReport).hasSize(14);
        for (Issue issue : linuxReport) {
            assertThat(issue.getFileName()).startsWith("/srv/www/jenkins/workspace/code_master/pump/");
        }

        var windowsReport = parse("issue55840.windows.txt");
        assertThat(windowsReport).hasSize(15);
        for (Issue issue : windowsReport) {
            assertThat(issue.getFileName()).startsWith("C:/jenkins/workspace/24-Test-Jenkins-WinTen-Extension@2/pump");
        }
    }

    /**
     * Parses a warning log with 4 doxygen 1.7.1 messages.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-6971">Issue 6971</a>
     */
    @Test
    void issue6971() {
        var warnings = parse("issue6971.txt");
        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings).hasSize(4);

            Iterator<? extends Issue> iterator = warnings.iterator();

            softly.assertThat(iterator.next())
                    .hasLineEnd(479)
                    .hasLineStart(479)
                    .hasMessage(
                            "the name `lcp_lexicolemke.c' supplied as the second argument in the \\file statement is not an input file")
                    .hasFileName("/home/user/myproject/helper/LCPcalc.cpp")
                    .hasSeverity(Severity.WARNING_NORMAL);

            softly.assertThat(iterator.next())
                    .hasLineEnd(19)
                    .hasLineStart(19)
                    .hasMessage("Unexpected character `\"'")
                    .hasFileName("/home/user/myproject/helper/SimpleTimer.h")
                    .hasSeverity(Severity.ERROR);

            softly.assertThat(iterator.next())
                    .hasLineEnd(357)
                    .hasLineStart(357)
                    .hasMessage("Member getInternalParser() (function) of class XmlParser is not documented.")
                    .hasFileName(".../XmlParser.h")
                    .hasSeverity(Severity.WARNING_NORMAL);

            softly.assertThat(iterator.next())
                    .hasLineEnd(39)
                    .hasLineStart(39)
                    .hasMessage(
                            "Member XmlMemoryEntityMapEntry (typedef) of class XmlMemoryEntityResolver is not documented.")
                    .hasFileName("P:/Integration/DjRip/djrip/workspace/libraries/xml/XmlMemoryEntityResolver.h")
                    .hasSeverity(Severity.WARNING_NORMAL);
        }
    }

    /**
     * Add support for Doxygen Warnings.
     *
     * @see <a href="https://issues.jenkins.io/browse/JENKINS-61855">Issue 61855</a>
     */
    @Test
    void issue61855() {
        var warnings = parse("issue61855.txt");

        assertThat(warnings).hasSize(7);

        int lineNumber = 115;

        for (Issue issue : warnings) {
            assertThat(issue.getFileName()).startsWith("/repo/src/libraries/base/include/configuration.h");
            assertThat(issue.getColumnStart()).isEqualTo(0);
            assertThat(issue.getColumnEnd()).isEqualTo(0);
            assertThat(issue.getSeverity()).isEqualTo(Severity.WARNING_NORMAL);
            assertThat(issue.getLineStart()).isEqualTo(lineNumber);
            assertThat(issue.getLineEnd()).isEqualTo(lineNumber);
            lineNumber += 5;
        }

        assertThat(warnings.get(0))
                .hasMessage("Member Notify(const IProperty &oProperty) override (function) of class property_variable< T > is not documented.");

        assertThat(warnings.get(1))
                .hasMessage("Member GetType(IString &&strType) const override (function) of class property_variable< T > is not documented.");

        assertThat(warnings.get(2))
                .hasMessage("Member GetValue() const override (function) of class property_variable< T > is not documented.");

        assertThat(warnings.get(3))
                .hasMessage("Member operator*() const (function) of class property_variable< T > is not documented.");

        assertThat(warnings.get(4))
                .hasMessage("Member operator const T &() const (function) of class property_variable< T > is not documented.");

        assertThat(warnings.get(5))
                .hasMessage("Member operator==(const T &oVal) (function) of class property_variable< T > is not documented.");

        assertThat(warnings.get(6))
                .hasMessage("Member operator!=(const T &oVal) (function) of class property_variable< T > is not documented.");
    }

    @SuppressWarnings("methodlength")
    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(19);

        Iterator<Issue> iterator = report.iterator();

        softly.assertThat(iterator.next())
                .hasLineEnd(171)
                .hasLineStart(171)
                .hasMessage(
                        "reached end of file while inside a dot block!\nThe command that should end the block seems to be missing!")
                .hasFileName("/home/user/myproject/component/odesolver/CentralDifferenceSolver.cpp")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineEnd(479)
                .hasLineStart(479)
                .hasMessage(
                        "the name `lcp_lexicolemke.c' supplied as the second argument in the \\file statement is not an input file")
                .hasFileName("/home/user/myproject/helper/LCPcalc.cpp")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineEnd(65)
                .hasLineStart(65)
                .hasMessage(
                        "documented function `sofa::core::componentmodel::behavior::BaseController::BaseController' was not declared or defined.")
                .hasFileName("/home/user/myproject/core/componentmodel/behavior/BaseController.cpp")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineEnd(72)
                .hasLineStart(72)
                .hasMessage(
                        "no matching class member found for\n  void sofa::core::componentmodel::behavior::BaseController::handleEvent(core::objectmodel::Event *event)")
                .hasFileName("/home/user/myproject/core/componentmodel/behavior/BaseController.cpp")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineEnd(699)
                .hasLineStart(699)
                .hasMessage(
                        "no uniquely matching class member found for\n  template <>\n  const char * sofa::defaulttype::Rigid3dTypes::Name()")
                .hasFileName("/home/user/myproject/defaulttype/RigidTypes.h")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineEnd(1351)
                .hasLineStart(1351)
                .hasMessage("""
                        no matching file member found for\s
                        defaulttype::RigidDeriv< 3, double > sofa::core::componentmodel::behavior::inertiaForce< defaulttype::RigidCoord< 3, double >, defaulttype::RigidDeriv< 3, double >, objectmodel::BaseContext::Vec3, defaulttype::RigidMass< 3, double >, objectmodel::BaseContext::SpatialVector >(const sofa::defaulttype::SolidTypes::SpatialVector &vframe, const objectmodel::BaseContext::Vec3 &aframe, const defaulttype::RigidMass< 3, double > &mass, const defaulttype::RigidCoord< 3, double > &x, const defaulttype::RigidDeriv< 3, double > &v)
                        Possible candidates:
                          Deriv inertiaForce(const SV &, const Vec &, const M &, const Coord &, const Deriv &)""")
                .hasFileName("/home/user/myproject/defaulttype/RigidTypes.h")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineEnd(569)
                .hasLineStart(569)
                .hasMessage("""
                        no uniquely matching class member found for
                          template < R >
                          SolidTypes< R >::Vec sofa::defaulttype::SolidTypes< R >::mult(const typename sofa::defaulttype::Mat< 3, 3, Real > &m, const typename SolidTypes< R >::Vec &v)
                        Possible candidates:
                          static Vec sofa::defaulttype::SolidTypes< R >::mult(const Mat &m, const Vec &v) at line 404 of file /home/user/myprojeOdeSolverct/defaulttype/SolidTypes.h""")
                .hasFileName("/home/user/myproject/defaulttype/SolidTypes.inl")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineEnd(227)
                .hasLineStart(227)
                .hasMessage(
                        "no uniquely matching class member found for\n  template < Real >\n  DualQuat< Real >::Vec sofa::helper::DualQuat< Real >::transform(const typename sofa::defaulttype::Vec< 3, Real > &vec)\nPossible candidates:\n  Vec sofa::helper::DualQuat< Real >::transform(const Vec &vec) at line 73 of file /home/user/myproject/helper/DualQuat.h")
                .hasFileName("/home/user/myproject/helper/DualQuat.inl")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineEnd(496)
                .hasLineStart(496)
                .hasMessage("""
                        no matching file member found for\s
                        void sofa::helper::lcp_lexicolemke(int *nn, double *vec, double *q, double *zlem, double *wlem, int *info, int *iparamLCP, double *dparamLCP)
                        Possible candidates:
                          int lcp_lexicolemke(int dim, double *q, double **M, double *res)
                          int lcp_lexicolemke(int dim, double *q, double **M, double **A, double *res)""")
                .hasFileName("/home/user/myproject/helper/LCPcalc.cpp")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineEnd(163)
                .hasLineStart(163)
                .hasMessage("Found unknown command `\\notify'")
                .hasFileName("/home/user/myproject/core/componentmodel/topology/BaseTopology.h")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineEnd(172)
                .hasLineStart(172)
                .hasMessage(
                        "argument 'sv' of command @param is not found in the argument list of sofa::core::componentmodel::behavior::inertiaForce(const SV &, const Vec &, const M &, const Coord &, const Deriv &)")
                .hasFileName("/home/user/myproject/core/componentmodel/behavior/Mass.h")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineEnd(97)
                .hasLineStart(97)
                .hasMessage(
                        "The following parameters of sofa::core::componentmodel::behavior::BaseForceField::addMBKdx(double mFactor, double bFactor, double kFactor) are not documented:\n  parameter 'mFactor'\n  parameter 'bFactor'\n  parameter 'kFactor'")
                .hasFileName("/home/user/myproject/core/componentmodel/behavior/BaseForceField.h")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineEnd(104)
                .hasLineStart(104)
                .hasMessage(
                        "The following parameters of sofa::core::componentmodel::behavior::BaseLMConstraint::ConstraintGroup::addConstraint(unsigned int i0, SReal c) are not documented:\n  parameter 'i0'")
                .hasFileName("/home/user/myproject/core/componentmodel/behavior/BaseLMConstraint.h")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineEnd(98)
                .hasLineStart(98)
                .hasMessage("explicit link request to 'index' could not be resolved")
                .hasFileName("/home/user/myproject/core/componentmodel/behavior/BaseMass.h")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineEnd(96)
                .hasLineStart(96)
                .hasMessage("Found unknown command `\\TODO'")
                .hasFileName("/home/user/myproject/core/componentmodel/behavior/OdeSolver.h")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(1) // Actually -1 in the file, but the line number of this kind of messages is irrelevant
                .hasMessage("Found unknown command `\\TODO'")
                .hasFileName(NO_FILE_NAME)
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineEnd(19)
                .hasLineStart(19)
                .hasMessage("Unexpected character `\"'")
                .hasFileName("/home/user/myproject/helper/SimpleTimer.h")
                .hasSeverity(Severity.ERROR);

        softly.assertThat(iterator.next())
                .hasLineStart(1) // Actually 1 in the file, but the line number of this kind of messages is irrelevant
                .hasMessage(
                        "The following parameters of sofa::component::odesolver::EulerKaapiSolver::v_peq(VecId v, VecId a, double f) are not documented:\n  parameter 'v'\n  parameter 'a'")
                .hasFileName("<v_peq>")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(iterator.next())
                .hasLineStart(0)
                .hasMessage(
                        "Could not read image `/home/user/myproject/html/struct_foo_graph.png' generated by dot!")
                .hasFileName("-")
                .hasSeverity(Severity.ERROR);
    }

    @Override
    protected IssueParser createParser() {
        return new DoxygenParser();
    }
}
