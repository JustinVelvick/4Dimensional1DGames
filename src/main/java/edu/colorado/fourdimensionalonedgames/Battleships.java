package edu.colorado.fourdimensionalonedgames;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
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

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("scene1.fxml"));
            HBox root = loader.load();


            FXMLController controller = loader.getController();

            controller.initializeBoards(blankBoard1, blankBoard2);



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

            Scene mainScene = new Scene(root, (width*2.5) , (height*1.3));

            //To change taskbar icon in client's OS
            Image icon = new Image("icon.jpg");
            primaryStage.getIcons().add(icon);

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
