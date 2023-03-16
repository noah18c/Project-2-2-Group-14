package DigitalAssistant.skillLogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Skill {

    private String name;
    private String prototype;
    private HashMap<String, ArrayList<String>> placeholders;
    private ArrayList<Action> actions;
    private HashMap<String, String> inputValues;

    public Skill(String name, String prototype) {
        this.name = name;
        this.prototype = prototype;
        this.placeholders = new HashMap<>();
        this.actions = new ArrayList<>();
    }

    // The method that checks whether it is correspoding skill to the input or not
    // if it is the corresponding skill saves the placeholder values to hashmap in order call proper action
    public boolean match(String input){

        Set<String> keySet = placeholders.keySet();
        ArrayList<String> keyList = new ArrayList<>(keySet);// get the key values of placeholders into arraylist like <DAY> <TIME>
        
        String regex = prototype.substring(0, prototype.length() - 1) + " " + prototype.charAt(prototype.length() - 1); // To spearate last char from the sentence for the case there is "?" at the end of the sentence
        String[] words = regex.split(" ");//assign every word to array
        

        String regexInput = input.substring(0, input.length() - 1) + " " + input.charAt(input.length() - 1); // To spearate last char from the sentence for the case there is "?" at the end of the sentence
        String[] wordsInput = regexInput.split(" ");//assign every word to array  
        
        ArrayList<Integer> indexOfPlaceholders = new ArrayList<>();

        if(words.length != wordsInput.length){
            return false;
        }
        else{
            for (int i = 0; i < words.length; i++) {
                for (int j = 0; j < keyList.size(); j++) {
                    if(words[i].equals(keyList.get(j))){
                        indexOfPlaceholders.add(i);
                    }
                }
            }
            for (int i = 0; i < wordsInput.length; i++) {
                if(!indexOfPlaceholders.contains(i)){
                    if(!wordsInput[i].equals(words[i])){
                        return false;
                    }
                }
            }
            inputValues = new HashMap<>();
            for (int i = 0; i < keyList.size(); i++) {
                for (int j = 0; j < indexOfPlaceholders.size(); j++) {
                    if(words[indexOfPlaceholders.get(j)].equals(keyList.get(i))){
                        setInputValue(keyList.get(i), wordsInput[indexOfPlaceholders.get(j)]);
                    }
                }
            }
        }
        performAction();
        return true;
    }

    public void performAction(){
        //Find Corresponding action for given input then trigger the action

        for (int i = 0; i < actions.size(); i++) {
            ArrayList<String> keyList = new ArrayList<>(actions.get(i).getActionValues().keySet());
            int checker = 0;
            for (int j = 0; j < keyList.size(); j++) {
                if(actions.get(i).getActionValues().get(keyList.get(j)).equals(inputValues.get(keyList.get(j)))){
                    checker++;
                }
            }
            if(checker == keyList.size()){
                actions.get(i).triggerAction();
                break;
            }
            else{
                if(i == actions.size()-1){
                    System.out.println("Missing Input!!");
                }
            }
        }
    }

    //If the input matches with current Skill, that method assigns key value pair.
    //For example if the input is "Which Lecture are there on Monday at 9?" the map will be like Day = Monday Time = 9
    public void setInputValue(String key, String value) {
        inputValues.put(key, value);
    }

    public String getInputValue(String key) {
        return inputValues.get(key);
    }    

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPrototype(String prototype) {
        this.prototype = prototype;
    }

    public String getPrototype() {
        return prototype;
    }

    public void setPlaceholderValue(String placeholder, ArrayList<String> values) {
        placeholders.put(placeholder, values);
    }

    public HashMap<String,ArrayList<String>> getPlaceholders(){
        return placeholders;
    }

    public void addAction(Action action) {
        actions.add(action);
    }

    public ArrayList<Action> getActions() {
        return actions;
    }
}