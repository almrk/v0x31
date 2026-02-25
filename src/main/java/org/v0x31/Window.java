package org.v0x31;

import org.lwjgl.glfw.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(Window.class);

    private long windowPointer;
    private float windowLastFrame;

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
        this.windowPointer = glfwCreateWindow(width, height, title, NULL, NULL);
        if (this.windowPointer == NULL) {
            logger.error("Failed to create GLFW window");
        } else {
            logger.info("Created window \"{}\", {}x{}", title, width, height);
        }

        // Enable V-Sync and show the window
        glfwMakeContextCurrent(this.windowPointer);
        glfwSwapInterval(1);
        glfwSetInputMode(this.windowPointer, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        glfwShowWindow(this.windowPointer);

        // Initialise OpenGL
        GL.createCapabilities();
    }

    @Override
    public void close() {
        glfwFreeCallbacks(this.windowPointer);
        glfwDestroyWindow(this.windowPointer);
        glfwTerminate();
    }

    public void update() {
        this.windowLastFrame = (float)glfwGetTime();

        glfwSwapBuffers(this.windowPointer);
        glfwPollEvents();
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(this.windowPointer);
    }

    public float deltaTime() {
        return (float)glfwGetTime() - this.windowLastFrame;
    }

    public void setWindowResizeCallback(GLFWFramebufferSizeCallbackI callback) {
        glfwSetFramebufferSizeCallback(this.windowPointer, callback);
    }

    public void setWindowFocusCallback(GLFWWindowFocusCallbackI callback) {
        glfwSetWindowFocusCallback(this.windowPointer, callback);
    }

    public void setKeyCallback(GLFWKeyCallbackI callback) {
        glfwSetKeyCallback(this.windowPointer, callback);
    }

    public void setMouseButtonCallback(GLFWMouseButtonCallbackI callback) {
        glfwSetMouseButtonCallback(this.windowPointer, callback);
    }

    public void setMouseMovementCallback(GLFWCursorPosCallbackI callback) {
        glfwSetCursorPosCallback(this.windowPointer, callback);
    }

    public void setMouseWheelCallback(GLFWScrollCallbackI callback) {
        glfwSetScrollCallback(this.windowPointer, callback);
    }
}