package edu.colorado.fourdimensionalonedgames.game;

import edu.colorado.fourdimensionalonedgames.MenuSceneController;
import edu.colorado.fourdimensionalonedgames.PlayerController;
import edu.colorado.fourdimensionalonedgames.game.attack.behavior.Attack;
import edu.colorado.fourdimensionalonedgames.game.attack.behavior.Reveal;
import edu.colorado.fourdimensionalonedgames.game.attack.upgrades.TierOneUpgrade;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.LargeWeapon;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.PenetratingSmallWeapon;
import edu.colorado.fourdimensionalonedgames.game.ship.Ship;
import edu.colorado.fourdimensionalonedgames.render.*;
import edu.colorado.fourdimensionalonedgames.render.tile.Tile;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Game {

    private static Game uniqueInstance;

    private Stage primaryStage;
    private MenuSceneController menuSceneController;
    private PlayerController player1Controller;
    private PlayerController player2Controller;

    private Scene player1Scene;
    private Scene player2Scene;
    private Scene menuScene;

    private final Render renderer;
    private final int numberofPlayers;
    private final int tileSize;
    private final int columns;
    private final int rows;

    //width, height, and depth of each player's board/grid
    private final int width; //x
    private final int height; //y
    private final int depth; //z

    private boolean player1Turn;

    private final List<Player> players = new ArrayList<>();

    public final static String SINGLE_SHOT = "Single Shot";
    public final static String SONAR_PULSE = "Sonar Pulse";
    public final static String SPACE_LASER = "Space Laser";


    //implements Singleton design pattern for Game object (only one is allowed to exist)
    public static synchronized Game getInstance(Stage primaryStage, Render renderer, int numberofPlayers, int tileSize, int columns, int rows, int depth) throws IOException {
        if(uniqueInstance == null){
            uniqueInstance = new Game(primaryStage, renderer, numberofPlayers, tileSize, columns, rows, depth);
        }
        return uniqueInstance;
    }


    //Game constructor which creates the Player objects
    /**
     * @param renderer (One renderer to contain all IRenderable objects)
     * @param numberofPlayers (number of players in the game)
     * @param tileSize (size of each tile in pixels
     * @param columns (number of columns on a single battleships board)
     * @param rows (number of rows on a single battleships board)
     */
    private Game(Stage primaryStage, Render renderer, int numberofPlayers, int tileSize, int columns, int rows, int depth) throws IOException {

        //set all of our private game attributes
        this.renderer = renderer;
        this.tileSize = tileSize;
        this.columns = columns;
        this.rows = rows;
        this.depth = depth;
        this.width = columns*tileSize;
        this.height = columns*tileSize;
        this.numberofPlayers = numberofPlayers;
        this.player1Turn = false;

        //create the players for this game (and their boards in the process)
        for(int i = 0; i < this.numberofPlayers; i++){
            Player newPlayer = new Player(this, new Board(columns, rows, depth, this.renderer), new Board(columns, rows, depth, this.renderer));
            this.players.add(newPlayer);
        }

        startGame(primaryStage);
    }

    ////////////////GAME CONSTRUCTOR FOR TESTS///////////////////////

    //Overloaded constructor for testing, the only difference is that this method does not call startGame (loads GUI)
    //This constructor does NOT take in a stage as it's just for non visual, logic testing
    public Game(Render renderer, int numberofPlayers, int tileSize, int columns, int rows, int depth){
        //set all of our private game attributes
        this.renderer = renderer;
        this.tileSize = tileSize;
        this.columns = columns;
        this.rows = rows;
        this.depth = depth;
        this.width = columns*tileSize;
        this.height = columns*tileSize;
        this.numberofPlayers = numberofPlayers;
        this.player1Turn = true;

        //create the players for this game (and their boards in the process)
        for(int i = 0; i < this.numberofPlayers; i++){
            Player newPlayer = new Player(this, new Board(columns, rows, depth, this.renderer), new Board(columns, rows, depth, this.renderer));
            this.players.add(newPlayer);
        }

        initializeBoards();
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
        this.player1Controller.initialize(this, getPlayers().get(0), getPlayers().get(1));


        //Load our player2Scene and accompying controller and store them
        loader = new FXMLLoader(getClass().getClassLoader().getResource("player2Scene.fxml"));
        HBox player2Root = loader.load();
        this.player2Scene = new Scene(player2Root, (width*2.5) , (height*1.3));

        this.player2Controller= loader.getController();
        this.player2Controller.initialize(this, getPlayers().get(1), getPlayers().get(0));


        //initialize both player's boards (visually via linking board Tiles with FXML GridPanes)
        initializeBoards();
        linkBoardVisuals();


        //Change taskbar icon in client's OS
        Image icon = new Image("icon.jpg");
        this.primaryStage.getIcons().add(icon);

        this.primaryStage.setScene(menuScene);
        this.primaryStage.setTitle("Battleships");
        this.primaryStage.show();
    }

    private void initializeBoards(){
        //Player 1's own board
        players.get(0).getBoard().initializeBoard();
        //Player 1's enemy board
        players.get(0).getEnemyBoardGui().initializeBoard();
        //Player 2's own board
        players.get(1).getBoard().initializeBoard();
        //Player 2's enemy board
        players.get(1).getEnemyBoardGui().initializeBoard();
    }

    ////////////////////////////////////  GUI RELATED METHODS //////////////////////////////////////////////
    private void linkBoardVisuals(){
        //Player 1's own gpane
        players.get(0).getBoard().linkBoardVisuals(player1Controller.getPlayergpane());
        //Player 1's enemy gpane
        players.get(0).getEnemyBoardGui().linkBoardVisuals(player1Controller.getEnemygpane());
        //Player 2's own gpane
        players.get(1).getBoard().linkBoardVisuals(player2Controller.getPlayergpane());
        //Player 2's enemy gpane
        players.get(1).getEnemyBoardGui().linkBoardVisuals(player2Controller.getEnemygpane());
    }

    //Method for testing, does same thing as initializeBoardVisuals, but does not initialize gridpanes
    //This method only gets called from game constructor that is for Testing purposes


    //logic for deciding what tiles to render
    //place ifs and switches for revealed, shot, depth (z) position, if ship is above my z, etc
    //TODO - Implement GridPane updating based on flags and z position
    public void updateEnemyGpane(Board board, GridPane gpane){
        Tile newtile;
        Tile oldTile;
        for (int i = 1; i <= columns; i++) {
            for (int j = 1; j <= rows; j++) {

                oldTile = board.tiles[i][j][0];
                //tile we will be adding to enemy gpane is actual tile on the enemy's board if its been shot
                if(oldTile.revealed){
                    newtile = board.tiles[i][j][0];
                    gpane.getChildren().remove(oldTile);
                    gpane.add(newtile, i, j);
                    this.renderer.unregister(oldTile);
                    this.renderer.register(newtile);
                }
            }
        }
    }

    //called when turns are passed back and forth
    public void updateScene(){
        //have JavaFX open up our player1Scene.fxml
        if(this.player1Turn){
            updateEnemyGpane(getPlayers().get(0).getEnemyBoardGui(), player1Controller.getEnemygpane());
            this.primaryStage.setTitle("Player 1's Turn");
            this.primaryStage.setScene(this.player1Scene);
            this.primaryStage.show();
        }
        //have JavaFX open up our player2Scene.fxml
        else{
            updateEnemyGpane(getPlayers().get(1).getEnemyBoardGui(), player2Controller.getEnemygpane());
            this.primaryStage.setTitle("Player 2's Turn");
            this.primaryStage.setScene(this.player2Scene);
            this.primaryStage.show();
        }
    }

    /////////////////////////////  LOGIC, NON GUI RELATED METHODS /////////////////////////////////////
    //called when a player's turn is over
    public void passTurn(){
        this.player1Turn = !this.player1Turn;
    }

    public void updateScores(){
        Player player1 = getPlayers().get(0);
        Player player2 = getPlayers().get(1);
        int player1score = player1.getScore();
        int player2score = player2.getScore();

        for(Ship ship : player1.getFleet().getDestroyedShips()){
            player2score += ship.size;
        }
        for(Ship ship : player2.getFleet().getDestroyedShips()){
            player1score += ship.size;
        }

        player1.setScore(player1score);
        player2.setScore(player2score);
    }

    public void checkUpgrades() {
        //CHECKING FOR UPGRADE UNLOCKS
        for(Player player : players){
            if(player.getUpgradeStatus() == TierOneUpgrade.UNLOCKED){
                player.setUpgradeStatus(TierOneUpgrade.USED);
                //player gets 2 sonar pulses for the rest of the game to use
                player.addWeapon(new LargeWeapon(new Reveal(), Game.SONAR_PULSE));
                player.addWeapon(new LargeWeapon(new Reveal(), Game.SONAR_PULSE));
                //player replaces Single Shot weapon with Space Laser weapon
                player.removeWeapon(Game.SINGLE_SHOT);
                player.addWeapon(new PenetratingSmallWeapon(new Attack(), Game.SPACE_LASER));
            }
        }
    }



    //checks if someone has won the game
    public boolean gameOver(){
        boolean over = true;

        if (getPlayers().get(0).getFleet().hasShip() && getPlayers().get(1).getFleet().hasShip()){
            over = false;
        }
        return over;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Render getRenderer() {
        return renderer;
    }

    public PlayerController getPlayer1Controller() {
        return player1Controller;
    }

    public PlayerController getPlayer2Controller() {
        return player2Controller;
    }

    public boolean isPlayer1Turn(){
        return player1Turn;
    }
}
