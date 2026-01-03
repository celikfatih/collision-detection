package com.fati.collision.shapes;

import com.fati.collision.collider.CircleCollider;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class CircleShape extends GeometricShape {
    private double radius;

    public CircleShape(double x, double y, double z, double radius) {
        super(x, y, z);
        this.radius = radius;

        this.shape = new Circle(radius, Color.DODGERBLUE);
        this.collider = new CircleCollider(radius);

        this.vy = 1.5;
        this.vx = 2.5;

        syncPosition();
    }

    @Override
    protected void syncPosition() {
        Circle circle = (Circle) shape;
        circle.setCenterX(x);
        circle.setCenterY(y);
    }

    @Override
    public void update(double minX, double maxX, double minY, double maxY) {
        doAction();

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

        if (collider != null) {
            collider.setX(this.x);
            collider.setY(this.y);
        }

        syncPosition();
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
