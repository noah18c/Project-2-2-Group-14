package DigitalAssistant.skillLogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Action {
    private String actionType;
    private String actionSentence;
    private HashMap<String,ArrayList<String>> actionValues;
    private String output;
    private String temporaryActionSentence;
    private HashMap<String,ArrayList<String>> inputValues;

    public Action(String actionType, String actionSentence , HashMap<String,ArrayList<String>> actionValues){
        this.actionSentence = actionSentence;
        this.actionType = actionType;
        this.actionValues = actionValues;
        //System.out.println(actionType + " " + actionInput + " " + actionValues);
    }

    public String triggerAction(){
        loadAction();
        return output;
    }

    public void setInputValues(HashMap<String,ArrayList<String>> inputValues){
        this.inputValues = inputValues;
    }

    public void changeActionSentence(){
        temporaryActionSentence = actionSentence;

        actionSentence = actionSentence.substring(0, actionSentence.length() - 1) + " " + actionSentence.charAt(actionSentence.length() - 1); // To spearate last char from the sentence for the case there is "?" at the end of the sentence
        String[] words = actionSentence.split(" ");

        Set<String> key = inputValues.keySet();
        ArrayList<String> keys = new ArrayList<>(key);
        
        for (int i = 0; i < keys.size(); i++) {
            ArrayList<String> values = inputValues.get(keys.get(i));
            for (int j = 0; j < words.length; j++) {
                if(words[j].equals(keys.get(i))){
                    words[j] = values.get(0);
                    values.remove(0);
                }
            }
        }
        actionSentence = "";
        for (int i = 0; i < words.length; i++) {
            actionSentence+= words[i] + " ";
        }
        actionSentence.trim();
    }


    public void loadAction(){
        changeActionSentence();

        if(actionType.equals("|Search|")){
            GoogleSearchAction();
        }
        else if(actionType.equals("|Print|")){
            PrintAction();
        }

        actionSentence = temporaryActionSentence;
    }

    private void GoogleSearchAction() {
        output = "Searching Google for ..." + actionSentence;
    }

    private void PrintAction(){
        output = actionSentence;
    }

    public String getActionType() {
        return actionType;
    }

    public String getActionSentence() {
        return actionSentence;
    }

    public HashMap<String, ArrayList<String>> getActionValues() {
        return actionValues;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public void setActionSentence(String actionInput) {
        this.actionSentence = actionInput;
    }

    public void setActionValues(HashMap<String, ArrayList<String>> actionValues) {
        this.actionValues = actionValues;
    }
}