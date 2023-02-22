package DigitalAssistant.gui.scenes;

import DigitalAssistant.Utilities.Handler;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class InitMenuScene {

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

    public void display() throws Exception{
        handler.getWindow().setTitle(this.title);
        Parent root = FXMLLoader.load(getClass().getResource("initMenuScene.fxml"));

        this.scene = new Scene(root, width, height);
        this.scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
    }

    public void proceed(ActionEvent e){
        System.out.println("hello");
    }

    public Scene getScene(){
        return this.scene;
    }

}
