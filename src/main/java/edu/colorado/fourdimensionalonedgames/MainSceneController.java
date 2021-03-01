package edu.colorado.fourdimensionalonedgames;

import edu.colorado.fourdimensionalonedgames.game.ship.Orientation;
import edu.colorado.fourdimensionalonedgames.game.Board;
import edu.colorado.fourdimensionalonedgames.game.attack.InvalidAttackException;
import edu.colorado.fourdimensionalonedgames.game.ship.Battleship;
import edu.colorado.fourdimensionalonedgames.game.ship.Destroyer;
import edu.colorado.fourdimensionalonedgames.game.ship.Minesweeper;
import edu.colorado.fourdimensionalonedgames.game.ship.Ship;
import edu.colorado.fourdimensionalonedgames.render.gui.AlertBox;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerFireInput;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerShipInput;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainSceneController implements Initializable {

    Board board1;
    Board board2;


    @FXML
    private GridPane gpane1;

    @FXML
    private Button p1PlaceShip;

    @FXML
    private GridPane gpane2;

    @FXML
    private Button p2PlaceShip;

    @FXML
    private Button fireWeapon1;

    @FXML
    private Button fireWeapon2;



    public void initializeBoards(Board board1, Board board2) {
        this.board1 = board1;
        this.board2 = board2;

        board1.initializeBoard(gpane1);
        board2.initializeBoard(gpane2);

    }


    //event handlers

    //on "Place Ship" button click, load the shipChoiceForm and envoke "showAndWait" which waits for
    //the form to close itself, ie. when the Confirm button is clicked, ShipChoiceForm.java will call
    //handleConfirmButton and close itself

    public void handlePlaceShip1(ActionEvent event) throws IOException {

        //not sure if this line of code is correct, or if there's an existing controller object to grab
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("shipChoiceForm.fxml"));
        Pane root = loader.load();

        ShipChoiceForm formController = loader.getController();


        //open a new shipChoiceForm and get results from the form stored as a PlayerShipInput object
        PlayerShipInput userInput = formController.display();


        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Ship Placement Form");
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.showAndWait();

        placeShip(userInput, this.board1, this.getGpane1());
    }

    public void handlePlaceShip2(ActionEvent event) throws IOException {

        //not sure if this line of code is correct, or if there's an existing controller object to grab
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("shipChoiceForm.fxml"));
        Pane root = loader.load();

        ShipChoiceForm formController = loader.getController();


        //open a new shipChoiceForm and get results from the form stored as a PlayerShipInput object
        PlayerShipInput userInput = formController.display();


        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Ship Placement Form");
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.showAndWait();

        placeShip(userInput, this.board2, this.getGpane2());
    }

    public void handleFireWeapon1(ActionEvent event) throws IOException {

        //not sure if this line of code is correct, or if there's an existing controller object to grab
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fireForm.fxml"));
        Pane root = loader.load();

        FireFormController formController = loader.getController();


        //open a new shipChoiceForm and get results from the form stored as a PlayerShipInput object
        PlayerFireInput userInput = formController.display();


        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Fire Weapon Form");
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.showAndWait();

        fireWeapon(userInput, this.board2, this.getGpane2());
    }

    public void handleFireWeapon2(ActionEvent event) throws IOException {

        //not sure if this line of code is correct, or if there's an existing controller object to grab
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fireForm.fxml"));
        Pane root = loader.load();

        FireFormController formController = loader.getController();


        //open a new shipChoiceForm and get results from the form stored as a PlayerShipInput object
        PlayerFireInput userInput = formController.display();


        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Fire Weapon Form");
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.showAndWait();

        fireWeapon(userInput, this.board1, this.getGpane1());
    }

    public void fireWeapon(PlayerFireInput input, Board board, GridPane gpane) {
        Point2D coordinate = new Point2D(input.getxCord(), input.getyCord());
        try {
            Ship attackedShip = board.attack(coordinate);

            if (attackedShip == null) {
                AlertBox.display("Miss", "Shot missed");
            }
            else if (attackedShip.destroyed()) {
                AlertBox.display("Ship Sunk", "The enemy's " + attackedShip.getType() + " has been sunk!");
            }
            else {
                AlertBox.display("Ship Hit", "Ship has been hit");
            }

            if(board.gameOver()) {
                AlertBox.display("Enemy Surrender!", "Congratulations, you sunk all of your enemies ships!");
            }
        }
        catch (InvalidAttackException e) {
            AlertBox.display("Invalid Coordinates", e.getErrorMsg());
        }
    }

    //place a predetermined valid ship on the board
    public void placeShip(PlayerShipInput input, Board board, GridPane gpane) {
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


        board.placeShip(gpane, direction, origin, newShip);
    }


    //getters
    public GridPane getGpane1() {
        return gpane1;
    }


    public Button getP1PlaceShip() {
        return p1PlaceShip;
    }

    public GridPane getGpane2() {
        return gpane2;
    }


    public Button getP2PlaceShip() {
        return p2PlaceShip;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}

