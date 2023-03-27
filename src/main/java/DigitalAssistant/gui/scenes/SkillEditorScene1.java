package DigitalAssistant.gui.scenes;

import DigitalAssistant.Utilities.Handler;
import DigitalAssistant.Utilities.SkillsUserInput;
import DigitalAssistant.Utilities.SlotValuePair;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Scanner;

public class SkillEditorScene1 implements Initializable {

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
    private Button addAction;

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
    private Button nextButton;

    @FXML
    private Button removeSlotValuePairButton;

    @FXML
    private TableView<SlotValuePair> tableView;

    private Stage window;

    private ObservableList<SlotValuePair> slotValuePairs;
    private HashMap<String, Boolean> inputHashmap;
    private static SkillsUserInput skillsUserInput;

    private boolean firstVisit;

    public SkillEditorScene1(Handler handler, Stage window){
        this.handler = handler;
        this.title = "Skill Editor Window";
        this.width = handler.getScreen().getWidth()/3;
        this.height = handler.getScreen().getHeight()/2;
        this.window = window;
        skillsUserInput = new SkillsUserInput("", "", FXCollections.observableArrayList(), new HashMap<>(), FXCollections.observableArrayList());
        slotValuePairs = skillsUserInput.getSlotValuePairs();
        inputHashmap = skillsUserInput.getInputHashMap();
        slotList = skillsUserInput.getSlotList();
        skillNameString = skillsUserInput.getSkillName();
        protoSentenceString = skillsUserInput.getProtoSentence();
        this.firstVisit = true;

    }

    public SkillEditorScene1(Handler handler, Stage window, SkillsUserInput skillsUserInput){
        this.handler = handler;
        this.title = "Skill Editor Window";
        this.width = handler.getScreen().getWidth()/3;
        this.height = handler.getScreen().getHeight()/2;
        this.window = window;
        SkillEditorScene1.skillsUserInput = skillsUserInput;
        slotValuePairs = skillsUserInput.getSlotValuePairs();
        inputHashmap = skillsUserInput.getInputHashMap();
        slotList = skillsUserInput.getSlotList();
        skillNameString = skillsUserInput.getSkillName();
        protoSentenceString = skillsUserInput.getProtoSentence();

        //enable buttons if there already is user input
        this.firstVisit = false;
    }

    public SkillsUserInput display() {

        handler.getWindow().setTitle(this.title);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("SkillEditorScene1.fxml"));
        loader.setController(this);

        try {
            this.scene = new Scene(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.scene.getStylesheets().add(getClass().getResource("SkillEditorStyles.css").toExternalForm());
        init();
        return skillsUserInput;
    }

    private void setDisableButtons(boolean disableButtons){
        addSlotButton.setDisable(disableButtons);
        nextButton.setDisable(disableButtons);
        removeSlotValuePairButton.setDisable(disableButtons);
    }

    private void init(){

        if(firstVisit)
            setDisableButtons(true);


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

           if(firstVisit)
               setDisableButtons(false);

        });

        this.addSlotButton.setOnAction(e -> {

            if(inputHashmap.get(slotChoiceBox.getValue()) == false && !slotValueText.getText().isEmpty()){
                slotValuePairs.add(new SlotValuePair(slotChoiceBox.getValue(), slotValueText.getText()));

            } else if (inputHashmap.get(slotChoiceBox.getValue()) == false && slotValueText.getText().isEmpty()){
                inputHashmap.put(slotChoiceBox.getValue(), true);
                slotValuePairs.add(new SlotValuePair(slotChoiceBox.getValue(), "INPUT-SLOT"));
            } else {

            }

            System.out.println(slotValuePairs.size());
            slotValueText.clear();
        });

        this.removeSlotValuePairButton.setOnAction(e -> {

            SlotValuePair selectedItem = tableView.getSelectionModel().getSelectedItem();

            if(inputHashmap.get(selectedItem.getSlot()) == true && selectedItem.getValue() == "INPUT-SLOT"){
                inputHashmap.put(selectedItem.getSlot(), false);
            }
            slotValuePairs.remove(selectedItem);

        });

        this.slotValueText.setOnKeyPressed(e ->{
            if(e.getCode() == KeyCode.ENTER){
                this.addSlotButton.fire();
            }
        });

        this.protoSentenceText.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER){
                this.insertSkillbutton.fire();
            }
        });

        this.nextButton.setOnAction(e -> {
            SkillEditorScene2 ses2 = new SkillEditorScene2(handler, window, new SkillsUserInput(skillNameString, protoSentenceString, slotValuePairs, inputHashmap, slotList));
            skillsUserInput = ses2.display();

            window.setScene(ses2.getScene());
            window.show();
        });

        this.tableView.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.DELETE){
                this.removeSlotValuePairButton.fire();
            }
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
                String slot = temp.get(i);
                while(temp.get(i).charAt(temp.get(i).length()-1) != '>'){
                    i++;
                    temp.add(in.next());
                    slot += " "+temp.get(i);
                }
                phList.add(slot);
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

        columnSlot.setCellValueFactory(new PropertyValueFactory<SlotValuePair, String>("slot"));
        columnValue.setCellValueFactory(new PropertyValueFactory<SlotValuePair, String>("value"));
    }
}
