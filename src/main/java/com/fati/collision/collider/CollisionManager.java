package com.fati.collision.collider;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CollisionManager {

        private static final Logger LOGGER = Logger.getLogger(CollisionManager.class.getName());

        private static final double TWO = 2.0;

        private CollisionManager() {
        }

        public static boolean checkCollision(GeometricCollider c1, GeometricCollider c2) {
                return switch (c1) {
                        case CircleCollider circle -> isColliding(circle, (RectangleCollider) c2);
                        case SphereCollider sphere -> isColliding(sphere, (CylinderCollider) c2);
                        case SurfaceCollider surface -> isColliding(surface, (RectangularPrismCollider) c2);
                        case RectangularPrismCollider prism -> isColliding(prism, (RectangularPrismCollider) c2);
                        case CapsuleCollider capsule -> isColliding(capsule, (CapsuleCollider) c2);
                        default -> false;
                };
        }

        public static boolean isColliding(CircleCollider circle, RectangleCollider rectangle) {
                double closestX = Math.clamp(circle.getX(), rectangle.getX(), rectangle.getX() + rectangle.getWidth());
                double closestY = Math.clamp(circle.getY(), rectangle.getY(), rectangle.getY() + rectangle.getHeight());

                LOGGER.log(Level.INFO, () -> "Closest point: (" + closestX + ", " + closestY + ")");

                double distanceX = circle.getX() - closestX;
                double distanceY = circle.getY() - closestY;

                LOGGER.log(Level.INFO,
                                () -> "Distance between the centre of the circle and the nearest point: (" + distanceX
                                                + ", "
                                                + distanceY + ")");

                double distanceSquared = (distanceX * distanceX) + (distanceY * distanceY);
                double radiusSquared = circle.getRadius() * circle.getRadius();

                LOGGER.log(Level.INFO,
                                () -> "(Pythagorean Theorem) Distance squared: " + distanceSquared
                                                + ", Radius squared: "
                                                + radiusSquared);

                return distanceSquared < radiusSquared;
        }

        public static boolean isColliding(SphereCollider sphere, CylinderCollider cylinder) {
                double distanceX = sphere.getX() - cylinder.getX();
                double distanceZ = sphere.getZ() - cylinder.getZ();

                LOGGER.log(Level.INFO,
                                () -> "Distance between the centre of the sphere and the base centre of the cylinder: ("
                                                + distanceX
                                                + ", " + distanceZ + ")");

                double horizontalDistanceSquared = (distanceX * distanceX) + (distanceZ * distanceZ);

                double totalRadius = cylinder.getRadius() + sphere.getRadius();

                if (horizontalDistanceSquared > (totalRadius * totalRadius)) {
                        return false;
                }

                double halfHeight = cylinder.getHeight() / 2.0;
                double cylinderBaseLimit = cylinder.getY() - halfHeight;
                double cylinderTopLimit = cylinder.getY() + halfHeight;

                return (sphere.getY() + sphere.getRadius() >= cylinderBaseLimit)
                                && (sphere.getY() - sphere.getRadius() <= cylinderTopLimit);
        }

        public static boolean isColliding(SurfaceCollider surface, RectangularPrismCollider prism) {
                double halfWidth = prism.getWidth() / TWO;
                double halfHeight = prism.getHeight() / TWO;
                double halfDepth = prism.getDepth() / TWO;

                double centreX = prism.getX();
                double centreY = prism.getY();
                double centreZ = prism.getZ();

                LOGGER.log(Level.INFO,
                                () -> "Centre of the rectangular prism: (" + centreX + ", " + centreY + ", " + centreZ
                                                + ")");

                double prismProjection = (halfWidth * Math.abs(surface.getX()))
                                + (halfHeight * Math.abs(surface.getY()))
                                + (halfDepth + Math.abs(surface.getZ()));

                LOGGER.log(Level.INFO,
                                () -> "Projection of the rectangular prism onto the surface: " + prismProjection);

                double distancePrismCentreToSurface = (surface.getX() * centreX)
                                + (surface.getY() * centreY)
                                + (surface.getZ() * centreZ)
                                + surface.getDepth();

                LOGGER.log(Level.INFO,
                                () -> "Distance between the centre of the rectangular prism and the surface: "
                                                + distancePrismCentreToSurface);

                return Math.abs(distancePrismCentreToSurface) < prismProjection;
        }

        public static boolean isColliding(RectangularPrismCollider prismFirst, RectangularPrismCollider prismSecond) {
                boolean xOverlap = Math
                                .abs(prismFirst.getX()
                                                - prismSecond.getX()) < (prismFirst.getWidth() + prismSecond.getWidth())
                                                                / 2.0;
                boolean yOverlap = Math
                                .abs(prismFirst.getY() - prismSecond
                                                .getY()) < (prismFirst.getHeight() + prismSecond.getHeight()) / 2.0;
                boolean zOverlap = Math
                                .abs(prismFirst.getZ()
                                                - prismSecond.getZ()) < (prismFirst.getDepth() + prismSecond.getDepth())
                                                                / 2.0;
                return xOverlap && yOverlap && zOverlap;
        }

        public static boolean isColliding(CapsuleCollider capsuleFirst, CapsuleCollider capsuleSecond) {
                double dx = capsuleFirst.getX() - capsuleSecond.getX();
                double dz = capsuleFirst.getZ() - capsuleSecond.getZ();

                double distanceSquared = (dx * dx) + (dz * dz);

                double radiusSum = capsuleFirst.getRadius() + capsuleSecond.getRadius();

                LOGGER.log(Level.INFO,
                                () -> "Distance squared: " + distanceSquared + ", Radius sum: " + radiusSum + ",");

                if (distanceSquared > (radiusSum * radiusSum)) {
                        return false;
                }

                double dy = Math.abs(capsuleFirst.getY() - capsuleSecond.getY());
                double heightSumHalved = (capsuleFirst.getHeight() + capsuleSecond.getHeight()) / 2.0;

                LOGGER.log(Level.INFO, () -> "Dy: " + dy + ", Height sum halved: " + heightSumHalved);

                return dy < heightSumHalved;
        }
}
