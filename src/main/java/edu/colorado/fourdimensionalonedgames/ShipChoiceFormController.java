package edu.colorado.fourdimensionalonedgames;

import edu.colorado.fourdimensionalonedgames.game.ship.Battleship;
import edu.colorado.fourdimensionalonedgames.game.ship.Destroyer;
import edu.colorado.fourdimensionalonedgames.game.ship.Minesweeper;
import edu.colorado.fourdimensionalonedgames.game.ship.Ship;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


//TODO - Create this popup box in scene builder and then FXML load it here
public class ShipChoiceFormController implements Initializable {

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
        if(this.validateForm(this.input)){
            this.input.setShipChoice(shipChoiceBox.getSelectionModel().getSelectedItem());
            this.input.setDirection(directionChoiceBox.getSelectionModel().getSelectedItem());
            this.input.setxCord(xCord.getText());
            this.input.setyCord(xCord.getText());

            Stage currentStage = (Stage) confirmButton.getScene().getWindow();
            currentStage.close();
        }
    }



    public void populateShipForm(List<Ship> ships){

        //Delete existing to create a fresh set of choices
        shipChoiceBox.getItems().removeAll(shipChoiceBox.getItems());
        List<String> choices = new ArrayList<>();
        for(Ship ship : ships){
            choices.add(ship.getType());
        }
        //add all choices to the dropdown
        shipChoiceBox.getItems().addAll(choices);

        //if there are no choices left, hide the dropdown
        if(shipChoiceBox.getItems().isEmpty()){
            shipChoiceBox.hide();
        }
        else{
            //Display the first choice as dropdown default
            shipChoiceBox.getSelectionModel().select(choices.get(0));
        }

    }



    //helper method to validate form before populating a PlayerShipInput object
    public boolean validateForm(PlayerShipInput input) {

        //check len
        if((input.getxCord()).length() != 1) {
            return false;
        }

        Boolean xValid = false;
        if((input.getxCord()).charAt(0) > 64 && (input.getxCord()).charAt(0) < 75){
            xValid=true;
        }
        else if((input.getxCord()).charAt(0) > 96 && (input.getxCord()).charAt(0) < 107){
            xValid=true;
        }

        Boolean yValid = true;
        if((int) (input.getyCord()).charAt(0) <= 10 || (int) (input.getyCord()).charAt(0) > 0){
            yValid = false;
        }

        boolean result = true;
        result = xValid&&yValid;
        return result;
    }

    //JavaFX calls this at the creation of any new form
    public void initialize(URL location, ResourceBundle resources) {
        //field initialization on form creation
        this.input = new PlayerShipInput("","","0","0");

        directionChoiceBox.getItems().removeAll(directionChoiceBox.getItems());
        directionChoiceBox.getItems().addAll("Up", "Down", "Left", "Right");
        directionChoiceBox.getSelectionModel().select("Up");
    }
}
