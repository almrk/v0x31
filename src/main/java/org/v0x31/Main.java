package org.v0x31;

import imgui.ImGui;

import static org.lwjgl.opengl.GL33.*;

public class Main {
    private static final int SIZEOF_FLOAT = 4;

    public static void main(String[] args) {
        float[] verticies = new float[]{
                // Positions          // Colors           // Texture coords
                 0.5f,  0.5f, 0.0f,   1.0f, 0.0f, 0.0f,   1.0f, 1.0f,   // top right
                 0.5f, -0.5f, 0.0f,   0.0f, 1.0f, 0.0f,   1.0f, 0.0f,   // bottom right
                -0.5f, -0.5f, 0.0f,   0.0f, 0.0f, 1.0f,   0.0f, 0.0f,   // bottom left
                -0.5f,  0.5f, 0.0f,   1.0f, 1.0f, 0.0f,   0.0f, 1.0f    // top left
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
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * SIZEOF_FLOAT, 0);
            glEnableVertexAttribArray(0);
            // Set color attribute
            glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * SIZEOF_FLOAT, 3 * SIZEOF_FLOAT);
            glEnableVertexAttribArray(1);
            // Set the texture coords attribute
            glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * SIZEOF_FLOAT, 6 * SIZEOF_FLOAT);
            glEnableVertexAttribArray(2);

            while (window.isOpen()) {
                glClearColor(0.2f, 0.2f, 0.3f, 1.0f);
                glClear(GL_COLOR_BUFFER_BIT);

                texture.bind();
                shader.use();
                glBindVertexArray(vao);
                glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

                window.beginImGui();
                ImGui.begin("Hello world!");
                ImGui.text("This is some text.");
                ImGui.button("Click me!");
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