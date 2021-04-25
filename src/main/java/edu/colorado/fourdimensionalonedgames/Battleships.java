package edu.colorado.fourdimensionalonedgames;

import edu.colorado.fourdimensionalonedgames.game.Game;
import edu.colorado.fourdimensionalonedgames.game.GameSettings;
import edu.colorado.fourdimensionalonedgames.render.Render;
import javafx.application.Application;
import javafx.stage.Stage;

//Main entry point for the application, but for the jar executable, the main entry point is GUIStarter.java
public class Battleships extends Application {

    //various game settings
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
