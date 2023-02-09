
package DigitalAssistant.gui.stages;

import DigitalAssistant.utilities.Handler;
import DigitalAssistant.gui.scenes.InitMenuScene;
import DigitalAssistant.utilities.LoadImages;
import javafx.application.Application;
import javafx.stage.Stage;


public class MenuStage extends Application {

    private Stage window;
    private Handler handler;

    private LoadImages loadImages;

    public MenuStage(){
        this.handler = new Handler(this);
        this.loadImages = new LoadImages();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.window = primaryStage;
        //this.window.getIcons().add(loadImages.getIcon());

        InitMenuScene scene = new InitMenuScene(handler);
        scene.display();

        primaryStage.setScene(scene.getScene());
        primaryStage.show();
    }

    public Stage getWindow() {
        return this.window;
    }

    public LoadImages getLoadImages(){
        return this.loadImages;
    }
}
