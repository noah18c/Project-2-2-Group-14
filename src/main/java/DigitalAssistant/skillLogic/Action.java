package DigitalAssistant.skillLogic;

import java.util.HashMap;

public class Action {
    private String actionType;
    private String actionInput;
    private HashMap<String,String> actionValues;

    public Action(String actionType, String actionInput , HashMap<String,String> actionValues){
        this.actionInput = actionInput;
        this.actionType = actionType;
        this.actionValues = actionValues;
        //System.out.println(actionType + " " + actionInput + " " + actionValues);
    }

    public void triggerAction(){
        loadAction();
    }

    public void loadAction(){
        if(actionType.equals("|Search|")){
            GoogleSearchAction();
        }
        else if(actionType.equals("|Print|")){
            PrintAction();
        }
    }

    private void GoogleSearchAction() {
        System.out.println("Searching Google for ..." + actionValues);
    }

    private void distanceFinderAction(HashMap<String,String> inputValues){

        String search = "How far is " + actionValues.get("CITY1") + " to " + actionValues.get("CITY2") + " by " + actionValues.get("METHOD");
        
    }

    private void PrintAction(){
        
        System.out.println(actionInput);
    }

    public String getActionType() {
        return actionType;
    }

    public String getActionInput() {
        return actionInput;
    }

    public HashMap<String, String> getActionValues() {
        return actionValues;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public void setActionInput(String actionInput) {
        this.actionInput = actionInput;
    }

    public void setActionValues(HashMap<String, String> actionValues) {
        this.actionValues = actionValues;
    }
}