package com.fati.collision.collider;

public class SphereCollider extends GeometricCollider {
    private double radius;

    public SphereCollider(double radius) {
        this.radius = radius;
    }

    public SphereCollider(double x, double y, double z, double radius) {
        super(x, y, z);
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
