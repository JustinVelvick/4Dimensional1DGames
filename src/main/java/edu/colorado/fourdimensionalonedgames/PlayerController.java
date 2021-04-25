package edu.colorado.fourdimensionalonedgames;

import edu.colorado.fourdimensionalonedgames.game.Board;
import edu.colorado.fourdimensionalonedgames.game.Game;
import edu.colorado.fourdimensionalonedgames.game.GameState;
import edu.colorado.fourdimensionalonedgames.game.Player;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import edu.colorado.fourdimensionalonedgames.game.attack.InvalidAttackException;
import edu.colorado.fourdimensionalonedgames.game.ship.Orientation;
import edu.colorado.fourdimensionalonedgames.game.ship.Ship;
import edu.colorado.fourdimensionalonedgames.render.gui.AlertBox;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerFireInput;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerShipInput;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class PlayerController implements Initializable {

    private Game game;
    private Player player;
    private Player enemyPlayer;

    @FXML
    private HBox playerSceneHbox;

    @FXML
    private Canvas playerGrid;

    @FXML
    private Canvas enemyGrid;

    @FXML
    private Pane moveInstructionsPane;

    @FXML
    private Button placeShipButton;

    @FXML
    private Button moveFleetButton;
    @FXML
    private Button undoMoveFleetButton;

    @FXML
    private Button passTurnButton;

    @FXML
    private Button fireWeaponButton;

    EventHandler<KeyEvent> handler = new EventHandler<>() {
        @Override
        public void handle(KeyEvent event) {
            switch (event.getCode()) {
                case W:
                    player.getFleetController().moveFleet(Orientation.up);
                    break;
                case S:
                    player.getFleetController().moveFleet(Orientation.down);
                    break;
                case A:
                    player.getFleetController().moveFleet(Orientation.left);
                    break;
                case D:
                    player.getFleetController().moveFleet(Orientation.right);
                    break;
                default:
                    break;
            }

            //after each individual move, check if game ended from player ramming own ships into mines
            if (game.isGameOver()) {
                game.gameOver();
            }
        }
    };


    //We call this at the creation of any new Player1Scene
    //The controller needs the game class in order to inform it that the pass turn button has been pressed
    //PARAMS: game class, player that owns this controller, enemy player of this controller
    public void initialize(Game game, Player thisPlayer, Player enemyPlayer) {
        this.game = game;
        this.player = thisPlayer;
        this.enemyPlayer = enemyPlayer;
    }

    @FXML
    public void handleFireWeaponButton(ActionEvent event) throws IOException {
        //open a new fireForm and get results from the form stored as a PlayerFireInput object
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fireForm.fxml"));
        Pane root = loader.load();

        FireFormController formController = loader.getController();
        //populate weapons list
        formController.initialize();
        formController.populateFireForm(this.player.getWeapons());

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Fire Weapon Form");
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.showAndWait();

        //object that will store the user's input
        PlayerFireInput userInput = formController.userInput();

        //fire weapon at enemy's actual board
        fireWeapon(enemyPlayer.getBoard(), userInput);
        //checks for conditional items (such as gaining sonar pulse after sinking first ship of the game)
        game.updateScores();
        game.checkUpgrades();
        if (game.isGameOver()) {
            game.gameOver();
        }
        fireWeaponButton.visibleProperty().setValue(false);
    }

    //generates appropriate AlertBox to user based on what resulted in attack
    public void fireWeapon(Board opponentBoard, PlayerFireInput userInput) {

        List<AttackResult> results;

        //a bunch of checks to display appropriate message boxes for each attack result
        try {
            //your enemy's real board and your own view of their board
            results = player.attack(opponentBoard, userInput);

            //Inform player of result of attacking each enemy tile (could be 1 tile or many)
            for (AttackResult attackResult : results) {
                Ship attackedShip = attackResult.getShip();

                if (attackedShip == null) {
                    if (!userInput.getWeaponChoice().equals("Nuke")) {
                        AlertBox.display("Miss", "Shot missed");
                    }
                } else {
                    if (attackedShip.destroyed()) {
                        AlertBox.display("Ship Sunk", "The enemy's " + attackedShip.getType() + " has been sunk!");
                    } else {
                        AlertBox.display("Ship Hit", "Ship has been hit");
                    }
                }
            }
        } catch (InvalidAttackException e) {
            AlertBox.display("Invalid Coordinates", e.getErrorMsg());
        }
    }

    //spawn a ShipChoiceForm, populate it's fields, and retrieve user input from ShipChoiceForm when it closes
    public void handlePlaceShipButton(ActionEvent event) throws IOException {
        //load and pop up the ShipChoiceForm
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("shipChoiceForm.fxml"));
        Pane root = loader.load();

        ShipChoiceFormController formController = loader.getController();

        //initialize form fields with updated list of ships in player's shipsToPlace
        formController.populateShipForm(this.player.getShipsToPlace());

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Ship Placement Form");
        stage.initModality(Modality.APPLICATION_MODAL);

        //open a new shipChoiceForm and get results from the form stored as a PlayerShipInput object
        stage.showAndWait();
        PlayerShipInput userInput = formController.display();
        placeShip(userInput);
    }

    public void placeShip(PlayerShipInput input) {

        //if placing a ship down on player's board fails
        if (!this.player.placeShip(input)) {
            AlertBox.display("Invalid Ship Placement", "Please place your ship on valid coordinates.");
        } else {
            if (player.getShipsToPlace().size() == 0) {
                passTurnButton.setVisible(true);
                placeShipButton.setVisible(false);
            }
        }
    }

    public void handleMoveFleetButton(ActionEvent e) {
        moveInstructionsPane.visibleProperty().setValue(true);
        passTurnButton.visibleProperty().setValue(false);
        moveFleetButton.visibleProperty().setValue(false);
        playerSceneHbox.getScene().addEventHandler(KeyEvent.KEY_PRESSED, handler);
    }

    public void handleDoneButton(ActionEvent e) {
        moveInstructionsPane.visibleProperty().setValue(false);
        passTurnButton.visibleProperty().setValue(true);
        moveFleetButton.visibleProperty().setValue(true);
        playerSceneHbox.getScene().removeEventHandler(KeyEvent.KEY_PRESSED, handler);
    }

    public void handleUndoMoveFleetButton(ActionEvent e) {
        player.getFleetController().undoMoveFleet();
    }


    public void handlePassTurnButton(ActionEvent e) {

        //one time if statement for passing to player 2's setup turn
        if (game.getGameState() == GameState.player1_setup) {
            game.setGameState(GameState.player2_setup);
            showCombatButtons();
            game.passSetupTurn();
            return;
        }

        //one time if statement for passing to player 1's first combat turn
        if (game.getGameState() == GameState.player2_setup) {
            game.setGameState(GameState.first_turn);
            showCombatButtons();
            game.passTurn();
            game.updateScene();
            return;
        }
        //normal game pass turn conditions

        //Turn is over when button is pressed
        this.game.passTurn();
        this.game.updateScene();

    }

    //helper method to toggle visible tags on buttons we don't want the player seeing until game start
    public void showAllButtons() {
        List<Button> buttons = getButtons();
        for (Button button : buttons) {
            button.setVisible(true);
        }
    }

    //buttons to show for when in main game (after setup and ship placement)
    public void showCombatButtons() {

        List<Button> buttons = getButtons();

        for (Button button : buttons) {
            if (button.equals(placeShipButton)) {
                button.setVisible(false);
            } else {
                button.setVisible(true);
            }
        }
    }

    //buttons to show for when in setup and ship placement phase
    public void showSetupButtons() {

        List<Button> buttons = getButtons();

        for (Button button : buttons) {
            if (button.equals(placeShipButton)) {
                button.setVisible(true);
            } else {
                button.setVisible(false);
            }
        }
    }

    //JavaFX calls this at the creation of any new form
    public void initialize(URL location, ResourceBundle resources) {

    }

    public Canvas getPlayerGrid() {
        return playerGrid;
    }

    public Canvas getEnemyGrid() {
        return enemyGrid;
    }

    public List<Button> getButtons() {
        List<Button> buttons = new ArrayList<>();
        buttons.add(fireWeaponButton);
        buttons.add(passTurnButton);
        buttons.add(moveFleetButton);
        buttons.add(undoMoveFleetButton);
        buttons.add(placeShipButton);

        return buttons;
    }

    public Button getPassTurnButton() {
        return passTurnButton;
    }

    public Button getFireWeaponButton() {
        return fireWeaponButton;
    }
}
