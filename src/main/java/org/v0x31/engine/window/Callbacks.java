package org.v0x31.engine.window;

public class Callbacks {
    public interface Load {
        void invoke();
    }

    public interface Render {
        void invoke();
    }

    public interface WindowEvent {
        void invoke(WindowState windowState);
    }

    public interface KeyboardEvent {
        void invoke(KeyboardState keyboardState);
    }

    public interface MouseEvent {
        void invoke(MouseState mouseState);
    }
}
