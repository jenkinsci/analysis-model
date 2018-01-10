package edu.hm.hafner.analysis;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 *  Tests the class {@link JavaPackageDetector}.
 */
class JavaPackageDetectorTest {
    /** The classifier under test. */
    private final JavaPackageDetector classifier = new JavaPackageDetector();

    /**
     * Checks whether we could identify a java package name.
     *
     * @throws IOException
     *             in case of an error
     */
    @Test
    public void checkPackage() {
        InputStream stream;
        stream = JavaPackageDetectorTest.class.getResourceAsStream("MavenJavaTest.txt");
        String packageName = classifier.detectPackageName(stream);

        try {
            assertThat(classifier.detectPackageName(stream)).isEqualTo("hudson.plugins.tasks.util");
        }
        finally {
            IOUtils.closeQuietly(stream);
        }
    }

    /**
     * Checks whether we do not detect a namespace in a text file.
     *
     * @throws IOException
     *             in case of an error
     */
    @Test
    public void checkEmptyPackageName() {
        String fileName = "pom.xml";
        InputStream stream = JavaPackageDetectorTest.class.getResourceAsStream(fileName);

        try {
            assertThat(classifier.detectPackageName(stream)).isEqualTo("-");
        }
        finally {
            IOUtils.closeQuietly(stream);
        }
    }

    /**
     * Checks whether we detect package names that use some different formatting.
     *
     * @throws IOException
     *             in case of an error
     */
    @Test
    public void checkComplicatedPackageName() {
        String fileName = "complicated-package-declaration.txt";
        InputStream stream = JavaPackageDetectorTest.class.getResourceAsStream(fileName);

        try {
            assertThat(classifier.detectPackageName(stream)).isEqualTo("hudson.plugins.findbugs.util");
        }
        finally {
            IOUtils.closeQuietly(stream);
        }
    }

    /**
     * Checks whether we correctly accept C# files.
     */
    @Test
    public void testFileSuffix() {
        assertThat(classifier.accepts("Action.java")).as("Does not accept a Java file.").isTrue();
        assertThat(classifier.accepts("ActionBinding.cs")).as("Accepts a non-Java file.").isFalse();
        assertThat(classifier.accepts("pom.xml")).as("Accepts a non-C# file.").isFalse();
    }
}
