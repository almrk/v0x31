package org.v0x31.game;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.v0x31.engine.Shader;
import org.v0x31.engine.Texture;
import org.v0x31.engine.Window;
import org.v0x31.engine.Camera;
import org.v0x31.engine.CameraMovement;
import org.v0x31.engine.Cube;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;

public class Main {
    private static final int SIZEOF_FLOAT = 4;

    public static void main(String[] args) {
        int vao = 0;
        int vbo = 0;
        int ebo = 0;

        try (
                Window window = new Window("v0x31", 800, 800);
                Texture texture = new Texture("textures/container.jpg");
                Shader shader = new Shader("shaders/vertex.glsl", "shaders/fragment.glsl");
        ) {
            Camera camera = new Camera();

            window.setWindowResizeCallback((_, width, height) -> {
                glViewport(0, 0, width, height);
            });
            window.setKeyCallback((_, key, scancode, action, mods) -> {
                switch (key) {
                    case GLFW_KEY_W -> camera.updatePosition(CameraMovement.forward, window.deltaTime());
                    case GLFW_KEY_S -> camera.updatePosition(CameraMovement.backward, window.deltaTime());
                    case GLFW_KEY_A -> camera.updatePosition(CameraMovement.left, window.deltaTime());
                    case GLFW_KEY_D -> camera.updatePosition(CameraMovement.right, window.deltaTime());
                }
            });
            window.setMouseMovementCallback((_, x, y) -> {
                camera.updateYawPitch(new Vector2f((float)x, (float)y));
            });
            window.setMouseWheelCallback((_, _, y) -> {
                camera.updateZoom((float)y);
            });

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

            while (!window.shouldClose()) {
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

                window.update();
            }
        }

        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
        glDeleteBuffers(ebo);
    }
}