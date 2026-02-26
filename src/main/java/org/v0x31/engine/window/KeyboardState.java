package org.v0x31.engine.window;

import static org.lwjgl.glfw.GLFW.*;

public class KeyboardState {
    public boolean[] keys;

    public KeyboardState() {
        keys = new boolean[GLFW_KEY_LAST];
    }
}
