# Collision Detection and Physics Simulation

## Summary
This project aims to simulate **Collision Detection** principles, one of the cornerstones of virtual reality (VR) and game engine development processes, in a 3D environment using JavaFX. within the scope of the project, mathematical models of different geometric shapes (sphere, cylinder, rectangular prism, capsule, circle, rectangle) have been created and the interactions of these shapes with each other have been analyzed in real-time within the framework of analytical geometry principles.

![Project Overview](screenshots/main-center.png)
![Project Overview](screenshots/main-right-side.png)
![Project Overview](screenshots/main-top.png)
*Figure (1-3): General view of the Collision Studio application from different angles. Collision simulations running simultaneously in four different regions in 3D space. The user can rotate the camera with the mouse and zoom in/out with scroll.*

---

## 1. Introduction and Purpose

Collision detection plays a critical role in computer graphics, game development, robotics, physics simulations, and virtual reality applications. This study demonstrates how collision states between basic geometric shapes can be mathematically determined and how these determinations can be visualized in a real-time simulation environment.

The main objectives of the project are:
- Implementation of collision detection algorithms for different geometric shapes
- Testing these algorithms in a 3D visualization environment
- Creating a visual feedback mechanism at the moment of collision
- Developing a user-interactive simulation environment

---

## 2. Project Architecture and Software Structure

The application is built on the **JavaFX 21** library and developed by strictly adhering to Object-Oriented Programming (OOP) principles. The project has a modular structure and includes separate collider and shape classes for each geometric shape.

### 2.1. Basic Components

#### 2.1.1. CollisionApplication.java
It is the main entry point of the application. It performs the following tasks:
- **3D Scene Setup**: A scene of 2400x1200 pixels is created
- **Camera Configuration**: 3D view is provided with PerspectiveCamera (Field of View: 40°, Near Clip: 0.1, Far Clip: 10000)
- **Lighting**: The scene is illuminated with AmbientLight and PointLight
- **Region Separation**: The scene is divided into four equal regions, simulating different collision scenarios in each
- **Animation Loop**: Shapes are updated and collision checks are performed in every frame with AnimationTimer
- **User Interaction**: Camera rotation with mouse drag, zoom functions with scroll

![Application Interface](screenshots/collision-manager.gif)
*Figure 4: Detailed view of the application interface. Circle-rectangle in the top left, prism-prism in the top right, capsule-capsule in the bottom left, sphere-cylinder collision simulations in the bottom right are seen.*

#### 2.1.2. CollisionManager.java
It is the central processing unit of the collision logic. Different collision tests are managed using Pattern matching (switch-case). It contains customized static methods for each collision type:
- `isColliding(CircleCollider, RectangleCollider)`
- `isColliding(SphereCollider, CylinderCollider)`
- `isColliding(SurfaceCollider, RectangularPrismCollider)`
- `isColliding(RectangularPrismCollider, RectangularPrismCollider)`
- `isColliding(CapsuleCollider, CapsuleCollider)`

#### 2.1.3. GeometricCollider (Abstract Class)
It is the base class from which all collision shapes are derived. It holds the position information (x, y, z) in 3D space and transfers it to subclasses via inheritance.

#### 2.1.4. GeometricShape (Abstract Class)
It manages visualization and physical update loops. For each shape:
- Position (x, y, z) and velocity (vx, vy, vz) information
- JavaFX Node reference (visual representation)
- Collider reference (for collision calculations)
- `update()` method for boundary checks and movement
- `onCollision()` method for visual feedback (red color)

### 2.2. Shape Hierarchy

The following shapes are implemented in the project:

**2D Shapes:**
- `CircleShape` / `CircleCollider`: Circle defined by radius parameter
- `RectangleShape` / `RectangleCollider`: Rectangle defined by width and height parameters

**3D Shapes:**
- `SphereShape` / `SphereCollider`: Sphere defined by radius parameter
- `CylinderShape` / `CylinderCollider`: Cylinder defined by radius and height parameters
- `RectangularPrismShape` / `RectangularPrismCollider`: Rectangular prism defined by width, height, and depth parameters
- `CapsuleShape` / `CapsuleCollider`: Capsule defined by radius and height parameters (cylindrical body + two hemispheres)
- `SurfaceCollider`: Plane surface defined by normal vector and depth parameters

---

## 3. Collision Principles and Algorithms

