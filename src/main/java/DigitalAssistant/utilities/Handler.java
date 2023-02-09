package DigitalAssistant.utilities;

import DigitalAssistant.gui.stages.MenuStage;
import javafx.stage.Stage;

public class Handler {

    private MenuStage menuStage;

    private Screen screen;

    private LoadImages loadImages;

    public Handler(MenuStage menuStage){
        this.menuStage = menuStage;
        this.screen = new Screen();
        this.loadImages = new LoadImages();
    }


    public Stage getWindow(){
        return this.menuStage.getWindow();
    }

    public Screen getScreen(){
        return this.screen;
    }

    public LoadImages getLoadImages(){
        return this.loadImages;
    }
}
