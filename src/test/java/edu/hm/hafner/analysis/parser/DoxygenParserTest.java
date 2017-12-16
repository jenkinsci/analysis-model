package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.IssuesAssert.*;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import static edu.hm.hafner.analysis.parser.ParserTester.*;


/**
 * Tests the class {@link DoxygenParser}.
 */
public class DoxygenParserTest extends AbstractParserTest {
    private static final String WARNING_CATEGORY = DEFAULT_CATEGORY;
    private static final String NO_FILE_NAME = "-";

    /**
     * Creates a new instance of {@link AbstractParserTest}.
     */
    protected DoxygenParserTest() {
        super("doxygen.txt");
    }

    /**
     * Verifies that parsing of long files does not fail.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-7178">Issue 7178</a>
     * @see <a href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6882582">JDK Bug 6882582</a>
     */
    @Test
    @Disabled("FIXME: Check with Java 8")
    public void issue7178() {
        Issues<Issue> warnings = parse("issue7178.txt");
        assertThat(warnings).isEmpty(); //seems to be 1
    }

    /**
     * Parses a warning log with 4 doxygen 1.7.1 messages.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-6971">Issue 6971</a>
     */
    @Test
    public void issue6971() {
        Issues<Issue> warnings = parse("issue6971.txt");
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(warnings).hasSize(4);

        Iterator<Issue> iterator = warnings.iterator();

        softly.assertThat(iterator.next())
                .hasLineEnd(479)
                .hasLineStart(479)
                .hasMessage("the name `lcp_lexicolemke.c' supplied as the second argument in the \\file statement is not an input file")
                .hasFileName("/home/user/myproject/helper/LCPcalc.cpp")
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.NORMAL);

