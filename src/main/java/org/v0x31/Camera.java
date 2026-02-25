package org.v0x31;

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

    public Camera() {
        this.position = new Vector3f(0.0f, 0.0f, 0.0f);
        this.front = new Vector3f(0.0f, 0.0f, 0.0f);
        this.up = new Vector3f(0.0f, 0.0f, 0.0f);
        this.right = new Vector3f(0.0f, 0.0f, 0.0f);
        this.worldUp = new Vector3f(0.0f, 0.0f, 0.0f);

        this.yaw = -90.0f;
        this.pitch = 0.0f;

        this.speed = 3.0f;
        this.sensitivity = SENSITIVITY_MIN;
        this.zoom = ZOOM_MAX;
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

    public void updatePosition(CameraMovement movement, float deltaTime) {
        float velocity = this.speed * deltaTime;
        switch (movement) {
            case forward -> this.position.add(new Vector3f(this.front).mul(velocity));
            case backward -> this.position.sub(new Vector3f(this.front).mul(velocity));
            case left -> this.position.sub(new Vector3f(this.right).mul(velocity));
            case right -> this.position.add(new Vector3f(this.right).mul(velocity));
        }
    }

    public void updateYawPitch(Vector2f position) {
        this.yaw += position.x * this.sensitivity;
        this.pitch += position.y * this.sensitivity;
        if (this.pitch > PITCH_MAX) {
            this.pitch = PITCH_MAX;
        }
        if (this.pitch < PITCH_MIN) {
            this.pitch = PITCH_MIN;
        }

        this.front.x = (float)Math.cos(Math.toRadians(this.yaw) * Math.cos(Math.toRadians(this.pitch)));
        this.front.y = (float)Math.sin(Math.toRadians(this.pitch));
        this.front.z = (float)Math.sin(Math.toRadians(this.yaw) * Math.cos(Math.toRadians(this.pitch)));
        this.front.normalize();

        this.right = new Vector3f(this.front).cross(this.worldUp).normalize();
        this.up = new Vector3f(this.front).cross(this.right).normalize();
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


