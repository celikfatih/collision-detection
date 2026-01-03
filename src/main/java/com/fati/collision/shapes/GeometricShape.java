package com.fati.collision.shapes;

import com.fati.collision.collider.GeometricCollider;
import javafx.scene.Node;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Shape3D;

import static javafx.scene.paint.Color.DARKOLIVEGREEN;
import static javafx.scene.paint.Color.DODGERBLUE;
import static javafx.scene.paint.Color.GOLDENROD;
import static javafx.scene.paint.Color.GRAY;
import static javafx.scene.paint.Color.LIGHTSKYBLUE;
import static javafx.scene.paint.Color.MEDIUMORCHID;
import static javafx.scene.paint.Color.RED;
import static javafx.scene.paint.Color.TEAL;
import static javafx.scene.paint.Color.WHITE;
import static javafx.scene.paint.Color.YELLOWGREEN;

public abstract class GeometricShape {
    protected double x;
    protected double y;
    protected double z;
    protected double vx = 2;
    protected double vy = 2;
    protected double vz = 0;
    protected Node shape;
    protected GeometricCollider collider;

    protected GeometricShape(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    protected abstract void syncPosition();

    public abstract void update(double minX, double maxX, double minY, double maxY);

    public void onCollision(boolean isCollided) {
        if (this.shape instanceof Shape3D shape3D) {
            PhongMaterial mat = (PhongMaterial) shape3D.getMaterial();
            mat = ifNullCreateNew(shape3D, mat);

            if (isCollided) {
                mat.setDiffuseColor(RED);
                mat.setSpecularColor(WHITE);
            } else {
                fillColorByShapeType(mat);
            }
        } else if (this.shape instanceof javafx.scene.shape.Shape s) {
            if (isCollided) {
                s.setFill(RED);
            } else {
                if (this instanceof RectangleShape) {
                    s.setFill(MEDIUMORCHID);
                } else {
                    s.setFill(DODGERBLUE);
                }
            }
        }
    }

    private void fillColorByShapeType(PhongMaterial mat) {
        switch (this) {
            case SphereShape s -> mat.setDiffuseColor(DODGERBLUE);
            case CylinderShape c -> mat.setDiffuseColor(DARKOLIVEGREEN);
            case RectangularPrismShape r -> {
                if (r.getWidth() == 150) {
                    mat.setDiffuseColor(GOLDENROD);
                } else {
                    mat.setDiffuseColor(LIGHTSKYBLUE);
                }
            }
            case CapsuleShape c -> {
                if (c.getHeight() == 100) {
                    mat.setDiffuseColor(TEAL);
                } else {
                    mat.setDiffuseColor(YELLOWGREEN);
                }
            }
            default -> mat.setDiffuseColor(GRAY);
        }
        mat.setSpecularColor(WHITE);
    }

    private static PhongMaterial ifNullCreateNew(Shape3D shape3D, PhongMaterial mat) {
        if (mat == null) {
            mat = new PhongMaterial();
            shape3D.setMaterial(mat);
        }
        return mat;
    }

    public void revertVelocity() {
        this.vx *= -1;
        this.vy *= -1;
    }

    public void doAction() {
        this.x += this.vx;
        this.y += this.vy;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public Node getShape() {
        return shape;
    }

    public void setShape(Node shape) {
        this.shape = shape;
    }

    public GeometricCollider getCollider() {
        return collider;
    }

    public void setCollider(GeometricCollider collider) {
        this.collider = collider;
    }
}
