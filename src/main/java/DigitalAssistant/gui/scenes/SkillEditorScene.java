package DigitalAssistant.gui.scenes;

import DigitalAssistant.Utilities.Handler;
import DigitalAssistant.Utilities.SlotValuePair;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Scanner;

public class SkillEditorScene implements Initializable {

    private Handler handler;
    private Scene scene;
    private String title;
    private int width, height;

    private String skillNameString, protoSentenceString;

    private ArrayList<String> slots;

    private ObservableList<String> slotList = FXCollections.observableArrayList();

    @FXML
    private Button addSlotButton;

    @FXML
    private TableColumn<SlotValuePair, String> columnSlot;

    @FXML
    private TableColumn<SlotValuePair, String> columnValue;

    @FXML
    private Button inputSlotButton;

    @FXML
    private Button inputSlotButton1;

    @FXML
    private Button insertSkillbutton;

    @FXML
    private TextField protoSentenceText;

    @FXML
    private TextField skillNameText;

    @FXML
    private ChoiceBox<String> slotChoiceBox;

    @FXML
    private TextField slotValueText;

    @FXML
    private Button submitButton;

    @FXML
    private Button removeSlotValuePairButton;

    @FXML
    private TableView<SlotValuePair> tableView;

    private ObservableList<SlotValuePair> slotValuePairs = FXCollections.observableArrayList();
    private HashMap<String, Boolean> inputHashmap = new HashMap<>();

    public SkillEditorScene(Handler handler){
        this.handler = handler;
        this.title = "Skill Editor Window";
        this.width = handler.getScreen().getWidth()/3;
        this.height = handler.getScreen().getHeight()/2;
    }

    public void display() {

        handler.getWindow().setTitle(this.title);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("SkillEditorScene.fxml"));
        loader.setController(this);

        try {
            this.scene = new Scene(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.scene.getStylesheets().add(getClass().getResource("SkillEditorStyles.css").toExternalForm());
        init();
    }

    private void init(){

        addSlotButton.setDisable(true);
        submitButton.setDisable(true);
        removeSlotValuePairButton.setDisable(true);

        insertSkillbutton.setOnAction(e -> {
            slotValuePairs.clear();

            this.protoSentenceString = this.protoSentenceText.getText();
            this.skillNameString = this.skillNameText.getText();
           this.protoSentenceText.clear();
           this.skillNameText.clear();
           this.slotList.clear();

           this.slots = readSlots(this.protoSentenceString);
           for(int i = 0; i < slots.size(); i++){
               slotList.add(slots.get(i));
               inputHashmap.put(slots.get(i), false);
           }
            slotChoiceBox.getSelectionModel().selectFirst();
            addSlotButton.setDisable(false);
            submitButton.setDisable(false);
            removeSlotValuePairButton.setDisable(false);

        });

        this.addSlotButton.setOnAction(e -> {

            if(inputHashmap.get(slotChoiceBox.getValue()) == false){
                slotValuePairs.add(new SlotValuePair(slotChoiceBox.getValue(), slotValueText.getText()));
                inputHashmap.put(slotChoiceBox.getValue(), true);
            } else {

            }

            slotValueText.clear();
        });

        this.submitButton.setOnAction(e -> {

        });

        this.removeSlotValuePairButton.setOnAction(e -> {
            slotValuePairs.remove(tableView.getSelectionModel().getSelectedItem());
        });


    }


    private void insertSlotValue(String value){
        //slotHashMap.get(slotChoiceBox.getValue()).
    }


    private ArrayList<String> readSlots(String input){
        ArrayList <String> phList = new ArrayList<String>();
        ArrayList<String> temp = new ArrayList<String>();
        String prototype = input;
        Scanner in = new Scanner(prototype);
        in.useDelimiter(" ");
        int i = 0;
        while(in.hasNext()){
            temp.add(in.next());
            char c = '<';
            if (temp.get(i).charAt(0) == c){
                phList.add(temp.get(i));
            }
            i++;
        }
        return phList;
    }

    public Scene getScene(){
        return this.scene;
    }

    public static void main(String[] args){
        String prototype = "";
        ArrayList <String> phList = new ArrayList<String>();
        ArrayList<String> temp = new ArrayList<String>();


        prototype = "hello <nope> what <yehaw>";
        Scanner in = new Scanner(prototype);
        in.useDelimiter(" ");
        int i = 0;
        while(in.hasNext()){
            temp.add(in.next());
            char c = '<';
            if (temp.get(i).charAt(0) == c){
                phList.add(temp.get(i));
            }
            i++;
        }


    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.slotChoiceBox.setItems(this.slotList);
        this.tableView.setItems(this.slotValuePairs);
        this.tableView.setEditable(true);

        columnSlot.setCellValueFactory(new PropertyValueFactory<SlotValuePair, String>("Slot"));
        columnValue.setCellValueFactory(new PropertyValueFactory<SlotValuePair, String>("Value"));
    }
}
