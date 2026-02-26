package org.v0x31.game;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.v0x31.engine.Shader;
import org.v0x31.engine.Texture;
import org.v0x31.engine.window.MouseState;
import org.v0x31.engine.window.Window;
import org.v0x31.engine.Camera;
import org.v0x31.engine.Cube;
import org.v0x31.engine.window.WindowState;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;

public class Main {
    private static final int SIZEOF_FLOAT = 4;

    static float deltaTime = 0.0f;
    static int vao = 0;
    static int vbo = 0;
    static int ebo = 0;

    public static void main(String[] args) {
        try (
            Window window = new Window("test", 800, 800);
            Texture texture = new Texture("textures/container.jpg");
            Shader shader = new Shader("shaders/vertex.glsl", "shaders/fragment.glsl");
        ) {
            Camera camera = new Camera();

            window.loadCallback = () -> {
                glEnable(GL_DEPTH_TEST);

                vao = glGenVertexArrays();
                vbo = glGenBuffers();
                ebo = glGenBuffers();

                // Copy the verticies and indicies data
                glBindVertexArray(vao);
                glBindBuffer(GL_ARRAY_BUFFER, vbo);
                glBufferData(GL_ARRAY_BUFFER, Cube.verticies, GL_STATIC_DRAW);
                glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
                glBufferData(GL_ELEMENT_ARRAY_BUFFER, Cube.indicies, GL_STATIC_DRAW);

                // Set position attribute
                glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * SIZEOF_FLOAT, 0);
                glEnableVertexAttribArray(0);
                // Set the texture coords attribute
                glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * SIZEOF_FLOAT, 3 * SIZEOF_FLOAT);
                glEnableVertexAttribArray(1);
            };
            window.windowEventCallback = (windowState) -> {
                if (windowState.event == WindowState.Event.resized) {
                    glViewport(0, 0, windowState.size.x, windowState.size.y);
                }
            };
            window.keyboardEventCallback = (keyboardState) -> {
                if (keyboardState.keys[GLFW_KEY_W]) camera.updatePosition(Camera.Movement.forward, window.getDeltaTime());
                if (keyboardState.keys[GLFW_KEY_S]) camera.updatePosition(Camera.Movement.backward, window.getDeltaTime());
                if (keyboardState.keys[GLFW_KEY_A]) camera.updatePosition(Camera.Movement.left, window.getDeltaTime());
                if (keyboardState.keys[GLFW_KEY_D]) camera.updatePosition(Camera.Movement.right, window.getDeltaTime());
            };
            window.mouseEventCallback = (mouseState) -> {
                switch (mouseState.event) {
                    case MouseState.Event.moved -> camera.updateYawPitch(new Vector2f((float)mouseState.position.x, (float)mouseState.position.y));
                    case MouseState.Event.wheel -> camera.updateZoom((float)mouseState.wheel.y);
                }
            };
            window.renderCallback = () -> {
                glClearColor(0.2f, 0.2f, 0.3f, 1.0f);
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

                texture.bind();
                shader.use();
                Matrix4f model = new Matrix4f();
                Matrix4f projection = new Matrix4f();
                //model.rotate((float)glfwGetTime() * (float)Math.toRadians(50.0f), 0.25f, 0.0f, 0.0f);
                projection.perspective((float)Math.toRadians(camera.getZoom()), 1, 0.1f, 100.0f);
                shader.setMat4("model", model);
                shader.setMat4("view", camera.getView());
                shader.setMat4("projection", projection);

                glBindVertexArray(vao);
                glDrawArrays(GL_TRIANGLES, 0, 36);
            };
            window.run();
        }

        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
        glDeleteBuffers(ebo);
    }
}