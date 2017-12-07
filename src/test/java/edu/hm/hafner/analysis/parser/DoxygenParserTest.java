package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Ignore;
import org.junit.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

/**
 * Tests the class {@link DoxygenParser}.
 */
public class DoxygenParserTest extends ParserTester {
    private static final String WARNING_TYPE = new DoxygenParser().getId();
    private static final String WARNING_CATEGORY = DEFAULT_CATEGORY;
    private static final String NO_FILE_NAME = "-";

    /**
     * Parses a file with Doxygen warnings.
     *
     * @throws IOException if the file could not be read
     */
    @SuppressWarnings("PMD.ExcessiveMethodLength")
    @Test
    public void testWarningsParser() throws IOException {
        Issues warnings = new DoxygenParser().parse(openFile());

        assertEquals(22, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        checkWarning(iterator.next(),
                0,
                "Output directory `doc/doxygen/framework' does not exist. I have created it for you.",
                NO_FILE_NAME,
                WARNING_TYPE, WARNING_CATEGORY, Priority.LOW);
        checkWarning(iterator.next(),
                171,
                "reached end of file while inside a dot block!\nThe command that should end the block seems to be missing!",
                "/home/user/myproject/component/odesolver/CentralDifferenceSolver.cpp",
                WARNING_TYPE, WARNING_CATEGORY, Priority.NORMAL);
        checkWarning(iterator.next(),
                479,
                "the name `lcp_lexicolemke.c' supplied as the second argument in the \\file statement is not an input file",
                "/home/user/myproject/helper/LCPcalc.cpp",
                WARNING_TYPE, WARNING_CATEGORY, Priority.NORMAL);
        checkWarning(iterator.next(),
                65,
                "documented function `sofa::core::componentmodel::behavior::BaseController::BaseController' was not declared or defined.",
                "/home/user/myproject/core/componentmodel/behavior/BaseController.cpp",
                WARNING_TYPE, WARNING_CATEGORY, Priority.NORMAL);
        checkWarning(iterator.next(),
                72,
                "no matching class member found for\n  void sofa::core::componentmodel::behavior::BaseController::handleEvent(core::objectmodel::Event *event)",
                "/home/user/myproject/core/componentmodel/behavior/BaseController.cpp",
                WARNING_TYPE, WARNING_CATEGORY, Priority.NORMAL);
        checkWarning(iterator.next(),
                699,
                "no uniquely matching class member found for\n  template <>\n  const char * sofa::defaulttype::Rigid3dTypes::Name()",
                "/home/user/myproject/defaulttype/RigidTypes.h",
                WARNING_TYPE, WARNING_CATEGORY, Priority.NORMAL);
        checkWarning(iterator.next(),
                1351,
                "no matching file member found for \ndefaulttype::RigidDeriv< 3, double > sofa::core::componentmodel::behavior::inertiaForce< defaulttype::RigidCoord< 3, double >, defaulttype::RigidDeriv< 3, double >, objectmodel::BaseContext::Vec3, defaulttype::RigidMass< 3, double >, objectmodel::BaseContext::SpatialVector >(const sofa::defaulttype::SolidTypes::SpatialVector &vframe, const objectmodel::BaseContext::Vec3 &aframe, const defaulttype::RigidMass< 3, double > &mass, const defaulttype::RigidCoord< 3, double > &x, const defaulttype::RigidDeriv< 3, double > &v)\nPossible candidates:\n  Deriv inertiaForce(const SV &, const Vec &, const M &, const Coord &, const Deriv &)",
                "/home/user/myproject/defaulttype/RigidTypes.h",
                WARNING_TYPE, WARNING_CATEGORY, Priority.NORMAL);
        checkWarning(iterator.next(),
                569,
                "no uniquely matching class member found for\n  template < R >\n  SolidTypes< R >::Vec sofa::defaulttype::SolidTypes< R >::mult(const typename sofa::defaulttype::Mat< 3, 3, Real > &m, const typename SolidTypes< R >::Vec &v)\nPossible candidates:\n  static Vec sofa::defaulttype::SolidTypes< R >::mult(const Mat &m, const Vec &v) at line 404 of file /home/user/myproject/defaulttype/SolidTypes.h",
                "/home/user/myproject/defaulttype/SolidTypes.inl",
                WARNING_TYPE, WARNING_CATEGORY, Priority.NORMAL);
        checkWarning(iterator.next(),
                227,
                "no uniquely matching class member found for\n  template < Real >\n  DualQuat< Real >::Vec sofa::helper::DualQuat< Real >::transform(const typename sofa::defaulttype::Vec< 3, Real > &vec)\nPossible candidates:\n  Vec sofa::helper::DualQuat< Real >::transform(const Vec &vec) at line 73 of file /home/user/myproject/helper/DualQuat.h",
                "/home/user/myproject/helper/DualQuat.inl",
                WARNING_TYPE, WARNING_CATEGORY, Priority.NORMAL);
        checkWarning(iterator.next(),
                496,
                "no matching file member found for \nvoid sofa::helper::lcp_lexicolemke(int *nn, double *vec, double *q, double *zlem, double *wlem, int *info, int *iparamLCP, double *dparamLCP)\nPossible candidates:\n  int lcp_lexicolemke(int dim, double *q, double **M, double *res)\n  int lcp_lexicolemke(int dim, double *q, double **M, double **A, double *res)",
                "/home/user/myproject/helper/LCPcalc.cpp",
                WARNING_TYPE, WARNING_CATEGORY, Priority.NORMAL);
        checkWarning(iterator.next(),
                163,
                "Found unknown command `\\notify'",
                "/home/user/myproject/core/componentmodel/topology/BaseTopology.h",
                WARNING_TYPE, WARNING_CATEGORY, Priority.NORMAL);
        checkWarning(iterator.next(),
                172,
                "argument 'sv' of command @param is not found in the argument list of sofa::core::componentmodel::behavior::inertiaForce(const SV &, const Vec &, const M &, const Coord &, const Deriv &)",
                "/home/user/myproject/core/componentmodel/behavior/Mass.h",
                WARNING_TYPE, WARNING_CATEGORY, Priority.NORMAL);
        checkWarning(iterator.next(),
                97,
                "The following parameters of sofa::core::componentmodel::behavior::BaseForceField::addMBKdx(double mFactor, double bFactor, double kFactor) are not documented:\n  parameter 'mFactor'\n  parameter 'bFactor'\n  parameter 'kFactor'",
                "/home/user/myproject/core/componentmodel/behavior/BaseForceField.h",
                WARNING_TYPE, WARNING_CATEGORY, Priority.NORMAL);
        checkWarning(iterator.next(),
                104,
                "The following parameters of sofa::core::componentmodel::behavior::BaseLMConstraint::ConstraintGroup::addConstraint(unsigned int i0, SReal c) are not documented:\n  parameter 'i0'",
                "/home/user/myproject/core/componentmodel/behavior/BaseLMConstraint.h",
                WARNING_TYPE, WARNING_CATEGORY, Priority.NORMAL);
        checkWarning(iterator.next(),
                98,
                "explicit link request to 'index' could not be resolved",
                "/home/user/myproject/core/componentmodel/behavior/BaseMass.h",
                WARNING_TYPE, WARNING_CATEGORY, Priority.NORMAL);
        checkWarning(iterator.next(),
                0, // Actually 1 in the file, but the line number of this kind of messages is irrelevant
                "Detected potential recursive class relation between class sofa::core::componentmodel::collision::Contact::Factory and base class Factory< std::string, Contact, std::pair< std::pair< core::CollisionModel *, core::CollisionModel * >, Intersection * > >!",
                NO_FILE_NAME,
                WARNING_TYPE, WARNING_CATEGORY, Priority.HIGH);
        checkWarning(iterator.next(),
                96,
                "Found unknown command `\\TODO'",
                "/home/user/myproject/core/componentmodel/behavior/OdeSolver.h",
                WARNING_TYPE, WARNING_CATEGORY, Priority.NORMAL);
        checkWarning(iterator.next(),
                0, // Actually -1 in the file, but the line number of this kind of messages is irrelevant
                "Found unknown command `\\TODO'",
                NO_FILE_NAME,
                WARNING_TYPE, WARNING_CATEGORY, Priority.NORMAL);
        checkWarning(iterator.next(),
                0, // Actually 1 in the file, but the line number of this kind of messages is irrelevant
                "Found unknown command `\\TODO'",
                NO_FILE_NAME,
                WARNING_TYPE, WARNING_CATEGORY, Priority.NORMAL);
        checkWarning(iterator.next(),
                19,
                "Unexpected character `\"'",
                "/home/user/myproject/helper/SimpleTimer.h",
                WARNING_TYPE, WARNING_CATEGORY, Priority.HIGH);
        checkWarning(iterator.next(),
                0, // Actually 1 in the file, but the line number of this kind of messages is irrelevant
                "The following parameters of sofa::component::odesolver::EulerKaapiSolver::v_peq(VecId v, VecId a, double f) are not documented:\n  parameter 'v'\n  parameter 'a'",
                NO_FILE_NAME,
                WARNING_TYPE, WARNING_CATEGORY, Priority.NORMAL);
        checkWarning(iterator.next(),
                0,
                "Could not read image `/home/user/myproject/html/struct_foo_graph.png' generated by dot!",
                NO_FILE_NAME,
                WARNING_TYPE, WARNING_CATEGORY, Priority.HIGH);
    }

    /**
     * Verifies that parsing of long files does not fail.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-7178">Issue 7178</a>
     * @see <a href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6882582">JDK Bug 6882582</a>
     */
    @Test
    @Ignore("FIXME: Check with Java 8")
    public void issue7178() throws IOException {
        Issues warnings = new DoxygenParser().parse(openFile("issue7178.txt"));

        assertEquals(0, warnings.size());
    }

    /**
     * Parses a warning log with 4 doxygen 1.7.1 messages.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-6971">Issue 6971</a>
     */
    @Test
    public void issue6971() throws IOException {
        Issues warnings = new DoxygenParser().parse(openFile("issue6971.txt"));

        assertEquals(4, warnings.size());
        Iterator<Issue> iterator = warnings.iterator();
        checkWarning(iterator.next(),
                479,
                "the name `lcp_lexicolemke.c' supplied as the second argument in the \\file statement is not an input file",
                "/home/user/myproject/helper/LCPcalc.cpp",
                WARNING_TYPE, WARNING_CATEGORY, Priority.NORMAL);
        checkWarning(iterator.next(),
                19,
                "Unexpected character `\"'",
                "/home/user/myproject/helper/SimpleTimer.h",
                WARNING_TYPE, WARNING_CATEGORY, Priority.HIGH);
        checkWarning(iterator.next(),
                357,
                "Member getInternalParser() (function) of class XmlParser is not documented.",
                ".../XmlParser.h",
                WARNING_TYPE, WARNING_CATEGORY, Priority.NORMAL);
        checkWarning(iterator.next(),
                39,
                "Member XmlMemoryEntityMapEntry (typedef) of class XmlMemoryEntityResolver is not documented.",
                "P:/Integration/DjRip/djrip/workspace/libraries/xml/XmlMemoryEntityResolver.h",
                WARNING_TYPE, WARNING_CATEGORY, Priority.NORMAL);
    }


    @Override
    protected String getWarningsFile() {
        return "doxygen.txt";
    }
}

