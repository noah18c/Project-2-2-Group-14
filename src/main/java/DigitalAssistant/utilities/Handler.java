package DigitalAssistant.utilities;

import DigitalAssistant.gui.stages.MenuStage;
import javafx.stage.Stage;

public class Handler {


    private MenuStage menuStage;

    private Screen screen;


    public Handler(MenuStage menuStage){
        this.menuStage = menuStage;
        this.screen = new Screen();

    }


    public Stage getWindow(){
        return this.menuStage.getWindow();
    }

    public Screen getScreen(){
        return this.screen;
    }


}
