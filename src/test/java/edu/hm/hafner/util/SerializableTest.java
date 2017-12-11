package edu.hm.hafner.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Base class to test the serialization of instances of {@link Serializable}.
 *
 * @author Ullrich Hafner
 */
public class SerializableTest {
    /**
     * Reads the contents of the desired resource. The rules for searching resources associated with this test class are
     * implemented by the defining {@linkplain ClassLoader class loader} of this test class.  This method delegates to
     * this object's class loader.  If this object was loaded by the bootstrap class loader, the method delegates to
     * {@link ClassLoader#getSystemResource}.
     * <p> Before delegation, an absolute resource name is constructed from the given resource name using this
     * algorithm:
     * </p>
     * <ul>
     * <li> If the {@code name} begins with a {@code '/'} (<tt>'&#92;u002f'</tt>), then the absolute name of the
     * resource is the portion of the {@code name} following the {@code '/'}.</li>
     * <li> Otherwise, the absolute name is of the following form:
     * <blockquote> {@code modified_package_name/name} </blockquote>
     * <p> Where the {@code modified_package_name} is the package name of this object with {@code '/'} substituted for
     * {@code '.'} (<tt>'&#92;u002e'</tt>).</li>
     * </ul>
     *
     * @param name
     *         name of the desired resource
     *
     * @return the content represented by a byte array
     */
    protected byte[] readResource(final String name) {
        try {
            return Files.readAllBytes(getPath(name));
        }
        catch (IOException | URISyntaxException e) {
            throw new AssertionError("Can't read resource " + name, e);
        }
    }

    private Path getPath(final String name) throws URISyntaxException {
        URL resource = getClass().getResource(name);
        if (resource == null) {
            throw new AssertionError("Can't find resource " + name);
        }
        return Paths.get(resource.toURI());
    }

    /**
     * Read all lines from the desired resource as a {@code Stream}, i.e. this method populates lazily as the stream is
     * consumed.
     * <p> Bytes from the resource are decoded into characters using the specified charset and the same line terminators
     * as specified by {@link Files#readAllLines(Path, Charset)} are supported.</p>
     *
     * @param name
     *         name of the desired resource
     * @param charset
     *         the charset to use for decoding
     *
     * @return the content represented by a byte array
     */
    protected Stream<String> readResourceToStream(final String name, final Charset charset) {
        try {
            return Files.lines(getPath(name), charset);
        }
        catch (IOException | URISyntaxException e) {
            throw new AssertionError("Can't read resource " + name, e);
        }
    }

    /**
     * Write the specified object to a byte array using an {@link ObjectOutputStream}. The class of the object, the
     * signature of the class, and the values of the non-transient and non-static fields of the class and all of its
     * supertypes are written. Objects referenced by this object are written transitively so that a complete equivalent
     * graph of objects can be reconstructed by an ObjectInputStream.
     *
     * @param object
     *         the object to serialize
     *
     * @return the object serialization
     */
    protected byte[] toByteArray(final Serializable object) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (ObjectOutputStream stream = new ObjectOutputStream(out)) {
            stream.writeObject(object);
        }
        catch (IOException exception) {
            throw new IllegalStateException("Can't serialize object " + object, exception);
        }
        return out.toByteArray();
    }
}
