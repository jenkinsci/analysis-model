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
 * FIXME: write comment.
 *
 * @author Ullrich Hafner
 */
public class SerializableTest {
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

    protected Stream<String> readResourceToStream(final String name, final Charset charset) {
        try {
            return Files.lines(getPath(name), charset);
        }
        catch (IOException | URISyntaxException e) {
            throw new AssertionError("Can't read resource " + name, e);
        }
    }

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
