package DigitalAssistant.gui.scenes;

import DigitalAssistant.Utilities.Handler;
import DigitalAssistant.gui.stages.SkillEditorStage;
import DigitalAssistant.skillLogic.SkillEditor;
import DigitalAssistant.skillLogic.FaceDetection;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatScene implements SceneInterface, Initializable {

    private Handler handler;
    private Scene scene;
    private String title;
    private int width, height;
    private SkillEditor skillEditor;

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

    @FXML
    private ChoiceBox<String> choiceBox;

    private ObservableList<String> choiceBoxOptions = FXCollections.observableArrayList();

    public ChatScene(Handler handler){
        this.handler = handler;
        this.title = "Go ahead... ask!";
        this.width = handler.getScreen().getWidth()/3;
        this.height = handler.getScreen().getHeight()/2;
        skillEditor = new SkillEditor();
    }

    /**
     * Method that displays this scene
     */
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
        //FaceDetection detector = new FaceDetection();
        init();
        initActions();
    }

    /**
     * Method for posting a string on side of the chatbox
     * @param isUser    if its a user, post on right, if not, post on left side
     * @param inputText the input text
     * @return the string that has been put in
     */
    public String post(boolean isUser, String inputText){
        String writtenText = inputText;

        Text textBlock = new Text(writtenText);
        textBlock.wrappingWidthProperty().bind(Bindings.divide(scrollPane.widthProperty(), 1.1));

        Label labelmsg = new Label("", textBlock);

        labelmsg.prefWidthProperty().bind(Bindings.divide(scrollPane.widthProperty(), 1.1));
        labelmsg.setMinHeight(30);
        messages.add(labelmsg);
        int lastIndex = messages.size()-1;

        if(isUser){
            textBlock.setTextAlignment(TextAlignment.RIGHT);
            textField.clear();
        } else {

            textBlock.setTextAlignment(TextAlignment.LEFT);
        }

        chatbox.getChildren().add(messages.get(lastIndex));

        return writtenText;
    }

    /**
     * Method for posting a string from the textfield on one side of the chatbox
     * @param isUser if its a user, post on right, if not, post on left side
     * @return the string that has been put in
     */

    public String post(boolean isUser){
        String writtenText = (String) textField.getText();
        Text textBlock = new Text(writtenText);
        textBlock.wrappingWidthProperty().bind(Bindings.divide(scrollPane.widthProperty(), 1.1));


        Label labelmsg = new Label("",textBlock);

        labelmsg.prefWidthProperty().bind(Bindings.divide(scrollPane.widthProperty(), 1.1));
        labelmsg.setMinHeight(30);
        messages.add(labelmsg);
        int lastIndex = messages.size()-1;

        if(isUser){
            textBlock.setTextAlignment(TextAlignment.RIGHT);
            messages.get(lastIndex).setAlignment(Pos.CENTER_RIGHT);
            textField.clear();
        } else {
            textBlock.setTextAlignment(TextAlignment.LEFT);
            messages.get(lastIndex).setAlignment(Pos.CENTER_LEFT);
        }


        chatbox.getChildren().add(messages.get(lastIndex));

        return writtenText;
    }


    /**
     * method that posts two buttons "add" and "get" for making choices
     * Can be edited for different choices
     */
    public void postButtons(){
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


    /**
     * Use this method if you want the bot to write something. It will do it on the left side and with an animation
     * @param inputText the input text
     */
    public void botWriter(String inputText){
        post(false, inputText);
        int lastIndex = messages.size()-1;

        Text textObject = (Text) messages.get(lastIndex).getGraphic();
        String content = textObject.getText();


        Animation animation = new Transition() {
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
    /**
     * Use this method if you want the bot to write something. It will do it on the left side and with an animation
     * @param inputText the input text
     */
    public void botWriter(Animation newAnimation, String inputText){
        post(false, inputText);
        int lastIndex = messages.size()-1;

        Text textObject = (Text) messages.get(lastIndex).getGraphic();
        String content = textObject.getText();


        Animation animation = new Transition() {
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

    /**
     * initial method that starts the sequence of chats
     */
    private void init() {
        botWriter("Hello user, what would you like to do?");
    }
    // inital method when no face was detected
    private void initFalse() {
        botWriter("I could not detect a user... Please come back");
    }

    private void initActions(){
        chatbox.setMinWidth(scrollPane.getMinWidth()-20);
        chatbox.setMinHeight(scrollPane.getMinHeight());
        chatbox.setAlignment(Pos.TOP_CENTER);
        scrollPane.vvalueProperty().bind(chatbox.heightProperty());


        /*
        skillButton.setOnAction(e -> {
            SkillEditorStage ses = new SkillEditorStage(handler, skillEditor);
            ses.display();
        });

         */

        choiceBox.setOnAction(e -> {
           switch(choiceBox.getSelectionModel().getSelectedItem()){
               case "Open skills editor":
                   SkillEditorStage ses = new SkillEditorStage(handler, skillEditor);
                   ses.display();
                   choiceBox.getSelectionModel().selectFirst();
                   break;
               case "View current skills":
                   SkillsList skillsList = new SkillsList(handler);
                   skillsList.display();
                   choiceBox.getSelectionModel().selectFirst();
                   break;
               case "Restart chat":
                   chatbox.getChildren().clear();
                   messages.clear();
                   init();
                   choiceBox.getSelectionModel().selectFirst();
                   break;
           }
        });

        /*
        clearButton.setOnAction(e -> {
            chatbox.getChildren().clear();
            messages.clear();
            init();
        });

         */

        textField.setOnKeyPressed(e ->{
            if(e.getCode() == KeyCode.ENTER){
                post(true);
                Text latestMessage = (Text) messages.get(messages.size()-1).getGraphic(); // Input from the user
                String result = skillEditor.search(latestMessage.getText()); // output based on the input
                botWriter(result); // prints the output
            }

        });


        sendButton.setOnAction(e -> {
            String writtenText = post(true);
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

    public Handler getHandler() {
        return handler;
    }

    public String getTitle() {
        return title;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }


    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    public Button getSendButton() {
        return sendButton;
    }

    public VBox getvBox() {
        return vBox;
    }

    public TextField getTextField() {
        return textField;
    }

    public List<Label> getMessages() {
        return messages;
    }

    public VBox getChatbox() {
        return chatbox;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        choiceBoxOptions.add("Select option");
        choiceBoxOptions.add("Open skills editor");
        choiceBoxOptions.add("View current skills");
        choiceBoxOptions.add("Restart chat");

        choiceBox.setItems(choiceBoxOptions);
        choiceBox.getSelectionModel().selectFirst();
    }
}
