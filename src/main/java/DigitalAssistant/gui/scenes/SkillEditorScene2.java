package DigitalAssistant.gui.scenes;

import DigitalAssistant.Utilities.Handler;
import DigitalAssistant.Utilities.SkillsVariables;
import DigitalAssistant.Utilities.SlotValuePair;
import DigitalAssistant.gui.stages.SkillEditorStage2;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Scanner;

public class SkillEditorScene2 implements Initializable {

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
    private TableColumn<SlotValuePair, String> columnSlot1;

    @FXML
    private TableColumn<SlotValuePair, String> columnSlot2;

    @FXML
    private TableColumn<SlotValuePair, String> columnSlot3;

    @FXML
    private TableColumn<SlotValuePair, String> columnSlot4;

    @FXML
    private TableColumn<SlotValuePair, String> columnAction;

    @FXML
    private TextField slotValueText;

    @FXML
    private TextField slotValueText1;

    @FXML
    private TextField slotValueText11;

    @FXML
    private TextField slotValueText111;
    @FXML
    private TextField slotValueText1111;


    @FXML
    private TableView<SlotValuePair> tableView;

    private ObservableList<SlotValuePair> slotValuePairs = FXCollections.observableArrayList();
    private HashMap<String, Boolean> inputHashmap = new HashMap<>();

    public SkillEditorScene2(Handler handler){
        this.handler = handler;
        this.title = "Skill Editor Window";
        this.width = handler.getScreen().getWidth()/3;
        this.height = handler.getScreen().getHeight()/2;
    }

    public void display() {

        handler.getWindow().setTitle(this.title);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("SkillEditorScene2.fxml"));
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
        this.addSlotButton.setOnAction(e -> {

            if(inputHashmap.get(slotValueText.getText()) == false && !slotValueText.getText().isEmpty()){
                slotValuePairs.add(new SlotValuePair(slotValueText1.getText(), slotValueText.getText()));

            } else if (inputHashmap.get(slotValueText.getText()) == false && slotValueText.getText().isEmpty()){
                inputHashmap.put(slotValueText.getText(), true);
                slotValuePairs.add(new SlotValuePair(slotValueText.getText(), "INPUT-SLOT1"));
            }

            if(inputHashmap.get(slotValueText1.getText()) == false && !slotValueText1.getText().isEmpty()){
                slotValuePairs.add(new SlotValuePair(slotValueText1.getText(), slotValueText1.getText()));

            } else if (inputHashmap.get(slotValueText1.getText()) == false && slotValueText1.getText().isEmpty()){
                inputHashmap.put(slotValueText1.getText(), true);
                slotValuePairs.add(new SlotValuePair(slotValueText1.getText(), "INPUT-SLOT2"));
            }

            if(inputHashmap.get(slotValueText11.getText()) == false && !slotValueText11.getText().isEmpty()){
                slotValuePairs.add(new SlotValuePair(slotValueText11.getText(), slotValueText11.getText()));

            } else if (inputHashmap.get(slotValueText11.getText()) == false && slotValueText11.getText().isEmpty()){
                inputHashmap.put(slotValueText11.getText(), true);
                slotValuePairs.add(new SlotValuePair(slotValueText11.getText(), "INPUT-SLOT3"));
            }
            if(inputHashmap.get(slotValueText111.getText()) == false && !slotValueText111.getText().isEmpty()){
                slotValuePairs.add(new SlotValuePair(slotValueText111.getText(), slotValueText111.getText()));

            } else if (inputHashmap.get(slotValueText111.getText()) == false && slotValueText111.getText().isEmpty()){
                inputHashmap.put(slotValueText111.getText(), true);
                slotValuePairs.add(new SlotValuePair(slotValueText111.getText(), "INPUT-SLOT4"));
            }
            if(inputHashmap.get(slotValueText1111.getText()) == false && !slotValueText1111.getText().isEmpty()){
                slotValuePairs.add(new SlotValuePair(slotValueText1111.getText(), slotValueText1111.getText()));

            } else if (inputHashmap.get(slotValueText1111.getText()) == false && slotValueText1111.getText().isEmpty()){
                inputHashmap.put(slotValueText1111.getText(), true);
                slotValuePairs.add(new SlotValuePair(slotValueText1111.getText(), "INPUT-SLOT4"));
            }

            System.out.println(slotValuePairs.size());
            slotValueText.clear();
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
        this.tableView.setItems(this.slotValuePairs);
        this.tableView.setEditable(true);

        columnSlot1.setCellValueFactory(new PropertyValueFactory<SlotValuePair, String>("Slot 1"));
        columnSlot2.setCellValueFactory(new PropertyValueFactory<SlotValuePair, String>("Slot 2"));
        columnSlot3.setCellValueFactory(new PropertyValueFactory<SlotValuePair, String>("Slot 3"));
        columnSlot4.setCellValueFactory(new PropertyValueFactory<SlotValuePair, String>("Slot 4"));
        columnAction.setCellValueFactory(new PropertyValueFactory<SlotValuePair, String>("Action"));
    }
}
