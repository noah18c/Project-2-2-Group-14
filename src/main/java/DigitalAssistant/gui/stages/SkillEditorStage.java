package DigitalAssistant.gui.stages;

import DigitalAssistant.Utilities.Handler;
import DigitalAssistant.Utilities.SkillsUserInput;
import DigitalAssistant.gui.scenes.SkillEditorScene1;
import DigitalAssistant.skillLogic.SkillEditor;
import javafx.stage.Stage;

public class SkillEditorStage {

    private Stage window;

    private SkillEditorScene1 scene;

    private Handler handler;
    private SkillEditor skillEditor;

    public SkillEditorStage(Handler handler, SkillEditor skillEditor){
        this.handler = handler;
        window = new Stage();
        this.skillEditor = skillEditor;
    }

    public SkillsUserInput display(){
        scene = new SkillEditorScene1(handler, window);

        SkillsUserInput skillsUserInput = scene.display();
        //skillEditor.retrieveSkil(scene.display());

        this.window.getIcons().add(handler.getLoadImages().getIcon());

        window.setScene(scene.getScene());
        window.show();
        return skillsUserInput;

    }




}
