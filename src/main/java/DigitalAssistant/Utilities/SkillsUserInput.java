package DigitalAssistant.Utilities;

import javafx.collections.ObservableList;

import java.util.HashMap;

public class SkillsUserInput {

    private String skillName, protoSentence;
    private ObservableList<SlotValuePair> slotValuePairs;
    private ObservableList<String> slotList;
    private HashMap<String, Boolean> inputHashMap;
    private ObservableList<Rule> rules;
    public SkillsUserInput(String skillName, String protoSentence, ObservableList<SlotValuePair> slotValuePairs, HashMap<String, Boolean> inputHashMap, ObservableList<String> slotList){
        this.skillName = skillName;
        this.protoSentence = protoSentence;
        this.slotValuePairs = slotValuePairs;
        this.inputHashMap = inputHashMap;
        this.slotList = slotList;
        this.rules = null;
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

    public ObservableList<String> getSlotList() {
        return slotList;
    }

    public void setSlotList(ObservableList<String> slotList) {
        this.slotList = slotList;
    }

    public ObservableList<Rule> getRules() {
        return rules;
    }

    public void setRules(ObservableList<Rule> rules) {
        this.rules = rules;
    }
}
