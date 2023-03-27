package DigitalAssistant.gui.scenes;

import DigitalAssistant.Utilities.Handler;
import DigitalAssistant.Utilities.Rule;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
    private ChoiceBox<String> actionSelector;

    @FXML
    private Button addPairButton;

    @FXML
    private TableColumn<Rule, String> columnAction;

    @FXML
    private TableColumn<Rule, String> columnOutput;

    @FXML
    private TableColumn<SlotValuePair, String> columnSlotAll;

    @FXML
    private TableColumn<SlotValuePair, String> columnSlotRule;

    @FXML
    private TableColumn<Rule, String> columnSlotValuePairs;

    @FXML
    private TableColumn<SlotValuePair, String> columnValueAll;

    @FXML
    private TableColumn<SlotValuePair, String> columnValueRule;

    @FXML
    private Button confirmRuleButton;

    @FXML
    private TextArea outputTextfield;

    @FXML
    private Button removePairButton;

    @FXML
    private Button removeRuleButton;

    @FXML
    private TableView<SlotValuePair> slotValueTable;

    @FXML
    private TableView<SlotValuePair> slotValueTableRule;

    @FXML
    private Button submitSkillButton;

    @FXML
    private TableView<Rule> tableView;

    @FXML
    private Button resetButton;
    @FXML
    private Button goBackButton;
    @FXML
    private Button cancelButton;


    private static SkillsUserInput skillsUserInput;

    private ObservableList<SlotValuePair> slotValuePairRule = FXCollections.observableArrayList();

    private ObservableList<Rule> rules = FXCollections.observableArrayList();

    private Stage window;


    public SkillEditorScene2(Handler handler, Stage window, SkillsUserInput skillsUserInput){
        this.handler = handler;
        this.window = window;
        this.title = "Skill Editor Window";
        this.width = handler.getScreen().getWidth()/3;
        this.height = handler.getScreen().getHeight()/2;
        this.skillsUserInput = skillsUserInput;




    }

    public SkillsUserInput display() {

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
        return skillsUserInput;
    }

    private void init(){

        this.addPairButton.setOnAction(e -> {
            SlotValuePair temp = this.slotValueTable.getSelectionModel().getSelectedItem();
            checkUnique(temp);
            this.slotValuePairRule.add(temp);
        });

        this.removePairButton.setOnAction(e -> {
            slotValuePairRule.remove(this.slotValueTableRule.getSelectionModel().getSelectedItem());
        });

        this.confirmRuleButton.setOnAction(e -> {
           Rule rule = new Rule(slotValuePairRule, actionSelector.getSelectionModel().getSelectedItem(), outputTextfield.getText());
           rules.add(rule);
        });
        this.submitSkillButton.setOnAction(e -> {
           skillsUserInput.setRules(rules);
           window.close();
        });
        this.removeRuleButton.setOnAction(e -> {
            rules.remove(tableView.getSelectionModel().getSelectedItem());
        });
        this.resetButton.setOnAction(e -> {
            rules.clear();
            slotValuePairRule.clear();
            outputTextfield.clear();
        });
        this.goBackButton.setOnAction(e -> {
            SkillEditorScene1 ses1 = new SkillEditorScene1(handler, window, skillsUserInput);
            ses1.display();

            window.setScene(ses1.getScene());
            window.show();
        });
        this.cancelButton.setOnAction(e -> {
            window.close();
        });


    }

    private boolean checkUnique(SlotValuePair toCheck){
        for(SlotValuePair pair : slotValuePairRule){
            if(pair.getSlot() == toCheck.getSlot()){
                slotValuePairRule.remove(pair);
                return false;
            }
        }
        return true;
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




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.slotValueTable.setItems(skillsUserInput.getSlotValuePairs());
        this.columnSlotAll.setCellValueFactory(new PropertyValueFactory<SlotValuePair, String>("slot"));
        this.columnValueAll.setCellValueFactory(new PropertyValueFactory<SlotValuePair, String>("value"));

        this.slotValueTableRule.setItems(this.slotValuePairRule);
        this.columnSlotRule.setCellValueFactory(new PropertyValueFactory<SlotValuePair, String>("slot"));
        this.columnValueRule.setCellValueFactory(new PropertyValueFactory<SlotValuePair, String>("value"));

        this.actionSelector.getItems().add("|Search|");
        this.actionSelector.getItems().add("|Print|");

        this.tableView.setItems(this.rules);
        this.columnSlotValuePairs.setCellValueFactory(new PropertyValueFactory<Rule, String>("slotValueString"));
        this.columnAction.setCellValueFactory(new PropertyValueFactory<Rule, String>("action"));
        this.columnOutput.setCellValueFactory(new PropertyValueFactory<Rule, String>("output"));
    }
}
