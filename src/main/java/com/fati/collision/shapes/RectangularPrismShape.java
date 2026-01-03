package com.fati.collision.shapes;

import com.fati.collision.collider.RectangularPrismCollider;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

import static javafx.scene.paint.Color.WHITE;

public class RectangularPrismShape extends GeometricShape {
    private double width;
    private double height;
    private double depth;

    public RectangularPrismShape(double x, double y, double z, double width, double height,
            double depth, double vx, double vy, double vz, Color color) {
        super(x, y, z);
        this.width = width;
        this.height = height;
        this.depth = depth;

        this.vx = vx;
        this.vy = vy;
        this.vz = vz;

        Box box = new Box(width, height, depth);
        PhongMaterial mat = new PhongMaterial(color);
        mat.setSpecularColor(WHITE);
        box.setMaterial(mat);

        this.shape = box;

        this.collider = new RectangularPrismCollider(width, height, depth);
        this.collider.setX(x);
        this.collider.setY(y);
        this.collider.setZ(z);

        syncPosition();
    }

    @Override
    protected void syncPosition() {
        Box b = (Box) this.shape;
        b.setTranslateX(this.x);
        b.setTranslateY(this.y);
        b.setTranslateZ(this.collider.getZ());
    }

    @Override
    public void update(double minX, double maxX, double minY, double maxY) {
        doAction();

        double currentZ = this.collider.getZ();
        double nextZ = currentZ + this.vz;
        double halfW = this.width / 2.0;

        if (this.x - halfW < minX) {
            this.x = minX + halfW;
            this.vx = Math.abs(this.vx);
        } else if (this.x + halfW > maxX) {
            this.x = maxX - halfW;
            this.vx = -Math.abs(this.vx);
        }

        double halfH = this.height / 2.0;
        if (this.y - halfH < minY) {
            this.y = minY + halfH;
            this.vy = Math.abs(this.vy);
        } else if (this.y + halfH > maxY) {
            this.y = maxY - halfH;
            this.vy = -Math.abs(this.vy);
        }

        double zLimit = 300;
        double halfD = this.depth / 2.0;

        if (nextZ - halfD < -zLimit) {
            nextZ = -zLimit + halfD;
            this.vz = Math.abs(this.vz);
        } else if (nextZ + halfD > zLimit) {
            nextZ = zLimit - halfD;
            this.vz = -Math.abs(this.vz);
        }

        this.collider.setX(this.x);
        this.collider.setY(this.y);
        this.collider.setZ(nextZ);

        syncPosition();
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
