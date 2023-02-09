
package DigitalAssistant.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class LaunchMenu extends Application {

    private String title;
    private Stage window;

    public LaunchMenu(){
        this.title = "Your friendly neighbourhood Digital Assistant";
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.window = stage;
        Parent root = FXMLLoader.load(getClass().getResource("scene.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        stage.setTitle(this.title);
        stage.setScene(scene);
        stage.show();
    }

}
