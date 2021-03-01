package edu.colorado.fourdimensionalonedgames;

import edu.colorado.fourdimensionalonedgames.render.gui.PlayerShipInput;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


//TODO - Create this popup box in scene builder and then FXML load it here
public class ShipChoiceForm implements Initializable {

    @FXML
    private TextField xCord;

    @FXML
    private TextField yCord;

    @FXML
    private ChoiceBox<String> directionChoiceBox;

    @FXML
    private ChoiceBox<String> shipChoiceBox;

    @FXML
    private Button confirmButton;

    private PlayerShipInput input;



    public PlayerShipInput display() throws IOException {
        return this.input;
    }

    @FXML
    public void handleConfirmButton(ActionEvent event) throws IOException {
        if(this.validateForm()){
            this.input.setShipChoice(shipChoiceBox.getSelectionModel().getSelectedItem());
            this.input.setDirection(directionChoiceBox.getSelectionModel().getSelectedItem());
            this.input.setxCord(Double.parseDouble(xCord.getText()));
            this.input.setyCord(Double.parseDouble(yCord.getText()));

            Stage currentStage = (Stage) confirmButton.getScene().getWindow();
            currentStage.close();
        }
    }

    //JavaFX calls this at the creation of any new form
    public void initialize(URL location, ResourceBundle resources) {

        //field initialization on form creation
        this.input = new PlayerShipInput("","","0","0");


        shipChoiceBox.getItems().removeAll(shipChoiceBox.getItems());
        shipChoiceBox.getItems().addAll("Minesweeper(2)", "Destroyer(3)", "Battleship(4)");
        shipChoiceBox.getSelectionModel().select("Minesweeper(2)");

        directionChoiceBox.getItems().removeAll(directionChoiceBox.getItems());
        directionChoiceBox.getItems().addAll("Up", "Down", "Left", "Right");
        directionChoiceBox.getSelectionModel().select("Up");

    }


    //helper method to validate form before populating a PlayerShipInput object
    public boolean validateForm() {
        Boolean result = true;



        return result;
    }
}
