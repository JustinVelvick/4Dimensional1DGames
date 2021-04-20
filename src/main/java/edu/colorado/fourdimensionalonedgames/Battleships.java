package edu.colorado.fourdimensionalonedgames;

import edu.colorado.fourdimensionalonedgames.game.Board;
import edu.colorado.fourdimensionalonedgames.game.Game;
import edu.colorado.fourdimensionalonedgames.game.GameSettings;
import edu.colorado.fourdimensionalonedgames.game.GameState;
import edu.colorado.fourdimensionalonedgames.render.Render;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.Scene;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Battleships extends Application {
    // fields
    static int speed = 5;
    static int tileSize = 40;
    static int rows = 10;
    static int columns = 10;
    static int depth = 2; //surface and 1 tile below surface
    static int numberOfPlayers = 2;

    public void start(Stage primaryStage) {
        try {
            //The one renderer object the program will contain every IRenderable Object
            Render renderer = new Render();
            //Define the game settings
            GameSettings settings = new GameSettings(numberOfPlayers, columns, rows, depth, tileSize);
            //Create the Singleton game object
            Game.getInstance(primaryStage, renderer, settings);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
