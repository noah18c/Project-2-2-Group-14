package DigitalAssistant.gui.scenes;

import DigitalAssistant.Utilities.Handler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class test {

    private Handler handler;
    private Scene scene;
    private String title;
    private int width, height;


    public test(Handler handler){
        this.handler = handler;
        this.title = "Your friendly neighbourhood Digital Assistant";
        this.width = handler.getScreen().getWidth()/3;
        this.height = handler.getScreen().getHeight()/2;
    }

    public void display() throws Exception{
        handler.getWindow().setTitle(this.title);
        Parent root = FXMLLoader.load(getClass().getResource("Test.fxml"));

        this.scene = new Scene(root, width, height);
        this.scene.getStylesheets().add(getClass().getResource("chatStyles.css").toExternalForm());
    }

    public Scene getScene(){
        return this.scene;
    }

}
