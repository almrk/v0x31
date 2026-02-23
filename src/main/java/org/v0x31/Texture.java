package org.v0x31;

import org.lwjgl.opengl.*;
import org.lwjgl.stb.*;
import java.nio.*;
import java.util.logging.*;

import static org.lwjgl.opengl.GL33.*;

public class Texture {
    private final int id;

    public Texture(String path) {
        Logger logger = Logger.getLogger(Texture.class.getName());

        // Create the texture and set parameters
        this.id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, this.id);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        // Load the image
        int[] width = new int[1];
        int[] height = new int[1];
        int[] nChannels = new int[1];
        ByteBuffer data = STBImage.stbi_load(path, width, height, nChannels, 0);
        if (data == null)
            logger.severe(String.format("Failed to load \"%s\"", path));

        // Copy the image pixels to the gpu/texture
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width[0], height[0], 0, GL_RGB, GL_UNSIGNED_BYTE, data);
        glGenerateMipmap(GL_TEXTURE_2D);

        // Free the image pixels
        STBImage.stbi_image_free(data);

        logger.info(String.format("Loaded image \"%s\", %dx%d, %d channels", path, width[0], height[0], nChannels[0]));
    }

    public void bind() {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, this.id);
    }
}
