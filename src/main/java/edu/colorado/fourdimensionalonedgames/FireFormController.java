package edu.colorado.fourdimensionalonedgames;

import edu.colorado.fourdimensionalonedgames.render.gui.PlayerFireInput;
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
public class FireFormController implements Initializable {

    @FXML
    private TextField xCord;

    @FXML
    private TextField yCord;

    @FXML
    private ChoiceBox<String> weaponChoiceBox;

    @FXML
    private Button confirmButton;

    private PlayerFireInput input;



    public PlayerFireInput userInput() throws IOException {
        return this.input;
    }

    @FXML
    public void handleConfirmButton(ActionEvent event) throws IOException {
        if(this.validateForm()){
            this.input.setWeaponChoice(weaponChoiceBox.getSelectionModel().getSelectedItem());

            this.input.setxCord(Double.parseDouble(xCord.getText()));
            this.input.setyCord(Double.parseDouble(yCord.getText()));

            Stage currentStage = (Stage) confirmButton.getScene().getWindow();
            currentStage.close();
        }
    }

    //JavaFX calls this at the creation of any new form
    public void initialize(URL location, ResourceBundle resources) {

        //field initialization on form creation
        this.input = new PlayerFireInput("","0","0");


        weaponChoiceBox.getItems().removeAll(weaponChoiceBox.getItems());
        weaponChoiceBox.getItems().addAll("Default", "Cross Bomb", "UAV");
        weaponChoiceBox.getSelectionModel().select("Default");
    }


    //helper method to validate form before populating a PlayerShipInput object
    public boolean validateForm() {
        Boolean result = true;



        return result;
    }
}
