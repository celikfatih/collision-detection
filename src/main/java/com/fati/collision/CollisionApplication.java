package com.fati.collision;

import com.fati.collision.collider.CollisionManager;
import com.fati.collision.shapes.CapsuleShape;
import com.fati.collision.shapes.CircleShape;
import com.fati.collision.shapes.CylinderShape;
import com.fati.collision.shapes.GeometricShape;
import com.fati.collision.shapes.RectangleShape;
import com.fati.collision.shapes.RectangularPrismShape;
import com.fati.collision.shapes.SphereShape;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CollisionApplication extends Application {

    private static final Logger LOGGER = Logger.getLogger(CollisionApplication.class.getName());

    private static final double WIDTH = 2400;
    private static final double HEIGHT = 1200;

    private static final double MID_X = WIDTH / 2;
    private static final double MID_Y = HEIGHT / 2;

    private static final double Z_LIMIT = 300;

    private static final double S1_CENTER_X = MID_X / 2;
    private static final double S1_CENTER_Y = MID_Y / 2;

    private static final double S2_CENTER_X = MID_X + (MID_X / 2);
    private static final double S2_CENTER_Y = MID_Y / 2;

    private static final double S3_CENTER_X = MID_X / 2;
    private static final double S3_CENTER_Y = MID_Y + (MID_Y / 2);

    private static final double S4_CENTER_X = MID_X + (MID_X / 2);
    private static final double S4_CENTER_Y = MID_Y + (MID_Y / 2);

    private final Map<String, Integer> totalCollisionCounts = new HashMap<>();

    private double anchorX;
    private double anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    private final Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);

    AnimationTimer timer;

    private final List<GeometricShape> shapes = List.of(
            new CircleShape(S1_CENTER_X - 50, S1_CENTER_Y, 0, 40),
            new RectangleShape(S1_CENTER_X + 50, S1_CENTER_Y, 0, 60, 100),
            new RectangularPrismShape(S2_CENTER_X, S2_CENTER_Y - 150, 0, 150, 100, 50,
                    -3.5, -2.0, -1.5, Color.GOLDENROD),
            new RectangularPrismShape(S2_CENTER_X, S2_CENTER_Y + 100, 0, 400, 50, 150,
                    2.5, 3.0, 1.0, Color.LIGHTSKYBLUE),
            new CapsuleShape(S3_CENTER_X - 50, S3_CENTER_Y, 0, 20, 100, 1.5, 2.5, 3.0, Color.TEAL),
            new CapsuleShape(S3_CENTER_X + 50, S3_CENTER_Y, 0, 30, 80, -3.5, -2.0, -3.5, Color.YELLOWGREEN),
            new SphereShape(S4_CENTER_X - 100, S4_CENTER_Y, 0, 30),
            new CylinderShape(S4_CENTER_X + 100, S4_CENTER_Y, 0, 100, 30));

    @Override
    public void start(Stage stage) {
        Group world = new Group();

        world.getChildren().add(createStudioBox(0, 0, -50, MID_X, MID_Y, 50));
        world.getChildren().add(createStudioBox(MID_X, 0, -Z_LIMIT, WIDTH, MID_Y, Z_LIMIT));
        world.getChildren().add(createStudioBox(0, MID_Y, -Z_LIMIT, MID_X, HEIGHT, Z_LIMIT));
        world.getChildren().add(createStudioBox(MID_X, MID_Y, -Z_LIMIT, WIDTH, HEIGHT, Z_LIMIT));

        shapes.forEach(shape -> {
            if (shape.getShape() != null)
                world.getChildren().add(shape.getShape());
        });

        AmbientLight ambientLight = new AmbientLight(Color.rgb(180, 180, 180));
        world.getChildren().add(ambientLight);

        PointLight pointLight = new PointLight(Color.WHITE);
        pointLight.setTranslateX(MID_X);
        pointLight.setTranslateY(MID_Y - 500);
        pointLight.setTranslateZ(-2000);
        world.getChildren().add(pointLight);

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.setFieldOfView(40);
        camera.setTranslateZ(-3000);

        Group root = new Group(world);

        world.setTranslateX(-MID_X);
        world.setTranslateY(-MID_Y);
        world.getTransforms().addAll(rotateX, rotateY);

        Scene scene = new Scene(root, WIDTH, HEIGHT, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.WHITESMOKE);
        scene.setCamera(camera);

        initMouseControl(scene, stage, camera);

        this.timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update(shapes.subList(0, 2), 0, MID_X, 0, MID_Y);
                update(shapes.subList(2, 4), MID_X, WIDTH, 0, MID_Y);
                update(shapes.subList(4, 6), 0, MID_X, MID_Y, HEIGHT);
                update(shapes.subList(6, 8), MID_X, WIDTH, MID_Y, HEIGHT);
            }
        };
        this.timer.start();

        stage.setTitle("Collision Studio");
        stage.setScene(scene);
        stage.show();
    }

    private void update(List<GeometricShape> shapes, double minX, double maxX, double minY, double maxY) {
        shapes.forEach(shape -> {
            shape.update(minX, maxX, minY, maxY);
            shape.onCollision(false);
        });

        for (int i = 0; i < shapes.size(); i++) {
            for (int j = i + 1; j < shapes.size(); j++) {
                GeometricShape s1 = shapes.get(i);
                GeometricShape s2 = shapes.get(j);
                boolean isCollided = CollisionManager.checkCollision(s1.getCollider(), s2.getCollider());
                if (isCollided) {
                    resolveCollision(s1, s2);
                }
            }
        }
    }

    private void resolveCollision(GeometricShape s1, GeometricShape s2) {
        s1.onCollision(true);
        s2.onCollision(true);

        updateCollisionCount(s1);
        updateCollisionCount(s2);

        LOGGER.log(Level.INFO,
                () -> String.format("Collision: %s vs %s. Total: %d",
                        s1.getClass().getSimpleName(),
                        s2.getClass().getSimpleName(),
                        totalCollisionCounts.get(s1.getClass().getSimpleName())));

        s1.revertVelocity();
        s2.revertVelocity();
    }

    private Box createStudioBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        double w = maxX - minX;
        double h = maxY - minY;
        double d = maxZ - minZ;
        if (d == 0)
            d = 2;

        Box box = new Box(w, h, d);
        box.setDrawMode(DrawMode.LINE);

        box.setTranslateX(minX + w / 2);
        box.setTranslateY(minY + h / 2);
        box.setTranslateZ(minZ + d / 2);

        PhongMaterial mat = new PhongMaterial(Color.DARKGRAY);
        box.setMaterial(mat);
        return box;
    }

    private void initMouseControl(Scene scene, Stage stage, PerspectiveCamera camera) {
        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = rotateX.getAngle();
            anchorAngleY = rotateY.getAngle();
        });

        scene.setOnMouseDragged(event -> {
            rotateX.setAngle(anchorAngleX - (anchorY - event.getSceneY()));
            rotateY.setAngle(anchorAngleY + (anchorX - event.getSceneX()));
        });

        stage.addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY();
            camera.setTranslateZ(camera.getTranslateZ() + delta * 2);
        });
    }

    private void updateCollisionCount(GeometricShape shape) {
        int count = totalCollisionCounts.getOrDefault(shape.getClass().getSimpleName(), 0);
        totalCollisionCounts.put(shape.getClass().getSimpleName(), count + 1);
    }
}
