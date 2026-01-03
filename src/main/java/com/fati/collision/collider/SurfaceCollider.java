package com.fati.collision.collider;

public class SurfaceCollider extends GeometricCollider {
    private double width;
    private double height;
    private double depth;

    public SurfaceCollider(double width, double height, double depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    public SurfaceCollider(double x, double y, double z, double depth) {
        super(x, y, 0);
        this.depth = depth;
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
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
