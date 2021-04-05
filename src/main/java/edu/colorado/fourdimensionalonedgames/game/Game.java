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
import edu.colorado.fourdimensionalonedgames.render.gui.AlertBox;
import edu.colorado.fourdimensionalonedgames.render.gui.Display;
import edu.colorado.fourdimensionalonedgames.render.gui.EnemyDisplay;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    private GameState gameState;

    private Stage primaryStage;
    private final Render renderer;
    private MenuSceneController menuSceneController;
    private PlayerController player1Controller;
    private PlayerController player2Controller;

    private Scene player1Scene;
    private Scene player2Scene;
    private Scene menuScene;

    private Display player1Display;
    private EnemyDisplay player1EnemyDisplay;
    private Display player2Display;
    private EnemyDisplay player2EnemyDisplay;


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
        this.gameState = GameState.player1_setup;

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
        this.gameState = GameState.player1_setup;

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
        this.menuScene = new Scene(menuRoot, (width*2), (height*1.2));

        this.menuSceneController = loader.getController();
        this.menuSceneController.initialize(this);


        //Load our player1Scene and accompying controller and store them
        loader = new FXMLLoader(getClass().getClassLoader().getResource("player1Scene.fxml"));
        HBox player1Root = loader.load();
        this.player1Scene = new Scene(player1Root, (width*2.3) , (height*1.3));

        this.player1Controller = loader.getController();
        this.player1Controller.initialize(this, getPlayers().get(0), getPlayers().get(1));


        //Load our player2Scene and accompying controller and store them
        loader = new FXMLLoader(getClass().getClassLoader().getResource("player2Scene.fxml"));
        HBox player2Root = loader.load();
        this.player2Scene = new Scene(player2Root, (width*2.3) , (height*1.3));

        this.player2Controller= loader.getController();
        this.player2Controller.initialize(this, getPlayers().get(1), getPlayers().get(0));


        //sets up observer pattern between boards and grid panes
        linkBoardVisuals(player1Controller.getPlayergpane(), player1Controller.getEnemygpane(), player2Controller.getPlayergpane(), player2Controller.getEnemygpane());
        //initialize both player's boards (visually via linking board Tiles with FXML GridPanes)
        initializeBoards();

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
    private void linkBoardVisuals(GridPane p1gpane, GridPane p1Enemygpane, GridPane p2gpane, GridPane p2Enemygpane){

        Board player1Board = players.get(0).getBoard();
        Board player1EnemyBoard = players.get(0).getEnemyBoardGui();
        Board player2Board = players.get(1).getBoard();
        Board player2EnemyBoard = players.get(1).getEnemyBoardGui();

        player1Display = new Display(p1gpane, player1Board.tiles, renderer);
        player1EnemyDisplay = new EnemyDisplay(p1Enemygpane, player1EnemyBoard.tiles, renderer);
        player2Display = new Display(p2gpane, player2Board.tiles, renderer);
        player2EnemyDisplay = new EnemyDisplay(p2Enemygpane, player2EnemyBoard.tiles, renderer);

        player1Board.registerObserver(player1Display);
        player2Board.registerObserver(player2Display);

        player1EnemyBoard.registerObserver(player1EnemyDisplay);
        player2EnemyBoard.registerObserver(player2EnemyDisplay);
    }

    /////////////////////////////  LOGIC, NON GUI RELATED METHODS /////////////////////////////////////

    //called once user presses start game from the main menu, gives restricted environment to force players to
    //put all of their ships down before they can pass turns
    public void passSetupTurn(){

        if(gameState == GameState.player1_setup){
            //Player 1's placing of ships
            this.primaryStage.setTitle("Player 1: Ship Placement");
            this.primaryStage.setScene(this.player1Scene);

            player1Controller.showSetupButtons();

            AlertBox.display("Place your ships!", "Place all ships down one by one on desired tiles.");

            this.primaryStage.show();
        }
        if(gameState == GameState.player2_setup){
            //Player 2's placing of ships
            this.primaryStage.setTitle("Player 2: Ship Placement");
            this.primaryStage.setScene(this.player2Scene);

            player2Controller.showSetupButtons();

            AlertBox.display("Place your ships!", "Place all ships down one by one on desired tiles.");
            this.primaryStage.show();
        }
    }

    //called when a player's turn is over
    public void passTurn(){
        this.player1Turn = !this.player1Turn;
        //to give each player a fresh, empty fleet movement stack for the next turn
        for(Player player : players){
            player.refreshFleetController();
        }
        updateScene();
    }

    //called when turns are passed back and forth
    public void updateScene(){

        //have JavaFX open up our player1Scene.fxml
        if(this.player1Turn){
            this.primaryStage.setTitle("Player 1's Turn");
            this.primaryStage.setScene(this.player1Scene);
            this.primaryStage.show();
            if(gameState == GameState.first_turn){
                AlertBox.display("Begin Combat!", "Welcome to the first turn Player 1! Please update this bland message :)");
            }
        }
        //have JavaFX open up our player2Scene.fxml
        else{
            this.primaryStage.setTitle("Player 2's Turn");
            this.primaryStage.setScene(this.player2Scene);
            this.primaryStage.show();
            if(gameState == GameState.first_turn){
                AlertBox.display("Begin Combat!", "Welcome to the first turn Player 2! Please update this bland message :)");
                setGameState(GameState.main_state);
            }
        }
    }

    public void updateScores(){
        Player player1 = getPlayers().get(0);
        Player player2 = getPlayers().get(1);
        int player1score = player1.getScore();
        int player2score = player2.getScore();

        for(Ship ship : player1.getFleet().getDestroyedShips()){
            player2score += ship.getSize();
        }
        for(Ship ship : player2.getFleet().getDestroyedShips()){
            player1score += ship.getSize();
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

    public GameState getGameState(){
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
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
