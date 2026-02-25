package org.v0x31.engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    // Constants
    private static final float PITCH_MIN = -89.0f;
    private static final float PITCH_MAX = 89.0f;
    private static final float SPEED_MIN = 1.0f;
    private static final float SPEED_MAX = 10.0f;
    private static final float SENSITIVITY_MIN = 0.1f;
    private static final float SENSITIVITY_MAX = 2.0f;
    private static final float ZOOM_MIN = 1.0f;
    private static final float ZOOM_MAX = 45.0f;

    // Attributes
    private Vector3f position;
    private Vector3f front;
    private Vector3f up;
    private Vector3f right;
    private Vector3f worldUp;
    // Angles
    private float yaw;
    private float pitch;
    // Options
    private float speed;
    private float sensitivity;
    private float zoom;
    // Mouse state
    private Vector2f lastMousePosition;

    public Camera() {
        this.position = new Vector3f(0.0f, 0.0f, 3.0f);
        this.front = new Vector3f(0.0f, 0.0f, -1.0f);
        this.up = new Vector3f(0.0f, 1.0f, 0.0f);
        this.right = new Vector3f(0.0f, 0.0f, 0.0f);
        this.worldUp = new Vector3f(0.0f, 1.0f, 0.0f);

        this.yaw = -90.0f;
        this.pitch = 0.0f;

        this.speed = 3.0f;
        this.sensitivity = SENSITIVITY_MIN;
        this.zoom = ZOOM_MAX;

        this.lastMousePosition = null;
    }

    public void setSpeed(float speed) {
        if (speed >= SPEED_MIN && speed <= SPEED_MAX) {
            this.speed = speed;
        }
    }

    public void setSensitivity(float sensitivity) {
        if (sensitivity >= SENSITIVITY_MIN && sensitivity <= SENSITIVITY_MAX) {
            this.sensitivity = sensitivity;
        }
    }

    public float getSpeed() {
        return this.speed;
    }

    public float getSensitivity() {
        return this.sensitivity;
    }

    public float getZoom() {
        return this.zoom;
    }

    public Matrix4f getView() {
        return new Matrix4f().lookAt(this.position, new Vector3f(this.position).add(this.front), this.up);
    }

    public void updatePosition(CameraMovement movement, float deltaTime) {
        float velocity = this.speed * deltaTime;
        switch (movement) {
            case forward -> this.position.add(new Vector3f(this.front).mul(velocity));
            case backward -> this.position.sub(new Vector3f(this.front).mul(velocity));
            case left -> this.position.sub(new Vector3f(this.right).mul(velocity));
            case right -> this.position.add(new Vector3f(this.right).mul(velocity));
        }
    }

    public void updateYawPitch(Vector2f mousePosition) {
        if (this.lastMousePosition == null) {
            this.lastMousePosition = mousePosition;
        }
        float xOffset = mousePosition.x - this.lastMousePosition.x;
        float yOffset = this.lastMousePosition.y - mousePosition.y;
        this.lastMousePosition = mousePosition;
        this.yaw += xOffset * this.sensitivity;
        this.pitch += yOffset * this.sensitivity;
        if (this.pitch > PITCH_MAX) {
            this.pitch = PITCH_MAX;
        }
        if (this.pitch < PITCH_MIN) {
            this.pitch = PITCH_MIN;
        }

        this.front.x = (float)Math.cos(Math.toRadians(this.yaw)) * (float)Math.cos(Math.toRadians(this.pitch));
        this.front.y = (float)Math.sin(Math.toRadians(this.pitch));
        this.front.z = (float)Math.sin(Math.toRadians(this.yaw)) * (float)Math.cos(Math.toRadians(this.pitch));
        this.front.normalize();

        this.right.set(this.front).cross(this.worldUp).normalize();
        this.up.set(this.right).cross(this.front).normalize();
    }

    public void updateZoom(float change) {
        this.zoom -= change;
        if (this.zoom < ZOOM_MIN) {
            this.zoom = ZOOM_MIN;
        }
        if (this.zoom > ZOOM_MAX) {
            this.zoom = ZOOM_MAX;
        }
    }
}


