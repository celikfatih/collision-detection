package com.fati.collision.collider;

public class RectangleCollider extends GeometricCollider {
    private double width;
    private double height;

    public RectangleCollider(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public RectangleCollider(double x, double y, double width, double height) {
        super(x, y, 0);
        this.width = width;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
