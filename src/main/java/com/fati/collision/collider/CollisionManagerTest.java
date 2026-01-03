package com.fati.collision.collider;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CollisionManagerTest {

    private static final Logger LOGGER = Logger.getLogger(CollisionManagerTest.class.getName());

    public static void main(String[] args) {
        Logger managerLogger = Logger.getLogger(CollisionManager.class.getName());
        managerLogger.setLevel(Level.OFF);

        LOGGER.log(Level.INFO, "==========================================");
        LOGGER.log(Level.INFO, "STARTING COLLISION MANAGER LOGIC TESTS");
        LOGGER.log(Level.INFO, "==========================================\n");

        testCircleRectangle();
        testSphereCylinder();
        testSurfacePrism();
        testPrismPrism();
        testCapsuleCapsule();

        LOGGER.log(Level.INFO, "\n==========================================");
        LOGGER.log(Level.INFO, "ALL TESTS COMPLETED");
        LOGGER.log(Level.INFO, "==========================================");
    }

    private static void testCircleRectangle() {
        LOGGER.log(Level.INFO, "--- Test 1: Circle vs Rectangle ---");

        CircleCollider c1 = new CircleCollider(50, 50, 10);
        RectangleCollider r1 = new RectangleCollider(0, 0, 100, 100);
        assertCollision("Circle inside Rectangle", c1, r1, true);

        CircleCollider c2 = new CircleCollider(100, 100, 5);
        RectangleCollider r2 = new RectangleCollider(0, 0, 20, 20);
        assertCollision("Circle far from Rectangle", c2, r2, false);
    }

    private static void testSphereCylinder() {
        LOGGER.log(Level.INFO, "\n--- Test 2: Sphere vs Cylinder ---");

        SphereCollider s1 = new SphereCollider(5, 25, 0, 5);
        CylinderCollider c1 = new CylinderCollider(0, 0, 0, 50, 10);
        assertCollision("Sphere intersecting Cylinder", s1, c1, true);

        SphereCollider s2 = new SphereCollider(0, 100, 0, 5);
        CylinderCollider c2 = new CylinderCollider(0, 0, 0, 50, 10);
        assertCollision("Sphere above Cylinder", s2, c2, false);
    }

    private static void testSurfacePrism() {
        LOGGER.log(Level.INFO, "\n--- Test 3: Surface vs RectangularPrism ---");

        SurfaceCollider surface = new SurfaceCollider(0, -1, 0, 600);

        RectangularPrismCollider p1 = new RectangularPrismCollider(100, 591, 0, 50, 20, 50);
        assertCollision("Prism hitting Surface", surface, p1, true);

        RectangularPrismCollider p2 = new RectangularPrismCollider(100, 100, 0, 50, 20, 50);
        assertCollision("Prism floating in air", surface, p2, false);
    }

    private static void testPrismPrism() {
        LOGGER.log(Level.INFO, "\n--- Test 4: Prism vs Prism ---");

        RectangularPrismCollider p1 = new RectangularPrismCollider(0, 0, 0, 10, 10, 10);
        RectangularPrismCollider p2 = new RectangularPrismCollider(5, 5, 5, 10, 10, 10);
        assertCollision("Prisms overlapping", p1, p2, true);

        RectangularPrismCollider p3 = new RectangularPrismCollider(100, 100, 100, 10, 10, 10);
        assertCollision("Prisms separated", p1, p3, false);
    }

    private static void testCapsuleCapsule() {
        LOGGER.log(Level.INFO, "\n--- Test 5: Capsule vs Capsule ---");

        CapsuleCollider c1 = new CapsuleCollider(0, 0, 0, 5, 20);
        CapsuleCollider c2 = new CapsuleCollider(2, 0, 0, 5, 20);
        assertCollision("Capsules close horizontally", c1, c2, true);

        CapsuleCollider c3 = new CapsuleCollider(0, 100, 0, 5, 20);
        assertCollision("Capsules separated vertically", c1, c3, false);
    }

    private static void assertCollision(String testName, GeometricCollider c1, GeometricCollider c2,
            boolean expectedResult) {
        boolean actualResult = CollisionManager.checkCollision(c1, c2);

        String status = (actualResult == expectedResult) ? "[PASSED]" : "[FAILED]";
        String expectationText = expectedResult ? "COLLISION" : "NO COLLISION";

        LOGGER.log(Level.INFO,
                () -> status + " " + testName + " | Expected: " + expectationText + " | Actual: " + actualResult);

        if (actualResult != expectedResult) {
            LOGGER.log(Level.WARNING, () -> ">>> ERROR: Logic mismatch in " + testName);
        }
    }
}
