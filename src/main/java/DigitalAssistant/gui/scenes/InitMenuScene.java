package DigitalAssistant.gui.scenes;

import DigitalAssistant.IVP.FaceRecognitionApp;
import DigitalAssistant.Utilities.Handler;
import DigitalAssistant.gui.stages.SkillEditorStage;
import DigitalAssistant.skillLogic.FaceDetection;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class InitMenuScene implements SceneInterface, Initializable {

    private Handler handler;
    private Scene scene;
    private String title;
    private int width, height;

    @FXML
    private Label introLabel;

    @FXML
    private Button proceedButton;

    @FXML
    private Button enterChatButton;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label progressLabel;

    @FXML
    private ChoiceBox<String> detectionSelectChoiceBox;

    private ObservableList<String> detectionSelectionList = FXCollections.observableArrayList();

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
        String selected = detectionSelectChoiceBox.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("initMenuScene2.fxml"));
        loader.setController(this);

        try {
            this.scene.setRoot(loader.load());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        this.enterChatButton.setDisable(true);
        executeFaceDetection(selected);

    }

    public void enterChat(ActionEvent e){
        ChatScene chatScene = new ChatScene(handler);
        chatScene.display();
        handler.getWindow().setScene(chatScene.getScene());
        handler.getWindow().show();
    }

    private void executeFaceDetection(String faceDetectionChoice){

        System.out.println("You chose: "+faceDetectionChoice);
        switch(faceDetectionChoice){
            case "Simple Face Detection":
                simpleFaceDetection();
                break;
            case "Eigen Face Recognition":
                eigenFaceRecognition();
                break;
            case "NN Face Detection":

                break;
        }
        this.enterChatButton.setDisable(false);
    }

    private void simpleFaceDetection(){
        FaceDetection fd = new FaceDetection();
        System.out.println("+++ Starting face detection...");
        boolean success = fd.faceDetection();

        try {
            if(fd.faceDetection()){
                progressBar.setProgress(1F);
                // System.out.println("face detection successful");
                System.out.println("### Haar Cascades successfully applied ###");
                progressLabel.setText("Face detection successful!");
                proceedButton.setDisable(false);
            }
            else {
                progressBar.setProgress(0.5F);
                System.out.println("--- Sorry, face detection failed");
                progressLabel.setText("Face detection failed. Trying again...");

                Thread.sleep(4000);
                // second attempt
                if(fd.faceDetection()){
                    progressBar.setProgress(1F);
                    // System.out.println("face detection successful");
                    System.out.println("### Haar Cascades successfully applied ###");
                    progressLabel.setText("Second attempt successful!");
                    proceedButton.setDisable(false);
                }
                else {
                    progressBar.setProgress(0.5F);
                    System.out.println("--- Sorry, face detection failed");
                    progressLabel.setText("Face detection failed. Trying again...");
                }
            }
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

    }

    private void eigenFaceRecognition(){
        //FaceRecognitionApp.eigenFaceRecognition();
        progressLabel.setText(FaceRecognitionApp.eigenFaceRecognition());
        progressBar.setProgress(1.0F);
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
            proceedButton.setDisable(false);
        });

        animation.play();


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        detectionSelectionList.add("Simple Face Detection");
        detectionSelectionList.add("Eigen Face Recognition");
        detectionSelectionList.add("NN Face Detection");

        detectionSelectChoiceBox.setItems(detectionSelectionList);
        detectionSelectChoiceBox.getSelectionModel().selectFirst();
    }


}
