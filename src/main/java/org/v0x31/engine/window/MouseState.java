package org.v0x31.engine.window;

import org.joml.Vector2f;

public class MouseState {
    public Event event;
    public Vector2f position;
    public Button button;
    public Vector2f wheel;

    public MouseState() {
        this.event = Event.none;
        this.position = new Vector2f();
        this.button = Button.none;
        this.wheel = new Vector2f();
    }

    public enum Event {
        none,
        moved,
        buttonDown,
        buttonUp,
        wheel
    }

    public enum Button {
        none,
        left,
        middle,
        right
    }
}