The collision algorithms used in the project are customized according to the geometric properties of the shapes. Each algorithm is based on mathematically proven geometry principles. Below are the basic collision states implemented in the project and their detailed mathematical explanations.

### 3.1. Circle - Rectangle Collision

In this scenario, the contact of a circle with a rectangle is checked. This is a frequently used collision type in 2D games and UI interactions.

![Circle-Rectangle Collision](screenshots/circle-collision.png)
*Figure 5: Collision detection between circle and rectangle. Both shapes turn red at the moment of collision. If the distance from the center of the circle to the nearest point of the rectangle is less than the radius of the circle, a collision occurs.*

**Mathematical Principle:**

The collision test is based on the principle of finding the point on the rectangle closest to the center of the circle. This approach is known as the "closest point on AABB (Axis-Aligned Bounding Box)" algorithm.

**Algorithm Steps:**

1. **Calculating the Nearest Point**: The center coordinates of the circle (Cₓ, Cᵧ) are taken and these coordinates are subjected to a "clamp" (restriction) process between the boundaries of the rectangle (Rₓₘᵢₙ, Rₓₘₐₓ, Rᵧₘᵢₙ, Rᵧₘₐₓ):

   $$P_x = \max(R_{x_{min}}, \min(C_x, R_{x_{max}}))$$
   
   $$P_y = \max(R_{y_{min}}, \min(C_y, R_{y_{max}}))$$

   As a result of this process, the closest point (Pₓ, Pᵧ) on (or inside) the rectangle to the circle is obtained.

2. **Distance Calculation**: The square of the Euclidean distance between the center of the circle and this nearest point is calculated:

   $$d^2 = (C_x - P_x)^2 + (C_y - P_y)^2$$

3. **Collision Check**: If the square of this distance is less than or equal to the square of the circle's radius, a collision has occurred:

   $$d^2 \leq r^2$$

   **Note**: Squaring is used as a performance optimization to avoid the cost of the square root operation. The square root function is much more costly than the squaring operation.

**Code Implementation:**
```java
double closestX = Math.clamp(circle.getX(), rectangle.getX(), 
                             rectangle.getX() + rectangle.getWidth());
double closestY = Math.clamp(circle.getY(), rectangle.getY(), 
                             rectangle.getY() + rectangle.getHeight());
double distanceSquared = (circle.getX() - closestX) * (circle.getX() - closestX) 
                       + (circle.getY() - closestY) * (circle.getY() - closestY);
double radiusSquared = circle.getRadius() * circle.getRadius();
return distanceSquared < radiusSquared;
```

---

### 3.2. Sphere - Cylinder Collision

The interaction of a sphere with a cylinder in 3D space is examined. This collision type is used especially in character movements and object interactions.

![Sphere-Cylinder Collision](screenshots/syphere-collision.png)
*Figure 6: Collision detection between sphere and cylinder. The sphere is checked against the cylinder's horizontal plane (XZ) circle and vertical (Y) boundaries. Both shapes turn red at the moment of collision.*

**Algorithm:**

This check is performed in two stages:

1. **Horizontal Distance Check**: The horizontal (in XZ plane) distance between the sphere center and the cylinder center is calculated. If this distance is greater than the sum of the cylinder radius and sphere radius, a collision is impossible:

   $$d_{horizontal}^2 = (S_x - C_x)^2 + (S_z - C_z)^2$$
   
   $$d_{horizontal}^2 > (r_{cylinder} + r_{sphere})^2 \Rightarrow \text{No Collision}$$

2. **Vertical Range Check**: If the horizontal collision condition is met, the vertical (Y axis) boundaries are checked. It is checked whether the lowest and highest points of the sphere remain within the height range of the cylinder:

   $$C_{y_{base}} = C_y - \frac{h_{cylinder}}{2}$$
   
   $$C_{y_{top}} = C_y + \frac{h_{cylinder}}{2}$$
   
   $$(S_y + r_{sphere} \geq C_{y_{base}}) \land (S_y - r_{sphere} \leq C_{y_{top}}) \Rightarrow \text{Collision Exists}$$

