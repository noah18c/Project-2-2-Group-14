package DigitalAssistant.Utilities;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;

public class SkillsVariables {

    private String skillName, protoSentence;
    private ObservableList<SlotValuePair> slotValuePairs;
    private HashMap<String, Boolean> inputHashMap;
    public SkillsVariables(String skillName, String protoSentence, ObservableList<SlotValuePair> slotValuePairs, HashMap<String, Boolean> inputHashMap){
        this.skillName = skillName;
        this.protoSentence = protoSentence;
        this.slotValuePairs = slotValuePairs;
        this.inputHashMap = inputHashMap;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getProtoSentence() {
        return protoSentence;
    }

    public void setProtoSentence(String protoSentence) {
        this.protoSentence = protoSentence;
    }

    public ObservableList<SlotValuePair> getSlotValuePairs() {
        return slotValuePairs;
    }

    public void setSlotValuePairs(ObservableList<SlotValuePair> slotValuePairs) {
        this.slotValuePairs = slotValuePairs;
    }

    public HashMap<String, Boolean> getInputHashMap() {
        return inputHashMap;
    }

    public void setInputHashMap(HashMap<String, Boolean> inputHashMap) {
        this.inputHashMap = inputHashMap;
    }
}
