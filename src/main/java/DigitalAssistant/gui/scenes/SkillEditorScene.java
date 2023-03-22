package DigitalAssistant.gui.scenes;

import DigitalAssistant.Utilities.Handler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class SkillEditorScene {

    private Handler handler;
    private Scene scene;
    private String title;
    private int width, height;

    public SkillEditorScene(Handler handler){
        this.handler = handler;
        this.title = "Go ahead... ask!";
        this.width = handler.getScreen().getWidth()/3;
        this.height = handler.getScreen().getHeight()/2;
    }

    public void display() {
        handler.getWindow().setTitle(this.title);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("SkillEditorStage.fxml"));
        loader.setController(this);

        try {
            this.scene = new Scene(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.scene.getStylesheets().add(getClass().getResource("SkillEditorStyles.css").toExternalForm());


    }

    public Scene getScene(){
        return this.scene;
    }


}
