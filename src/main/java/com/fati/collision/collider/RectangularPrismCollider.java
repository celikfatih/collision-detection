package com.fati.collision.collider;

public class RectangularPrismCollider extends GeometricCollider {
    private double width;
    private double height;
    private double depth;

    public RectangularPrismCollider(double width, double height, double depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    public RectangularPrismCollider(double x, double y, double z, double width, double height, double depth) {
        super(x, y, z);
        this.height = height;
        this.width = width;
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

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }
}
