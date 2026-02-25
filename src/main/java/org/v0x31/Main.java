package org.v0x31;

import imgui.ImGui;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL33.*;

public class Main {
    private static final int SIZEOF_FLOAT = 4;

    public static void main(String[] args) {
        float[] verticies = new float[]{
                // Positions          // Texture coords
                -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,
                0.5f, -0.5f, -0.5f,  1.0f, 0.0f,
                0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,

                -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
                0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
                -0.5f,  0.5f,  0.5f,  0.0f, 1.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,

                -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
                -0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
                -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

                0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
                0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

                -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                0.5f, -0.5f, -0.5f,  1.0f, 1.0f,
                0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
                0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,

                -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
                0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
                -0.5f,  0.5f,  0.5f,  0.0f, 0.0f,
                -0.5f,  0.5f, -0.5f,  0.0f, 1.0f
        };
        int[] indicies = new int[]{
                0, 1, 3, // first triangle
                1, 2, 3  // second triangle
        };
        int vao = 0;
        int vbo = 0;
        int ebo = 0;

        try (
                Window window = new Window("v0x31", 800, 800);
                Texture texture = new Texture("textures/container.jpg");
                Shader shader = new Shader("shaders/vertex.glsl", "shaders/fragment.glsl");
        ) {
            glEnable(GL_DEPTH_TEST);

            vao = glGenVertexArrays();
            vbo = glGenBuffers();
            ebo = glGenBuffers();

            // Copy the verticies and indicies data
            glBindVertexArray(vao);
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, verticies, GL_STATIC_DRAW);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicies, GL_STATIC_DRAW);

            // Set position attribute
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * SIZEOF_FLOAT, 0);
            glEnableVertexAttribArray(0);
            // Set the texture coords attribute
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * SIZEOF_FLOAT, 3 * SIZEOF_FLOAT);
            glEnableVertexAttribArray(1);

            while (window.isOpen()) {
                glClearColor(0.2f, 0.2f, 0.3f, 1.0f);
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

                texture.bind();
                shader.use();
                Matrix4f model = new Matrix4f(
                        1.0f, 0.0f, 0.0f, 0.0f,
                        0.0f, 1.0f, 0.0f, 0.0f,
                        0.0f, 0.0f, 1.0f, 0.0f,
                        0.0f, 0.0f, 0.0f, 1.0f
                );
                Matrix4f view = new Matrix4f(
                        1.0f, 0.0f, 0.0f, 0.0f,
                        0.0f, 1.0f, 0.0f, 0.0f,
                        0.0f, 0.0f, 1.0f, 0.0f,
                        0.0f, 0.0f, 0.0f, 1.0f
                );
                Matrix4f projection = new Matrix4f(
                        1.0f, 0.0f, 0.0f, 0.0f,
                        0.0f, 1.0f, 0.0f, 0.0f,
                        0.0f, 0.0f, 1.0f, 0.0f,
                        0.0f, 0.0f, 0.0f, 1.0f
                );
                model.rotate((float)glfwGetTime() * (float)Math.toRadians(50.0f), 0.0f, 1.0f, 0.5f);
                view.translate(0.0f, 0.0f, -3.0f);
                projection.perspective((float)Math.toRadians(70.0f), 1, 0.1f, 100.0f);
                shader.setMat4("model", model);
                shader.setMat4("view", view);
                shader.setMat4("projection", projection);

                glBindVertexArray(vao);
                glDrawArrays(GL_TRIANGLES, 0, 36);

                window.beginImGui();
                ImGui.begin("Developer toolbox");
                ImGui.separatorText("Shader");
                if (ImGui.button("Recompile")) {
                    shader.recompile();
                }
                ImGui.end();
                window.endImGui();

                window.update();
            }
        }

        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
        glDeleteBuffers(ebo);
    }
}