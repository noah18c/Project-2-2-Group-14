package DigitalAssistant.gui.stages;

import DigitalAssistant.Utilities.Handler;
import DigitalAssistant.gui.scenes.SkillEditorScene1;
import javafx.stage.Stage;

public class SkillEditorStage {

    private Stage window;

    private SkillEditorScene1 scene;

    private Handler handler;

    public SkillEditorStage(Handler handler){
        this.handler = handler;
        window = new Stage();
    }

    public void display(){
        scene = new SkillEditorScene1(handler);
        scene.display();



        this.window.getIcons().add(handler.getLoadImages().getIcon());

        window.setScene(scene.getScene());
        window.show();

    }




}
