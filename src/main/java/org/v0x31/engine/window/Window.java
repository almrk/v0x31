package org.v0x31.engine.window;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.lwjgl.opengl.GL;
import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(Window.class);

    // GLFW window pointer
    private long windowPointer;
    // State
    private WindowState windowState;
    private KeyboardState keyboardState;
    private MouseState mouseState;
    private float lastFrameTime;
    private float deltaTime;
    // Callbacks
    public Callbacks.Load loadCallback;
    public Callbacks.Render renderCallback;
    public Callbacks.WindowEvent windowEventCallback;
    public Callbacks.KeyboardEvent keyboardEventCallback;
    public Callbacks.MouseEvent mouseEventCallback;

    public Window(String title, int width, int height) {
        windowState = new WindowState();
        keyboardState = new KeyboardState();
        mouseState = new MouseState();

        loadCallback = null;
        renderCallback = null;
        windowEventCallback = null;
        keyboardEventCallback = null;
        mouseEventCallback = null;

        GLFWErrorCallback.createPrint(System.err).set();

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

        this.windowPointer = glfwCreateWindow(width, height, title, NULL, NULL);
        if (this.windowPointer == NULL) {
            logger.error("Failed to create GLFW window");
        } else {
            logger.info("Created window \"{}\", {}x{}", title, width, height);
        }

        setGlfwWindowEventCallbacks();
        setGlfwKeyboardEventCallbacks();
        setGlfwMouseEventCallbacks();

        glfwMakeContextCurrent(this.windowPointer);
        glfwSwapInterval(1);
        glfwSetInputMode(this.windowPointer, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        glfwShowWindow(this.windowPointer);

        GL.createCapabilities();
    }

    @Override
    public void close() {
        glfwFreeCallbacks(this.windowPointer);
        glfwDestroyWindow(this.windowPointer);
        glfwTerminate();
    }

    public void run() {
        if (this.loadCallback != null) {
            this.loadCallback.invoke();
        }

        while (!glfwWindowShouldClose(this.windowPointer)) {
            float currentTime = (float)glfwGetTime();
            this.deltaTime = currentTime - this.lastFrameTime;
            this.lastFrameTime = currentTime;

            if (this.renderCallback != null) {
                this.renderCallback.invoke();
            }

            glfwSwapBuffers(this.windowPointer);
            glfwPollEvents();
        }
    }

    public float getLastFrameTime() {
        return this.lastFrameTime;
    }

    public float getDeltaTime() {
        return this.deltaTime;
    }

    private void setGlfwWindowEventCallbacks() {
        glfwSetFramebufferSizeCallback(this.windowPointer, (_, w, h) -> {
            if (this.windowEventCallback == null) {
                return;
            }
            this.windowState.size.x = w;
            this.windowState.size.y = h;
            this.windowEventCallback.invoke(this.windowState);
        });
        glfwSetWindowPosCallback(this.windowPointer, (_, x, y) -> {
            if (this.windowEventCallback == null) {
                return;
            }
            this.windowState.position.x = x;
            this.windowState.position.y = y;
            this.windowEventCallback.invoke(this.windowState);
        });
        glfwSetWindowFocusCallback(this.windowPointer, (_, isFocused) -> {
            if (this.windowEventCallback == null) {
                return;
            }
            this.windowState.event = isFocused ? WindowState.Event.focusGained : WindowState.Event.focusLost;
            this.windowEventCallback.invoke(this.windowState);
        });
    }

    private void setGlfwKeyboardEventCallbacks() {
        glfwSetKeyCallback(this.windowPointer, (_, key, _, action, _) -> {
            if (this.keyboardEventCallback == null) {
                return;
            }
            if (key >= 0 && key <= GLFW_KEY_LAST) {
                this.keyboardState.keys[key] = action != GLFW_RELEASE;
                this.keyboardEventCallback.invoke(this.keyboardState);
            }
        });
    }

    private void setGlfwMouseEventCallbacks() {
        glfwSetCursorPosCallback(this.windowPointer, (_, x, y) -> {
            if (this.mouseEventCallback == null) {
                return;
            }
            this.mouseState.event = MouseState.Event.moved;
            this.mouseState.position.x = (float)x;
            this.mouseState.position.y = (float)y;
            this.mouseEventCallback.invoke(this.mouseState);
        });
        glfwSetMouseButtonCallback(this.windowPointer, (_, button, action, _) -> {
            if (this.mouseEventCallback == null) {
                return;
            }
            switch (action) {
                case GLFW_PRESS -> this.mouseState.event = MouseState.Event.buttonDown;
                case GLFW_RELEASE -> this.mouseState.event = MouseState.Event.buttonUp;
            }
            switch (button) {
                case GLFW_MOUSE_BUTTON_LEFT -> this.mouseState.button = MouseState.Button.left;
                case GLFW_MOUSE_BUTTON_MIDDLE -> this.mouseState.button = MouseState.Button.middle;
                case GLFW_MOUSE_BUTTON_RIGHT -> this.mouseState.button = MouseState.Button.right;
            }

            this.mouseEventCallback.invoke(this.mouseState);
        });
        glfwSetScrollCallback(this.windowPointer, (_, x, y) -> {
            if (this.mouseEventCallback == null) {
                return;
            }
            this.mouseState.wheel.x = (float)x;
            this.mouseState.wheel.y = (float)y;
            this.mouseEventCallback.invoke(this.mouseState);
        });
    }
}