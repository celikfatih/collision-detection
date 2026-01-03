package com.fati.collision.collider;

public class CircleCollider extends GeometricCollider {
    private double radius;

    public CircleCollider(double radius) {
        this.radius = radius;
    }

    public CircleCollider(double x, double y, double radius) {
        super(x, y, 0);
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
