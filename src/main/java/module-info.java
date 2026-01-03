module com.fati.collision {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;
    requires java.logging;
    requires annotations;

    opens com.fati.collision to javafx.fxml;
    exports com.fati.collision;
    exports com.fati.collision.collider;
    exports com.fati.collision.shapes;
    opens com.fati.collision.collider to javafx.fxml;
}