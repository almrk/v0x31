package org.v0x31;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.glfw.ImGuiImplGlfw;
import imgui.gl3.ImGuiImplGl3;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window implements AutoCloseable {
    private final long windowPtr;
    private final ImGuiImplGlfw imGuiGlfw;
    private final ImGuiImplGl3 imGuiGl;

    public Window(String title, int width, int height) {
        // GLFW error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // GLFW initialisation & configuration
        if (!glfwInit())
            throw new IllegalStateException("Failed to initialise GLFW");
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        // Create the window
        this.windowPtr = glfwCreateWindow(width, height, title, NULL, NULL);
        if (this.windowPtr == NULL)
            throw new RuntimeException("Failed to create GLFW window");

        glfwSetFramebufferSizeCallback(this.windowPtr, (wp, w, h) -> {
            glViewport(0, 0, w, h);
        });

        // Enable V-Sync and show the window
        glfwMakeContextCurrent(this.windowPtr);
        glfwSwapInterval(1);
        glfwShowWindow(this.windowPtr);

        // Initialise OpenGL
        GL.createCapabilities();

        // Initialise ImGui
        ImGui.createContext();

        ImGuiIO io = ImGui.getIO();
        io.setIniFilename(null);
        io.setFontGlobalScale(1.5f);

        this.imGuiGlfw = new ImGuiImplGlfw();
        this.imGuiGlfw.init(this.windowPtr, true);
        this.imGuiGl = new ImGuiImplGl3();
        this.imGuiGl.init("#version 330");
    }

    @Override
    public void close() {
        // Cleanup ImGui
        this.imGuiGl.shutdown();
        this.imGuiGlfw.shutdown();
        ImGui.destroyContext();

        // Cleanup GLFW
        glfwFreeCallbacks(this.windowPtr);
        glfwDestroyWindow(this.windowPtr);
        glfwTerminate();
    }

    public boolean isOpen() {
        return !glfwWindowShouldClose(this.windowPtr);
    }

    public void update() {
        glfwSwapBuffers(this.windowPtr);
        glfwPollEvents();
    }

    public void beginImGui() {
        this.imGuiGl.newFrame();
        this.imGuiGlfw.newFrame();
        ImGui.newFrame();
    }

    public void endImGui() {
        ImGui.render();
        this.imGuiGl.renderDrawData(ImGui.getDrawData());
    }
}