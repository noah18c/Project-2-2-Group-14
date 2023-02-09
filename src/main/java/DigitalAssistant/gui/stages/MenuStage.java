
package DigitalAssistant.gui.stages;

import DigitalAssistant.utilities.Handler;
import DigitalAssistant.gui.scenes.InitMenuScene;
import javafx.application.Application;
import javafx.stage.Stage;


public class MenuStage extends Application {

    private Stage window;
    private Handler handler;

    public MenuStage(){
        this.handler = new Handler(this);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.window = primaryStage;
        this.window.getIcons().add(handler.getLoadImages().getIcon());
        InitMenuScene scene = new InitMenuScene(handler);
        scene.display();
        primaryStage.setScene(scene.getScene());
        primaryStage.show();
    }

    public Stage getWindow() {
        return this.window;
    }
}
