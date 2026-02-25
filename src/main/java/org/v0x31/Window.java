package org.v0x31;

import org.lwjgl.glfw.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(Window.class);

    private final long pointer;

    public Window(String title, int width, int height) {
        // GLFW error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // GLFW initialisation & configuration
        if (!glfwInit()) {
            logger.error("Failed to initialise GLFW");
        } else {
            logger.info("Initialised GLFW successfully");
        }
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        // Create the window
        this.pointer = glfwCreateWindow(width, height, title, NULL, NULL);
        if (this.pointer == NULL) {
            logger.error("Failed to create GLFW window");
        } else {
            logger.info("Created window \"{}\", {}x{}", title, width, height);
        }

        // Enable V-Sync and show the window
        glfwMakeContextCurrent(this.pointer);
        glfwSwapInterval(1);
        glfwShowWindow(this.pointer);

        // Initialise OpenGL
        GL.createCapabilities();
    }

    @Override
    public void close() {
        glfwFreeCallbacks(this.pointer);
        glfwDestroyWindow(this.pointer);
        glfwTerminate();
    }

    public void update() {
        glfwSwapBuffers(this.pointer);
        glfwPollEvents();
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(this.pointer);
    }

    public void setWindowResizeCallback(GLFWFramebufferSizeCallbackI callback) {
        glfwSetFramebufferSizeCallback(this.pointer, callback);
    }

    public void setWindowFocusCallback(GLFWWindowFocusCallbackI callback) {
        glfwSetWindowFocusCallback(this.pointer, callback);
    }

    public void setKeyCallback(GLFWKeyCallbackI callback) {
        glfwSetKeyCallback(this.pointer, callback);
    }

    public void setMouseButtonCallback(GLFWMouseButtonCallbackI callback) {
        glfwSetMouseButtonCallback(this.pointer, callback);
    }

    public void setMouseMovementCallback(GLFWCursorPosCallbackI callback) {
        glfwSetCursorPosCallback(this.pointer, callback);
    }

    public void setMouseWheelCallback(GLFWScrollCallbackI callback) {
        glfwSetScrollCallback(this.pointer, callback);
    }
}