**Code Implementation:**
```java
double distanceX = sphere.getX() - cylinder.getX();
double distanceZ = sphere.getZ() - cylinder.getZ();
double horizontalDistanceSquared = (distanceX * distanceX) + (distanceZ * distanceZ);
double totalRadius = cylinder.getRadius() + sphere.getRadius();

if (horizontalDistanceSquared > (totalRadius * totalRadius)) {
    return false; // No horizontal collision
}

double halfHeight = cylinder.getHeight() / 2.0;
double cylinderBaseLimit = cylinder.getY() - halfHeight;
double cylinderTopLimit = cylinder.getY() + halfHeight;

return (sphere.getY() + sphere.getRadius() >= cylinderBaseLimit)
    && (sphere.getY() - sphere.getRadius() <= cylinderTopLimit);
```

---

### 3.3. Surface - Rectangular Prism Collision

It is the collision of a large surface (floor or wall) with a prism. These types of collisions are used in scenarios such as characters walking on the floor or objects hitting walls.

**Mathematical Approach:**

This algorithm is a simplified version of the "Separating Axis Theorem (SAT)" principle. The distance from the center of the prism to the surface is calculated. Also, a "projection" (impact radius) is determined by projecting the dimensions of the prism (half width, half height, half depth) onto the surface normal.

**Algorithm:**

1. **Prism Projection**: The half dimensions of the prism (halfWidth, halfHeight, halfDepth) are projected onto the surface normal (Nₓ, Nᵧ, Nᵧ):

   $$Projection = \left(\frac{w}{2} \cdot |N_x|\right) + \left(\frac{h}{2} \cdot |N_y|\right) + \left(\frac{d}{2} \cdot |N_z|\right)$$

   This value represents the maximum distance covered by the prism along the surface normal.

2. **Distance Calculation**: The distance of the prism center (Pₓ, Pᵧ, Pᵧ) to the surface is calculated using the plane equation:

   $$Distance = (N_x \cdot P_x) + (N_y \cdot P_y) + (N_z \cdot P_z) + D$$

   Here D is the depth (offset) parameter of the surface.

3. **Collision Check**: If the absolute distance value is less than the projection value, the prism has penetrated the surface:

   $$|Distance| < Projection \Rightarrow \text{Collision Exists}$$

**Code Implementation:**
```java
double halfWidth = prism.getWidth() / 2.0;
double halfHeight = prism.getHeight() / 2.0;
double halfDepth = prism.getDepth() / 2.0;

double prismProjection = (halfWidth * Math.abs(surface.getX()))
                      + (halfHeight * Math.abs(surface.getY()))
                      + (halfDepth * Math.abs(surface.getZ()));

double distancePrismCentreToSurface = (surface.getX() * prism.getX())
                                    + (surface.getY() * prism.getY())
                                    + (surface.getZ() * prism.getZ())
                                    + surface.getDepth();

return Math.abs(distancePrismCentreToSurface) < prismProjection;
```

---

### 3.4. Rectangular Prism - Rectangular Prism Collision

The collision of two rectangular prisms is known as the "Axis-Aligned Bounding Box (AABB)" collision test. This is the most commonly used collision type in 3D games.

![Prism-Prism Collision](screenshots/prism-collision.png)
*Figure 7: Collision detection between two rectangular prisms. Overlap check is performed on every axis (X, Y, Z). If there is overlap on all axes, collision occurs.*

**Algorithm:**

According to the Separating Axis Theorem (SAT), for two AABBs to collide, there must be overlap on all three axes (X, Y, Z). If there is no overlap on any axis, there is no collision.

**Check For Each Axis:**

$$|P1_x - P2_x| < \frac{w_1 + w_2}{2} \Rightarrow \text{Overlap on X axis}$$

$$|P1_y - P2_y| < \frac{h_1 + h_2}{2} \Rightarrow \text{Overlap on Y axis}$$

$$|P1_z - P2_z| < \frac{d_1 + d_2}{2} \Rightarrow \text{Overlap on Z axis}$$

**Collision Condition:**

$$\text{Collision} \Leftrightarrow \text{X overlap} \land \text{Y overlap} \land \text{Z overlap}$$

**Code Implementation:**
```java
boolean xOverlap = Math.abs(prismFirst.getX() - prismSecond.getX()) 
                < (prismFirst.getWidth() + prismSecond.getWidth()) / 2.0;
boolean yOverlap = Math.abs(prismFirst.getY() - prismSecond.getY()) 
                < (prismFirst.getHeight() + prismSecond.getHeight()) / 2.0;
boolean zOverlap = Math.abs(prismFirst.getZ() - prismSecond.getZ()) 
                < (prismFirst.getDepth() + prismSecond.getDepth()) / 2.0;
return xOverlap && yOverlap && zOverlap;
```

