package org.v0x31;

import java.io.InputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;

public class ResourceManager {
    public static String readFileAsString(String path) throws IOException {
        // Open an InputStream for the resource file specified in the path
        try (InputStream inStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
            if (inStream == null) {
                throw new FileNotFoundException(path);
            }
            // Create string from the bytes of the resource file
            return new String(inStream.readAllBytes());
        }
    }

    public static ByteBuffer readFileAsBytes(String path) throws IOException {
        // Open an InputStream for the resource file specified in the path
        try (InputStream inStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
            if (inStream == null) {
                throw new FileNotFoundException(path);
            }
            // Convert the bytes from the resource file to a direct ByteBuffer for LWJGL
            byte[] bytes = inStream.readAllBytes();
            ByteBuffer buffer = ByteBuffer.allocateDirect(bytes.length);
            buffer.put(bytes);
            buffer.flip();
            return buffer;
        }
    }
}
