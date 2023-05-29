package DigitalAssistant.skillLogic;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Locale;
import java.util.ResourceBundle;


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
        //System.out.println(actionType + " " + actionSentence + " " + actionValues);
        //System.out.println(actionSentence);
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
        else if(actionType.equals("|Translate|")){
            translationAction();
        }

        //TODO : More action types ,API's...

        
        actionSentence = temporaryActionSentence;
    }

    private void translationAction(){
        if(actionValues.get("<LANGUAGE>").get(0).equalsIgnoreCase("Japanese")){
            Translator translator = new Translator("ja");
            String sentence = actionSentence;
            String translatedSentence = translator.translate(sentence);
            output = "Translated Sentence: " + translatedSentence;   
        }
        else if(actionValues.get("<LANGUAGE>").get(0).equalsIgnoreCase("Spanish")){
            Translator translator = new Translator("es");
            String sentence = actionSentence;
            String translatedSentence = translator.translate(sentence);
            output = "Translated Sentence: " + translatedSentence;
        }
        else if(actionValues.get("<LANGUAGE>").get(0).equalsIgnoreCase("German")){
            Translator translator = new Translator("de");
            String sentence = actionSentence;
            String translatedSentence = translator.translate(sentence);
            output = "Translated Sentence: " + translatedSentence;
        }
        else if(actionValues.get("<LANGUAGE>").get(0).equalsIgnoreCase("French")){
            Translator translator = new Translator("fr");
            String sentence = actionSentence;
            String translatedSentence = translator.translate(sentence);
            output = "Translated Sentence: " + translatedSentence;
        }
        else if(actionValues.get("<LANGUAGE>").get(0).equalsIgnoreCase("Italian")){
            Translator translator = new Translator("it");
            String sentence = actionSentence;
            String translatedSentence = translator.translate(sentence);
            output = "Translated Sentence: " + translatedSentence;
        }
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
        if(!lastChar.equals(">")){
            if (lastChar.matches("\\p{Punct}")) {
                String sentenceWithoutLastChar = sentence.substring(0, sentence.length() - 1);
                return sentenceWithoutLastChar + " " + lastChar;
            }
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

    public boolean equalHashmap(HashMap<String, ArrayList<String>> hashmapToCheck){
        ArrayList<String> keySet = new ArrayList<>(actionValues.keySet());
        ArrayList<String> keySetInput = new ArrayList<>(hashmapToCheck.keySet());

        if(keySet.equals(keySetInput)){
            return false;
        }

        for (int i = 0; i < keySet.size(); i++) {
            if(!(actionValues.get(keySet.get(i)).equals(hashmapToCheck.get(keySet.get(i))))){
                return false;
            }            
        }
        
        return true;
    }

    public String getValuesAsString(String key){
        ArrayList<String> values = inputValues.get(key);
        String result = "";

        for (int index = 0; index < values.size(); index++) {
            result += key + " " + values.get(index);
        }
        
        return result;
    }

    public class Translator {
    private ResourceBundle resourceBundle;

    public Translator(String language) {
        Locale locale = new Locale.Builder().setLanguage(language).build();
        resourceBundle = ResourceBundle.getBundle("translations", locale);
    }

    public String translate(String sentence) {
        String translatedText = resourceBundle.getString(sentence);
        return translatedText;
    }

    }

    
}