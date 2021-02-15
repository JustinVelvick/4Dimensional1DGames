package edu.colorado.fourdimensionalonedgames;

import java.util.Random;

import edu.colorado.fourdimensionalonedgames.gui.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Battleships extends Application {
    // fields
    static int speed = 5;
    static int tileSize = 20;

    static int width = 900;
    static int height = 450;


    Grid grid;

    public void start(Stage primaryStage) {
        try {

            /*vertical column container holding children (in this case just the canvas, but other elements can be added
              later akin to html*/

            Group root = new Group(); //might want to use Group instead of VBox...


            //grid extends JavaFx's Canvas class, which is effectively what we draw on
            grid = new Grid(width,height, tileSize, Color.BLACK);

            //graphics context is what we call all of our draw functions on ie. drawline, oval, etc

            //add our canvas/grid to the VBox container
            root.getChildren().add(grid);

            //I made a method for render called "tick" which, via the code below, is called once a frame
            Render renderer = new Render();

            new AnimationTimer() {
                long lastTick = 0;

                public void handle(long now) {
                    if (lastTick == 0) {
                        lastTick = now;
                        //call tick to display an image since we're in a new frame
                        renderer.tick(grid);
                        return;
                    }

                    if (now - lastTick > 1000000000 / speed) {
                        lastTick = now;
                        renderer.tick(grid);
                    }
                }

            }.start();

            Scene scene = new Scene(root, width , height);


            primaryStage.setScene(scene);
            primaryStage.setTitle("Battleships");
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

        launch(args);
    }
}
