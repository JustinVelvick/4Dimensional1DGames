package edu.colorado.fourdimensionalonedgames;

import edu.colorado.fourdimensionalonedgames.game.Game;
import edu.colorado.fourdimensionalonedgames.render.Render;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;

public class Battleships extends Application {
    // fields
    static int speed = 10;
    static int tileSize = 40;
    static int rows = 10;
    static int columns = 10;
    static int numberOfPlayers = 2;


    public void start(Stage primaryStage) {
        try {

            //The one renderer object the program will have to contain every IRenderable Object
            Render renderer = new Render();

            //Create the game object
            Game newGame = new Game(primaryStage, renderer, numberOfPlayers, tileSize, columns, rows);

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
