module edu.colorado.fourdimensionalonedgames {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.junit.jupiter.api;
    requires org.junit.jupiter.engine;

    opens edu.colorado.fourdimensionalonedgames to javafx.fxml;
    exports edu.colorado.fourdimensionalonedgames;
    exports edu.colorado.fourdimensionalonedgames.game;
    exports edu.colorado.fourdimensionalonedgames.game.attack;
    exports edu.colorado.fourdimensionalonedgames.game.attack.upgrades;
    exports edu.colorado.fourdimensionalonedgames.game.attack.behavior;
    exports edu.colorado.fourdimensionalonedgames.game.attack.weapon;
    exports edu.colorado.fourdimensionalonedgames.game.ship;
    exports edu.colorado.fourdimensionalonedgames.render;
    exports edu.colorado.fourdimensionalonedgames.render.gui;
    exports edu.colorado.fourdimensionalonedgames.render.tile;
}
