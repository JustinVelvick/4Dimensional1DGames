package edu.colorado.fourdimensionalonedgames.game;

import edu.colorado.fourdimensionalonedgames.MainSceneController;
import edu.colorado.fourdimensionalonedgames.render.*;
import edu.colorado.fourdimensionalonedgames.render.tile.LetterTile;
import edu.colorado.fourdimensionalonedgames.render.tile.Tile;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Game {

    private MenuSceneController menuSceneController;
    private Player1SceneController player1SceneController;
    private Player2SceneController player2SceneController;
    private Render renderer;
    private int numberofPlayers;
    private int tileSize;
    private int columns;
    private int rows;

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

        //Load our main scene and set up it's controller
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("menuScene.fxml"));
        HBox root = loader.load();

        MenuSceneController controller = loader.getController();



        this.menuSceneController = menuSceneController;
        this.renderer = renderer;
        this.tileSize = tileSize;
        this.columns = columns;
        this.rows = rows;
        this.numberofPlayers = numberofPlayers;


        //create the players for this game (and their boards in the process)
        for(int i = 0; i < this.numberofPlayers; i++){
            Player newPlayer = new Player(new Board(columns, rows, this.renderer));
            this.players.add(newPlayer);
        }

        //create the visual boards with canvas tiles (should be in for loop above, but need to refactor how our grids are stored)
        this.players.get(0).getBoard().initializeBoard(menuSceneController.getGpane1());
        this.players.get(1).getBoard().initializeBoard(menuSceneController.getGpane2());

        //pass JavaFX controller the game object so it has access to players, boards, etc
        this.menuSceneController.initialize(this);

    }

    //called when a player's turn is over
    public void passTurn(){

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
