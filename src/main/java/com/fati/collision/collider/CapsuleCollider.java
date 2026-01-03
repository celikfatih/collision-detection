package com.fati.collision.collider;

public class CapsuleCollider extends GeometricCollider {
    private double radius;
    private double height;

    public CapsuleCollider(double radius, double height) {
        this.radius = radius;
        this.height = height;
    }

    public CapsuleCollider(double x, double y, double z, double radius, double height) {
        super(x, y, z);
        this.radius = radius;
        this.height = height;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
