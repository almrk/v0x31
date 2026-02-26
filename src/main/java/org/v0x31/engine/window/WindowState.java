package org.v0x31.engine.window;

import org.joml.Vector2i;

public class WindowState {
    public Event event;
    public Vector2i size;
    public Vector2i position;

    public WindowState() {
        event = Event.none;
        size = new Vector2i();
        position = new Vector2i();
    }

    public enum Event {
        none,
        resized,
        moved,
        focusGained,
        focusLost
    }
}
