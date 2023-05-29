package DigitalAssistant.skillLogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class Skill {
    private String name;
    private String prototype;
    private HashMap<String, ArrayList<String>> placeholders;
    private ArrayList<Action> actions;
    private HashMap<String, ArrayList<String>> inputValues;

    public Skill(String name, String prototype) {
        this.name = name;
        this.prototype = prototype;
        this.placeholders = new HashMap<>();
        this.actions = new ArrayList<>();

    }

    public String start(HashMap<String, ArrayList<String>> inputValues){        
        this.inputValues = inputValues;
        return performAction();
    }

    public String performAction(){
        
        // System.out.println(inputValues);

        // for (int i = 0; i < actions.size(); i++) {
        //     System.out.println(actions.get(i).getActionValues());
        // }

        for (int i = 0; i < actions.size(); i++) {
            if(checkEquality(actions.get(i).getActionValues(), inputValues)){
                actions.get(i).setInputValues(inputValues);
                return actions.get(i).triggerAction();
            }
        }

        try {
        //Find Corresponding action for given input then trigger the action
        for (int i = 0; i < actions.size(); i++) {
            
            Action currentAction = actions.get(i);
            ArrayList<String> actionKeyList = new ArrayList<>(currentAction.getActionValues().keySet());
            ArrayList<String> inputValuesKeyList = new ArrayList<>(inputValues.keySet());
            
            int counter = 0;
            for (int j = 0; j < actionKeyList.size(); j++) {
                for (int j2 = 0; j2 < inputValuesKeyList.size(); j2++) {
                    if(actionKeyList.get(j) == inputValuesKeyList.get(j2)){
                        for (int k = 0; k < currentAction.getActionValues().get(actionKeyList.get(j)).size(); k++) {             
                            if(currentAction.getActionValues().get(actionKeyList.get(j)).get(k).equalsIgnoreCase(inputValues.get(inputValuesKeyList.get(j2)).get(k))){
                                counter++;
                            }
                            else if(currentAction.getActionValues().get(actionKeyList.get(j)).size() == 0){
                                counter ++;
                            }
                            else if(currentAction.getActionValues().get(actionKeyList.get(j)).get(k).equalsIgnoreCase("@INPUT")){
                                counter++;
                            }
                        }
                    }
                }
            }
            
            int number= 0;
            for (int j = 0; j < actionKeyList.size(); j++) {
                number+= currentAction.getActionValues().get(actionKeyList.get(j)).size();
            }
        
            if(counter == number){
                currentAction.setInputValues(inputValues);
                return currentAction.triggerAction();
            }
        }

    } catch (Exception e) {
        // TODO: handle exception
    }
        return "I couldn't find the response for the given value(s), please be more precise.";
    }

    //If the input matches with current Skill, that method assigns key value pair.
    //For example if the input is "Which Lecture are there on Monday at 9?" the map will be like Day = Monday Time = 9
    public void setInputValue(String key, ArrayList<String> value) {
        inputValues.put(key, value);
    }

    public ArrayList<String> getInputValue(String key) {
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

    /*
     * Creates a string in the format of the skills.txt template for saving skills as plain text.
     */
    public String toFileFormatString(){
        StringBuilder builder = new StringBuilder();

        //Main info and start of Skill
        builder.append("-----\n");
        builder.append("Name " + this.getName() + "\n");
        builder.append("Question " + this.getPrototype() + "\n");

        //Slots
        Set<String> keySet = this.getPlaceholders().keySet();//Making the hashMap iterable
        ArrayList<String> keyList = new ArrayList<>(keySet);
        
        for(String key : keyList){//Iterates through each slot type. (eg. "<CITY>, <DAY>, <TIME>, etc...")
            builder.append("Slot " + key + " ");

            //Adds placeholders to the line for the current slot.
            for(String placeholder : this.getPlaceholders().get(key)){
                //Check if we have reached the end of the list so no uneccessary spaces are added.
                if(this.getPlaceholders().get(key).indexOf(placeholder) == this.getPlaceholders().get(key).size()-1){
                    builder.append(placeholder);
                }else{
                    builder.append(placeholder + " ");
                }
            }

            builder.append("\n");
        }

        builder.append("--\n");
        //Actions

        for(Action action : this.getActions()){//Uses same structure as slots.
            builder.append("Action ");
            Set<String> keys = action.getActionValues().keySet();
            ArrayList<String> keyListorino = new ArrayList<>(keys);
            //Action permutations
            for(String key : keyListorino){
                //Check if we have reached the end of the list so no uneccessary spaces are added.
                if(keyListorino.indexOf(key) == keyListorino.size()-1){//If it's the last Action key, we need to not print an extra space.
                    for(String keyStr : action.getActionValues().get(key)){//For each Action x Slot pair inside the ArrayList for each Action slot (eg: <CITY>) we append the pair
                        if(action.getActionValues().get(key).indexOf(keyStr) == action.getActionValues().get(key).size()-1){//If it's the last inner pair we omit a space
                            builder.append(key + " " + keyStr);
                        }else{
                            builder.append(key + " " + keyStr + " ");
                        }
                    }        
                }else{
                    for(String keyStr : action.getActionValues().get(key)){
                        builder.append(key + " " + keyStr + " ");
                    } 
                }
            }
            //Actiontype and Action output string
            builder.append(" " + action.getActionType() + " " + action.getActionSentence() + "\n");
        }
        
        builder.append("+++++");
        //End
        return builder.toString();
    }


    public static HashMap<String, ArrayList<String>> matchHashMap(HashMap<String, ArrayList<String>> inputHashMap, String inputSentence) {
        HashMap<String, ArrayList<String>> matchedHashMap = new HashMap<>();

        // loop through each key in the inputHashMap
        for (String key : inputHashMap.keySet()) {
            ArrayList<String> values = inputHashMap.get(key);

            // create a new ArrayList to hold the matching values from the inputSentence
            ArrayList<String> matchedValues = new ArrayList<>();

            // loop through each value in the values ArrayList for the current key
            for (String value : values) {

                // check if the inputSentence contains the current value
                    inputSentence = inputSentence.replaceAll("[!" + 
                    ".?'\"\\-,;:()\\[\\]{}\\\\|#$%^&*_+=~`]", "");//Regex replaces all non-alphabetic characters
                    
                    //String regexInput = input.substring(0, input.length() - 1) + " " + input.charAt(input.length() - 1); // To separate last char from the sentence for the case there is "?" at the end of the sentence
                    String[] preWordsInput = inputSentence.split(" ");//assign every word to array
                    
                    for (int i = 0; i < preWordsInput.length; i++) {
                        if(preWordsInput[i].equalsIgnoreCase(value)){
                            // if it does, add the value to the matchedValues ArrayList
                            if(!matchedValues.contains(value)){
                                matchedValues.add(value);
                            }
                        }
                    }
            }
            // add the key and matchedValues ArrayList to the matchedHashMap
            matchedHashMap.put(key, matchedValues);
        }
        return matchedHashMap;
    }


    public static boolean checkEquality(HashMap<String, ArrayList<String>> hashMap1, HashMap<String, ArrayList<String>> hashMap2) {
        // check if the hashMaps are the same instance or both null
        if (hashMap1 == hashMap2) {
            return true;
        }
        // loop through each key in the hashMap1
        for (String key : hashMap1.keySet()) {
            ArrayList<String> values1 = hashMap1.get(key);
            ArrayList<String> values2 = hashMap2.get(key);

            if(values1.size() != 0){
                if(!values1.equals(values2)){
                    return false;
                }
            }
        }
        return true;
    }

    // ***THIS METHOD HAS BEEN SCRAPPED***   //

    // The method that checks whether it is corresponding skill to the input or not
    // If it is the corresponding skill saves the placeholder values to hashmap in order to call proper action
    public String match(String input){
        inputValues = new HashMap<>();
        Set<String> keySet = placeholders.keySet();
        ArrayList<String> keyList = new ArrayList<>(keySet); //get the key values of placeholders into arraylist like <DAY> <TIME>

        prototype = prototype.replaceAll("[!" + 
        ".?'\"\\-,;:()\\[\\]{}\\\\|#$%^&*_+=~`]", "");//Regex replaces all non-alphabetic characters
        
        //String regex = prototype.substring(0, prototype.length() - 1) + " " + prototype.charAt(prototype.length() - 1); // To spearate last char from the sentence for the case there is "?" at the end of the sentence 
        String[] words = prototype.split(" ");//assign every word to array

        input = input.replaceAll("[!" + 
        ".?'\"\\-,;:()\\[\\]{}\\\\|#$%^&*_+=~`]", "");//Regex replaces all non-alphabetic characters
        
        //String regexInput = input.substring(0, input.length() - 1) + " " + input.charAt(input.length() - 1); // To separate last char from the sentence for the case there is "?" at the end of the sentence
        String[] preWordsInput = input.split(" ");//assign every word to array

        int numberOfSpace = 0;
        for (int i = 0; i < preWordsInput.length; i++) {
            if(preWordsInput[i].length() == 0){
                numberOfSpace++;
            }
        }
    
        String[] wordsInput  = new String[preWordsInput.length-numberOfSpace];
        int index = 0;
        for (int i = 0; i < preWordsInput.length; i++) {
            if(!(preWordsInput[i].length() == 0)){
                wordsInput[index] = preWordsInput[i];
                index++;
            }
        }
        
        HashMap<String, ArrayList<Integer>> indexOfPlaceholders = new HashMap<>();
        ArrayList<Integer> allIndexes = new ArrayList<>();

        inputValues = matchHashMap(placeholders, input);

        for (int i = 0; i < actions.size(); i++) {
            if(checkEquality(actions.get(i).getActionValues(), inputValues)){
                actions.get(i).setInputValues(inputValues);
                return actions.get(i).triggerAction();
            }
        }

        inputValues = new HashMap<>();

        try {
        //FIND THE INDEX OF PLACEHOLDERS IN PROTOTYPE SENTENCE
        for (int i = 0; i < keyList.size(); i++) {
            ArrayList<Integer> indexesForKey = new ArrayList<>();
            for (int j = 0; j < words.length; j++) {
                if(words[j].equals(keyList.get(i))){
                    allIndexes.add(j);
                    indexesForKey.add(j);
                }
            }
            indexOfPlaceholders.put(keyList.get(i), indexesForKey);
        }

        // GET THE INPUT VALUES FROM INPUT SENTENCE
        ArrayList<String> keySetIndex = new ArrayList<>(indexOfPlaceholders.keySet());

        for (int i = 0; i < keySetIndex.size(); i++) {
            ArrayList<String> values = new ArrayList<>();
            for (int j = 0; j < indexOfPlaceholders.get(keySetIndex.get(i)).size(); j++) {
                values.add(wordsInput[indexOfPlaceholders.get(keySetIndex.get(i)).get(j)]);
            }
            inputValues.put(keySetIndex.get(i), values);
        }                    
        } catch (Exception e) {
            // TODO: handle exception
        }
        
        return performAction();
    }
}