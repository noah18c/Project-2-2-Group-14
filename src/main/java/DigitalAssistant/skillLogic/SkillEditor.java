package DigitalAssistant.skillLogic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class SkillEditor {
    private List<Skill> skills;
    private String filename;
    public static ArrayList<Event> events = new ArrayList<>();
    
    public SkillEditor(String filename) {
        skills = new ArrayList<>();
        this.filename = filename;
        loadSkills();
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void addSkill(Skill skill) {
        skills.add(skill);
    }

    public static void main(String[] args) {
        SkillEditor skillEditor = new SkillEditor("/Users/user/Documents/GitHub/Project-2-2-Group-14/src/main/java/DigitalAssistant/skillLogic/skills.txt");
        String input = "What is the distance between maastricht to adasd?";
        System.out.println(input);
        System.out.println(skillEditor.search(input));
    }

    public String search(String input){
        for (int i = 0; i <skills.size(); i++) {
            if(skills.get(i).match(input)){
                return skills.get(i).performAction();
            }
        }
        return "NO SKILLS FOUND FOR GIVEN INPUT";
    }


    /*
     *This method goes through the skills from first added to last added skill and deletes a skill object if it's name matches another skill further down the list.
     */
    public void deleteDuplicateSkills(){
        ArrayList<Skill> skillsToRemove = new ArrayList<>();
      
        for(int i = 0; i < skills.size(); i++){
            for(int j = i; j < skills.size(); j++){
                if(i != j && skills.get(i).getName().equals(skills.get(j).getName())){
                    skillsToRemove.add(skills.get(i));
                    System.out.println("Removing " + skills.get(i).getName());
                }
            }
        }


        skills.removeAll(skillsToRemove);
    }
    
    /**
     * This method takes all defined skill objects and overwrites the skills.txt file with all newly saved skills by printing each object in the correct format.
     */
    public void saveSkills() {
        loadSkills();//Loads the skills again just in case.
        deleteDuplicateSkills();//Gets rid of duplicate skills before saving new file
        try {
            FileWriter file = new FileWriter("src\\main\\java\\DigitalAssistant\\skillLogic\\skillsTest.txt", false);
            PrintWriter writer = new PrintWriter(file);
            //Prints to file the skill object in loadSkill() readable format 
            for(Skill skill : skills){
                if(skills.indexOf(skill) == skills.size()-1){
                    writer.print(skill.toFileFormatString());
                }else{
                    writer.println(skill.toFileFormatString());
                }
            }
            
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSkills() {//Reads skills.txt and makes skill objects from each skill already contained in the file
        this.skills = new ArrayList<>();//Resets list for use outside of constructor
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
                        HashMap<String,ArrayList<String>> actionValues = new HashMap<>(); 
                        String actionType = new String();
                        String actionInput = new String();
                        

                        for (int i = 0; i < actionLineString.length; i++) {
                            if(actionLineString[i].startsWith("|") && actionLineString[i].endsWith("|")){
                                actionType = actionLineString[i];
                                for (int j = i+1; j < actionLineString.length; j++) {
                                    if(j == actionLineString.length-1){
                                        actionInput += actionLineString[j];
                                    }
                                    else{
                                        actionInput += actionLineString[j] + " ";
                                    }
                                    
                                }
                            }   
                        }

                        actionLine = actionLine.replace(actionType, "");  //delete the actionType part to reach actionValues
                        actionLine = actionLine.replace(actionInput, ""); //delete the actionInput part to reach actionValues
                        actionLineString = actionLine.split(" "); // Split every word of line and add every word to array

                        for (int j = 0; j < placeholders.size(); j++) { 
                            ArrayList<String> valuesForPlaceholder = new ArrayList<>();
                            
                            for (int i = 0; i < actionLineString.length; i++) {
                                if(actionLineString[i].equals(placeholders.get(j))){
                                    valuesForPlaceholder.add(actionLineString[i+1]);
                                }
                            }

                            actionValues.put(placeholders.get(j), valuesForPlaceholder);
                        }


                        //System.out.println(actionType + " " + actionInput);
                        // System.out.println(actionValues);
                        // System.out.println(" -------- ");

                        Action action = new Action(actionType, actionInput, actionValues);
                        skill.addAction(action);
                    }
                }
                
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found");
        }
    }

    public static void loadEvents(){
        try (BufferedReader reader = new BufferedReader(new FileReader("src\\main\\java\\DigitalAssistant\\skillLogic\\Events.txt"))) {
            String currentLine = "";
            int i = 0;
            while(currentLine != null){//While the file has not ended
                //  System.out.println(currentLine);
                
                i++;
                //System.out.println("Event " + i);

                currentLine = reader.readLine();
                if(currentLine == null){break;}
                String eventName = currentLine;
                String parametersStr = reader.readLine();//string for scanner

                ArrayList<String> parameters = new ArrayList<>();//list to append to
                Scanner paraScanner = new Scanner(parametersStr);
                paraScanner.useDelimiter("-");//seperates the parametersStr by the '-' character


                while(paraScanner.hasNext()){
                    parameters.add(paraScanner.next());//adds each parameter to the list
                }
                paraScanner.close();

                Event temp = new Event(eventName, parameters);//creates event from read
                // System.out.println("Adding " + eventName);
                SkillEditor.events.add(temp);        
                
            }

            reader.close();
        } catch (IOException e) {
            System.err.println("Error reading skills from file: " + e.getMessage());
        }
    }

        /**
     * Method to extract info from user input when asking him to define an unknown skill!
     * Needed ONLY when the assistant recognizes unfamiliar skill -> gets stuck
     * if skill (prototype or placeholder) unknown then do questionnaire & store answers; if info complete then declareSKill(input)
     * Dynamic Questionnaire implemented in front end !?
     * extraction of answers in back end in this method !
     * 
     * input: the stored user answers
     * method creates a skill object which is added to skills list and can be written to the skills.txt file
     * @param ArrayList of strings
     * ---------------------------
     * Questions to tunnel him:
     * I don't get this. Would you like to define a new skill? I'd be happy to learn more to help you with your needs.
     *  A: Yes / No 
     * Perfect! What would you like to call the skill? 
     *  A0: ¡skillname¿
     * Provide a list of placeholders formatted with ¡¿ and seperated by commas
     *  A1: ¡sports¿, ¡cook timer¿ etc.
     * Enter a prototype sentence for the skill ¡skillname¿. It must include the placeholders!
     *  A2: ¡This is a prototype sentence with ¡placeholder¿ . 
     * Provide values for the placeholder(s)! separated by commas
     *  A3: Volleyball, Handball, Windsurfing
     * Your new skill ¡skillName¿ has been defined!
     *
     */

     public void declareSkill(ArrayList<String> input){
        String skillName = "";
        String prototype = "";
        String phName = "";
        ArrayList<String> values = new ArrayList<String>(); // placeholder values
        
        // extract skill name, prototype sentence, placeholder and its values if specified
        skillName = input.get(0);
        prototype = input.get(2);
        phName = input.get(1);
        String temp = input.get(3);
        Scanner scan = new Scanner(temp);
        scan.useDelimiter(",");
        while (scan.hasNext()){
            values.add(scan.next());
        }
        scan.close();

        // create skill object with that stuff
        Skill skill = new Skill(skillName, prototype);
        addSkill(skill);
        skill.setPlaceholderValue(phName, values);          // placeholders is a hashmap of String and ArrayList
        saveSkills();           // write the new skill to skills.txt
        //loadSkills();           // !? creates a loaded skill again!?
        /** 
        if (in.toLowerCase().equals("yes")){
            // post next response to the chat --> "Perfect! What would you like to call the skill?"
            // botWriter(false, "Perfect! What would you like to call the skill?")

            skillName = in.toLowerCase();
            // post next response to the chat --> "Enter a prototype sentence to use the skill ¡skillname¿ with."
            // botWriter(false, "Enter a prototype sentence to use the skill ¡skillname¿ with.")
            // get new input
            prototype = in;

        }
        if (in.toLowerCase().equals("no")){
            System.out.println("Try a different skill command. Here is a list of skills I understand.");
        }
        */
    }

    /**
     * Used when user wants to add new values to existing placeholders of defined skills
     * @param prototype
     * @param input
     * user prompt:
     * Add values to this skill ¡skillName¿ / this prototype ¡prototype¿
     * or when bot gets stuck:
     * Would you like to define a new skill / prototype or add new values to an existing skill / prototype?
     * A: add values to existing prototype
     * Specify the prototype in the following format: ¡prototype¿
     * A: ¡prototype¿ // use as input!
     * Specify new values you would like to add to this prototype // use as input!
     */
    public void addValue(String prototype, String input){
        ArrayList<String> values = new ArrayList<String>();
        Scanner scan = new Scanner(input);
        scan.useDelimiter(",");
        while (scan.hasNext()){
            values.add(scan.next());
        }
        scan.close();
        // save values
        // updateSkills();
    }

    public static void addEventToFile(String eventName, ArrayList<String> parameters){
        try {
            FileWriter file = new FileWriter("src\\main\\java\\DigitalAssistant\\skillLogic\\Events.txt", true);
            PrintWriter writer = new PrintWriter(file);
            writer.println("\n"+eventName);
            String paraStr = "";
            for(String parameter : parameters){
                if(parameters.indexOf(parameter) == parameters.size()-1){
                    paraStr += parameter;
                }else{
                    paraStr += parameter+"-";
                }
                
            }
            writer.println(paraStr);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String findSkill(String input){
        for (int i = 0; i <this.skills.size(); i++) {
            this.skills.get(i).match(input);
            return "Marieke"; 
        }
        return "No skill found";
    }    
}
