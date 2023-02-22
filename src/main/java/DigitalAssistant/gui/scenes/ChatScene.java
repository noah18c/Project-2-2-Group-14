package DigitalAssistant.gui.scenes;

import DigitalAssistant.Utilities.Handler;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class ChatScene implements SceneInterface {

    private Handler handler;
    private Scene scene;
    private String title;
    private int width, height;


    public ChatScene(Handler handler){
        this.handler = handler;
        this.title = "Go ahead... ask!";
        this.width = handler.getScreen().getWidth()/3;
        this.height = handler.getScreen().getHeight()/2;
    }

    public void display() {
        handler.getWindow().setTitle(this.title);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ChatScene.fxml"));
        loader.setController(this);

        try {
            this.scene = new Scene(loader.load(), width, height);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
    }

    public Scene getScene(){
        return this.scene;
    }
}
