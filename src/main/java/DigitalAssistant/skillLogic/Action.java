package DigitalAssistant.skillLogic;

import java.net.URL;
import java.net.URLConnection;
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
        // System.out.println(actionType + " " + actionSentence + " " + actionValues);
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
        actionSentence = separateLastCharIfPunctuation(actionSentence);
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
        //TODO : More action types ,API's...

        
        actionSentence = temporaryActionSentence;
    }

    private void GoogleSearchAction() {
        try
            {
                URL url=new URL("https://google.co.in");
                URLConnection connection=url.openConnection();
                connection.connect();
                java.awt.Desktop.getDesktop().browse(java.net.URI.create("http://www.google.com/search?hl=en&q="+actionSentence.replace(" ", "+")+"&btnG=Google+Search"));
            }
            catch(Exception ee)
            {
                System.out.println("Google Error!");
            }
        output = "Searching Google for ..." + actionSentence;
    }

    public static String separateLastCharIfPunctuation(String sentence) {
        String lastChar = sentence.substring(sentence.length() - 1);
        if (lastChar.matches("\\p{Punct}")) {
            String sentenceWithoutLastChar = sentence.substring(0, sentence.length() - 1);
            return sentenceWithoutLastChar + " " + lastChar;
        }
        return sentence;
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