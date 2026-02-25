package org.v0x31;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.lwjgl.BufferUtils;
import org.joml.Matrix4f;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL33.*;

public class Shader implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(Shader.class);

    private final String vertexShaderPath;
    private final String fragmentShaderPath;
    private int id;

    public Shader(String vertexShaderPath, String fragmentShaderPath) {
        this.vertexShaderPath = vertexShaderPath;
        this.fragmentShaderPath = fragmentShaderPath;
        this.recompile();
    }

    @Override
    public void close() {
        glDeleteProgram(this.id);
    }

    public void recompile() {
        // Free the old program
        glDeleteProgram(this.id);

        // Compile and link the vertex and fragment shader in a program
        int vertexShader = this.compileShader(this.vertexShaderPath, GL_VERTEX_SHADER);
        int fragmentShader = this.compileShader(this.fragmentShaderPath, GL_FRAGMENT_SHADER);
        this.id = this.linkProgram(vertexShader, fragmentShader);

        // Cleanup
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }

    private int compileShader(String path, int type) {
        // Load the GLSL vertex and fragment shader source files
        String shaderSource = "";
        try {
            shaderSource = ResourceManager.readFileAsString(path);
        } catch (FileNotFoundException fileNotFoundException) {
            logger.error("The shader \"{}\" was not found", fileNotFoundException.getMessage());
        } catch (IOException ioException) {
            logger.info("IOException received : {}", ioException.getMessage());
        }

        // Compile the shader source
        int shader = glCreateShader(type);
        glShaderSource(shader, shaderSource);
        glCompileShader(shader);

        // Check for errors
        int[] success = new int[1];
        glGetShaderiv(shader, GL_COMPILE_STATUS, success);
        if (success[0] != GL_TRUE) {
            logger.error("Failed to load fragment shader \"{}\"\n{}", path, glGetShaderInfoLog(shader));
        } else {
            logger.info("Compiled shader \"{}\" successfully", path);
        }

        return shader;
    }

    private int linkProgram(int vertexShader, int fragmentShader) {
        // Link the compiled results of the shaders into a program
        int program = glCreateProgram();
        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);
        glLinkProgram(program);

        // Check for errors
        int[] success = new int[1];
        glGetProgramiv(program, GL_LINK_STATUS, success);
        if (success[0] != GL_TRUE) {
            logger.error("Failed to link shader program\n{}", glGetShaderInfoLog(program));
        } else {
            logger.info("Linked shader program successfully");
        }

        return program;
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

    public void setMat4(String name, Matrix4f matrix) {
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(16);
        matrix.get(floatBuffer);
        glUniformMatrix4fv(glGetUniformLocation(this.id, name), false, floatBuffer);
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
