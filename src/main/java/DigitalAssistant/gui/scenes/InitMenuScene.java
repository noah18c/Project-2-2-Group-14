package DigitalAssistant.gui.scenes;

import DigitalAssistant.Utilities.Handler;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class InitMenuScene implements SceneInterface{

    private Handler handler;
    private Scene scene;
    private String title;
    private int width, height;


    public InitMenuScene(Handler handler){
        this.handler = handler;
        this.title = "Your friendly neighbourhood Digital Assistant";
        this.width = handler.getScreen().getWidth()/3;
        this.height = handler.getScreen().getHeight()/2;
    }

    public void display() {
        handler.getWindow().setTitle(this.title);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("initMenuScene.fxml"));
        loader.setController(this);


        try {
            this.scene = new Scene(loader.load(), width, height);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
    }

    public void proceed(ActionEvent e){
        ChatScene chatScene = new ChatScene(handler);
        chatScene.display();
        handler.getWindow().setScene(chatScene.getScene());
        handler.getWindow().show();
    }

    public Scene getScene(){
        return this.scene;
    }

}
