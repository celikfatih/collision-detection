package com.fati.collision.shapes;

import com.fati.collision.collider.RectangleCollider;
import javafx.scene.shape.Rectangle;

import static javafx.scene.paint.Color.MEDIUMORCHID;

public class RectangleShape extends GeometricShape {
    private double width;
    private double height;

    public RectangleShape(double x, double y, double z, double width, double height) {
        super(x, y, z);
        this.width = width;
        this.height = height;

        this.shape = new Rectangle(width, height, MEDIUMORCHID);
        this.collider = new RectangleCollider(width, height);

        this.vx = -2.0;
        this.vy = -2.0;

        syncPosition();
    }

    @Override
    protected void syncPosition() {
        Rectangle r = (Rectangle) this.shape;
        r.setTranslateX(this.x);
        r.setTranslateY(this.y);
    }

    @Override
    public void update(double minX, double maxX, double minY, double maxY) {
        doAction();

        if (this.x < minX) {
            this.x = minX;
            this.vx = Math.abs(this.vx);
        } else if (this.x + this.width > maxX) {
            this.x = maxX - this.width;
            this.vx = -Math.abs(this.vx);
        }

        if (this.y < minY) {
            this.y = minY;
            this.vy = Math.abs(this.vy);
        } else if (this.y + this.height > maxY) {
            this.y = maxY - this.height;
            this.vy = -Math.abs(this.vy);
        }

        if (collider != null) {
            collider.setX(this.x);
            collider.setY(this.y);
        }

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
}
