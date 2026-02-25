package org.v0x31.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.lwjgl.stb.STBImage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL33.*;

public class Texture implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(Texture.class);

    private final int id;

    public Texture(String path) {
        // Create the texture and set parameters
        this.id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, this.id);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        // Load the image
        ByteBuffer image = null;
        try {
            image = ResourceManager.readFileAsBytes(path);
        } catch (FileNotFoundException fileNotFoundException) {
            logger.error("The texture \"{}\" was not found", fileNotFoundException.getMessage());
        } catch (IOException ioException) {
            logger.error("IOException received : {}", ioException.getMessage());
        }

        // Parse the image
        int[] width = new int[1];
        int[] height = new int[1];
        int[] nChannels = new int[1];
        ByteBuffer pixels = STBImage.stbi_load_from_memory(image, width, height, nChannels, 0);
        if (pixels == null) {
            logger.error("Failed to load \"{}\"", path);
        }

        // Copy the image pixels to the GPU/texture
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width[0], height[0], 0, GL_RGB, GL_UNSIGNED_BYTE, pixels);
        glGenerateMipmap(GL_TEXTURE_2D);

        // Free the image pixels
        STBImage.stbi_image_free(pixels);

        logger.info("Loaded image \"{}\", {}x{}, {} channels", path, width[0], height[0], nChannels[0]);
    }

    @Override
    public void close() {
        glDeleteTextures(this.id);
    }

    public void bind() {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, this.id);
    }
}
