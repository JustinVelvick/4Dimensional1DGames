package edu.colorado.fourdimensionalonedgames;

import edu.colorado.fourdimensionalonedgames.game.Board;
import edu.colorado.fourdimensionalonedgames.game.Game;
import edu.colorado.fourdimensionalonedgames.render.Render;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
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

            //The one renderer object the program will have to contain every IRenderable Object
            Render renderer = new Render();

            //Create the game object
            Game newGame = Game.getInstance(primaryStage, renderer, numberOfPlayers, tileSize, columns, rows, depth);

            //To give a refresh rate to our display
            new AnimationTimer() {
                long lastTick = 0;

                public void handle(long now) {
                    if (lastTick == 0) {
                        lastTick = now;
                        //call tick to display an image since we're in a new frame
                        newGame.getRenderer().tick();
                        return;
                    }

                    if (now - lastTick > 1000000000 / speed) {
                        lastTick = now;
                        newGame.getRenderer().tick();
                    }
                }

            }.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
