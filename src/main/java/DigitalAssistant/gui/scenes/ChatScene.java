package DigitalAssistant.gui.scenes;

import DigitalAssistant.Utilities.Handler;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;

public class ChatScene implements SceneInterface {

    private Handler handler;
    private Scene scene;
    private String title;
    private int width, height;

    @FXML
    private ChoiceBox<?> choiceBox;

    @FXML
    private Button clearButton;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Button sendButton;

    @FXML
    private VBox vBox;

    @FXML
    private TextField textField;

    private List<Label> messages = new ArrayList<>();
    @FXML
    private VBox chatbox;


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
            this.scene = new Scene(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.scene.getStylesheets().add(getClass().getResource("chatStyles.css").toExternalForm());
        init();

    }
    private String post(boolean isUser, String inputText){
        String writtenText = inputText;

        Text textBlock = new Text(writtenText);
        textBlock.wrappingWidthProperty().bind(Bindings.divide(scrollPane.widthProperty(), 1.1));
        Label labelmsg = new Label("", textBlock);

        labelmsg.prefWidthProperty().bind(Bindings.divide(scrollPane.widthProperty(), 1.1));
        labelmsg.setMinHeight(30);
        messages.add(labelmsg);
        int lastIndex = messages.size()-1;

        if(isUser){

            messages.get(lastIndex).setAlignment(Pos.CENTER_RIGHT);
            textField.clear();
        } else {

            messages.get(lastIndex).setAlignment(Pos.CENTER_LEFT);
        }

        chatbox.getChildren().add(messages.get(lastIndex));

        return writtenText;
    }

    private String post(boolean isUser){
        String writtenText = (String) textField.getText();
        Text textBlock = new Text(writtenText);
        textBlock.wrappingWidthProperty().bind(Bindings.divide(scrollPane.widthProperty(), 1.1));


        Label labelmsg = new Label("",textBlock);

        labelmsg.prefWidthProperty().bind(Bindings.divide(scrollPane.widthProperty(), 1.1));
        labelmsg.setMinHeight(30);
        messages.add(labelmsg);
        int lastIndex = messages.size()-1;

        if(isUser){

            messages.get(lastIndex).setAlignment(Pos.CENTER_RIGHT);
            textField.clear();
        } else {

           messages.get(lastIndex).setAlignment(Pos.CENTER_LEFT);
        }


        chatbox.getChildren().add(messages.get(lastIndex));

        return writtenText;
    }

    private void postAddGet(){
        FlowPane fp = new FlowPane();
        fp.getStyleClass().add("flowpane");
        fp.prefWidthProperty().bind(Bindings.divide(scrollPane.widthProperty(), 1.1));

        Button buttonAdd = new Button("ADD");
        buttonAdd.setAlignment(Pos.CENTER);
        Button buttonGet = new Button("GET");
        buttonAdd.setMinHeight(30);
        buttonGet.setMinHeight(30);

        buttonAdd.setOnAction(e -> {
            botWriter("You have chosen to "+buttonAdd.getText()+" a new skill.");
            buttonAdd.setDisable(true);
            buttonGet.setDisable(true);
        });

        buttonGet.setOnAction(e -> {
            botWriter("You have chosen to "+buttonGet.getText()+" an existing skill.");
            buttonAdd.setDisable(true);
            buttonGet.setDisable(true);
        });



        fp.getChildren().addAll(buttonAdd, buttonGet);

        chatbox.getChildren().add(fp);
    }


    private void botWriter(String inputText){
        post(false, inputText);
        int lastIndex = messages.size()-1;

        Text textObject = (Text) messages.get(lastIndex).getGraphic();
        String content = textObject.getText();


        final Animation animation = new Transition() {
            {
                setCycleDuration(Duration.millis(2000));
            }

            protected void interpolate(double frac) {
                final int length = content.length();
                final int n = Math.round(length * (float) frac);
                textObject.setText(content.substring(0, n));
                //messages.get(lastIndex).setText(content.substring(0, n));

            }
        };
        animation.play();
    }



    private void init() {
        botWriter("Hello user, would you like to add a skill or get an existing one?");
        postAddGet();

        chatbox.setMinWidth(scrollPane.getMinWidth()-20);
        chatbox.setMinHeight(scrollPane.getMinHeight());
        chatbox.setAlignment(Pos.TOP_CENTER);
        scrollPane.vvalueProperty().bind(chatbox.heightProperty());


        clearButton.setOnAction(e -> {
            chatbox.getChildren().clear();
            messages.clear();
            botWriter("Hello user, how can I help you today?");
        });

        textField.setOnKeyPressed(e ->{
            if(e.getCode() == KeyCode.ENTER){
                post(true);
            }
        });


        sendButton.setOnAction(e -> {
            String writtenText = post(false);

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

        });


    }


    public Scene getScene(){
        return this.scene;
    }


}
