package DigitalAssistant.gui.scenes;

import DigitalAssistant.Utilities.Handler;
import DigitalAssistant.skillLogic.FaceDetection;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class InitMenuScene implements SceneInterface {

    private Handler handler;
    private Scene scene;
    private String title;
    private int width, height;

    @FXML
    private Label introLabel;

    @FXML
    private Button proceedButton;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label progressLabel;



    public InitMenuScene(Handler handler){
        this.handler = handler;
        this.title = "Your friendly neighbourhood Digital Assistant";
        this.width = handler.getScreen().getWidth()/3;
        this.height = handler.getScreen().getHeight()/2;
    }

    public void display() {
        handler.getWindow().setTitle(this.title);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("initMenuScene.fxml"));
        loader.setController(this);


        try {
            this.scene = new Scene(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.scene.getStylesheets().add(getClass().getResource("defaultStyles.css").toExternalForm());

        proceedButton.setDisable(true);
        this.AnimateText();
    }

    public void proceed(ActionEvent e){
        ChatScene chatScene = new ChatScene(handler);
        chatScene.display();
        handler.getWindow().setScene(chatScene.getScene());
        handler.getWindow().show();
    }

    public Scene getScene(){
        return this.scene;
    }

    public void AnimateText() {
        String content = introLabel.getText();
        final Animation animation = new Transition() {
            {
                setCycleDuration(Duration.millis(2000));
            }

            protected void interpolate(double frac) {
                final int length = content.length();
                final int n = Math.round(length * (float) frac);
                introLabel.setText(content.substring(0, n));
            }
        };
        animation.setOnFinished(e ->{
            System.out.println("+++ Starting face detection...");

            while(!FaceDetection.faceDetection() && false){
                progressBar.setProgress(0.5F);
                System.out.println("face detection failed");
                progressLabel.setText("face detection failed, trying again...");
            }

            progressBar.setProgress(1F);
            System.out.println("face detection successful");
            progressLabel.setText("Face detection successful!");
            proceedButton.setDisable(false);
        });

        animation.play();


    }

}
