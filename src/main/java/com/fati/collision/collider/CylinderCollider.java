package com.fati.collision.collider;

public class CylinderCollider extends GeometricCollider {
    private double height;
    private double radius;

    public CylinderCollider(double height, double radius) {
        this.height = height;
        this.radius = radius;
    }

    public CylinderCollider(double x, double y, double z, double height, double radius) {
        super(x, y, z);
        this.height = height;
        this.radius = radius;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
