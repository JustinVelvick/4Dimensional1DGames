package edu.colorado.fourdimensionalonedgames;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.effect.Light;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ResourceBundle;

public class FXMLController implements Initializable{

    Board board1;
    Board board2;


    @FXML
    private GridPane gpane1;

    @FXML
    private TextField p1x;

    @FXML
    private TextField p1y;

    @FXML
    private Button p1PlaceShip;

    @FXML
    private GridPane gpane2;

    @FXML
    private TextField p2x;

    @FXML
    private TextField p2y;

    @FXML
    private Button p2PlaceShip;

    @FXML
    private ChoiceBox<String> shipBox1;

    @FXML
    private ChoiceBox<String> orientationBox1;

    @FXML
    private ChoiceBox<String> shipBox2;

    @FXML
    private ChoiceBox<String> orientationBox2;

    public void initializeBoards(Board board1, Board board2) {
        this.board1 = board1;
        this.board2 = board2;

        board1.initializeBoard(gpane1);
        board2.initializeBoard(gpane2);

    }


    //event handlers

    //on "Place Ship" button click
    public void handlePlaceShip1(ActionEvent event){
        Orientation direction = Orientation.down;
        double x =  Double.parseDouble(p1x.getText());
        double y =  Double.parseDouble(p1y.getText());
        Point2D origin = new Point2D(x, y);

        Ship newShip = new Destroyer();

        String shipChoice = shipBox1.getSelectionModel().getSelectedItem();
        String orientationChoice = orientationBox1.getSelectionModel().getSelectedItem();

        switch (shipChoice){
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

        switch (orientationChoice){
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


        this.board1.placeShip(getGpane1(), direction, origin, newShip);
    }

    public void handlePlaceShip2(ActionEvent event){
        Orientation direction = Orientation.down;
        double x =  Double.parseDouble(p2x.getText());
        double y =  Double.parseDouble(p2y.getText());
        Point2D origin = new Point2D(x, y);

        Ship newShip = new Destroyer();

        String shipChoice = shipBox2.getSelectionModel().getSelectedItem();
        String orientationChoice = orientationBox2.getSelectionModel().getSelectedItem();

        switch (shipChoice){
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

        switch (orientationChoice){
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


        this.board2.placeShip(getGpane2(), direction, origin, newShip);
    }




    //getters
    public GridPane getGpane1() {
        return gpane1;
    }

    public TextField getP1x() {
        return p1x;
    }

    public TextField getP1y() {
        return p1y;
    }

    public Button getP1PlaceShip() {
        return p1PlaceShip;
    }

    public GridPane getGpane2() {
        return gpane2;
    }

    public TextField getP2x() {
        return p2x;
    }

    public TextField getP2y() {
        return p2y;
    }

    public Button getP2PlaceShip() {
        return p2PlaceShip;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        shipBox1.getItems().removeAll(shipBox1.getItems());
        shipBox1.getItems().addAll("Minesweeper(2)", "Destroyer(3)", "Battleship(4)");
        shipBox1.getSelectionModel().select("Minesweeper(2)");

        orientationBox1.getItems().removeAll(orientationBox1.getItems());
        orientationBox1.getItems().addAll("Up", "Down", "Left", "Right");
        orientationBox1.getSelectionModel().select("Up");

        shipBox2.getItems().removeAll(shipBox2.getItems());
        shipBox2.getItems().addAll("Minesweeper(2)", "Destroyer(3)", "Battleship(4)");
        shipBox2.getSelectionModel().select("Minesweeper(2)");

        orientationBox2.getItems().removeAll(orientationBox2.getItems());
        orientationBox2.getItems().addAll("Up", "Down", "Left", "Right");
        orientationBox2.getSelectionModel().select("Up");
    }
}

