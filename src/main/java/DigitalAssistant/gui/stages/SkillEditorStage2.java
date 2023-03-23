package DigitalAssistant.gui.stages;

import DigitalAssistant.Utilities.Handler;
import DigitalAssistant.gui.scenes.SkillEditorScene1;
import DigitalAssistant.gui.scenes.SkillEditorScene2;
import javafx.stage.Stage;

public class SkillEditorStage2 {

    private Stage window;

    private SkillEditorScene2 scene;

    private Handler handler;

    public SkillEditorStage2(Handler handler){
        this.handler = handler;
        window = new Stage();
    }

    public void display(){
        scene = new SkillEditorScene2(handler);
        scene.display();



        this.window.getIcons().add(handler.getLoadImages().getIcon());

        window.setScene(scene.getScene());
        window.show();

    }




}
