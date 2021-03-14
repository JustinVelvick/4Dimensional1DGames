package edu.colorado.fourdimensionalonedgames.render.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;


//TODO - Create this popup box in scene builder and then FXML load it here
public class AlertBox {
    public static void display(String title, String message) {
        if(Screen.getPrimary() == null){
            return;
        }
        Stage popUp = new Stage();
        //Block events to other stages/windows
        popUp.initModality(Modality.APPLICATION_MODAL);

        popUp.setTitle(title);
        popUp.setMinWidth(200);

        Label label = new Label();
        label.setText(message);
        Button closeWindow = new Button("Okay");
        closeWindow.setOnAction(event -> popUp.close());


        //Layout setup
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeWindow);
        layout.setAlignment(Pos.CENTER);

        //Display popUp and wait for it to be manually closed
        Scene scene = new Scene(layout);
        popUp.setScene(scene);
        popUp.showAndWait();
    }
}
