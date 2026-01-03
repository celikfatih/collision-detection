package com.fati.collision.shapes;

import com.fati.collision.collider.CapsuleCollider;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;

public class CapsuleShape extends GeometricShape {
    private double radius;
    private double height;
    private Color color;

    public CapsuleShape(double x, double y, double z, double radius, double height,
            double vx, double vy, double vz, Color color) {
        super(x, y, z);
        this.radius = radius;
        this.height = height;
        this.color = color;

        this.vx = vx;
        this.vy = vy;
        this.vz = vz;

        Group capsuleGroup = new Group();

        PhongMaterial mat = new PhongMaterial(color);
        mat.setSpecularColor(Color.WHITE);

        double cylinderHeight = height - (2 * radius);
        if (cylinderHeight < 0)
            cylinderHeight = 0;

        Cylinder cylinder = new Cylinder(radius, cylinderHeight);
        cylinder.setMaterial(mat);

        Sphere topSphere = new Sphere(radius);
        topSphere.setMaterial(mat);
        topSphere.setTranslateY(-(cylinderHeight / 2.0));

        Sphere bottomSphere = new Sphere(radius);
        bottomSphere.setMaterial(mat);
        bottomSphere.setTranslateY(cylinderHeight / 2.0);

        capsuleGroup.getChildren().addAll(cylinder, topSphere, bottomSphere);

        this.shape = capsuleGroup;

        this.collider = new CapsuleCollider(radius, height);
        this.collider.setX(x);
        this.collider.setY(y);
        this.collider.setZ(z);

        syncPosition();
    }

    @Override
    public void onCollision(boolean isCollided) {
        Group group = (Group) this.shape;
        Color c = isCollided ? Color.RED : this.color;
        PhongMaterial mat = new PhongMaterial(c);
        mat.setSpecularColor(Color.WHITE);

        for (Node node : group.getChildren()) {
            if (node instanceof Shape3D s) {
                s.setMaterial(mat);
            }
        }
    }

    @Override
    protected void syncPosition() {
        this.shape.setTranslateX(this.x);
        this.shape.setTranslateY(this.y);
        this.shape.setTranslateZ(this.collider.getZ());
    }

    @Override
    public void update(double minX, double maxX, double minY, double maxY) {
        doAction();

        double currentZ = this.collider.getZ();
        double nextZ = currentZ + this.vz;

        if (this.x - radius < minX) {
            this.x = minX + radius;
            this.vx = Math.abs(this.vx);
        } else if (this.x + radius > maxX) {
            this.x = maxX - radius;
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

        this.collider.setX(this.x);
        this.collider.setY(this.y);
        this.collider.setZ(nextZ);

        syncPosition();
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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
