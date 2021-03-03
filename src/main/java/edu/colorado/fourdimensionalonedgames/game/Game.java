package edu.colorado.fourdimensionalonedgames.game;

import edu.colorado.fourdimensionalonedgames.MenuSceneController;
import edu.colorado.fourdimensionalonedgames.Player1Controller;
import edu.colorado.fourdimensionalonedgames.Player2Controller;
import edu.colorado.fourdimensionalonedgames.render.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Game {

    private Stage primaryStage;
    private MenuSceneController menuSceneController;
    private Player1Controller player1Controller;
    private Player2Controller player2Controller;

    private Scene player1Scene;
    private Scene player2Scene;
    private Scene menuScene;

    private Render renderer;
    private int numberofPlayers;
    private int tileSize;
    private int columns;
    private int rows;

    //width and height of each player's board/grid
    private int width;
    private int height;

    private boolean player1Turn;

    private List<Player> players = new ArrayList<>();



    //Game constructor which creates the Player objects
    /**
     * @param renderer (One renderer to contain all IRenderable objects)
     * @param numberofPlayers (number of players in the game)
     * @param tileSize (size of each tile in pixels
     * @param columns (number of columns on a single battleships board)
     * @param rows (number of rows on a single battleships board)
     */
    public Game(Stage primaryStage, Render renderer, int numberofPlayers, int tileSize, int columns, int rows) throws IOException {

        //set all of our private game attributes
        this.renderer = renderer;
        this.tileSize = tileSize;
        this.columns = columns;
        this.rows = rows;
        this.width = columns*tileSize;
        this.height = columns*tileSize;
        this.numberofPlayers = numberofPlayers;
        this.player1Turn = false;

        //create the players for this game (and their boards in the process)
        for(int i = 0; i < this.numberofPlayers; i++){
            Player newPlayer = new Player(new Board(columns, rows, this.renderer), new Board(columns, rows, this.renderer));
            this.players.add(newPlayer);
        }




        startGame(primaryStage);

    }


    //called ONCE on game start
    private void startGame(Stage primaryStage) throws IOException {
        //Store our application's parent stage (the stage we set all our scenes to upon turn switch)
        this.primaryStage = primaryStage;


        //Load our menu scene and accompying controller and store them
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("menuScene.fxml"));
        Pane menuRoot = loader.load();
        this.menuScene = new Scene(menuRoot, (width*2.5), (height*1.3));

        this.menuSceneController = loader.getController();
        this.menuSceneController.initialize(this);


        //Load our player1Scene and accompying controller and store them
        loader = new FXMLLoader(getClass().getClassLoader().getResource("player1Scene.fxml"));
        HBox player1Root = loader.load();
        this.player1Scene = new Scene(player1Root, (width*2.5) , (height*1.3));

        this.player1Controller = loader.getController();
        this.player1Controller.initialize(this);


        //Load our player2Scene and accompying controller and store them
        loader = new FXMLLoader(getClass().getClassLoader().getResource("player2Scene.fxml"));
        HBox player2Root = loader.load();
        this.player2Scene = new Scene(player2Root, (width*2.5) , (height*1.3));

        this.player2Controller= loader.getController();
        this.player2Controller.initialize(this);


        //initialize both player's boards (visually)
        initializeBoardVisuals();


        //Change taskbar icon in client's OS
        Image icon = new Image("icon.jpg");
        this.primaryStage.getIcons().add(icon);

        this.primaryStage.setScene(menuScene);
        this.primaryStage.setTitle("Battleships");
//        this.primaryStage.setMinHeight(250);
//        this.primaryStage.setMinWidth(500);
//
//        this.primaryStage.setMaxHeight(500);
//        this.primaryStage.setMaxWidth(1000);
        this.primaryStage.show();
    }





    private void initializeBoardVisuals(){
        players.get(0).getBoard().initializeBoard(player1Controller.getPlayergpane());
        players.get(0).getEnemyBoard().initializeBoard(player1Controller.getEnemygpane());
        players.get(1).getBoard().initializeBoard(player2Controller.getPlayergpane());
        players.get(1).getEnemyBoard().initializeBoard(player2Controller.getEnemygpane());
    }


    //called when a player's turn is over
    public void passTurn(){
        this.player1Turn = !this.player1Turn;

        //have JavaFX open up our player1Scene.fxml
        if(this.player1Turn){


            this.primaryStage.setTitle("Player 1's Turn");
            this.primaryStage.setScene(this.player1Scene);
            this.primaryStage.show();
        }
        //have JavaFX open up our player2Scene.fxml
        else{




            this.primaryStage.setTitle("Player 2's Turn");
            this.primaryStage.setScene(this.player2Scene);
            this.primaryStage.show();
        }

    }

    //checks if someone has won the game
    public boolean gameOver(){
        return false;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Render getRenderer() {
        return renderer;
    }
}
