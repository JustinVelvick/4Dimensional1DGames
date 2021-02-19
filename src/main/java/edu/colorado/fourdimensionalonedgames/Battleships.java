package edu.colorado.fourdimensionalonedgames;

import java.util.Random;

import edu.colorado.fourdimensionalonedgames.gui.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Battleships extends Application {
    // fields
    static int speed = 5;
    static int tileSize = 40;
    static int rows = 10;
    static int columns = 10;

    //width and height of each player's board/grid
    static int width = columns*tileSize;
    static int height = rows*tileSize;


    public void start(Stage primaryStage) {
        try {

            /*vertical column container holding children (in this case just the canvas, but other elements can be added
              later akin to html*/


            //width,height, tileSize, Color.BLACK
            //grid extends JavaFx's Canvas class, which is effectively what we draw on
            Render renderer = new Render();


            Board blankBoard1 = new Board(columns, rows, renderer);
            Board blankBoard2 = new Board(columns, rows, renderer);

            Orientation direction = Orientation.left;
            double x = 5;
            double y = 4;
            Point2D origin = new Point2D(x,y);
            Battleship newShip = new Battleship();

            Orientation direction2 = Orientation.right;
            double x2 = 7;
            double y2 = 6;
            Point2D origin2 = new Point2D(x2,y2);
            Minesweeper newShip2 = new Minesweeper();

            Orientation direction3 = Orientation.down;
            double x3 = 2;
            double y3 = 7;
            Point2D origin3 = new Point2D(x3,y3);
            Destroyer newShip3 = new Destroyer();

            blankBoard1.placeShip(direction, origin, newShip);
            blankBoard1.placeShip(direction2, origin2, newShip2);
            blankBoard1.placeShip(direction3, origin3, newShip3);


/*            StackPane player1Layout = new StackPane();
            player1Layout.getChildren().add();
            SubScene player1SubScene = new SubScene(player1Layout, width, height);

            StackPane player2Layout = new StackPane();
            player1Layout.getChildren().add();
            SubScene player2SubScene = new SubScene(player2Layout, width, height);
*/

            //Stage >> Scene >> Container >> Node
            HBox root = new HBox();
            //HBox root = new HBox(10);
            //root.setAlignment(Pos.CENTER);
            //add our canvas/grid to the VBox container
            root.getChildren().addAll(blankBoard1.grid, blankBoard2.grid);


            //Both sides of the game will have its own Renderer





            new AnimationTimer() {
                long lastTick = 0;

                public void handle(long now) {
                    if (lastTick == 0) {
                        lastTick = now;
                        //call tick to display an image since we're in a new frame
                        renderer.tick();
                        return;
                    }

                    if (now - lastTick > 1000000000 / speed) {
                        lastTick = now;
                        renderer.tick();
                    }
                }

            }.start();

            Scene mainScene = new Scene(root, (width*2.5) , (height*1.2));


            primaryStage.setScene(mainScene);
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
