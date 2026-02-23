package org.v0x31;

import org.lwjgl.opengl.*;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.logging.*;

import static org.lwjgl.opengl.GL33.*;

public class Shader {
    private final int id;

    public Shader(String vertexShaderPath, String fragmentShaderPath) {
        Logger logger = Logger.getLogger(Shader.class.getName());

        // Load the GLSL vertex and fragment shader source files.
        String vertexShaderSource = "";
        String fragmentShaderSource = "";
        try {
            Files.readString(getClass().getClassLoader().getResource(vertexShaderPath).toURI()) {
                vertexShaderSource = vertexShaderFile.
            }
            try (InputStream fragmentShaderStream = getClass().getClassLoader().getResourceAsStream(vertexShaderPath)) {
                fragmentShaderSource = fragmentShaderStream.readAllBytes().toString();
            }
        } catch (IOException ie) {
            logger.severe(String.format("Failed to read file \"%s\"", ie.getMessage()));
        } catch (URISyntaxException use) {
            logger.severe(use.getMessage());
        }
        logger.info(String.format("Loaded vertex shader \"%s\"", vertexShaderPath));
        logger.info(String.format("Loaded fragment shader \"%s\"", fragmentShaderPath));

        System.out.println(vertexShaderSource);

        int[] success = new int[1];

        // Compile the vertex shader source.
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertexShaderSource);
        glCompileShader(vertexShader);
        glGetShaderiv(vertexShader, GL_COMPILE_STATUS, success);
        if (success[0] != GL_TRUE) {
            logger.severe(String.format("Failed to load vertex shader \"%s\"\n%s", vertexShaderPath, glGetShaderInfoLog(vertexShader)));
        }

        // Compile the fragment shader source.
        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fragmentShaderSource);
        glCompileShader(fragmentShader);
        glGetShaderiv(fragmentShader, GL_COMPILE_STATUS, success);
        if (success[0] != GL_TRUE) {
            logger.severe(String.format("Failed to load fragment shader \"%s\"\n%s", fragmentShaderPath, glGetShaderInfoLog(fragmentShader)));
        }

        // Link the compiled results of the shaders into a program
        this.id = glCreateProgram();
        glAttachShader(this.id, vertexShader);
        glAttachShader(this.id, fragmentShader);
        glLinkProgram(this.id);
        glGetProgramiv(this.id, GL_LINK_STATUS, success);
        if (success[0] != GL_TRUE) {
            logger.severe(glGetShaderInfoLog(this.id));
        }

        logger.info("Shader compiled successfully");

        // Cleanup
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }

    public void use() {
        glUseProgram(this.id);
    }

    public void setBoolean(String name, boolean value) {
        glUniform1i(glGetUniformLocation(this.id, name), (value ? 1 : 0));
    }

    public void setInt(String name, int value) {
        glUniform1i(glGetUniformLocation(this.id, name), value);
    }

    public void setFloat(String name, float value) {
        glUniform1f(glGetUniformLocation(this.id, name), value);
    }

    public boolean getBoolean(String name) {
        return glGetUniformi(this.id, glGetUniformLocation(this.id, name)) == 1;
    }

    public int getInt(String name) {
        return glGetUniformi(this.id, glGetUniformLocation(this.id, name));
    }

    public float getFloat(String name) {
        return glGetUniformf(this.id, glGetUniformLocation(this.id, name));
    }
}
