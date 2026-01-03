package com.fati.collision.shapes;

import com.fati.collision.collider.CylinderCollider;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;

import static javafx.scene.paint.Color.DARKOLIVEGREEN;
import static javafx.scene.paint.Color.WHITE;

public class CylinderShape extends GeometricShape {
    private double height;
    private double radius;

    public CylinderShape(double x, double y, double z, double height, double radius) {
        super(x, y, z);

        this.height = height;
        this.radius = radius;

        Cylinder cylinder = new Cylinder(radius, height);
        PhongMaterial mat = new PhongMaterial();
        mat.setDiffuseColor(DARKOLIVEGREEN);
        mat.setSpecularColor(WHITE);
        cylinder.setMaterial(mat);

        this.shape = cylinder;

        CylinderCollider cylinderCollider = new CylinderCollider(height, radius);
        cylinderCollider.setZ(z);
        this.collider = cylinderCollider;

        this.vy = 1.5;
        this.vx = 3.0;

        syncPosition();
    }

    @Override
    protected void syncPosition() {
        Cylinder cylinder = (Cylinder) shape;
        cylinder.setTranslateX(x);
        cylinder.setTranslateY(y);
        cylinder.setTranslateZ(collider.getZ());
    }

    @Override
    public void update(double minX, double maxX, double minY, double maxY) {
        doAction();

        double currentZ = collider.getZ();
        double nextZ = currentZ + this.vz;

        if (this.x - this.radius < minX) {
            this.x = minX + this.radius;
            this.vx = Math.abs(this.vx);
        } else if (this.x + this.radius > maxX) {
            this.x = maxX - this.radius;
            this.vx = -Math.abs(this.vx);
        }

        double halfHeight = this.height / 2.0;

        if (this.y - halfHeight < minY) {
            this.y = minY + halfHeight;
            this.vy = Math.abs(this.vy);
        } else if (this.y + halfHeight > maxY) {
            this.y = maxY - halfHeight;
            this.vy = -Math.abs(this.vy);
        }

        double zLimit = 300;
        double halfD = this.radius;

        if (nextZ - halfD < -zLimit) {
            nextZ = -zLimit + halfD;
            this.vz = Math.abs(this.vz);
        } else if (nextZ + halfD > zLimit) {
            nextZ = zLimit - halfD;
            this.vz = -Math.abs(this.vz);
        }

        collider.setZ(nextZ);
        collider.setX(this.x);
        collider.setY(this.y);

        syncPosition();
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
