package org.v0x31;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import static org.lwjgl.opengl.GL33.*;

public class Shader {
    private final int id;

    public Shader(String vertexShaderPath, String fragmentShaderPath) {
        Logger logger = Logger.getLogger(Shader.class.getName());

        // Load the GLSL vertex and fragment shader source files.
        String vertexShaderSource = "";
        String fragmentShaderSource = "";
        try {
            vertexShaderSource = ResourceManager.readFileAsString(vertexShaderPath);
            fragmentShaderSource = ResourceManager.readFileAsString(fragmentShaderPath);
        } catch (FileNotFoundException fileNotFoundException) {
            logger.severe(String.format("The shader \"%s\" was not found", fileNotFoundException.getMessage()));
        } catch (IOException ioException) {
            logger.severe(String.format("IOException received : %s", ioException.getMessage()));
        }

        logger.info(String.format("Loaded vertex shader \"%s\"", vertexShaderPath));
        logger.info(String.format("Loaded fragment shader \"%s\"", fragmentShaderPath));

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
