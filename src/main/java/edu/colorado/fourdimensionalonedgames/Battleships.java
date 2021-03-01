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
    static int numberOfPlayers = 2;

    //width and height of each player's board/grid
    static int width = columns*tileSize;
    static int height = rows*tileSize;


    public void start(Stage primaryStage) {
        try {

            //The one renderer object the program will have to contain every IRenderable Object
            Render renderer = new Render();

            //Create the game object
            Game newGame = new Game(renderer, numberOfPlayers, speed, tileSize, columns, rows);



            //Load our main scene and set up it's controller
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("mainScene.fxml"));
            HBox root = loader.load();

            MainSceneController controller = loader.getController();

            controller.initializeBoards(newGame.getPlayers().get(0).getBoard(), newGame.getPlayers().get(1).getBoard());



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


            //Set up our JavaFX scene and stage
            Scene mainScene = new Scene(root, (width*2.5) , (height*1.3));

            //Change taskbar icon in client's OS
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
