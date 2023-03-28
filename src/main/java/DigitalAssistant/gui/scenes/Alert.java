package DigitalAssistant.gui.scenes;

import DigitalAssistant.Utilities.Handler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class Alert {
    private String input;
    private Stage window;
    private Scene scene;
    private Handler handler;
    @FXML
    private Label message;
    @FXML
    private Button okButton;

    public Alert(Handler handler, String input){
        this.handler = handler;
        this.input = input;
    }

    public void display(){
        window = new Stage();
        window.getIcons().add(handler.getLoadImages().getIcon());
        window.setTitle("Warning!");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Alert.fxml"));
        loader.setController(this);

        try {
            this.scene = new Scene(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.scene.getStylesheets().add(getClass().getResource("defaultStyles.css").toExternalForm());

        message.setText(input);

        okButton.setOnAction(e -> {
            window.close();
        });



        window.setScene(scene);
        window.show();

    }


}