---

### 3.5. Capsule - Capsule Collision

The collision of two capsules requires consideration of both their cylindrical bodies and the hemispheres at their ends. Capsules are frequently used in character animations and characteristically shaped objects.

![Capsule-Capsule Collision](screenshots/capsule-collision.png)
*Figure 8: Collision detection between two capsules. Collision of cylindrical bodies in the horizontal plane (XZ), overlap of height ranges in the vertical axis (Y) is checked.*

**Method:**

As a simplified model, when capsules are aligned vertically:

1. **Horizontal Distance Check**: The horizontal distance (XZ plane) between the center axes of the two capsules is checked:

   $$d_{horizontal}^2 = (C1_x - C2_x)^2 + (C1_z - C2_z)^2$$
   
   $$d_{horizontal}^2 > (r_1 + r_2)^2 \Rightarrow \text{No Collision}$$

2. **Vertical Range Check**: Vertically (Y axis), the average of the heights of the two capsules is used as a threshold value:

   $$d_y = |C1_y - C2_y|$$
   
   $$h_{threshold} = \frac{h_1 + h_2}{2}$$
   
   $$d_y < h_{threshold} \Rightarrow \text{Collision Exists}$$

**Code Implementation:**
```java
double dx = capsuleFirst.getX() - capsuleSecond.getX();
double dz = capsuleFirst.getZ() - capsuleSecond.getZ();
double distanceSquared = (dx * dx) + (dz * dz);
double radiusSum = capsuleFirst.getRadius() + capsuleSecond.getRadius();

if (distanceSquared > (radiusSum * radiusSum)) {
    return false; // No horizontal collision
}

double dy = Math.abs(capsuleFirst.getY() - capsuleSecond.getY());
double heightSumHalved = (capsuleFirst.getHeight() + capsuleSecond.getHeight()) / 2.0;
return dy < heightSumHalved;
```

---

## 4. Visual Feedback and User Interaction

### 4.1. Collision Visualization

When a collision is detected, the colors of the relevant shapes automatically turn red. This allows the user to instantly observe collisions. When the collision ends, the shapes return to their original colors.

![Collision Moment](screenshots/capsule-collision.png)
*Figure 9: Shapes turning red at the moment of collision. This visual feedback is critical to verify that the algorithms are working correctly.*

### 4.2. Camera Control

The user can examine the scene from different angles with the mouse:
- **Mouse Drag**: Rotates the camera around X and Y axes
- **Mouse Scroll**: Zooms by moving the camera forward and backward

![Camera Control](screenshots/mouse-scroll.gif)
*Figure 10: The user can examine the scene from different angles by rotating the camera with the mouse. Zooming in and out can be done with scroll.*

### 4.3. Logging System

During each collision check, detailed mathematical calculations are printed to the console using Java Logger. These logs can be used to test the accuracy of the algorithms and for debugging.

**Example Log Output:**
```
INFO: Closest point: (550.0, 600.0)
INFO: Distance between the centre of the circle and the nearest point: (50.0, 0.0)
INFO: (Pythagorean Theorem) Distance squared: 2500.0, Radius squared: 1600.0
INFO: Collision: CircleShape vs RectangleShape. Total: 1
```

---

## 5. Technical Details and Performance

### 5.1. Technologies Used

- **Programming Language**: Java (JDK 21+)
- **Visualization Library**: JavaFX 21 (3D API)
- **Game Engine Library**: FXGL 17.3 (partial usage)
- **Build Tool**: Maven
- **Test Framework**: JUnit 5.10.2
- **Logging**: `java.util.logging`

### 5.2. Performance Optimizations

1. **Use of Square Instead of Square Root**: Performance has improved by comparing square values instead of square root calculation in distance comparisons.

2. **Early Exit**: In some algorithms (e.g. sphere-cylinder, capsule-capsule), if the horizontal distance check fails, false is returned without performing the vertical check.

3. **Static Methods**: All methods in the CollisionManager class are static, thus the cost of object creation is eliminated.

4. **Pattern Matching**: Using Java 21's pattern matching feature, the collision type determination process has been optimized.

### 5.3. Architectural Design Principles

