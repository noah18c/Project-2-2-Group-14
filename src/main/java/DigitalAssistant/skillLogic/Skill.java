package DigitalAssistant.skillLogic;

import java.util.ArrayList;
import java.util.HashMap;
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

    // The method that checks whether it is corresponding skill to the input or not
    // if it is the corresponding skill saves the placeholder values to hashmap in order call proper action
    public String match(String input){
        inputValues = new HashMap<>();
        Set<String> keySet = placeholders.keySet();
        ArrayList<String> keyList = new ArrayList<>(keySet); //get the key values of placeholders into arraylist like <DAY> <TIME>
        
        String regex = prototype.substring(0, prototype.length() - 1) + " " + prototype.charAt(prototype.length() - 1); // To spearate last char from the sentence for the case there is "?" at the end of the sentence
        String[] words = regex.split(" ");//assign every word to array

        String regexInput = input.substring(0, input.length() - 1) + " " + input.charAt(input.length() - 1); // To separate last char from the sentence for the case there is "?" at the end of the sentence
        String[] wordsInput = regexInput.split(" ");//assign every word to array  
        
        HashMap<String, ArrayList<Integer>> indexOfPlaceholders = new HashMap<>();
        ArrayList<Integer> allIndexes = new ArrayList<>();


        // for (int i = 0; i < actions.size(); i++) {
        //     Action currentAction = actions.get(i); 
        //     HashMap<String,ArrayList<String>> currentValues = currentAction.getActionValues();
        //     ArrayList<String> keySetCurrentValues = new ArrayList<>(currentValues.keySet());

        //     for (int j = 0; j < keySetCurrentValues.size(); j++) {
        //         ArrayList<String> values = currentValues.get(keySetCurrentValues.get(j));
        //         ArrayList<String> Valuesinput = new ArrayList<>();
        //         for (int j2 = 0; j2 < values.size(); j2++) {
        //             for (int k = 0; k < wordsInput.length; k++) {
        //                 if(wordsInput[k].equals(values.get(j2))){
        //                     Valuesinput.add(values.get(j2));
        //                     //inputValues.get(keySetCurrentValues.get(j)).add(values.get(j2));
        //                 }
        //             }
        //         }
        //         inputValues.put(keySetCurrentValues.get(j), Valuesinput);
                
        //     }
        // }

        // String result = performAction();
        // if(!result.equals("No Action Found for given values!")){
        //     return result;
        // }
        // else{
        //     inputValues = new HashMap<>();
        // }


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
        // //MATCHING LOOP
        // for (int i = 0; i < wordsInput.length; i++) {
        //     if(!allIndexes.contains(i)){
        //         if(!wordsInput[i].equals(words[i])){
        //             return false;
        //         }
        //     }
        // }
        // IF IT MATCHES, GET THE INPUT VALUES FROM INPUT SENTENCE
        
        ArrayList<String> keySetIndex = new ArrayList<>(indexOfPlaceholders.keySet());

        for (int i = 0; i < keySetIndex.size(); i++) {
            ArrayList<String> values = new ArrayList<>();
            for (int j = 0; j < indexOfPlaceholders.get(keySetIndex.get(i)).size(); j++) {
                values.add(wordsInput[indexOfPlaceholders.get(keySetIndex.get(i)).get(j)]);
            }
            inputValues.put(keySetIndex.get(i), values);
        }
        return performAction();
    }


    public String performAction(){
        //Find Corresponding action for given input then trigger the action
        for (int i = 0; i < actions.size(); i++) {
            
            Action currentAction = actions.get(i);
            ArrayList<String> actionKeyList = new ArrayList<>(currentAction.getActionValues().keySet());
            ArrayList<String> inputValuesKeyList = new ArrayList<>(inputValues.keySet());
            
            //System.out.println(inputValuesKeyList + "  " + inputValues + " " + actionKeyList + " " + currentAction.getActionValues());

            int counter = 0;
            for (int j = 0; j < actionKeyList.size(); j++) {
                for (int j2 = 0; j2 < inputValuesKeyList.size(); j2++) {
                    if(actionKeyList.get(j) == inputValuesKeyList.get(j2)){
                        for (int k = 0; k < currentAction.getActionValues().get(actionKeyList.get(j)).size(); k++) {             
                            if(currentAction.getActionValues().get(actionKeyList.get(j)).get(k).equals(inputValues.get(inputValuesKeyList.get(j2)).get(k))){
                                counter++;
                            }
                            else if(currentAction.getActionValues().get(actionKeyList.get(j)).size() == 0){
                                counter ++;
                            }
                            else if(currentAction.getActionValues().get(actionKeyList.get(j)).get(k).equals("@INPUT")){
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
                if(keyListorino.indexOf(key) == keyListorino.size()-1){
                    builder.append(key + " " + action.getActionValues().get(key));
                }else{
                    builder.append(key + " " + action.getActionValues().get(key) + " ");
                }
            }
            //Actiontype and Action output string
            builder.append(action.getActionType() + " " + action.getActionSentence() + "\n");
        }
        
        builder.append("+++++");
        //End
        return builder.toString();
    }
}