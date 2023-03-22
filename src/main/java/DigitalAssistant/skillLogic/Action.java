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
<<<<<<< HEAD
        if (actionType.equals(null)) {
            UnknownAction();
        }
        else if(actionType.equals("|Search|")){
=======
        if(actionType.equals("|Search|")){
>>>>>>> 2355ce325f46de977ba0d04c30cf0884415a19b0
            GoogleSearchAction();
        }
        else if(actionType.equals("|Print|")){
            PrintAction();
        }
    }

<<<<<<< HEAD
    private void UnknownAction() {
        System.out.println("I'm sorry, there is no action");
    }

    private void GoogleSearchAction() {
        System.out.println("Searching Google for ...");
    }

    private void CalendarAction() {
        System.out.println("Calender Action");
=======
    private void GoogleSearchAction() {
        System.out.println("Searching Google for ..." + actionValues);
>>>>>>> 2355ce325f46de977ba0d04c30cf0884415a19b0
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