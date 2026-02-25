package org.v0x31;

import org.joml.Vector3f;

public class Camera {
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
    public float speed;
    public float sensitivity;
    public float zoom;

    public Camera() {
        this.position = new Vector3f(0.0f, 0.0f, 0.0f);
        this.front = new Vector3f(0.0f, 0.0f, 0.0f);
        this.up = new Vector3f(0.0f, 0.0f, 0.0f);
        this.right = new Vector3f(0.0f, 0.0f, 0.0f);
        this.worldUp = new Vector3f(0.0f, 0.0f, 0.0f);

        this.yaw = -90.0f;
        this.pitch = 0.0f;

        this.speed = 3.0f;
        this.sensitivity = 0.1f;
        this.zoom = 70.0f;
    }
}
