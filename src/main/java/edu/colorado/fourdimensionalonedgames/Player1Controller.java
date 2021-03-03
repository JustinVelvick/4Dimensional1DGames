package edu.colorado.fourdimensionalonedgames;

import edu.colorado.fourdimensionalonedgames.game.Board;
import edu.colorado.fourdimensionalonedgames.game.Game;
import edu.colorado.fourdimensionalonedgames.game.Player;
import edu.colorado.fourdimensionalonedgames.game.ship.*;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerFireInput;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerShipInput;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class Player1Controller implements Initializable {


    private Game game;
    private Player player1;


    @FXML
    private GridPane playergpane;

    @FXML
    private GridPane enemygpane;

    @FXML
    private Button placeShipButton;

    @FXML
    private Button fireWeaponButton;


    //We call this at the creation of any new Player1Scene
    public void initialize(Game game) {
        this.game = game;
        this.player1 = game.getPlayers().get(0);
    }

    @FXML
    public void handleFireWeaponButton(ActionEvent event) throws IOException {
        //not sure if this line of code is correct, or if there's an existing controller object to grab
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fireForm.fxml"));
        Pane root = loader.load();

        FireFormController formController = loader.getController();
        formController.initialize(this.game);

        //open a new shipChoiceForm and get results from the form stored as a PlayerShipInput object
        PlayerFireInput userInput = formController.userInput();


        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Fire Weapon Form");
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.showAndWait();

      //  fireWeapon(userInput, this.board2, this.getGpane2());
    }

    public void handlePlaceShipButton(ActionEvent event) throws IOException{

        //not sure if this line of code is correct, or if there's an existing controller object to grab
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("shipChoiceForm.fxml"));
        Pane root = loader.load();

        ShipChoiceFormController formController = loader.getController();


        //open a new shipChoiceForm and get results from the form stored as a PlayerShipInput object
        PlayerShipInput userInput = formController.display();


        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Ship Placement Form");
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.showAndWait();

        placeShip(userInput);
    }

    public void placeShip(PlayerShipInput input) {
        Orientation direction = Orientation.down;
        double x =  input.getxCord();
        double y =  input.getyCord();
        Point2D origin = new Point2D(x, y);

        Ship newShip = new Destroyer();

        String shipChoice = input.getShipChoice();
        String orientationChoice = input.getDirection();

        switch (shipChoice) {
            case "Battleship(4)":
                newShip = new Battleship();
                break;

            case "Destroyer(3)":
                newShip = new Destroyer();
                break;

            case "Minesweeper(2)":
                newShip = new Minesweeper();
                break;
        }

        switch (orientationChoice) {
            case "Up":
                direction = Orientation.up;
                break;

            case "Down":
                direction = Orientation.down;
                break;

            case "Left":
                direction = Orientation.left;
                break;

            case "Right":
                direction = Orientation.right;
                break;
        }


        player1.getBoard().placeShip(this.getPlayergpane(), direction, origin, newShip);
    }

    //JavaFX calls this at the creation of any new form
    public void initialize(URL location, ResourceBundle resources) {


    }


    //helper method to validate form before populating a PlayerShipInput object
    public boolean validateForm() {
        Boolean result = true;



        return result;
    }

    public GridPane getPlayergpane() {
        return playergpane;
    }

    public GridPane getEnemygpane() {
        return enemygpane;
    }
}
