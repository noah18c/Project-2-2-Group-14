package DigitalAssistant.gui.scenes;

import DigitalAssistant.Utilities.Handler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

import java.io.IOException;

public class ChatScene implements SceneInterface, Initializable {

    private Handler handler;
    private Scene scene;
    private String title;
    private int width, height;

    @FXML
    private ChoiceBox<?> choiceBox;

    @FXML
    private ListView<String> listview;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Button sendButton;

    @FXML
    private TextField textField;
    ObservableList<String> observableItems = FXCollections.observableArrayList();


    public ChatScene(Handler handler){
        this.handler = handler;
        this.title = "Go ahead... ask!";
        this.width = handler.getScreen().getWidth()/3;
        this.height = handler.getScreen().getHeight()/2;
    }

    public void display() {
        handler.getWindow().setTitle(this.title);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ChatScene.fxml"));
        loader.setController(this);

        try {
            this.scene = new Scene(loader.load(), width, height);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        init();
    }

    private void init() {

        this.textField.setOnKeyPressed(e -> {
            if(e.getCode().equals(KeyCode.ENTER)){
                writeText(textField.getText());
                textField.clear();
            }
        });


        sendButton.setOnAction(e -> {
            writeText(textField.getText());
            textField.clear();
        });


    }

    private void writeText(String writtenText){
        listview.getItems().addAll(writtenText);
    }


    public Scene getScene(){
        return this.scene;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listview.setItems(observableItems);
    }
}
