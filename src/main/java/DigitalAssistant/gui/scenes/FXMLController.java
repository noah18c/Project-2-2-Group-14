package DigitalAssistant.gui.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class FXMLController {


    @FXML
    private Label label;
    private Stage currentWindow;
    private Scene currentScene;

    public void proceed(ActionEvent e) throws IOException {
        /*
        ((Node) e.getSource()).getClass().
        ChatScene chatScene = new ChatScene(current);
        Parent root = FXMLLoader.load(getClass().getResource("ChatScene.fxml"));
        currentWindow = (Stage) ((Node) e.getSource()).getScene().getWindow();
        newScene = new Scene(root);
        currentWindow.setScene(currentScene);
        currentWindow.show();

         */
    }


}
