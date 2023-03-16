package DigitalAssistant.skillLogic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class SkillEditor {
    private List<Skill> skills;
    private String filename;
    
    public SkillEditor(String filename) {
        skills = new ArrayList<>();
        this.filename = filename;
        loadSkills();
    }

    public List<Skill> getSkills() {
        return skills;
    }
  
    public void saveSkill(Skill skill) {
    }

    private void loadSkills() {//Reads skills.txt and makes skill objects from each skill already contained in the file
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                if (line.equals("-----")) {
                    // Start of new skill
                    String name = scanner.nextLine().trim();
                    name = name.replace("Name ", ""); // Name of the Skill

                    String prototype = scanner.nextLine().trim();
                    prototype = prototype.replace("Question ", ""); // Prototype Sentence

                    Skill skill = new Skill(name, prototype);

                    ArrayList<String> placeholders = new ArrayList<>(); //We will assign placeholder keys to the arraylist in order to use later with creation of Actions.
                    
                    // Read slots (placeholders key value)
                    while (true) {
                        String slotLine = scanner.nextLine().trim();

                        if (slotLine.startsWith("--")) { //Represents the end of slots and the beggining of actions
                            // Done reading slots, start reading actions
                            break;
                        }

                        slotLine = slotLine.replace("Slot ", ""); // Deletes the word "Slot " from the line 
                        String[] slot = slotLine.split(" "); //Splits every word then assgins all of the wors to the array

                        String slotKey = slot[0]; // the key of the line for example it can be <DAY>
                        placeholders.add(slotKey);// we will use these key values later

                        String[] slotValuesArray = Arrays.copyOfRange(slot, 1, slot.length); // Removed the key <DAY> ,and assign rest of the line to another array
                        ArrayList<String> slotValues = new ArrayList<>();

                        for (int i = 0; i < slotValuesArray.length; i++) {
                            slotValues.add(slotValuesArray[i]); // Convert String Array to ArrayList
                        }

                        skill.setPlaceholderValue(slotKey, slotValues); // For each slot we created a key and the arraylist of values for corresponding key, then assigned the key value pair to the hashmap of skill.
                    }                    
                    //System.out.println(skill.getPlaceholders().keySet());

                    // Read actions
                    while (scanner.hasNextLine()) {

                        String actionLine = scanner.nextLine().trim();
                        actionLine = actionLine.replace("Action ", ""); // Delete the "Action " from the line

                        if (actionLine.equals("+++++") || actionLine.equals(null)) { // end of the skill.
                            // End of skill
                            skills.add(skill);
                            break;
                        }

                        String[] actionLineString = actionLine.split(" "); // Split every word of line and add every word to array
                       
                        /*  
                         * Example Line: "Action <DAY> Monday <TIME>  9 |Print| We start the week with math"
                         * Already deleted the "Action " part so we have: "<DAY> Monday <TIME>  9 |Print| We start the week with math".
                         * The HashMap actionValues contains the keu value for this example it contains <DAY> MONDAY
                         *                                                                              <TIME> 9
                         * actionType is the "|Print|" which means we will use print action for the "MONDAY" and "9"
                         * actionInput is the line  "We start the week with math" ,so this is the sentence we will print.
                         */
                        HashMap<String,String> actionValues = new HashMap<>(); 
                        String actionType = new String();
                        String actionInput = new String();

                        for (int i = 0; i < actionLineString.length; i++) {
                            for (int j = 0; j < placeholders.size(); j++) {
                                if(actionLineString[i].equals(placeholders.get(j))){
                                    actionValues.put(placeholders.get(j), actionLineString[i+1]);
                                }
                            }
                        }

                        for (int i = 0; i < actionLineString.length; i++) {
                            if(actionLineString[i].startsWith("|") && actionLineString[i].endsWith("|")){
                                actionType = actionLineString[i];
                                for (int j = i+1; j < actionLineString.length; j++) {
                                    actionInput += actionLineString[j] + " ";
                                }
                            }
                        }

                        // System.out.println(actionType + " " + actionInput);
                        // System.out.println(actionValues);

                        Action action = new Action(actionType, actionInput, actionValues);
                        skill.addAction(action);
                    }
                }
                
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found");
        }
    }

    public static void main(String[] args) {
        SkillEditor skillEditor = new SkillEditor("/Users/user/Documents/GitHub/Project-2-2-Group-14/src/main/java/DigitalAssistant/skillLogic/skills.txt");
        String input = "Which lectures are there on Monday at 8?";
        System.out.println(input);
        for (int i = 0; i < skillEditor.skills.size(); i++) {
            skillEditor.skills.get(i).match(input);
        }
    }
    
}