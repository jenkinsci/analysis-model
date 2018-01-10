package edu.hm.hafner.analysis;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link CsharpNamespaceDetector}.
 */
class CsharpNamespaceDetectorTest {
    /** The classifier under test. */
    private final CsharpNamespaceDetector classifier = new CsharpNamespaceDetector();

    /**
     * Checks whether we correctly detect the namespace of the specified file.
     */
    @Test
    public void checkClassificationJavaFormatting() {
        String fileName = "ActionBinding.cs";
        InputStream stream = CsharpNamespaceDetectorTest.class.getResourceAsStream(fileName);
        try {
            assertThat(classifier.detectPackageName(stream)).isEqualTo("Avaloq.SmartClient.Utilities");
        }
        finally {
            IOUtils.closeQuietly(stream);
        }
    }

    /**
     * Checks whether we correctly detect the namespace of the specified file.
     *
     * @throws IOException
     *             in case of an error
     */
    @Test
    public void checkClassificationOriginalFormatting() {
        String fileName = "ActionBinding-Original-Formatting.cs";
        InputStream stream = CsharpNamespaceDetectorTest.class.getResourceAsStream(fileName);

        try {
            assertThat(classifier.detectPackageName(stream)).isEqualTo("Avaloq.SmartClient.Utilities");
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
        InputStream stream = CsharpNamespaceDetectorTest.class.getResourceAsStream(fileName);

        try {
            assertThat(classifier.detectPackageName(stream)).isEqualTo("-");
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
        assertThat(classifier.accepts("ActionBinding.cs")).as("Does not accept a C# file.").isTrue();
        assertThat(classifier.accepts("ActionBinding.cs.c")).as("Accepts a non-C# file.").isFalse();
        assertThat(classifier.accepts("Action.java")).as("Accepts a non-C# file.").isFalse();
        assertThat(classifier.accepts("pom.xml")).as("Accepts a non-C# file.").isFalse();
    }
}

