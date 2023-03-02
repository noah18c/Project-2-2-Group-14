package DigitalAssistant.gui.scenes;

import DigitalAssistant.Utilities.Handler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;

import javax.swing.text.Document;
import java.io.IOException;
import java.net.*;
import java.util.ResourceBundle;
import java.io.IOException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private VBox vBox;

    @FXML
    private TextField textField;
    ObservableList<String> observableItems = FXCollections.observableArrayList("");


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

        sendButton.setOnAction(e -> {
            String writtenText = (String) textField.getText();
            textField.clear();

            try
            {
                URL url=new URL("https://google.co.in");
                URLConnection connection=url.openConnection();
                connection.connect();
                java.awt.Desktop.getDesktop().browse(java.net.URI.create("http://www.google.com/search?hl=en&q="+writtenText.replace(" ", "+")+"&btnG=Google+Search"));

            }
            catch(Exception ee)
            {
                System.out.println("shit");
            }


            listview.getItems().addAll(writtenText);
        });


    }


    public Scene getScene(){
        return this.scene;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listview.setItems(observableItems);
    }
}