- **Separation of Concerns**: Visualization (Shape) and collision calculations (Collider) are kept in separate classes.
- **Single Responsibility Principle**: Each class has a single responsibility.
- **Open/Closed Principle**: To add new shape types, it is not necessary to change the existing code, only newly classes are added.
- **Dependency Inversion**: GeometricShape depends on the GeometricCollider interface, not on concrete implementations.

---

## 6. Simulation Scenarios

The application includes collision simulations running simultaneously in four different regions:

### Scenario 1: Top Left Region - Circle and Rectangle
- **Shapes**: Blue circle (radius: 40) and Purple rectangle (60x100)
- **Movement**: Both shapes move randomly within boundaries
- **Collision Type**: Closest point algorithm on 2D plane

### Scenario 2: Top Right Region - Two Rectangular Prisms
- **Shapes**: Golden yellow small prism (150x100x50) and Light blue large prism (400x50x150)
- **Movement**: They move at different speeds
- **Collision Type**: AABB (Axis-Aligned Bounding Box) collision test

### Scenario 3: Bottom Left Region - Two Capsules
- **Shapes**: Teal colored capsule (radius: 20, height: 100) and Yellow-green capsule (radius: 30, height: 80)
- **Movement**: They move in different directions and speeds
- **Collision Type**: Horizontal and vertical range check

### Scenario 4: Bottom Right Region - Sphere and Cylinder
- **Shapes**: Blue sphere (radius: 30) and Dark olive green cylinder (radius: 30, height: 100)
- **Movement**: They move in 3D space
- **Collision Type**: Horizontal distance + vertical range check

![All Scenarios](screenshots/circle-collision.png)
![All Scenarios](screenshots/prism-collision.png)
![All Scenarios](screenshots/capsule-collision.png)
![All Scenarios](screenshots/syphere-collision.png)
*Figure 11-14: Collision moment images of four different collision scenarios. Each region tests a different collision algorithm.*

---

## 7. Conclusion and Evaluation

With this study, it has been successfully demonstrated how theoretical physics and geometry formulas can be transformed into a real-time software simulation. The created "Collision Studio" environment offers the opportunity to visually verify the accuracy performance of the algorithms by rendering four different physical interactions simultaneously.

### 7.1. Achievements

- ✅ Successful implementation of five different collision algorithms
- ✅ Real-time visualization and feedback mechanism
- ✅ Modular and extensible code architecture
- ✅ User interactive 3D simulation environment
- ✅ Detailed logging and debugging options

### 7.2. Future Improvements

- **Collision Response**: Currently, only speed is reversed at the moment of collision. Conservation of momentum and energy loss calculations can be added for more realistic physics simulation.
- **Rotation Support**: currently all shapes are axis-aligned. By adding rotation support, more complex collision scenarios can be tested.
- **Spatial Partitioning**: Spatial partitioning algorithms like Octree or BSP Tree can be added for performance optimization for a large number of objects.
- **GJK/EPA Algorithm**: GJK (Gilbert-Johnson-Keerthi) and EPA (Expanding Polytope Algorithm) algorithms can be implemented for more complex shapes.

---

## 8. User Guide

### 8.1. Running the Project

1. **Requirements**: JDK 21 or higher, Maven 3.6+
2. **Compilation**: `mvn clean compile`
3. **Execution**: `mvn javafx:run`
4. **Alternative**: Run `CollisionApplication` class from IDE

### 8.2. Controls

- **Mouse Drag**: Rotate Camera
- **Mouse Scroll**: Zoom in/out
- **Console**: View collision logs

### 8.3. Code Structure

```
src/main/java/com/fati/collision/
├── CollisionApplication.java      # Main application
├── collider/                      # Collision detection classes
│   ├── GeometricCollider.java
│   ├── CollisionManager.java
│   ├── CircleCollider.java
│   ├── RectangleCollider.java
│   ├── SphereCollider.java
│   ├── CylinderCollider.java
│   ├── RectangularPrismCollider.java
│   ├── CapsuleCollider.java
│   └── SurfaceCollider.java
└── shapes/                        # Visualization classes
    ├── GeometricShape.java
    ├── CircleShape.java
    ├── RectangleShape.java
    ├── SphereShape.java
    ├── CylinderShape.java
    ├── RectangularPrismShape.java
    └── CapsuleShape.java
```
---

**Prepared by**: Fatih CELIK  
**Date**: 2025  
**Version**: 1.0