        softly.assertThat(iterator.next())
                .hasLineEnd(19)
                .hasLineStart(19)
                .hasMessage("Unexpected character `\"'")
                .hasFileName("/home/user/myproject/helper/SimpleTimer.h")
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.HIGH);

        softly.assertThat(iterator.next())
                .hasLineEnd(357)
                .hasLineStart(357)
                .hasMessage("Member getInternalParser() (function) of class XmlParser is not documented.")
                .hasFileName(".../XmlParser.h")
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.NORMAL);

        softly.assertThat(iterator.next())
                .hasLineEnd(39)
                .hasLineStart(39)
                .hasMessage("Member XmlMemoryEntityMapEntry (typedef) of class XmlMemoryEntityResolver is not documented.")
                .hasFileName("P:/Integration/DjRip/djrip/workspace/libraries/xml/XmlMemoryEntityResolver.h")
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.NORMAL);

        softly.assertAll();
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {
        assertThat(issues).hasSize(21).hasDuplicatesSize(1);

        Iterator<Issue> iterator = issues.iterator();

        softly.assertThat(iterator.next())
                .hasLineEnd(0)
                .hasLineStart(0)
                .hasMessage("Output directory `doc/doxygen/framework' does not exist. I have created it for you.")
                .hasFileName(NO_FILE_NAME)
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.LOW);

        softly.assertThat(iterator.next())
                .hasLineEnd(171)
                .hasLineStart(171)
                .hasMessage("reached end of file while inside a dot block!\nThe command that should end the block seems to be missing!")
                .hasFileName("/home/user/myproject/component/odesolver/CentralDifferenceSolver.cpp")
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.NORMAL);

        softly.assertThat(iterator.next())
                .hasLineEnd(479)
                .hasLineStart(479)
                .hasMessage("the name `lcp_lexicolemke.c' supplied as the second argument in the \\file statement is not an input file")
                .hasFileName("/home/user/myproject/helper/LCPcalc.cpp")
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.NORMAL);

        softly.assertThat(iterator.next())
                .hasLineEnd(65)
                .hasLineStart(65)
                .hasMessage("documented function `sofa::core::componentmodel::behavior::BaseController::BaseController' was not declared or defined.")
                .hasFileName("/home/user/myproject/core/componentmodel/behavior/BaseController.cpp")
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.NORMAL);

        softly.assertThat(iterator.next())
                .hasLineEnd(72)
                .hasLineStart(72)
                .hasMessage("no matching class member found for\n  void sofa::core::componentmodel::behavior::BaseController::handleEvent(core::objectmodel::Event *event)")
                .hasFileName("/home/user/myproject/core/componentmodel/behavior/BaseController.cpp")
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.NORMAL);

        softly.assertThat(iterator.next())
                .hasLineEnd(699)
                .hasLineStart(699)
                .hasMessage("no uniquely matching class member found for\n  template <>\n  const char * sofa::defaulttype::Rigid3dTypes::Name()")
                .hasFileName("/home/user/myproject/defaulttype/RigidTypes.h")
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.NORMAL);

        softly.assertThat(iterator.next())
                .hasLineEnd(1351)
                .hasLineStart(1351)
                .hasMessage("no matching file member found for \ndefaulttype::RigidDeriv< 3, double > sofa::core::componentmodel::behavior::inertiaForce< defaulttype::RigidCoord< 3, double >, defaulttype::RigidDeriv< 3, double >, objectmodel::BaseContext::Vec3, defaulttype::RigidMass< 3, double >, objectmodel::BaseContext::SpatialVector >(const sofa::defaulttype::SolidTypes::SpatialVector &vframe, const objectmodel::BaseContext::Vec3 &aframe, const defaulttype::RigidMass< 3, double > &mass, const defaulttype::RigidCoord< 3, double > &x, const defaulttype::RigidDeriv< 3, double > &v)\nPossible candidates:\n  Deriv inertiaForce(const SV &, const Vec &, const M &, const Coord &, const Deriv &)")
                .hasFileName("/home/user/myproject/defaulttype/RigidTypes.h")
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.NORMAL);

        softly.assertThat(iterator.next())
                .hasLineEnd(569)
                .hasLineStart(569)
                .hasMessage("no uniquely matching class member found for\n  template < R >\n  SolidTypes< R >::Vec sofa::defaulttype::SolidTypes< R >::mult(const typename sofa::defaulttype::Mat< 3, 3, Real > &m, const typename SolidTypes< R >::Vec &v)\nPossible candidates:\n  static Vec sofa::defaulttype::SolidTypes< R >::mult(const Mat &m, const Vec &v) at line 404 of file /home/user/myproject/defaulttype/SolidTypes.h")
                .hasFileName("/home/user/myproject/defaulttype/SolidTypes.inl")
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.NORMAL);

        softly.assertThat(iterator.next())
                .hasLineEnd(227)
                .hasLineStart(227)
                .hasMessage("no uniquely matching class member found for\n  template < Real >\n  DualQuat< Real >::Vec sofa::helper::DualQuat< Real >::transform(const typename sofa::defaulttype::Vec< 3, Real > &vec)\nPossible candidates:\n  Vec sofa::helper::DualQuat< Real >::transform(const Vec &vec) at line 73 of file /home/user/myproject/helper/DualQuat.h")
                .hasFileName("/home/user/myproject/helper/DualQuat.inl")
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.NORMAL);

        softly.assertThat(iterator.next())
                .hasLineEnd(496)
                .hasLineStart(496)
                .hasMessage("no matching file member found for \nvoid sofa::helper::lcp_lexicolemke(int *nn, double *vec, double *q, double *zlem, double *wlem, int *info, int *iparamLCP, double *dparamLCP)\nPossible candidates:\n  int lcp_lexicolemke(int dim, double *q, double **M, double *res)\n  int lcp_lexicolemke(int dim, double *q, double **M, double **A, double *res)")
                .hasFileName("/home/user/myproject/helper/LCPcalc.cpp")
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.NORMAL);

        softly.assertThat(iterator.next())
                .hasLineEnd(163)
                .hasLineStart(163)
                .hasMessage("Found unknown command `\\notify'")
                .hasFileName("/home/user/myproject/core/componentmodel/topology/BaseTopology.h")
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.NORMAL);

        softly.assertThat(iterator.next())
                .hasLineEnd(172)
                .hasLineStart(172)
                .hasMessage("argument 'sv' of command @param is not found in the argument list of sofa::core::componentmodel::behavior::inertiaForce(const SV &, const Vec &, const M &, const Coord &, const Deriv &)")
                .hasFileName("/home/user/myproject/core/componentmodel/behavior/Mass.h")
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.NORMAL);

        softly.assertThat(iterator.next())
                .hasLineEnd(97)
                .hasLineStart(97)
                .hasMessage("The following parameters of sofa::core::componentmodel::behavior::BaseForceField::addMBKdx(double mFactor, double bFactor, double kFactor) are not documented:\n  parameter 'mFactor'\n  parameter 'bFactor'\n  parameter 'kFactor'")
                .hasFileName("/home/user/myproject/core/componentmodel/behavior/BaseForceField.h")
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.NORMAL);

        softly.assertThat(iterator.next())
                .hasLineEnd(104)
                .hasLineStart(104)
                .hasMessage("The following parameters of sofa::core::componentmodel::behavior::BaseLMConstraint::ConstraintGroup::addConstraint(unsigned int i0, SReal c) are not documented:\n  parameter 'i0'")
                .hasFileName("/home/user/myproject/core/componentmodel/behavior/BaseLMConstraint.h")
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.NORMAL);

        softly.assertThat(iterator.next())
                .hasLineEnd(98)
                .hasLineStart(98)
                .hasMessage("explicit link request to 'index' could not be resolved")
                .hasFileName("/home/user/myproject/core/componentmodel/behavior/BaseMass.h")
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.NORMAL);

        softly.assertThat(iterator.next())
                .hasLineEnd(0) // Actually 1 in the file, but the line number of this kind of messages is irrelevant
                .hasLineStart(0) // Actually 1 in the file, but the line number of this kind of messages is irrelevant
                .hasMessage("Detected potential recursive class relation between class sofa::core::componentmodel::collision::Contact::Factory and base class Factory< std::string, Contact, std::pair< std::pair< core::CollisionModel *, core::CollisionModel * >, Intersection * > >!")
                .hasFileName(NO_FILE_NAME)
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.HIGH);

        softly.assertThat(iterator.next())
                .hasLineEnd(96)
                .hasLineStart(96)
                .hasMessage("Found unknown command `\\TODO'")
                .hasFileName("/home/user/myproject/core/componentmodel/behavior/OdeSolver.h")
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.NORMAL);

        softly.assertThat(iterator.next())
                .hasLineEnd(0) // Actually -1 in the file, but the line number of this kind of messages is irrelevant
                .hasLineStart(0) // Actually -1 in the file, but the line number of this kind of messages is irrelevant
                .hasMessage("Found unknown command `\\TODO'")
                .hasFileName(NO_FILE_NAME)
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.NORMAL);

        softly.assertThat(iterator.next())
                .hasLineEnd(19)
                .hasLineStart(19)
                .hasMessage("Unexpected character `\"'")
                .hasFileName("/home/user/myproject/helper/SimpleTimer.h")
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.HIGH);

        softly.assertThat(iterator.next())
                .hasLineEnd(0) // Actually 1 in the file, but the line number of this kind of messages is irrelevant
                .hasLineStart(0) // Actually 1 in the file, but the line number of this kind of messages is irrelevant
                .hasMessage("The following parameters of sofa::component::odesolver::EulerKaapiSolver::v_peq(VecId v, VecId a, double f) are not documented:\n  parameter 'v'\n  parameter 'a'")
                .hasFileName(NO_FILE_NAME)
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.NORMAL);

        softly.assertThat(iterator.next())
                .hasLineEnd(0)
                .hasLineStart(0)
                .hasMessage("Could not read image `/home/user/myproject/html/struct_foo_graph.png' generated by dot!")
                .hasFileName(NO_FILE_NAME)
                .hasCategory(WARNING_CATEGORY)
                .hasPriority(Priority.HIGH);

        softly.assertAll();
    }

    @Override
    protected AbstractParser createParser() {
        return new DoxygenParser();
    }
}

