package edu.colorado.fourdimensionalonedgames;

import edu.colorado.fourdimensionalonedgames.game.Player;
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
    private ChoiceBox<String> submergeChoiceBox;

    @FXML
    private Button confirmButton;

    private PlayerShipInput input;



    public PlayerShipInput display(){
        return this.input;
    }

    @FXML
    public void handleConfirmButton(ActionEvent event){

        PlayerShipInput tentativeInput = new PlayerShipInput();

        if(shipChoiceBox.getSelectionModel().getSelectedItem().equals("Submarine")){
            tentativeInput.setShipChoice(shipChoiceBox.getSelectionModel().getSelectedItem());
            tentativeInput.setDirection(directionChoiceBox.getSelectionModel().getSelectedItem());
            tentativeInput.setxCord(xCord.getText());
            tentativeInput.setyCord(xCord.getText());
            tentativeInput.setSubmergeChoice(submergeChoiceBox.getSelectionModel().getSelectedItem());
        }
        else{
            tentativeInput.setShipChoice(shipChoiceBox.getSelectionModel().getSelectedItem());
            tentativeInput.setDirection(directionChoiceBox.getSelectionModel().getSelectedItem());
            tentativeInput.setxCord(xCord.getText());
            tentativeInput.setyCord(xCord.getText());
        }

        if(this.validateForm(tentativeInput)){
            this.input = tentativeInput;

            //close this window (Stage)
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



    //helper method to validate form before sending PlayerShipInput object
    public boolean validateForm(PlayerShipInput input) {

        //check len
        if((input.getxCord().length() != 1) || (input.getyCord().length() > 2) || (input.getyCord().length() < 1)) {
            return false;
        }


        //for x, values entered are either a-j or A-J, use ascii table values to check
        boolean xValid = false;
        int xAscii = input.getxCord().charAt(0);

        //upper case A-J check
        if(xAscii > 64 && xAscii < 75){
            xValid=true;
        }
        //lower case a-j check
        else if(xAscii > 96 && xAscii < 107){
            xValid=true;
        }

        //y values can be integers [1,10]
        boolean yValid = true;
        int y = Integer.parseInt(input.getyCord());
        if(y > 10 || y < 1){
            yValid = false;
        }

        boolean result = xValid&&yValid;
        return result;
    }

    //JavaFX calls this at the creation of any new form
    public void initialize(URL location, ResourceBundle resources) {
        //field initialization on form creation
        this.input = new PlayerShipInput();

        directionChoiceBox.getItems().removeAll(directionChoiceBox.getItems());
        directionChoiceBox.getItems().addAll("Up", "Down", "Left", "Right");
        directionChoiceBox.getSelectionModel().select("Up");
    }
}
