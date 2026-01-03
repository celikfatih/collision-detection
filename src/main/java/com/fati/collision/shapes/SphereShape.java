package com.fati.collision.shapes;

import com.fati.collision.collider.SphereCollider;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

public class SphereShape extends GeometricShape {
    private double radius;
    private double vz;

    public SphereShape(double x, double y, double z, double radius) {
        super(x, y, z);
        this.radius = radius;

        Sphere sphere3D = new Sphere(radius);
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.BLUE);
        material.setSpecularColor(Color.WHITE);
        material.setSpecularPower(32);
        sphere3D.setMaterial(material);

        this.shape = sphere3D;

        SphereCollider sphereCollider = new SphereCollider(radius);
        sphereCollider.setZ(z);
        this.collider = sphereCollider;

        this.vz = 1.5;
        this.vx = 3.0;
        this.vy = 3.5;

        syncPosition();
    }

    @Override
    protected void syncPosition() {
        Sphere s = (Sphere) this.shape;
        s.setTranslateX(x);
        s.setTranslateY(y);
        s.setTranslateZ(collider.getZ());
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

        if (this.y - this.radius < minY) {
            this.y = minY + this.radius;
            this.vy = Math.abs(this.vy);
        } else if (this.y + this.radius > maxY) {
            this.y = maxY - this.radius;
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

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getVz() {
        return vz;
    }

    public void setVz(double vz) {
        this.vz = vz;
    }
}
