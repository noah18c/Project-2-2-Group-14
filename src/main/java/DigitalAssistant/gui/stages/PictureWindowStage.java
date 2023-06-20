package DigitalAssistant.gui.stages;

import DigitalAssistant.Utilities.Handler;
import DigitalAssistant.Utilities.LoadImages;
import DigitalAssistant.gui.scenes.SkillEditorScene1;
import DigitalAssistant.skillLogic.SkillEditor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class PictureWindowStage {

    private Stage window;

    private Scene scene;

    private StackPane root;

    private Handler handler;

    public PictureWindowStage(Handler handler){
        this.handler = handler;

    }

    public void display(){
        window = new Stage();
        root = new StackPane();
        Image image = new Image(LoadImages.class.getResource("/DigitalAssistant/img/AI-icon.png").toString());

        ImageView imageView = new ImageView(image);


        this.window.getIcons().add(new Image(LoadImages.class.getResource("/DigitalAssistant/haarcascades/detectedFace.jpg").toString()));

        root.getChildren().add(imageView);
        scene = new Scene(root);
        window.setScene(scene);
        window.show();

        long startTime = System.currentTimeMillis();
        long duration = 10000; //10 seconds
        while(System.currentTimeMillis() - startTime < duration){

        }
        window.close();

    }

}
