module edu.colorado.fourdimensionalonedgames {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.junit.jupiter.api;
    requires org.junit.jupiter.engine;
    //requires org.junit.platform.commons;

    opens edu.colorado.fourdimensionalonedgames to javafx.fxml;
    exports edu.colorado.fourdimensionalonedgames;
    exports edu.colorado.fourdimensionalonedgames.render;

}